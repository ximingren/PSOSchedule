package com.ximingren.jdbcDemo;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.jdbcDemo.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName PSOService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/2/28 19:40
 */
public class PSOService {
    //粒子集合
    private List<Particle> swarm = new ArrayList<>();
    private static final String UNFIXED_TIME = "unFixedTime";//没有固定时间
    private static final String IS_FIXED_TIME = "isFixedTime";//固定时间

    public void init() {
        List<ClassTask> classTaskList = ClassTaskJDBC.selectClassTask();
        List<Map<String, List<String>>> particleXMap = ClassTaskService.coding(classTaskList);
        List<String> unfixedClassList = particleXMap.get(0).get(UNFIXED_TIME);
        List<String> fixedClassList = particleXMap.get(0).get(IS_FIXED_TIME);
        List<String> resultClassList = new ArrayList<>();
        resultClassList.addAll(fixedClassList);
        resultClassList.addAll(unfixedClassList);
        //根据班级进行分组
        Map<String, List<String>> individualMap = ClassTaskService.transformIndividual(resultClassList);
        Map<String, List<Particle>> individualParticleMap = new HashMap<>();
        for (String classNo : individualMap.keySet()) {
            //参数是这个班的排课计划,返回值是这个班的粒子种群
            List<Particle> particleList = ClassTaskService.codingTime(individualMap.get(classNo));
        }
    }

    public static void main(String[] args) {
        PSOService psoService = new PSOService();
        psoService.init();
    }
}
