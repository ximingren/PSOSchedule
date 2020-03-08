package com.ximingren.scheduleDemo;

/**
 * @ClassName PSOConstants
 * @Description TODO
 * @Author ximingren
 * @Date 2020/2/27 10:45
 */
public interface PSOConstants {
    int SWARM_SIZE=30;//种群大小
    int MAX_ITERATION = 1000;//迭代次数
    int PROBLEM_DIMENSION=2;//解域空间的维度
    double C1=0.8;//概率因子C1
    double C2=0.8;//概率因子C2
    double W_UPPERBOUND=1.0;//惯性因子
    double W_LOWERBOUND=0.0;//惯性因子
    double INCREASE_VALUE = 5.0;
    double ITERATION_REINIT = 300;
    double INFERIOR_VALUE = 5;
    double INCREASE_SCALE = 0.8;
}
