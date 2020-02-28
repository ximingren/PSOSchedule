package com.ximingren.psoExample.full;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * @ClassName PSOUtility
 * @Description 工具类
 * @Author ximingren
 * @Date 2020/2/27 11:48
 */
public class PSOUtility {
    public static int getMinPos(double[] list) {
        int pos=0;
        double minValue = list[0];
        for (int i = 0; i < list.length; i++) {
            if (list[i] < minValue) {
                pos=i;
                minValue = list[i];
            }
        }
        return pos;
    }

    public static void readProperty() throws IOException {
        Properties properties = new Properties();
        properties.load(Object.class.getResourceAsStream("/Z.properties"));
        Set<Map.Entry<Object, Object>> entrySet = properties.entrySet();
        for (Map.Entry<Object, Object> entry : entrySet) {
            System.out.println(entry.getKey()+" "+entry.getValue());
        }
    }

    public static void main(String[] args) {
        try {
            readProperty();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
