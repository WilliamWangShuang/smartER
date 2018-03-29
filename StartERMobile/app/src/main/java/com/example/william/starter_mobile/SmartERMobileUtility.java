package com.example.william.starter_mobile;

public class SmartERMobileUtility {
    public static String getExceptionInfo(Exception ex) {
        StringBuilder sb = new StringBuilder((ex.getMessage() + "\n"));
        for (StackTraceElement el : ex.getStackTrace()) {
            sb.append(el.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}
