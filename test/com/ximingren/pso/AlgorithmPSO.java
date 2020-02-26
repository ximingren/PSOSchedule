package com.ximingren.pso;

/**
 * ClassName AlgorithmPSO
 * Description 源码在这里 https://gitee.com/dengfaheng/PSOSimpleExample?_from=gitee_search，我这个只是自己跟着敲了一遍方便理解
 * 通过求解函数y=-x*(x-2)在[0,2]上最大值的粒子群算法
 * Author ximingren
 * Date 2020/2/26 11:02
 */
public class AlgorithmPSO {
    private int n = 2;//粒子个数
    private double[] y;//适应度值
    private double[] x;//x和y是粒子们所在的位置
    private double[] v;//粒子速度集合
    private double c1 = 2;//学习因子
    private double c2 = 2;//学习因子
    private double pbest[];//pbest是单独粒子自己到目前为止发现最好的位置
    private double gbest; //gbest是pbest中的最好值
    private double vmax = 0.1;//速度最大值

    //适应度计算函数，每个粒子都有它的适应度
    //这里的适应度函数是根据求解函数y=-x*(x-2)在[0,2]上最大值的粒子群算法
    private void fitnessFunction() {
        for (int i = 0; i < n; i++) {
            y[i] = -1 * x[i] * (x[i] - 2);
        }
    }

    //初始化
    private void init() {
        x = new double[n];
        v = new double[n];
        y = new double[n];
        pbest = new double[n];
        //手动放置两个粒子的初始位置
        x[0] = 0.0;
        x[1] = 2.0;
        v[0] = 0.01;
        v[1] = 0.02;
        fitnessFunction();
        for (int i = 0; i < n; i++) {
            pbest[i] = y[i];
            if (y[i] > gbest) gbest = y[i];
        }
        System.out.println("算法开始，起始最优解:" + gbest);
        System.out.println("\n");
    }

    /**
     * 粒子群算法
     *
     * @param max 迭代次数
     */
    private void PSO(int max) {
        for (int i = 0; i < max; i++) {
            double w = 0.4;//惯性因子
            for (int j = 0; j < n; j++) {
                //更新位置和速度，根据公式三更新速度，公式二更新粒子当前位置
                v[j] = w * v[j] + c1 * Math.random() * (pbest[j] - x[j]) + c2 * Math.random() * (gbest - x[j]);
                if (v[j] > vmax) v[j] = vmax;//控制速度不超过最大值
                x[j] += v[j];
                //越界判断，范围限定在[0,2]中
                if (x[j] > 2) x[j] = 2;
                if (x[j] < 0) x[j] = 0;
            }
            fitnessFunction();
            //更新个体极值和群体极值
            for (int j = 0; j < n; j++) {
                pbest[j] = getMax(y[j], pbest[j]);
                if (pbest[j] > gbest) gbest = pbest[j];
                System.out.println("粒子n" + j + ":x=" + x[j] + " " + "v= " + v[j]);
            }
            System.out.println("第" + (i + 1) + "次迭代,全局最优解gbest=" + gbest);
            System.out.println("\n");
        }
    }

    private double getMax(double a, double b) {
        return a > b ? a : b;
    }

    public static void main(String[] args) {
        AlgorithmPSO algorithmPSO = new AlgorithmPSO();
        algorithmPSO.init();
        algorithmPSO.PSO(100);
    }
}
