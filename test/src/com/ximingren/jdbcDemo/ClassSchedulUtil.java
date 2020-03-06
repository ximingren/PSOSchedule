package com.ximingren.jdbcDemo;

import com.ximingren.CourseSchedule.Bean.vo.ConstantInfo;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;

public class ClassSchedulUtil {

    /**
     * 划分出编码片段
     *
     * @param aim    目标片段
     * @param source 编码片段
     * @return 目标编码片段
     */
    public static String cutCode(String aim, String source) {
        switch (aim) {
            case ConstantInfo.IS_FIX:
                return source.substring(0, 1);
            case ConstantInfo.COLLEGE_NO:
                return source.substring(1, 3);
            case ConstantInfo.CLASS_NO:
                return source.substring(3, 13);
            case ConstantInfo.TEACHER_NO:
                return source.substring(13, 21);
            case ConstantInfo.COURSE_NO:
                return source.substring(21, 27);
            case ConstantInfo.COURSE_ATTR:
                return source.substring(27, 29);
            case ConstantInfo.CLASS_TIME:
                return source.substring(29, 31);
            case ConstantInfo.CLASSROOM_NO:
                return source.substring(31);
            default:
                return "";
        }
    }

    //判断同一个班是否在同一时间内上课有重复
    private static Boolean isTimeNoRepe(String time, String location, List<Particle> particleList) {
        //获得班级编号
        String classNo = cutCode(ConstantInfo.CLASS_NO, location);
        for (Particle particle : particleList) {
            String str = particle.getLocation();
            //判断班级编号是否相等
            if (classNo.equals(cutCode(ConstantInfo.CLASS_NO, str))) {
                //班级编号相等的则判断时间是否有重复，没有返回true
                String classTime = cutCode(ConstantInfo.CLASS_TIME, str);
                if (time.equals(classTime)) {
                    return false;
                }
            }
        }
        return true;
    }

    //随机生成时间片
    public static String randomTime(String location, List<Particle> particleList) {
        int min = 1;
        int max = 25;
        String time;
        //随机生成1到25范围的数字，并将其转化为字符串，方便进行编码
        int temp = min + (int) (Math.random() * (max + 1 - min));
        if (temp < 10) {
            time = "0" + temp;
        } else {
            time = "" + temp;
        }
        if (isTimeNoRepe(time, location, particleList)) {
            return time;
        } else {
            return randomTime(location, particleList);
        }
    }

    /***
     * 计算适应度值,即为计算课程离散程度期望值
     * @param particle 当前粒子
     * @param particleList 子种群粒子集合
     * @return
     */
    public static double alculateFitnessValue(Particle particle, List<Particle> particleList) {
        double K1 = 0.3;//专业课所占权重
        double K2 = 0.1;//选修课所占权重
        double K3 = 0.1;//体育课所占权重
        double K4 = 0.3;//实验课所占权重
        double K5 = 0.2;//课程离散程度所占权重
        int F1 = 0;//专业课期望总值
        int F2 = 0;//选修课期望总值
        int F3 = 0;//体育课期望总值
        int F4 = 0;//实验课期望总值
        int F5;//课程离散程度期望总值
        double Fx;//适应度值

        String location = particle.getLocation();//粒子位置
        String courseAttr = cutCode(ConstantInfo.COURSE_ATTR, location);//获得课程属性
        String classTime = cutCode(ConstantInfo.CLASS_TIME, location);//获得该课程的开课时间
        if (courseAttr.equals(ConstantInfo.PROFESSIONAL_CODE)) {
            F1 = F1 + calculateProfessExpect(classTime);
        } else if (courseAttr.equals(ConstantInfo.ELECTIVE_CODE)) {
            F2 = F2 + calculateElectiveExpect(classTime);
        } else if (courseAttr.equals(ConstantInfo.PHYSICAL_CODE)) {
            F3 = F3 + calculatePhysicalExpect(classTime);
        } else {
            F4 = F4 + calculateExperimentExpect(classTime);
        }
        //计算课程离散程度
        F5 = calculateDiscreteExpect(particleList);
        Fx = K1 * F1 + K2 * F2 + K3 * F3 + K4 * F4 + K5 * F5;
        return Fx;
    }

    /**
     * 根据上课时间确定课程在哪个级别，根据此级别返回对应的期望值
     * @param courseValue 课程时间列表
     * @param expectValueList 期望值列表
     * @param classTime 上课时间
     * @return
     */
    public static int getClassTimeIndex(String[][] courseValue,int [] expectValueList, String classTime) {
        for (int i = 0; i < expectValueList.length; i++) {
            String[] values = courseValue[i];
            if (ArrayUtils.indexOf(values, classTime) != -1) {
                return expectValueList[i];
            }
        }
        return 0;
    }
    public static String updateRandomValue(String [][] expectValue,String classTime,String firstLocation,List<Particle>swarm,int iterNum) {
        String nextValue = null;
        for (String[] values :expectValue) {
            int index = ArrayUtils.indexOf(values, classTime);
            if (index != -1) {
                if (iterNum>=10) {
                    values=expectValue[iterNum%expectValue.length];
                }
                int size = values.length;
                String nextClassTime = values[(int) (Math.random() * size)];
                if (isTimeNoRepe(nextClassTime, firstLocation, swarm)) {
                    return nextClassTime;
                } else {
                    iterNum = iterNum + 1;
                    return updateRandomValue(expectValue, classTime, firstLocation, swarm,iterNum);
                }
            }
        }
        return nextValue;
    }
    /***
     * 计算专业课期望值
     * @param classTime 上课时间
     * @return
     */
    private static int calculateProfessExpect(String classTime) {
        String[][] courseValue = ConstantInfo.PROPESSIONAL_VALUE;//课程时间分类列表
        int[] expectValueList = new int[]{10, 8, 4, 2, 0};//课程期望值数组
        return getClassTimeIndex(courseValue,expectValueList,classTime);
    }

    /***
     * 计算选修课期望值
     * @param classTime 上课时间
     * @return
     */
    private static int calculateElectiveExpect(String classTime) {
        String[][] courseValue = ConstantInfo.ELECTIVE_VALUE;
        int [] expectValueList = new int[]{10, 8, 4, 0};
        return getClassTimeIndex(courseValue,expectValueList,classTime);
    }

    /***
     * 计算体育课期望值
     * @param classTime 上课时间
     * @return
     */
    private static int calculatePhysicalExpect(String classTime) {
        String[][] courseValue = ConstantInfo.PHYSICAL_VALUE;
        int [] expectValueList = new int[]{10, 8, 4, 0};
        return getClassTimeIndex(courseValue,expectValueList,classTime);

    }

    /***
     * 计算实验课期望值
     * @param classTime 上课时间
     * @return
     */
    private static int calculateExperimentExpect(String classTime) {
        String[][] courseValue = ConstantInfo.EXPERIMENT_VALUE;
        int [] expectValueList = new int[]{10, 8, 6,4, 0};
        return getClassTimeIndex(courseValue,expectValueList,classTime);
    }

    /***
     * 计算课程离散度期望值
     * @param particleList
     * @return
     */
    private static int calculateDiscreteExpect(List<Particle> particleList) {
        //离散程度期望值
        int F5 = 0;
        //对排课粒子根据课程进行分组
        Map<String, List<String>> classTimeMap = courseGrouping(particleList);
        for (List<String> classTimeList : classTimeMap.values()) {
            //有两次及以上的排课计划
            if (classTimeList.size() > 1) {
                for (int i = 0; i < classTimeList.size() - 1; ++i) {
                    int temp = Integer.parseInt(classTimeList.get(++i)) - Integer.parseInt(classTimeList.get(i - 1));
                    F5 = F5 + judgingDiscreteValues(temp);
                }
            }
        }
        return F5;
    }

    /**
     * 将一个个体（班级课表）的同一门课程的所有上课时间进行一个统计，并且进行一个分组
     *
     * @param particleList
     * @return
     */
    private static Map<String, List<String>> courseGrouping(List<Particle> particleList) {
        Map<String, List<String>> classTimeMap = new HashMap<>();
        //先将一个班级课表所上的课程区分出来（排除掉重复的课程）
        for (Particle particle : particleList) {
            String location = particle.getLocation();
            classTimeMap.put(cutCode(ConstantInfo.COURSE_NO, location), null);
        }
        //遍历课程
        for (String courseNo : classTimeMap.keySet()) {
            List<String> classTimeList = new ArrayList<>();
            for (Particle particle : particleList) {
                String location = particle.getLocation();
                //获得同一门课程的所有上课时间片
                if (cutCode(ConstantInfo.COURSE_NO, location).equals(courseNo)) {
                    classTimeList.add(cutCode(ConstantInfo.CLASS_TIME, location));
                }
            }
            //将课程的时间片进行排序
            Collections.sort(classTimeList);
            classTimeMap.put(courseNo, classTimeList);
        }
        return classTimeMap;
    }


    /***
     * 判断两课时间差在那个区间并返回对于的期望值
     * @param temp
     * @return
     */
    private static int judgingDiscreteValues(int temp) {
        int[] tenExpectValue = {5, 6, 7, 8};//期望值为10时两课之间的时间差
        int[] sixExpectValue = {4, 9, 10, 11, 12, 13};//期望值为6时两课之间的时间差
        int[] fourExpectValue = {3, 14, 15, 16, 17, 18};//期望值为4时两课之间的时间差
        int[] twoExpectValue = {2, 19, 20, 21, 22, 23};//期望值为2时两课之间的时间差
        //int [] zeroExpectValue = {1,24};//期望值为0时两课之间的时间差

        if (ArrayUtils.contains(tenExpectValue, temp)) {
            return 10;
        } else if (ArrayUtils.contains(sixExpectValue, temp)) {
            return 6;
        } else if (ArrayUtils.contains(fourExpectValue, temp)) {
            return 4;
        } else if (ArrayUtils.contains(twoExpectValue, temp)) {
            return 2;
        } else {
            return 0;
        }
    }
}
