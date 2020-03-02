package com.ximingren.jdbcDemo;

/**
 * @ClassName Location
 * @Description 粒子位置类
 * @Author ximingren
 * @Date 2020/2/27 10:59
 */
public class Location {
    private String[] loc;

    public Location(String[] loc) {
        this.loc = loc;
    }

    public String[] getLoc() {
        return loc;
    }

    public void setLoc(String[] loc) {
        this.loc = loc;
    }
}
