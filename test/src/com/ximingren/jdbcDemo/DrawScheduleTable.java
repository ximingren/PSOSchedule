package com.ximingren.jdbcDemo;

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
        for (Particle particle : particleList) {
            Location location = particle.getLocation();
            scheduleTable.put(location.getLoc()[1], location.getLoc()[0]);
        }
        System.out.println("========"+"表头"+"========");
        for (int i = 1; i <= 5; i++) {
            for (int j = 0; j < 5; j++) {
                String index = String.valueOf(i + (j * 5));
                if (i + (j * 5) < 10) {
                    index = "0" + (i + (j * 5));
                }
                System.out.print(scheduleTable.get(index)+" ");
//                System.out.print(i+(j*7)+" ");
            }
            System.out.println("\n");
        }
    }

    public static void main(String[] args) {
//        init();
    }
}
