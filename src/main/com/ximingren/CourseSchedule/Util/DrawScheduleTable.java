package com.ximingren.CourseSchedule.Util;

import com.ximingren.CourseSchedule.Bean.vo.ConstantInfo;
import com.ximingren.CourseSchedule.Bean.vo.Particle;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName DrawScheduleTable
 * @Description TODO
 * @Author ximingren
 * @Date 2020/2/29 11:49
 */
public class DrawScheduleTable {
    private static Map<String, String> scheduleTable = new HashMap<>();

    public static void draw(List<Particle> particleList) {
        scheduleTable.clear();
        int num = 0;
        for (Particle particle : particleList) {
            String location = particle.getLocation();
            String classTime = ClassScheduleUtil.cutCode("classTime", location);
            if (location != null) {
                num = num + 1;
//                System.out.println(location);
            }
            scheduleTable.put(classTime, location);
        }
        System.out.println("课程数量：" + num);
        for (int i = 1; i <= 5; i++) {
            for (int j = 0; j < 5; j++) {
                String index = String.valueOf(i + (j * 5));
                if (i + (j * 5) < 10) {
                    index = "0" + (i + (j * 5));
                }
                try {
                    if (scheduleTable.get(index) != null) {
                        System.out.print(scheduleTable.get(index) + " ");
                    } else {
                        System.out.print(null + "  ");
                    }
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println(scheduleTable.get(index));
                }
//                System.out.print(i+(j*7)+" ");
            }
            System.out.println("\n");
        }
    }

    public static void main(String[] args) {
//        init();
    }
}
