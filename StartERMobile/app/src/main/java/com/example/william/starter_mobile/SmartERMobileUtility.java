package com.example.william.starter_mobile;

import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import smartER.db.SmartERDbHelper;

public class SmartERMobileUtility extends Application {
    // Global variable - currenet temperature
    private static double currentTemp;
    // washing machine start work time;
    private static int wsStartWorkTime;
    // air conditioner work time
    private  static ArrayList<Integer> workTime;
    // flag to indicate if keep generating washing machine data for continuous consideration
    private static boolean isContinuGenerate;
    // resident ID
    private static int resId;
    // Random generator
    private final static Random random = new Random();
    // current Context
    private static Context mContext;

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

    // getters and setters
    public static void setCurrentTemp(double val){
        currentTemp = val;
    }

    public static double getCurrentTemp(){
        return currentTemp;
    }

    public static int getWsStartWorkTime() {
        return wsStartWorkTime;
    }

    public static ArrayList<Integer> getWorkTime() {
        return workTime;
    }

    public static boolean isContinuGenerate() {
        return isContinuGenerate;
    }

    public static void setWsStartWorkTime(int val) {
        wsStartWorkTime = val;
    }

    public static void setWorkTime(ArrayList<Integer> val) {
        workTime = val;
    }

    public static void setContinuGenerate(boolean continuGenerate) {
        isContinuGenerate = continuGenerate;
    }

    public static int getResId() {
        return resId;
    }

    public static void setResId(int resId) {
        SmartERMobileUtility.resId = resId;
    }

    public static void setmContext(Context mContext) {
        SmartERMobileUtility.mContext = mContext;
    }

    public static Context getmContext() {
        return mContext;
    }

    // reset base values that are used to generate appliance data
    public static void resetCtxBasedValue() {
        // initial flag isContinueGenerate
        SmartERMobileUtility.setContinuGenerate(false);
        // initial air conditioner work time
        ArrayList<Integer> workTime = new ArrayList<Integer>();

        // Generate a washing machine start work time
        SmartERMobileUtility.setWsStartWorkTime(SmartERMobileUtility.getRandomIntegerNumber(6, 18));
        // Generate air conditioner work time
        int generatedWorkTime = SmartERMobileUtility.getRandomIntegerNumber(0, 24);
        int workCount = 0;
        // air conditioner can only work between 9am and 11pm. and up to work 10 hrs.
        while (9 <= generatedWorkTime && generatedWorkTime <= 23 && workCount < 10) {
            if(!workTime.contains(generatedWorkTime)) {
                generatedWorkTime = SmartERMobileUtility.getRandomIntegerNumber(0, 25);
                workTime.add(generatedWorkTime);
                workCount ++;
            }
        }
        SmartERMobileUtility.setWorkTime(workTime);
    }

    // get current hour
    public static int getCurrentHour() {
        // get current system time hour
        Date date = new Date();   // current date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int currentH = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
        return currentH;
    }
}
