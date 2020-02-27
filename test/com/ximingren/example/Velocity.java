package com.ximingren.example;

/**
 * @ClassName Velocity
 * @Description 粒子速度类
 * @Author ximingren
 * @Date 2020/2/27 10:54
 */
public class Velocity {
    private double[] vel;

    public Velocity(double[] vel) {
        this.vel = vel;
    }

    public double[] getVel() {
        return vel;
    }

    public void setVel(double[] vel) {
        this.vel = vel;
    }
}
