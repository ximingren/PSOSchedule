package com.ximingren.pso;

import java.util.Map;

/**
 * @ClassName AlgorithmPSO2
 * @Description 用PSO算法求解f(x, y)=x3+3xy2-15x-12y的最大值,参考https://blog.csdn.net/qq_27124771/article/details/80945337的代码
 * @Author ximingren
 * @Date 2020/2/26 16:09
 */
public class AlgorithmPSO2 {
    private int n;//粒子数量
    private ParticleState[] p;//粒子数组
    private ParticleState[] v;//速度数组
    private ParticleState[] pbest;//粒子最优解
    private ParticleState gbest;//全局最优解
    private double vmax;//最大速度
    private int c1, c2;//学习因子

    //适应度函数
    public void fitnessFunction() {
        for (int i = 0; i < n; i++) {
            p[i].f = p[i].x * p[i].x * p[i].x + 3 * p[i].x * p[i].y * p[i].y - 15 * p[i].x - 12 * p[i].y;
        }
    }

    public void init() {
        n = 1000;//一千个粒子
        p = new ParticleState[n];
        v = new ParticleState[n];
        v = new ParticleState[n];
        pbest = new ParticleState[n];
        gbest = new ParticleState(0.0, 0.0);
        c1 = c2 = 2;
        vmax = 0.1;
        for (int i = 0; i < n; i++) {
            p[i] = new ParticleState(Math.random(), Math.random());
            v[i] = new ParticleState(Math.random() * vmax, Math.random() * vmax);
        }
        fitnessFunction();
        gbest.f = Integer.MIN_VALUE;
        for (int i = 0; i < n; i++) {
            pbest[i] = p[i];
            if (p[i].f > gbest.f) {
                gbest = p[i];
            }
        }
        System.out.println("初始最优值：" + gbest.x + "  " + gbest.y + "  " + gbest.f);
    }

    public void PSO(int max) {
        for (int i = 0; i < max; i++) {
            double w = 0.3;//惯性因子（惯性权重）
            for (int j = 0; j < n; j++) {
                double vx = w * v[j].x + c1 * Math.random() * (pbest[j].x - p[j].x) + c2 * Math.random() * (gbest.x - p[j].x);
                double vy = w * v[j].y + c1 * Math.random() * (pbest[j].y - p[j].y) + c2 * Math.random() * (gbest.y - p[j].y);

                if (vx > vmax) vx = vmax;
                if (vy > vmax) vy = vmax;

                v[j] = new ParticleState(vx, vy);
                //更新粒子位置
                p[j].x += v[j].x;
                p[j].y += v[j].y;
            }
            fitnessFunction();

            //更新个体极值和群体极值
            for (int j = 0; j < n; j++) {
                if (p[j].f > pbest[j].f) {
                    pbest[j] = p[j];
                }
                if (p[j].f > gbest.f) {
                    gbest = p[j];
                }
            }
            System.out.println("===" + i + "====" + gbest.x + "  " + gbest.y + "  " + gbest.f);
        }
    }

    public static void main(String[] args) {
        AlgorithmPSO2 algorithmPSO2 = new AlgorithmPSO2();
        algorithmPSO2.init();
        algorithmPSO2.PSO(100);
    }

    /**
     * 粒子状态类
     */
    class ParticleState {
        public double x;
        public double y;
        public double f;//适应度

        ParticleState(double x, double y) {
            this.x = x;
            this.y = y;

        }
    }
}
