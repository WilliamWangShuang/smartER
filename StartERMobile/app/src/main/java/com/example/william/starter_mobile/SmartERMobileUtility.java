package com.example.william.starter_mobile;

import java.util.Random;

public class SmartERMobileUtility {
    // Random generator
    private final static Random random = new Random();

    // format exception message
    public static String getExceptionInfo(Exception ex) {
        StringBuilder sb = new StringBuilder((ex.getMessage() + "\n"));
        for (StackTraceElement el : ex.getStackTrace()) {
            sb.append(el.toString());
            sb.append("\n");
        }

        return sb.toString();
    }

    // Generate a double number in a given range [min,max)
    public static double getRandomDoubleNumber(double minValue, double maxValue){
        // return random number in range
        return  Math.round((minValue + (maxValue - minValue) * random.nextDouble()) * 100) / 100.0;
    }

    // Generate a integer in a given range [min, max)
    public static int getRandomIntegerNumber(int minValue, int maxValue){
        // return random number in range
        return  random.nextInt((maxValue - minValue) + 1) + minValue;
    }
}
