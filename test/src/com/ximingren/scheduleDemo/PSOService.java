package com.ximingren.scheduleDemo;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;

import java.util.*;

/**
 * @ClassName PSOService
 * @Description PSO粒子群算法的实现
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
                //记录最初粒子的位置，方便后续检测优化效果
                List<Particle> initList = new ArrayList<>();
                for (Particle initParticle : particleList) {
                    Particle particle = new Particle();
                    particle.setLocation(initParticle.getLocation());
                    initList.add(particle);
                }
                initSubSpeciesMap.put(classNo, initList);
                gbestFitnessValueMap.put(classNo, new ArrayList<>());
            }
            //初始化适应度值
            updateFitnessList();

        } else {
            System.out.println("无排课任务");
        }
    }

    /**
     * 更新适应度和最优解
     */
    public void updateFitnessList() {
        //检测是否有冲突
        List<Particle> noConflictParticleList = ClassTaskService.conflictResolution(swarm);
        //分割成子种群,每个子种群分别进行适应度计算
        subSpeciesPartilesMap = ClassTaskService.transformSubSpecies(noConflictParticleList);
        for (String classNo : subSpeciesPartilesMap.keySet()) {
            double allFitnessValue = 0.0;
            if (!gBestParticle.keySet().contains(classNo)) {
                //第一次，最优解粒子默认为第一个粒子
                gBestParticle.put(classNo, subSpeciesPartilesMap.get(classNo).get(0));
            }
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
    public boolean judgeConverge() {
        int classNum = gbestFitnessValueMap.keySet().size();
        double err_tolerance = 0.0;
        int num = 0;
        for (String classNo : gbestFitnessValueMap.keySet()) {
            List<Double> list = gbestFitnessValueMap.get(classNo);
            double subValue = list.get(list.size() - 1) - list.get(0);
            if (subValue > PSOConstants.INCREASE_VALUE) {
                num = num + 1;
            }
            err_tolerance += subValue;
        }
        if (num >= (int) (classNum * PSOConstants.INCREASE_SCALE)) {
            return false;
        }
        return true;
    }

    /**
     * 粒子群优化算法Main函数
     */
    public void executePSO() {
        System.out.println("==========开始==========");

        //初始化
        init();
        //已迭代的次数
        int iterNum = 0;
        int reRandomTimeNum = 0;
        //是否到达结束标准
        boolean continueIteration = true;
        while (continueIteration) {
            //更新粒子位置
            ClassTaskService.selectiveParticle(subSpeciesPartilesMap, gBestParticle, swarm);
            //所有的子种群粒子位置更新完成，进行更新适应度
            updateFitnessList();
            continueIteration = judgeConverge();
            iterNum = iterNum + 1;
            if (iterNum > PSOConstants.ITERATION_REINIT) {
                reRandomTimeNum = reRandomTimeNum + 1;
                for (Particle particle : swarm) {
                    if (particle.getFitnessValue() < PSOConstants.INFERIOR_VALUE) {
//                        重新分配时间
                        String newTime = ClassSchedulUtil.randomTime(particle.getLocation(), swarm);
                        particle.setLocation(particle.getLocation().substring(0, 29) + newTime);
                    }
                }
                //清空变量

                System.out.println("重新初始化");
                //                gbestFitnessValueMap.clear();
//                gBestParticle.clear();
//                swarm.clear();
//                subSpeciesPartilesMap.clear();
//                init();
                iterNum = 0;
            }
        }
        //分配教室
        swarm = ClassTaskService.finalResult(swarm);
        //分配好时间和教师的总种群集合
        subSpeciesPartilesMap = ClassTaskService.transformSubSpecies(swarm);
        showBeforeAndAfterTable(continueIteration);
        System.out.println("迭代次数： " + iterNum);
        System.out.println("重新分配适应值低的排课粒子的时间次数： " + reRandomTimeNum);

    }

    public void showBeforeAndAfterTable(boolean flag) {
        if (!flag) {
            for (String classNo : gbestFitnessValueMap.keySet()) {
                System.out.println("初始课表：");
                DrawScheduleTable.draw(initSubSpeciesMap.get(classNo));
                System.out.println("优化后的课表：");
                DrawScheduleTable.draw(subSpeciesPartilesMap.get(classNo));

                System.out.println("============" + classNo + "===============");
                List<Double> list = gbestFitnessValueMap.get(classNo);
                System.out.println(list.get(0) + " " + list.get(list.size() - 1));
                System.out.println("\n");
            }
        }
    }

    public static void main(String[] args) {
        PSOService psoService = new PSOService();
        psoService.executePSO();
    }
}
