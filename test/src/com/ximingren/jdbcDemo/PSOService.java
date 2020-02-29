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
    //粒子集合
    private List<Particle> swarm = new ArrayList<>();
    //每个粒子的最优解时所在的位置
    private List<Location> pBestLocation = new ArrayList<>();
    //每个粒子的最优解
    private Map<String, List<Double>> pBest = new HashMap<>();
    //全局最优解
    private Map<String, Particle> gBest = new HashMap<>();
    private static final String UNFIXED_TIME = "unFixedTime";//没有固定时间
    private static final String IS_FIXED_TIME = "isFixedTime";//固定时间
    private Map<String, List<String>> subSpeciesStringMap;
    private Map<String, List<Particle>> subSpeciesPartilesMap;
    private Map<String, List<Double>> gbestClassMap = new HashMap<>();
    /**
     * 初始化种群，父种群中有子种群；子种群即是每个班级的排课粒子集合
     */
    public void init() {
        List<ClassTask> classTaskList = ClassTaskJDBC.selectClassTask();
        List<Map<String, List<String>>> particleXMap = ClassTaskService.coding(classTaskList);
        List<String> unfixedClassList = particleXMap.get(0).get(UNFIXED_TIME);
        List<String> fixedClassList = particleXMap.get(0).get(IS_FIXED_TIME);
        List<String> resultClassList = new ArrayList<>();
        resultClassList.addAll(fixedClassList);
        resultClassList.addAll(unfixedClassList);
        //根据班级进行分组,得到若干个子种群
        subSpeciesStringMap = ClassTaskService.transformIndividual(resultClassList);
        Map<String, List<Particle>> individualParticleMap = new HashMap<>();
        //对每个子种群进行初始化
        for (String classNo : subSpeciesStringMap.keySet()) {
            //参数是该班的排课计划,返回值是子粒子种群
            List<Particle> particleList = ClassTaskService.codingTime(subSpeciesStringMap.get(classNo));
            swarm.addAll(particleList);
            gbestClassMap.put(classNo, new ArrayList<>());
        }
        updateFintnessList();

    }

    public void updateFintnessList() {
        //检测是否有冲突
        List<Particle> noConflictParticleList = ClassTaskService.conflictResolution(swarm);
        //分割成子种群
        subSpeciesPartilesMap = ClassTaskService.transformSubSpecies(noConflictParticleList);
        for (String classNo : subSpeciesPartilesMap.keySet()) {
            gBest.put(classNo,subSpeciesPartilesMap.get(classNo).get(0));
            for (Particle particle : subSpeciesPartilesMap.get(classNo)) {
                double fitnessValue = ClassSchedulUtil.alculateExpectedValue(particle, subSpeciesPartilesMap.get(classNo));
                if (gBest.get(classNo).getFitnessValue() < fitnessValue) {
                    gBest.replace(classNo, particle);
                }
                particle.setFitnessValue(fitnessValue);
                particle.setpBestLocation(fitnessValue, particle.getLocation());
            }
            List<Double> list = gbestClassMap.get(classNo);
            list.add(Double.valueOf(gBest.get(classNo).getFitnessValue()));
            gbestClassMap.replace(classNo, list);
        }
    }
    public void execute(){
        System.out.println("开始--");
        init();
        int t=0;//已迭代的次数
        while (t < MAX_ITERATION) {
            swarm.clear();
            for (String classNo : subSpeciesPartilesMap.keySet()) {
                    List<Particle> selectiveParticleList = ClassTaskService.selectiveGene(subSpeciesPartilesMap.get(classNo), gBest.get(classNo));
                    subSpeciesPartilesMap.replace(classNo, selectiveParticleList);
                    swarm.addAll(selectiveParticleList);
            }
            updateFintnessList();
            t = t + 1;
        }
        for (String classNo : gbestClassMap.keySet()) {
            System.out.println("========="+classNo+"==========");
            List<Double> list = gbestClassMap.get(classNo);
//            for (Double d : list) {
//                System.out.print(d+" ");
//            }
            System.out.println(list.get(0)+" "+list.get(list.size()-1));
            System.out.println("\n");
        }

    }

    public static void main(String[] args) {
        PSOService psoService = new PSOService();
        psoService.execute();
    }
}
