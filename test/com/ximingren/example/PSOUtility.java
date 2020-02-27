package com.ximingren.example;

/**
 * @ClassName PSOUtility
 * @Description 工具类
 * @Author ximingren
 * @Date 2020/2/27 11:48
 */
public class PSOUtility {
    public static int getMinPos(double[] list) {
        int pos=0;
        double minValue = list[0];
        for (int i = 0; i < list.length; i++) {
            if (list[i] < minValue) {
                pos=i;
                minValue = list[i];
            }
        }
        return pos;
    }
}
