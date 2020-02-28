package com.ximingren.jdbcDemo;

import com.ximingren.CourseSchedule.Bean.po.ClassTask;
import com.ximingren.CourseSchedule.Bean.vo.ConstantInfo;
import com.ximingren.jdbcDemo.Particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            System.out.println(ClassSchedulUtil.cutGene("classNo", gene) + "  " + classTime);
            Location location = new Location(new String[]{gene, classTime});
            particle.setLocation(location);
            resultGeneList.add(particle);
        }
        return resultGeneList;
    }
}
