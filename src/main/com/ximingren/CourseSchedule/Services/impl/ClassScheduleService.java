package com.ximingren.CourseSchedule.Services.impl;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.po.ClassroomLocation;
import com.ximingren.CourseSchedule.Bean.po.CoursePlan;
import com.ximingren.CourseSchedule.Bean.vo.ConstantInfo;
import com.ximingren.CourseSchedule.Bean.vo.PSOConstants;
import com.ximingren.CourseSchedule.Bean.vo.Particle;
import com.ximingren.CourseSchedule.Dao.*;
import com.ximingren.CourseSchedule.Services.IClassScheduleService;
import com.ximingren.CourseSchedule.Util.ClassScheduleUtil;
import com.ximingren.CourseSchedule.Util.DrawScheduleTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @ClassName ClassScheduleService
 * @Description TODO
 * @Author ximingren
 * @Date 2020/3/7 15:57
 */
@Service
public class ClassScheduleService implements IClassScheduleService {
    @Autowired
    private ClassTaskDao classTaskDao;
    @Autowired
    private LocationInfoDao locationInfoDao;
    @Autowired
    private ClassroomLocationDao classroomLocationDao;
    @Autowired
    private ClassInfoDao classInfoDao;
    @Autowired
    private CoursePlanDao coursePlanDao;
    //子种群哈希表
    private Map<String, List<Particle>> subSpeciesParticlesMap;
    //粒子集合
    private List<Particle> swarm = new ArrayList<>();
    //最初粒子哈希表
    private Map<String, List<Particle>> initSubSpeciesMap = new HashMap<>();
    //子种群最优解粒子的适应度历史记录
    private Map<String, List<Double>> gbestFitnessValueMap = new HashMap<>();
    //全局最优解粒子
    private Map<String, Particle> gBestParticleMap = new HashMap<>();
    //是否到达算法迭代结束标准
    private boolean continueIteration = true;
    //随机数生成器
    private static Random generator = new Random();

    /**
     * 排课主函数
     *
     * @return 是否排课成功
     */
    public boolean executeSchedule() {
        ClassTask classTask = new ClassTask();
        classTask.setSemester("2015-2016-1");
        List<ClassTask> classTaskList = classTaskDao.selectBySemester(classTask);
        //迭代次数
        int iterNum = 0;
        //重新部分初始化次数
        int reRandomTimeNum = 0;
        try {
            //初始化粒子群
            init(classTaskList);
            while (continueIteration) {
                //更新粒子位置
                selectiveParticle();
                //粒子位置更新完成之后，更新适应度值
                updateFitnessList();
                //判断是否到达算法结束标准
                continueIteration = judgeConverge();
                //对部分粒子重新分配时间
                if (++iterNum > PSOConstants.ITERATION_REINIT) {
                    reRandomPartTime();
                    ++reRandomTimeNum;
                }
            }
            //分配教室
            finalResult(swarm);
            showBeforeAndAfterTable(continueIteration);
            System.out.println("迭代次数： " + iterNum);
            System.out.println("重新分配适应值低的排课粒子的时间次数： " + reRandomTimeNum);
            List<CoursePlan> coursePlanList = decoding();
            coursePlanDao.deleteAllTable();
            for (CoursePlan coursePlan : coursePlanList) {
                coursePlanDao.insertCoursePlan(coursePlan);
            }
            for (ClassTask classTask1 : classTaskList) {
                coursePlanDao.updateCoursePlan(classTask1);
            }
            return true;
        } catch (Exception e) {
            System.out.println("PSO算法迭代发送异常" + e);
            return false;
        }
    }

    /**
     * 解码成对象
     * @return
     */
    private List<CoursePlan> decoding() {
        List<CoursePlan> coursePlanList = new ArrayList<>();
        for (Particle particle : swarm) {
            CoursePlan coursePlan = new CoursePlan();
            String location = particle.getLocation();
            coursePlan.setCollegeno(ClassScheduleUtil.cutCode(ConstantInfo.COLLEGE_NO, location));
            coursePlan.setClassno(ClassScheduleUtil.cutCode(ConstantInfo.CLASS_NO, location));
            coursePlan.setCourseno(ClassScheduleUtil.cutCode(ConstantInfo.COURSE_NO, location));
            coursePlan.setTeacherno(ClassScheduleUtil.cutCode(ConstantInfo.TEACHER_NO, location));
            coursePlan.setClassroomno(ClassScheduleUtil.cutCode(ConstantInfo.CLASSROOM_NO, location));
            coursePlan.setClasstime(ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, location));
            coursePlanList.add(coursePlan);
        }
        return coursePlanList;
    }

    private void showBeforeAndAfterTable(boolean flag) {
        if (!flag) {
            for (String classNo : gbestFitnessValueMap.keySet()) {
                System.out.println("初始课表：");
                DrawScheduleTable.draw(initSubSpeciesMap.get(classNo));
                System.out.println("优化后的课表：");
                DrawScheduleTable.draw(subSpeciesParticlesMap.get(classNo));
                System.out.println("============" + classNo + "===============");
                List<Double> list = gbestFitnessValueMap.get(classNo);
                System.out.println(list.get(0) + " " + list.get(list.size() - 1));
                System.out.println("\n");
            }
        }
    }

    /**
     * 准备开始分配教室
     *
     * @param allParticleMap
     * @return
     */
    private void finalResult(List<Particle> allParticleMap) {
        List<Particle> resultList = new ArrayList<>();//用来存放结果（加上教室编号的粒子）
        String classroomNo;//教室编号
        List<String> collegeNoList = classTaskDao.selectByColumnName(ConstantInfo.COLLEGE_NO);//学院编号集合
        Map<String, List<Particle>> map = particleByCollege(allParticleMap, collegeNoList);//将粒子按学院分配
        for (String collegeNo : map.keySet()) {
            String teachBuildNo = locationInfoDao.selectBuildNo(collegeNo);//根据教务处划分的教学区域，查询学院对应的教学楼编号
            //根据教学楼编号查询出该教学楼下所有的教室来进行随机分配
            List<ClassroomLocation> classroomLocationList = classroomLocationDao.selectByTeachBuildNo(teachBuildNo);
            List<Particle> tempResultGeneList = map.get(collegeNo);//根据学院编号查询出该学院下的粒子准备开始安排教室
            for (Particle particle : tempResultGeneList) {
                String location = particle.getLocation();
                classroomNo = assignClassroom(location, classroomLocationList, resultList);
                location = location + classroomNo;
                particle.setLocation(location);
                resultList.add(particle);
            }
        }
    }

    /**
     * 分配教室
     *
     * @param location              粒子编码，即粒子位置信息
     * @param classroomLocationList 该教学楼的所有教室
     * @param resultList            结果粒子集合
     * @return
     */
    private String assignClassroom(String location, List<ClassroomLocation> classroomLocationList, List<Particle> resultList) {
        List<ClassroomLocation> sportsBuildingInfo = classroomLocationDao.selectByTeachBuildNo("08");//体育大楼
        List<ClassroomLocation> experimentalBuildingInfo = classroomLocationDao.selectByTeachBuildNo("09");//实验大楼
        List<ClassroomLocation> medicalBuildingInfo = classroomLocationDao.selectByTeachBuildNo("10");//医学实验大楼

        String classNo = ClassScheduleUtil.cutCode(ConstantInfo.CLASS_NO, location);//班级编号
        int studentNumber = classInfoDao.selectStudentNumber(classNo);//班级学生数量
        String courseAttr = ClassScheduleUtil.cutCode(ConstantInfo.COURSE_ATTR, location);//课程属性
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

    /**
     * 选择教室
     * @param studentNumber 学生数量
     * @param location 粒子位置
     * @param classroomLocationList 教室列表
     * @param resultList 粒子集合
     * @return
     */
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
     *
     * @param studentNumber
     * @param location
     * @param classroomLocation
     * @param resultList
     * @return
     */
    private static Boolean judgingClassroom(int studentNumber, String location, ClassroomLocation classroomLocation, List<Particle> resultList) {
        String courseAttr = ClassScheduleUtil.cutCode(ConstantInfo.COURSE_ATTR, location);
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
            if (ClassScheduleUtil.cutCode(ConstantInfo.COURSE_ATTR, location).equals(classroomLocation.getClassroomattr())) {
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


    /**
     * 判断在同一时间片是否有同时两个班级在同一个教室上课
     * @param location
     * @param resultList
     * @param classroomLocation
     * @return
     */
    private static Boolean isRedundant(String location, List<Particle> resultList, ClassroomLocation classroomLocation) {
        //当教室还没有开始分配时，第一个分配的班级可以随意分配教室而不会重复
        if (resultList.size() == 0) {
            return true;
        } else {
            for (Particle particle : resultList) {
                String tempLocation = particle.getLocation();
                //如果分配的教室已经在之前分配了则需要去判断时间是否有冲突
                if (ClassScheduleUtil.cutCode(ConstantInfo.CLASSROOM_NO, tempLocation).equals(classroomLocation.getClassroomno())) {
                    //如果时间一样的话测表示有冲突
                    if (ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, location).equals(ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, tempLocation))) {
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
    private static Map<String, List<Particle>> particleByCollege(List<Particle> resultGeneList, List<String> collegeNoList) {
        Map<String, List<Particle>> map = new HashMap<>();
        for (String collegeNo : collegeNoList) {
            List<Particle> particleList = new ArrayList<>();
            for (Particle particle : resultGeneList) {
                String location = particle.getLocation();
                if (ClassScheduleUtil.cutCode(ConstantInfo.COLLEGE_NO, location).equals(collegeNo)) {
                    particleList.add(particle);
                }
            }
            if (particleList.size() > 0) {
                map.put(collegeNo, particleList);
            }
        }
        return map;
    }

    /**
     * 对部分粒子重新分配时间
     */
    private void reRandomPartTime() {
        for (Particle particle : swarm) {
            //选出适应度值低的粒子，进行时间重新分配
            if (particle.getFitnessValue() < PSOConstants.INFERIOR_VALUE) {
                //   重新分配时间
                String newTime = ClassScheduleUtil.randomTime(particle.getLocation(), swarm);
                particle.setLocation(particle.getLocation().substring(0, 29) + newTime);
            }
        }
    }

    /**
     * 算法收敛标准，即算法结束标准
     *
     * @return
     */
    private boolean judgeConverge() {
        int classNum = gbestFitnessValueMap.keySet().size();
        int num = 0;
        for (String classNo : gbestFitnessValueMap.keySet()) {
            List<Double> list = gbestFitnessValueMap.get(classNo);
            double subValue = list.get(list.size() - 1) - list.get(0);
            if (subValue > PSOConstants.INCREASE_VALUE) {
                num = num + 1;
            }
        }
        if (num >= (int) (classNum * PSOConstants.INCREASE_SCALE)) {
            return false;
        }
        return true;
    }

    /**
     * 初始化粒子群，父种群中有子种群，子种群即是每个班级的排课粒子集合
     */
    private void init(List<ClassTask> classTaskList) {
        //根据学期，获取排课计划
        if (classTaskList != null) {
            //对排课任务进行粒子化
            List<Particle> allParticleList = coding(classTaskList);
            //根据班级对所有排课粒子进行分组，得到若干个子种群
            subSpeciesParticlesMap = transformSubSpecies(allParticleList);
            //对每个子种群进行初始化
            for (String classNo : subSpeciesParticlesMap.keySet()) {
                //参数是子种群，返回值是分配好时间的子种群粒子列表
                List<Particle> haveTimeParticleList = allocatTime(subSpeciesParticlesMap.get(classNo));
                //加入总种群集合中，方便后续进行冲突检测
                swarm.addAll(haveTimeParticleList);

                //记录最初粒子的位置，方便后续检测优化效果
                List<Particle> initList = new ArrayList<>();
                for (Particle haveTimeParticle : haveTimeParticleList) {
                    Particle temp = new Particle(haveTimeParticle.getLocation());
                    initList.add(temp);
                }
                initSubSpeciesMap.put(classNo, initList);
                gbestFitnessValueMap.put(classNo, new ArrayList<>());
            }
            //所有子种群初始化完之后，进行适应度值的初始化
            updateFitnessList();
        }
    }

    /**
     * 将从表中查询的开课任务书对象集合进行编码，组成粒子的课程信息维度的数据
     * 编码规则为：是否固定+年级编号+班级编号+教师编号+课程编号+课程属性+开课时间
     * 其中如果是否固定为否则开课时间默认填充为"00"
     *
     * @param classTaskList 排课计划列表
     * @return List<Particle> 所有排课粒子
     */
    private List<Particle> coding(List<ClassTask> classTaskList) {
        List<Particle> resultList = new ArrayList<>();//存放编码后的排课粒子
        for (ClassTask classTask : classTaskList) {
            //根据isFix的值判断是否固定，为1则不固定classTime默认填充”00“
            if (classTask.getIsfix().equals("1")) {
                //计算一周的上课次数，一次对应一个粒子，两次对应两个粒子
                int size = classTask.getWeeksnumber() / 2;
                for (int i = 0; i < size; i++) {
                    String particleLocation = classTask.getIsfix() + classTask.getCollegeno() + classTask.getClassno() + classTask.getTeacherno() + classTask.getCourseno() + classTask.getCourseattr() + "00";
                    //确定了粒子的位置后添加进粒子列表中
                    Particle particle = new Particle(particleLocation);
                    resultList.add(particle);
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
                    resultList.add(particle);
                }
            }
        }
        return resultList;
    }

    /**
     * 将粒子集合分组
     * @param allParticleList 总粒子集合
     * @return
     */
    private Map<String, List<Particle>> transformSubSpecies(List<Particle> allParticleList) {
        //子种群哈希表
        Map<String, List<Particle>> subSpecicesMap = new HashMap<>();
        //获得排课任务中的排课班级
        List<String> classNoList = classTaskDao.selectByColumnName(ConstantInfo.CLASS_NO);
        for (String classNo : classNoList) {
            List<Particle> particleList = new ArrayList<>();
            for (Particle particle : allParticleList) {
                //划分出班级编码
                if (classNo.equals(ClassScheduleUtil.cutCode(ConstantInfo.CLASS_NO, particle.getLocation()))) {
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

    /**
     * 分配时间
     * @param particleList 粒子列表
     * @return 分配好时间的粒子列表
     */
    private List<Particle> allocatTime(List<Particle> particleList) {
        List<Particle> haveTimeParticleList = new ArrayList<>();
        for (Particle particle : particleList) {
            //粒子位置
            String location = particle.getLocation();
            //获得排课时间
            String classTime = ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, location);
            if (classTime.equals("00")) {
                //随机化选取一个不重复的时间片段
                classTime = ClassScheduleUtil.randomTime(location, haveTimeParticleList);
                location = location.substring(0, 29) + classTime;
                particle.setLocation(location);
            }
            //加入分配了时间的粒子
            haveTimeParticleList.add(particle);
        }
        return haveTimeParticleList;
    }

    /**
     * 更新适应度值和种群的个体极值及局部极值
     */
    private void updateFitnessList() {
        //检测冲突，解决冲突
        conflictResolution();
        //分子种群进行
        for (String classNo : subSpeciesParticlesMap.keySet()) {
            double allFitnessValue = 0.0;
            if (!gBestParticleMap.keySet().contains(classNo)) {
                //第一次，最优解粒子就设置为第一个粒子
                gBestParticleMap.put(classNo, subSpeciesParticlesMap.get(classNo).get(0));
            }
            //对子种群的每个粒子进行适应度值计算，然后跟最优解粒子进行适应度比较
            for (Particle particle : subSpeciesParticlesMap.get(classNo)) {
                //计算适应度值
                double fitnessValue = ClassScheduleUtil.alculateFitnessValue(particle, subSpeciesParticlesMap.get(classNo));
                //跟最优解粒子进行比较
                if (gBestParticleMap.get(classNo).getFitnessValue() < fitnessValue) {
                    //更新子种群的全局最优解粒子
                    gBestParticleMap.replace(classNo, particle);
                }
                //累积所有排课粒子的总适应度值
                allFitnessValue += fitnessValue;
                particle.setFitnessValue(fitnessValue);
                //设置粒子个体极值
                particle.setpBestLocation(fitnessValue, particle.getLocation());
            }
            //更新子种群的总适应度值
            List<Double> list = gbestFitnessValueMap.get(classNo);
            list.add(allFitnessValue);
            gbestFitnessValueMap.replace(classNo, list);
        }
    }

    /**
     * 解决冲突，同一时间同个老师上多门课的冲突
     */
    private void conflictResolution() {
        exit:
        for (int i = 0; i < swarm.size(); ++i) {
            Particle particle = swarm.get(i);//获得排课粒子
            String location = particle.getLocation();//粒子编码，也就是粒子位置
            String teacherNo = ClassScheduleUtil.cutCode(ConstantInfo.TEACHER_NO, location);//教师编号
            String classTime = ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, location);//上课时间
            for (int j = i + 1; j < swarm.size(); ++j) {
                String tempLocation = swarm.get(j).getLocation();
                String tempTeacherNo = ClassScheduleUtil.cutCode(ConstantInfo.TEACHER_NO, tempLocation);
                String tempClassTime = ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, tempLocation);
                //有同一时间同一个教师上多门课的现象
                if (teacherNo.equals(tempTeacherNo) && classTime.equals(tempClassTime)) {
                    //更新冲突的粒子的位置
                    String newClassTime = ClassScheduleUtil.randomTime(location, swarm);
                    location = location.substring(0, 29) + newClassTime;
                    particle.setLocation(location);
                    continue exit;
                }
            }
        }
    }

    /**
     * 运用遗传算法中的交叉操作来解决离散粒子群位置更新，即在一定概率下进行两两粒子的交叉操作
     */
    private void selectiveParticle() {
        //对每个子种群单独操作
        for (String classNo : subSpeciesParticlesMap.keySet()) {
            List<Particle> individualList = subSpeciesParticlesMap.get(classNo);
            //获得子种群的全局极值粒子
            Particle gBestParticle = gBestParticleMap.get(classNo);
            //对每个粒子进行单独更新
            for (Particle particle : individualList) {
                //获得当前粒子的位置
                String location = particle.getLocation();
                //获取粒子历史最优位置
                String pBestLocation = particle.getpBestLocation();
                //获得子种群全局最优位置
                String gBestLocation = gBestParticle.getLocation();
                //排课粒子时间不固定
                if (ClassScheduleUtil.cutCode(ConstantInfo.IS_FIX, location).equals("1")) {
                    //以C1的概率进行交叉操作，若随机数大于C1，则将粒子和粒子个体历史极值位置进行交叉操作
                    if (generator.nextDouble() > PSOConstants.C1) {

                        String tempTime = ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, location);
                        location = location.substring(0, 29) + getNextValue(location, gBestLocation);
                        particle.setLocation(location);
                        pBestLocation = pBestLocation.substring(0, 29) + tempTime;
                        particle.setpBestLocationForce(particle.getpBestValue(), pBestLocation);
                    }
                    //以C2的概率进行交叉操作，若随机数大于C2，则将粒子和全局极值位置进行交叉操作
                    if (generator.nextDouble() > PSOConstants.C2) {
                        location = particle.getLocation();
                        String tempTime = ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, location);
                        location = location.substring(0, 29) + getNextValue(location, gBestLocation);
                        particle.setLocation(location);
                        gBestLocation = gBestLocation.substring(0, 29) + tempTime;
                        gBestParticle.setLocation(gBestLocation);
                    }
                    //变异操作，增加粒子多样性
                    if (generator.nextDouble() > 0.6) {
                        int min = 0;
                        int max = individualList.size() - 1;
                        int firstIndex = min + (int) (Math.random() * (max + 1 - min));//选取第一个随机数
                        int secondIndex = min + (int) (Math.random() * (max + 1 - min));//选取第二个随机数
                        Particle particle1 = individualList.get(firstIndex);
                        Particle particle2 = individualList.get(secondIndex);
                        String location1 = ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, particle1.getLocation());
                        String location2 = ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, particle2.getLocation());
                        particle1.setLocation(particle1.getLocation().substring(0, 29) + location2);
                        particle2.setLocation(particle2.getLocation().substring(0, 29) + location1);
                    }
                }
            }
        }
    }

    /**
     * 得到下一个排课时间
     * @param firstLocation
     * @param secondLocation
     * @return
     */
    private String getNextValue(String firstLocation, String secondLocation) {
        String courseAttr = ClassScheduleUtil.cutCode(ConstantInfo.COURSE_ATTR, secondLocation);//获得课程属性
        String classTime = ClassScheduleUtil.cutCode(ConstantInfo.CLASS_TIME, secondLocation);//上课时间
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
        } else {
            //实验课
            values = ConstantInfo.EXPERIMENT_VALUE;
        }
        return ClassScheduleUtil.updateRandomValue(values, classTime, firstLocation, swarm, 0);
    }
}
