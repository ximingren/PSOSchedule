package com.ximingren.psoExample.full;

/**
 * @ClassName PSOConstants
 * @Description TODO
 * @Author ximingren
 * @Date 2020/2/27 10:45
 */
public interface PSOConstants {
    int SWARM_SIZE=30;//种群大小
    int MAX_ITERATION = 100;//迭代次数
    int PROBLEM_DIMENSION=2;//解域空间的维度
    double C1=2.0;//学习因子1
    double C2=2.0;//学习因子2
    double W_UPPERBOUND=1.0;//惯性因子
    double W_LOWERBOUND=0.0;//惯性因子
}
