package com.ximingren.example;

/**
 * @ClassName Particle
 * @Description 粒子类
 * @Author ximingren
 * @Date 2020/2/27 10:50
 */
public class Particle {
    private double fitnessValue;//粒子的适应度
    private Velocity velocity;//粒子的速度
    private Location location;//粒子所在的位置
    public Particle(){
        super();
    }
    public Particle(double fitnessValue, Velocity velocity, Location location) {
        this.fitnessValue = fitnessValue;
        this.location = location;
        this.velocity = velocity;
    }
    public Velocity getVelocity(){
        return velocity;
    }
    public void setVelocity(Velocity velocity){
        this.velocity=velocity;
    }
    public Location getLocation(){
        return location;
    }
    public void setLocation(Location location){
        this.location=location;
    }
    public double getFitnessValue(){
        //粒子执行适应度函数，得到适应度值
        fitnessValue = ProblemSet.evaluate(location);
        return fitnessValue;
    }
}
