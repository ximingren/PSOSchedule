package com.ximingren.example;

/**
 * @ClassName Location
 * @Description 粒子位置类
 * @Author ximingren
 * @Date 2020/2/27 10:59
 */
public class Location {
    private double[] loc;

    public Location(double[] loc) {
        this.loc = loc;
    }

    public double[] getLoc() {
        return loc;
    }

    public void setLoc(double[] loc) {
        this.loc = loc;
    }
}
