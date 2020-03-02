package com.ximingren.jdbcDemo;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.po.ClassroomLocation;
import com.ximingren.CourseSchedule.Bean.vo.ConstantInfo;
import com.ximingren.jdbcDemo.Particle;
import org.apache.commons.lang3.ArrayUtils;

import java.util.*;


/**
 * @ClassName ClassTaskService
 * @Description 计划用二维空间来解，分别为课程信息和时间片段
 * @Author ximingren
 * @Date 2020/2/28 19:34
 */
public class ClassTaskService {
    private static Random generator = new Random();

    /**
     * 将从表中查询的开课任务书对象集合进行编码，组成粒子的课程信息维度的数据
     * 编码规则为：是否固定+年级编号+班级编号+教师编号+课程编号+课程属性+开课时间
     * 其中如果是否固定为否则开课时间默认填充为"00"
     *
     * @param classTaskList
     * @return List<Particle> 所有排课粒子
     */
    public static List<Particle> coding(List<ClassTask> classTaskList) {
        List<Particle> particleList = new ArrayList<>();
        List<Particle> unFixedTimeParticleList = new ArrayList<>();//不固定时间的编码粒子组
        List<Particle> isFixedTimeParticleList = new ArrayList<>();//固定时间的编码粒子组
        for (ClassTask classTask : classTaskList) {
            //根据isFix的值判断是否固定，为1则不固定classTime默认填充”00“
            if (classTask.getIsfix().equals("1")) {
                //计算一周的上课次数，一次对应一个粒子，两次对应两个粒子
                int size = classTask.getWeeksnumber() / 2;
                for (int i = 0; i < size; i++) {
                    String particleLocation = classTask.getIsfix() + classTask.getCollegeno() + classTask.getClassno() + classTask.getTeacherno() + classTask.getCourseno() + classTask.getCourseattr() + "00";
                    //确定了粒子的位置后添加进粒子列表中
                    Particle particle = new Particle(particleLocation);
                    unFixedTimeParticleList.add(particle);
                }
            }
            //isFix的值为2则classTime有值，需要对classTime的值进行切割
            if (classTask.getIsfix().equals("2")) {
                int size = classTask.getWeeksnumber() / 2;
                for (int i = 0; i < size; i++) {
                    //获取已固定课程的时间
                    String classTime = classTask.getClasstime().substring(i * 2, (i + 1) * 2);
                    //确定了粒子的位置后添加进粒子列表中
                    String particleLocation = classTask.getIsfix() + classTask.getCollegeno() + classTask.getClassno() + classTask.getTeacherno() + classTask.getCourseno() + classTask.getCourseattr() + classTime;
                    Particle particle = new Particle(particleLocation);
                    isFixedTimeParticleList.add(particle);
                }
            }
        }
        //划分为固定时间和非固定时间的
        particleList.addAll(unFixedTimeParticleList);
        particleList.addAll(isFixedTimeParticleList);
        return particleList;
    }


    /***
     *  将编码按班级进行分类，形成初始个体（不含教室的初始课表）
     * @param allParticleList 所有的排课粒子
     * @return
     */
    public static Map<String, List<Particle>> transformSubSpecies(List<Particle> allParticleList) {
        //子种群哈希表
        Map<String, List<Particle>> subSpecicesMap = new HashMap<>();
        //获得排课任务中的排课班级
        List<String> classNoList = ClassTaskJDBC.selectDistinctName(ConstantInfo.CLASS_NO);
        for (String classNo : classNoList) {
            List<Particle> particleList = new ArrayList<>();
            for (Particle particle : allParticleList) {
                //划分出班级编码
                if (classNo.equals(ClassSchedulUtil.cutCode(ConstantInfo.CLASS_NO, particle.getLocation()))) {
                    particleList.add(particle);
                }
            }
            //放入种群中
            if (particleList.size() > 1) {
                subSpecicesMap.put(classNo, particleList);


            }
        }
        return subSpecicesMap;
    }


    /***
     * 对粒子进行时间上的初始分配
     * @param particleList 所有的排课粒子
     * @return 初始化时间后的排课粒子
     */
    public static List<Particle> allocatTime(List<Particle> particleList) {
        List<Particle> initParticleList = new ArrayList<>();
        for (Particle particle : particleList) {
            String location = particle.getLocation();
            //获得排课时间
            String classTime = ClassSchedulUtil.cutCode(ConstantInfo.CLASS_TIME, location);
            if (classTime.equals("00")) {
                //随机化选取一个不重复的时间片值,
                classTime = ClassSchedulUtil.randomTime(location, initParticleList);
                location = location.substring(0, 29) + classTime;
            }
            particle.setLocation(location);
            //初始化分配完成
            initParticleList.add(particle);
        }
        return initParticleList;
    }

    /**
     * 解决冲突，同一时间一个教师上多门课的冲突
     *
     * @param swarmList 排课任务粒子
     * @return
     */
    public static List<Particle> conflictResolution(List<Particle> swarmList) {
        exit:
        for (int i = 0; i < swarmList.size(); ++i) {
            String location = swarmList.get(i).getLocation();//粒子编码，也就是粒子位置
            String teacherNo = ClassSchedulUtil.cutCode(ConstantInfo.TEACHER_NO, location);//教师编号
            String classTime = ClassSchedulUtil.cutCode(ConstantInfo.CLASS_TIME, location);//上课时间
            for (int j = i + 1; j < swarmList.size(); ++j) {
                String tempLocation = swarmList.get(j).getLocation();
                String tempTeacherNo = ClassSchedulUtil.cutCode(ConstantInfo.TEACHER_NO, tempLocation);
                String tempClassTime = ClassSchedulUtil.cutCode(ConstantInfo.CLASS_TIME, tempLocation);
                //有同一时间同一个教师上多门课的现象
                if (teacherNo.equals(tempTeacherNo) && classTime.equals(tempClassTime)) {
                    //更新冲突的粒子的位置
                    String newClassTime = ClassSchedulUtil.randomTime(location, swarmList);
                    location = location.substring(0, 29) + newClassTime;
                    continue exit;
                }

            }
        }
        return swarmList;
    }

    public static String getNextValue(String location) {
        String courseAttr = ClassSchedulUtil.cutCode(ConstantInfo.COURSE_ATTR, location);//获得课程属性
        String classTime = ClassSchedulUtil.cutCode(ConstantInfo.CLASS_TIME, location);//上课时间
        String[][] values = null;
        if (courseAttr.equals(ConstantInfo.PROFESSIONAL_CODE)) {
            //专业课
            values = ConstantInfo.PROPESSIONAL_VALUE;
        } else if (courseAttr.equals(ConstantInfo.ELECTIVE_CODE)) {
            //选修课
            values = ConstantInfo.ELECTIVE_VALUE;
        } else if (courseAttr.equals(ConstantInfo.PHYSICAL_CODE)) {
            //体育课
            values = ConstantInfo.PHYSICAL_VALUE;
        } else  {
            //实验课
            values = ConstantInfo.EXPERIMENT_VALUE;;
        }
        return ClassSchedulUtil.getRandomValue(values, classTime);
    }
    /**
     * 运用遗传算法中的交叉操作来解决离散粒子群位置更新，即在一定概率下进行两两粒子的交叉操作
     *
     * @param subSpeciesPartilesMap
     * @param gBestParticleMap
     * @return
     */
    public static List<Particle> selectiveGene(Map<String, List<Particle>> subSpeciesPartilesMap, Map<String, Particle> gBestParticleMap) {
        List<Particle> swarmList = new ArrayList<>();
        //对每个子种群进行独立操作
        for (String classNo : subSpeciesPartilesMap.keySet()) {
            List<Particle> individualList = subSpeciesPartilesMap.get(classNo);
            Particle gBestParticle = gBestParticleMap.get(classNo);
            //对每个粒子进行单独更新
            for (Particle particle : individualList) {
                //获得当前粒子的位置和粒子历史最优位置
                String location = particle.getLocation();
//                //获取个体最优解位置
                String pBestLocation = particle.getpBestLocation();
//                //获得全局最优位置
                String gBestLocation = gBestParticle.getLocation();
//                //如果排课粒子时间不是固定的
                if (ClassSchedulUtil.cutCode(ConstantInfo.IS_FIX, location).equals("1")) {
//                    //获得他们对应的时间
                    String firstClassTime = getNextValue(location);
                    String secondClassTime = getNextValue(pBestLocation);
                    String thirdClassTime = getNextValue(gBestLocation);
//                    //首先是将粒子和个体历史最优解粒子进行交叉操作
//                    if (generator.nextDouble() >0.6) {
//                        location = location.substring(0, 29) + secondClassTime;
//                        pBestLocation = pBestLocation.substring(0, 29) + firstClassTime;
//                        particle.setpBestLocationForce(particle.getpBestValue(), pBestLocation);
//                    }
                    //然后是交叉后的粒子和全局最优粒子进行交叉
                    if (generator.nextDouble() > 0.5) {
                        secondClassTime = getNextValue(location);
                        location = location.substring(0, 29) + thirdClassTime;
                        gBestLocation = gBestLocation.substring(0, 29) + secondClassTime;
                    }
                }
//                更新粒子位置
                particle.setLocation(location);
                gBestParticle.setLocation(gBestLocation);
            }
            swarmList.addAll(individualList);
        }
        return swarmList;
    }

    /**
     * 准备开始分配教室
     *
     * @param allParticleMap
     * @return
     */
    public static List<Particle> finalResult(List<Particle> allParticleMap) {
        List<Particle> resultList = new ArrayList<>();//用来存放结果（加上教室编号的基因）
        String classroomNo;//教室编号
        List<String> collegeNoList = ClassTaskJDBC.selectDistinctName(ConstantInfo.COLLEGE_NO);//学院编号集合
        Map<String, List<Particle>> map = geneByCollege(allParticleMap, collegeNoList);//将粒子按学院分配
        for (String collegeNo : map.keySet()) {
            String teachBuildNo = ClassTaskJDBC.selectTeachBuildNo(collegeNo);//根据教务处划分的教学区域，查询学院对应的教学楼编号
            //根据教学楼编号查询出该教学楼下所有的教室来进行随机分配
            List<ClassroomLocation> classroomLocationList = ClassTaskJDBC.selectClassRoomLocation(teachBuildNo);
            List<Particle> tempResultGeneList = map.get(collegeNo);//根据学院编号查询出该学院下的粒子准备开始安排教室
            for (Particle particle : tempResultGeneList) {
                String location = particle.getLocation();
                classroomNo = assignClassroom(location, classroomLocationList, resultList);
                location = location + classroomNo;
                particle.setLocation(location);
                resultList.add(particle);
            }
        }

        return resultList;
    }

    /**
     * 分配教室
     *
     * @param location              粒子编码，即粒子位置信息
     * @param classroomLocationList 该教学楼的所有教师
     * @param resultList
     * @return
     */
    public static String assignClassroom(String location, List<ClassroomLocation> classroomLocationList, List<Particle> resultList) {
        List<ClassroomLocation> sportsBuildingInfo = ClassTaskJDBC.selectClassRoomLocation("08");//体育大楼
        List<ClassroomLocation> experimentalBuildingInfo = ClassTaskJDBC.selectClassRoomLocation("09");//实验大楼
        List<ClassroomLocation> medicalBuildingInfo = ClassTaskJDBC.selectClassRoomLocation("10");//医学实验大楼

        String classNo = ClassSchedulUtil.cutCode(ConstantInfo.CLASS_NO, location);//班级编号
        int studentNumber = ClassTaskJDBC.selectStudentNumber(classNo);//班级学生数量
        String courseAttr = ClassSchedulUtil.cutCode(ConstantInfo.COURSE_ATTR, location);//课程属性
        //如果课程属性是"03"表示体育课，从体育楼里选择一个教室
        if (courseAttr.equals(ConstantInfo.PHYSICAL_CODE)) {
            return chooseClassroom(studentNumber, location, sportsBuildingInfo, resultList);
        } else if (courseAttr.equals(ConstantInfo.ELECTRICITY_CODE) || courseAttr.equals(ConstantInfo.COMPUTER_CODE) || courseAttr.equals(ConstantInfo.PHYSICAL_EXPERIMENT_CODE)) {
            //如果课程属性是"08"、"10"、"12"表示电子实验课、计算机实验课、物理实验课则需要在实验楼里选择一个教室
            return chooseClassroom(studentNumber, location, experimentalBuildingInfo, resultList);
        } else if (courseAttr.equals(ConstantInfo.MEDICAL_CODE)) {
            //如果课程属性是"04"表示医学实验课，需要从医学楼里选择一个教室
            return chooseClassroom(studentNumber, location, medicalBuildingInfo, resultList);
        } else {
            //理论实验课码值以及其他特殊的码值，不需要在特殊的教学楼里分配。由教务处指定的楼已经可以直接获取
            return chooseClassroom(studentNumber, location, classroomLocationList, resultList);
        }
    }

    //选择教室
    private static String chooseClassroom(int studentNumber, String location, List<ClassroomLocation> classroomLocationList, List<Particle> resultList) {
        int min = 0;
        int max = classroomLocationList.size() - 1;
        int temp = min + (int) (Math.random() * (max + 1 - min));//生成一个随机数，用来选取一个教室
        ClassroomLocation classroomLocation = classroomLocationList.get(temp);
        if (judgingClassroom(studentNumber, location, classroomLocation, resultList)) {
            return classroomLocation.getClassroomno();
        } else {
            return chooseClassroom(studentNumber, location, classroomLocationList, resultList);
        }
    }

    /**
     * 判断教室是否符合要求，教室属性和课程属性是否对应、教室容量是否大于学生上课人数
     * @param studentNumber
     * @param location
     * @param classroomLocation
     * @param resultList
     * @return
     */
    private static Boolean judgingClassroom(int studentNumber, String location, ClassroomLocation classroomLocation, List<Particle> resultList) {
        String courseAttr = ClassSchedulUtil.cutCode(ConstantInfo.COURSE_ATTR, location);
        //如果课程属性是"01"或者"02"则表示是理论课教室，则教室属性为"01"即可
        if (courseAttr.equals(ConstantInfo.PROFESSIONAL_CODE) || courseAttr.equals(ConstantInfo.ELECTIVE_CODE)) {
            //课程属性"01"或者"02"同时教室属性为"01"
            if (classroomLocation.getClassroomattr().equals("01")) {
                //教室容量大于学上课人数
                if (studentNumber <= classroomLocation.getCapacity()) {
                    //判断教室上课时间是否重复
                    return isRedundant(location, resultList, classroomLocation);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            //如果课程属性不为"01或者"02"则课程属性和教室属性一一对应
            if (ClassSchedulUtil.cutCode(ConstantInfo.COURSE_ATTR, location).equals(classroomLocation.getClassroomattr())) {
                //教室容量大于学生容量
                if (studentNumber <= classroomLocation.getCapacity()) {
                    //判断教室上课时间是否重复
                    return isRedundant(location, resultList, classroomLocation);
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

    }

    //判断在同一时间片是否有同时两个班级在同一个教室上课
    private static Boolean isRedundant(String location, List<Particle> resultList, ClassroomLocation classroomLocation) {
        //当教室还没有开始分配时，第一个分配的班级可以随意分配教室而不会重复
        if (resultList.size() == 0) {
            return true;
        } else {
            for (Particle particle : resultList) {
                String tempLocation = particle.getLocation();
                //如果分配的教室已经在之前分配了则需要去判断时间是否有冲突
                if (ClassSchedulUtil.cutCode(ConstantInfo.CLASSROOM_NO, tempLocation).equals(classroomLocation.getClassroomno())) {
                    //如果时间一样的话测表示有冲突
                    if (ClassSchedulUtil.cutCode(ConstantInfo.CLASS_TIME, location).equals(ClassSchedulUtil.cutCode(ConstantInfo.CLASS_TIME, tempLocation))) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 将粒子集合按照学院进行划分
     *
     * @param resultGeneList
     * @param collegeNoList
     * @return
     */
    private static Map<String, List<Particle>> geneByCollege(List<Particle> resultGeneList, List<String> collegeNoList) {
        Map<String, List<Particle>> map = new HashMap<>();
        for (String collegeNo : collegeNoList) {
            List<Particle> particleList = new ArrayList<>();
            for (Particle particle : resultGeneList) {
                String location = particle.getLocation();
                if (ClassSchedulUtil.cutCode(ConstantInfo.COLLEGE_NO, location).equals(collegeNo)) {
                    particleList.add(particle);
                }
            }
            if (particleList.size() > 0) {
                map.put(collegeNo, particleList);
            }
        }
        return map;
    }
}
