package com.ximingren.CourseSchedule.Bean.vo;

/**
 * @ClassName Particle
 * @Description 粒子类
 * @Author ximingren
 * @Date 2020/2/27 10:50
 */
public class Particle {
    private double fitnessValue;//粒子的适应度
    private String location;//粒子所在的位置
    private String pBestLocation;//粒子局部最优解
    private double  pBestValue=0.0;//粒子局部最优解
    public Particle(){
        super();
    }
    public Particle(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setFitnessValue(double fitnessValue) {
        this.fitnessValue = fitnessValue;
    }

    public double getFitnessValue() {
        return this.fitnessValue;
    }

    /**
     * 设置粒子个体最优解
     * @param value
     * @param location
     */
    public void setpBestLocation(double value, String  location) {
        if (pBestValue == 0.0) {
            //第一次设置最优解
            pBestValue = value;
            pBestLocation = location;
        } else if (pBestValue < value) {
            //比最优解值要大
            pBestValue = value;
            pBestLocation = location;
        }
    }

    public void setpBestLocationForce(double value, String  location) {
        pBestLocation = location;
        pBestValue = value;
    }
    public String  getpBestLocation() {
        return this.pBestLocation;
    }
    public double getpBestValue() {
        return this.pBestValue;
    }

    public static void main(String[] args) {
        String[][] s = new String[][]{{"12"}, {"32"}};
        System.out.println(s.length);
    }
}
