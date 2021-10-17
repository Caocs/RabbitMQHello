package com.java.ccs.study.rabbitmq.demo.util;

/**
 * @author caocs
 * @date 2021/10/10
 */
public class SleepUtil {

    public static void sleep(int second) {
        try {
            Thread.sleep(1000 * second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
