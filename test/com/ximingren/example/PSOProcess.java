package com.ximingren.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @ClassName PSOProcess
 * @Description TODO
 * @Author ximingren
 * @Date 2020/2/27 10:45
 */
public class PSOProcess implements PSOConstants {
    //粒子集合
    private List<Particle> swarm = new ArrayList<Particle>();
    //每个粒子的最优解时所在的位置
    private List<Location> pBestLocation = new ArrayList<>();
    //每个粒子的最优解
    private double[] pBest = new double[SWARM_SIZE];

    //全局最优解
    private double gBest;
    //全局最优解时所在的位置
    private Location gBestLocation;

    //适应度集合
    private double[] fitnessValueList = new double[SWARM_SIZE];

    //随机数生成器，将生成0.0到1.0之间的double值
    Random generator = new Random();

    public void execute() {
        //初始化种群
        initializeSwarm();
        //更新适应值列表
        updateFitnessList();
        //初始化pBest和pBestLocation
        for (int i = 0; i < SWARM_SIZE; i++) {
            pBest[i] = fitnessValueList[i];
            pBestLocation.add(swarm.get(i).getLocation());
        }
        int t = 0;
        double w;
        double err = 9999;
        //双重判断,迭代次数和错误率
        while (t < MAX_ITERATION && err > ProblemSet.ERR_TOLERANCE) {
            //1.更新pBest
            for (int i = 0; i < SWARM_SIZE; i++) {
                if (fitnessValueList[i] < pBest[i]) {
                    pBest[i] = fitnessValueList[i];
                    pBestLocation.set(i, swarm.get(i).getLocation());
                }
            }
            //2.更新gBest
            //因为待解的问题是找到适应度函数的最小值
            int bestParticleIndex = PSOUtility.getMinPos(fitnessValueList);
            if (t == 0 || fitnessValueList[bestParticleIndex] < gBest) {
                gBest = fitnessValueList[bestParticleIndex];
                gBestLocation = swarm.get(bestParticleIndex).getLocation();
            }

            w = W_UPPERBOUND - ((double) t / MAX_ITERATION) * (W_UPPERBOUND - W_LOWERBOUND);
            for (int i = 0; i < SWARM_SIZE; i++) {
                double r1 = generator.nextDouble();
                double r2 = generator.nextDouble();

                Particle particle = swarm.get(i);

                //对每个粒子更新速度
                double[] newVel = new double[PROBLEM_DIMENSION];
                newVel[0] = (w * particle.getVelocity().getVel()[0])
                        + (r1 * C1) * (pBestLocation.get(i).getLoc()[0] - particle.getLocation().getLoc()[0])
                        + (r2 * C2) * (gBestLocation.getLoc()[0] - particle.getLocation().getLoc()[0]);
                newVel[1] = (w * particle.getVelocity().getVel()[1])
                        + (r1 * C1) * (pBestLocation.get(i).getLoc()[1] - particle.getLocation().getLoc()[1])
                        + (r2 * C2) * (gBestLocation.getLoc()[1] - particle.getLocation().getLoc()[1]);
                Velocity velocity = new Velocity(newVel);
                particle.setVelocity(velocity);

                //更新粒子的位置
                double[] newLoc = new double[PROBLEM_DIMENSION];
                newLoc[0] = particle.getLocation().getLoc()[0] + newVel[0];
                newLoc[1] = particle.getLocation().getLoc()[1] + newVel[1];
                Location location = new Location(newLoc);
                particle.setLocation(location);

                System.out.println("===" + i + "===");
            }
            err = ProblemSet.evaluate(gBestLocation) - 0;
            System.out.println("迭代" + t + " ");
            System.out.println("     Best X: " + gBestLocation.getLoc()[0]);
            System.out.println("     Best Y: " + gBestLocation.getLoc()[1]);
            System.out.println("     Value: " + ProblemSet.evaluate(gBestLocation));

            t++;
            updateFitnessList();
        }
    }

    /**
     * 初始化种群
     */
    public void initializeSwarm() {
        Particle particle;
        for (int i = 0; i < SWARM_SIZE; i++) {
            particle = new Particle();

            //初始化随机位置
            double[] loc = new double[PROBLEM_DIMENSION];
            loc[0] = ProblemSet.LOC_X_LOW + generator.nextDouble() * (ProblemSet.LOC_X_HIGH - ProblemSet.LOC_X_LOW);
            loc[1] = ProblemSet.LOC_Y_LOW + generator.nextDouble() * (ProblemSet.LOC_Y_HIGH - ProblemSet.LOC_Y_LOW);
            Location location = new Location(loc);

            double[] vel = new double[PROBLEM_DIMENSION];
            vel[0] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
            vel[1] = ProblemSet.VEL_LOW + generator.nextDouble() * (ProblemSet.VEL_HIGH - ProblemSet.VEL_LOW);
            Velocity velocity = new Velocity(vel);

            particle.setLocation(location);
            particle.setVelocity(velocity);
            swarm.add(particle);
        }
    }

    /**
     * 更新适应值列表
     */
    public void updateFitnessList() {
        for (int i = 0; i < SWARM_SIZE; i++) {
            fitnessValueList[i] = swarm.get(i).getFitnessValue();
        }
    }
}
