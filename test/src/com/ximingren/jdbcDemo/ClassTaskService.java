package com.ximingren.jdbcDemo;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.vo.ConstantInfo;
import com.ximingren.jdbcDemo.Particle;

import java.util.*;

import static com.ximingren.jdbcDemo.ClassTaskJDBC.selectDistinctClassNo;

/**
 * @ClassName ClassTaskService
 * @Description 计划用二维空间来解，分别为课程信息和时间片段
 * @Author ximingren
 * @Date 2020/2/28 19:34
 */
public class ClassTaskService {
    private static final String UNFIXED_TIME = "unFixedTime";//没有固定时间
    private static final String IS_FIXED_TIME = "isFixedTime";//固定时间
    private static Random generator = new Random();

    /**
     * 将从表中查询的开课任务书对象集合进行编码，组成粒子的课程信息维度的数据
     * 编码规则为：是否固定+年级编号+班级编号+教师编号+课程编号+课程属性+开课时间
     * 其中如果是否固定为否则开课时间默认填充为"00"
     *
     * @param classTaskList
     * @return List<String>
     */
    public static List<Map<String, List<String>>> coding(List<ClassTask> classTaskList) {
        List<Map<String, List<String>>> geneList = new ArrayList<>();
        Map<String, List<String>> particleXListMap = new HashMap<>();
        List<String> unFixedTimeParticleXList = new ArrayList<>();//不固定时间的编码基因组
        List<String> isFixedTimeParticleXList = new ArrayList<>();//固定时间的编码基因组
        for (ClassTask classTask : classTaskList) {
            //根据isFix的值判断是否固定，为1则不固定classTime默认填充”00“
            if (classTask.getIsfix().equals("1")) {
                //计算一周的上课次数，一次对应一个粒子的维度，2次对应两个粒子的维度。依此类推
                int size = classTask.getWeeksnumber() / 2;
                for (int i = 0; i < size; i++) {
                    String particleX = classTask.getIsfix() + classTask.getCollegeno() + classTask.getClassno() + classTask.getTeacherno() + classTask.getCourseno() + classTask.getCourseattr() + "00";
                    unFixedTimeParticleXList.add(particleX);
                }
            }
            //isFix的值为2则classTime有值，需要对classTime的值进行切割
            if (classTask.getIsfix().equals("2")) {
                int size = classTask.getWeeksnumber() / 2;
                for (int i = 0; i < size; i++) {
                    String classTime = classTask.getClasstime().substring(i * 2, (i + 1) * 2);
                    String particleX = classTask.getIsfix() + classTask.getCollegeno() + classTask.getClassno() + classTask.getTeacherno() + classTask.getCourseno() + classTask.getCourseattr() + classTime;
                    isFixedTimeParticleXList.add(particleX);
                }
            }
        }
        //划分为固定时间和非固定时间的
        particleXListMap.put(UNFIXED_TIME, unFixedTimeParticleXList);
        particleXListMap.put(IS_FIXED_TIME, isFixedTimeParticleXList);
        geneList.add(particleXListMap);
        return geneList;
    }

    //将编码按班级进行分类，形成初始个体（不含教室的初始课表）
    public static Map<String, List<String>> transformIndividual(List<String> resultGeneList) {
        Map<String, List<String>> individualMap = new HashMap<>();
        List<String> classNoList = selectDistinctClassNo();
        for (String classNo : classNoList) {
            List<String> geneList = new ArrayList<>();
            for (String gene : resultGeneList) {
                if (classNo.equals(ClassSchedulUtil.cutGene(ConstantInfo.CLASS_NO, gene))) {
                    geneList.add(gene);
                }
            }

            if (geneList.size() > 1) {
                individualMap.put(classNo, geneList);
            }
        }
        return individualMap;
    }

    //将编码按班级进行分类，形成初始个体（不含教室的初始课表）
    public static Map<String, List<Particle>> transformSubSpecies(List<Particle> resultGeneList) {
        Map<String, List<Particle>> individualMap = new HashMap<>();
        List<String> classNoList = selectDistinctClassNo();
        for (String classNo : classNoList) {
            List<Particle> geneList = new ArrayList<>();
            for (Particle gene : resultGeneList) {
                if (classNo.equals(ClassSchedulUtil.cutGene(ConstantInfo.CLASS_NO, gene.getLocation().getLoc()[0]))) {
                    geneList.add(gene);
                }
            }

            if (geneList.size() > 1) {
                individualMap.put(classNo, geneList);
            }
        }
        return individualMap;
    }

    public static List<Particle> codingTime(List<String> geneList) {
        List<Particle> resultGeneList = new ArrayList<>();
        //时间不固定的基因由程序进行随机分配
        for (String gene : geneList) {
            Particle particle = new Particle();
            //获取一个不重复的时间片值
            String classTime = ClassSchedulUtil.cutGene("classTime", gene);
            if (classTime.equals("00")) {
                classTime = ClassSchedulUtil.randomTime(gene, resultGeneList);
                gene = gene.substring(0, 29) + classTime;
            }
            Location location = new Location(new String[]{gene, classTime});
            particle.setLocation(location);
            resultGeneList.add(particle);
        }
        return resultGeneList;
    }

    //解决冲突，同一时间一个教师上多门课的冲突
    public static List<Particle> conflictResolution(List<Particle> resultGeneList) {
        exit:
        for (int i = 0; i < resultGeneList.size(); ++i) {
            String gene = resultGeneList.get(i).getLocation().getLoc()[0];
            String teacherNo = ClassSchedulUtil.cutGene(ConstantInfo.TEACHER_NO, gene);
            String classTime = ClassSchedulUtil.cutGene(ConstantInfo.CLASS_TIME, gene);
            for (int j = i + 1; j < resultGeneList.size(); ++j) {
                String tempGene = resultGeneList.get(j).getLocation().getLoc()[0];
                String tempTeacherNo = ClassSchedulUtil.cutGene(ConstantInfo.TEACHER_NO, tempGene);
                String tempClassTime = ClassSchedulUtil.cutGene(ConstantInfo.CLASS_TIME, tempGene);
                if (teacherNo.equals(tempTeacherNo) && classTime.equals(tempClassTime)) {
                    String newClassTime = ClassSchedulUtil.randomTime(gene, resultGeneList);
                    gene = gene.substring(0, 29) + newClassTime;
                    continue exit;
                }

            }
        }
        return resultGeneList;
    }

    //个体间的随机选择两条基因准备进行杂交并生成一个新个体
    public static List<Particle> selectiveGene(List<Particle> individualList, Particle gBestParticle) {
        int min = 0;
        int max = individualList.size() - 1;
        boolean flag;
        for (Particle particle : individualList) {
                //获得当前粒子的位置和粒子历史最优位置
                String location = particle.getLocation().getLoc()[0];
                String pBestLocation = particle.getpBestLocation().getLoc()[0];
                if (!ClassSchedulUtil.cutGene(ConstantInfo.IS_FIX, location).equals("2")) {                //判断选择的两条基因对应的时间值是否固定，如果固定则重新选择两条
                    //获得他们对应的时间
                    String secondClassTime = ClassSchedulUtil.cutGene(ConstantInfo.CLASS_TIME, pBestLocation);
                    //首先是将粒子和历史粒子进行交叉操作
                    if (generator.nextDouble() > 0.5) {
                        location = location.substring(0, 29) + secondClassTime;
                    }
                    //获得全局最优位置
                    String gBestLocation = gBestParticle.getLocation().getLoc()[0];
                    String thirdClassTime = ClassSchedulUtil.cutGene(ConstantInfo.CLASS_TIME, gBestLocation);
                    //然后是交叉后的粒子和全局最优粒子进行交叉
                    if(generator.nextDouble()>0.6){
                        secondClassTime = ClassSchedulUtil.cutGene(ConstantInfo.CLASS_TIME, location);
                        location = location.substring(0, 29) + thirdClassTime;
                        gBestLocation = gBestLocation.substring(0, 29) + secondClassTime;
                    }
                    //更新粒子位置
                    particle.setLocation(new Location(new String[]{location, thirdClassTime}));
                    gBestParticle.setLocation(new Location(new String[]{gBestLocation, secondClassTime}));
                    //对原有的基因进行移除，然后将交换过时间的两条基因添加进去
                    flag = true;
                }
        }
        return individualList;
    }

}
