package com.ximingren.jdbcDemo;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.jdbcDemo.Particle;

import java.util.*;

import static com.ximingren.jdbcDemo.PSOConstants.MAX_ITERATION;

/**
 * @ClassName PSOService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/2/28 19:40
 */
public class PSOService {
    //种群集合
    private List<Particle> swarm = new ArrayList<>();
    //全局最优解
    private Map<String, Particle> gBestParticle = new HashMap<>();
    //子种群哈希表
    private Map<String, List<Particle>> subSpeciesPartilesMap;
    //子种群最优解粒子的适应度历史纪律
    private Map<String, List<Double>> gbestFitnessValueMap = new HashMap<>();
    private Map<String, List<Particle>> initSubSpeciesMap = new HashMap<>();

    /**
     * 初始化种群，父种群中有子种群；子种群即是每个班级的排课粒子集合
     */
    public void init() {
        //获取排课计划
        List<ClassTask> classTaskList = ClassTaskJDBC.selectClassTask();
        if (classTaskList != null) {
            //对排课任务进行粒子化
            List<Particle> allParticleList = ClassTaskService.coding(classTaskList);
            //根据班级对所有排课粒子进行分组,得到若干个子种群
            subSpeciesPartilesMap = ClassTaskService.transformSubSpecies(allParticleList);
            //对每个子种群进行初始化
            for (String classNo : subSpeciesPartilesMap.keySet()) {
                //参数是子种群,返回值是分配好时间的子种群粒子列表
                List<Particle> particleList = ClassTaskService.allocatTime(subSpeciesPartilesMap.get(classNo));
                //加入总种群集合中，方便后续进行检测冲突
                swarm.addAll(particleList);
                List<Particle> initList = new ArrayList<>();
                for (Particle particle : particleList) {
                    Particle particle1 = new Particle();
                    particle1.setLocation(particle.getLocation());
                    initList.add(particle1);
                }
                initSubSpeciesMap.put(classNo, initList);
                gbestFitnessValueMap.put(classNo, new ArrayList<>());
            }
            updateFintnessList();
        } else {
            System.out.println("无排课任务");
        }
    }

    /**
     * 更新适应度和最优解
     */
    public void updateFintnessList() {
        //检测是否有冲突
        List<Particle> noConflictParticleList = ClassTaskService.conflictResolution(swarm);
        //分割成子种群,每个子种群分别进行适应度计算
        subSpeciesPartilesMap = ClassTaskService.transformSubSpecies(noConflictParticleList);
        for (String classNo : subSpeciesPartilesMap.keySet()) {
            double allFitnessValue = 0.0;
            //第一次，最优解粒子默认为第一个粒子
            gBestParticle.put(classNo, subSpeciesPartilesMap.get(classNo).get(0));
            //对子种群的每个粒子进行适应度计算，来跟最优解粒子进行比较
            for (Particle particle : subSpeciesPartilesMap.get(classNo)) {
                //计算适应度
                double fitnessValue = ClassSchedulUtil.alculateFitnessValue(particle, subSpeciesPartilesMap.get(classNo));
                //跟最优解粒子进行比较
                if (gBestParticle.get(classNo).getFitnessValue() < fitnessValue) {
                    //更新子种群的全局最优解粒子
                    gBestParticle.replace(classNo, particle);
                }
                //所有排课粒子的总适应度值
                allFitnessValue += fitnessValue;
                particle.setFitnessValue(fitnessValue);
                //设置粒子pBest
                particle.setpBestLocation(fitnessValue, particle.getLocation());
            }
            List<Double> list = gbestFitnessValueMap.get(classNo);
            list.add(allFitnessValue);
            gbestFitnessValueMap.replace(classNo, list);
        }
    }

    /**
     * 算法收敛标准，即算法结束标准
     *
     * @return
     */
    public boolean judgeErr() {
        int num = 0;
        int classNum = gbestFitnessValueMap.keySet().size();
        double err_tolerance = 0.0;
        for (String classNo : gbestFitnessValueMap.keySet()) {
            List<Double> list = gbestFitnessValueMap.get(classNo);
            double subValue = list.get(list.size() - 1) - list.get(0);
            err_tolerance += subValue;
            if (subValue > 0) {
                //表示这个子种群有优化
                num = num + 1;
            }
        }
//        System.out.println(err_tolerance / classNum);
        if (err_tolerance / classNum > 10.0) {
            return false;
        }
//        if (num == gbestFitnessValueMap.keySet().size() - 1) {
//            return false;
//        }
        return true;
    }

    /**
     * 粒子群优化算法Main函数
     */
    public void execute() {
        System.out.println("==========开始==========");
        //清空变量

        //初始化
        init();
        //已迭代的次数
        int t = 0;
        //是否到达结束标准
        boolean flag = true;
        while (flag) {
            //每次迭代都要清空总种群集合，方便后续添加进更新后的新粒子来进行检测冲突
            swarm.clear();
            //更新粒子位置
            swarm = ClassTaskService.selectiveGene(subSpeciesPartilesMap, gBestParticle);
            //所有的子种群粒子位置更新完成，进行更新适应度
            updateFintnessList();
            flag = judgeErr();
            t = t + 1;
            if (t > 5000) {
                gbestFitnessValueMap.clear();
                gBestParticle.clear();
                swarm.clear();
                subSpeciesPartilesMap.clear();
                System.out.println("重新初始化");
                execute();
            }
//            System.out.println(t);
        }
        swarm = ClassTaskService.finalResult(swarm);
        subSpeciesPartilesMap = ClassTaskService.transformSubSpecies(swarm);
        if (!flag) {
            System.out.println("迭代次数： " + t);
            for (String classNo : gbestFitnessValueMap.keySet()) {
                DrawScheduleTable.draw(initSubSpeciesMap.get(classNo));
                DrawScheduleTable.draw(subSpeciesPartilesMap.get(classNo));
                System.out.println("=========" + classNo + "==========");
                List<Double> list = gbestFitnessValueMap.get(classNo);
//            for (Double d : list) {
//                System.out.print(d+" ");
//            }
                System.out.println(list.get(0) + " " + list.get(list.size() - 1));
                System.out.println("\n");
            }
        }
        System.exit(0);
    }

    public static void main(String[] args) {
        PSOService psoService = new PSOService();
        psoService.execute();
    }
}
