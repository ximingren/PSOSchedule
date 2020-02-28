package com.ximingren.psoExample.full;

/**
 * @ClassName ProblemSet
 * @Description PSO算法待解决的问题
 * @Author ximingren
 * @Date 2020/2/27 11:05
 */
public class ProblemSet {
    public static final double LOC_X_LOW=1;
    public static final double LOC_X_HIGH=4;

    public static final double LOC_Y_LOW=-1;
    public static final double LOC_Y_HIGH=1;

    public static final double VEL_LOW=-1;
    public static final double VEL_HIGH=1;

    //容忍度越小，结果越精确，但迭代次数增加
    public static final double ERR_TOLERANCE=1E-20;

    public static double evaluate(Location location) {
        double result=0;
        double x = location.getLoc()[0];//位置的x位置
        double y = location.getLoc()[1];//位置的y位置

        result = Math.pow(2.8125 - x + x * Math.pow(y, 4), 2)
                + Math.pow(2.25 - x + x * Math.pow(y, 2), 2)
                + Math.pow(1.5 - x + x * y, 2);
        return result;
    }
}
