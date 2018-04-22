package com.example.william.starter_mobile;

import android.app.Application;
import android.content.Context;

import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;

import smartER.db.SmartERDbHelper;
import smartER.db.SmartERDbUtility;
import smartER.webservice.MapWebservice;
import smartER.webservice.SmartERUserWebservice;

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
    // resident adddress
    private static String address;
    // resident first name
    private static String firstName;
    // Random generator
    private final static Random random = new Random();
    // current Context
    private static Context mContext;
    // sync one record result
    private static String syncOneRecordResult;
    // latitude of current user
    private static double latitude;
    // longtitude of current user
    private static double longtiude;

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

    public static String getAddress() {
        return address;
    }

    public static String getFirstName() {
        return firstName;
    }

    public static void setAddress(String address) {
        SmartERMobileUtility.address = address;
    }

    public static void setFirstName(String firstName) {
        SmartERMobileUtility.firstName = firstName;
    }

    public static void setmContext(Context mContext) {
        SmartERMobileUtility.mContext = mContext;
    }

    public static double getLatitude() {
        return latitude;
    }

    public static double getLongtiude() {
        return longtiude;
    }

    public static void setLatitude(double latitude) {
        SmartERMobileUtility.latitude = latitude;
    }

    public static void setLongtiude(double longtiude) {
        SmartERMobileUtility.longtiude = longtiude;
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

        int workCount = 0;
        // air conditioner can only work between 9am and 11pm. and up to work 10 hrs.
        while (workCount < 10) {
            // Generate air conditioner work time
            int generatedWorkTime = SmartERMobileUtility.getRandomIntegerNumber(0, 25);
            if(9 <= generatedWorkTime && generatedWorkTime <= 23  && !workTime.contains(generatedWorkTime)) {
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

    public static String getSyncOneRecordResult() {
        return syncOneRecordResult;
    }

    public static void setSyncOneRecordResult(String syncOneRecordResult) {
        SmartERMobileUtility.syncOneRecordResult = syncOneRecordResult;
    }

    // get the resident usage info object based on latlng key in map
    public static MapWebservice.ResidentUsageInfoEntity getReidentBasedOnLatLngMapKey(int resid, List<MapWebservice.ResidentUsageInfoEntity> residentInfoList) {
        MapWebservice.ResidentUsageInfoEntity resident = null;
        for (MapWebservice.ResidentUsageInfoEntity residentUsageInfoEntity : residentInfoList) {
            if (resid == residentUsageInfoEntity.getResId()) {
                resident = residentUsageInfoEntity;
                break;
            }
        }

        return resident;
    }

    // parse JSONObject for one usage data
    public static JSONObject parseJsonObjForOneData() throws IOException, JSONException {
        SmartERDbUtility smartERDbUtility;
        JSONObject result = new JSONObject();
        // get user profile json object
        JSONObject userProfileJsonObject = SmartERUserWebservice.findCurrentUserById();

        // get appliance usage from SQLite db
        smartERDbUtility = new SmartERDbUtility(getmContext());
        SmartERDbUtility.AppUsageEntity appUsageEntity = smartERDbUtility.getCurrentHourAppUsage(SmartERMobileUtility.getCurrentHour(), SmartERMobileUtility.getResId());

        // parse result Json object
        if (appUsageEntity != null) {
            result.put(Constant.WS_KEY_AC_USAGE, appUsageEntity.getAcUsage());
            result.put(Constant.WS_KEY_FRIDGE_USAGE, appUsageEntity.getFirdgeUsage());
            result.put(Constant.WS_KEY_RESID, userProfileJsonObject);
            result.put(Constant.WS_KEY_TEMPERATURE, (int) SmartERMobileUtility.getCurrentTemp());
            SimpleDateFormat df = new SimpleDateFormat(Constant.SERVER_DATE_FORMAT);
            result.put(Constant.WS_KEY_USAGE_DATE, df.format(new Date()));
            result.put(Constant.WS_KEY_USAGE_HOUR, SmartERMobileUtility.getCurrentHour());
            result.put(Constant.WS_KEY_WM_USAGE, appUsageEntity.getWmUsage());
        }

        return result;
    }

    // parse Json object for all data in SQLite
    public static JSONArray parseJsonObjForAllData() throws IOException, JSONException {
        JSONArray result = new JSONArray();
        // get user profile json object
        JSONObject userProfileJsonObject = SmartERUserWebservice.findCurrentUserById();

        // get SQLite data helper
        SmartERDbUtility smartERDbUtility = new SmartERDbUtility(getmContext());
        // get all data in SQLite
        List<SmartERDbUtility.AppUsageEntity> appUsageList = smartERDbUtility.getAllExistData();
        for (SmartERDbUtility.AppUsageEntity entity : appUsageList) {
            JSONObject temp = new JSONObject();
            temp.put(Constant.WS_KEY_AC_USAGE, entity.getAcUsage());
            temp.put(Constant.WS_KEY_FRIDGE_USAGE, entity.getFirdgeUsage());
            temp.put(Constant.WS_KEY_RESID, userProfileJsonObject);
            temp.put(Constant.WS_KEY_TEMPERATURE, (int)SmartERMobileUtility.getCurrentTemp());
            SimpleDateFormat df =  new SimpleDateFormat(Constant.SERVER_DATE_FORMAT);
            temp.put(Constant.WS_KEY_USAGE_DATE, df.format(new Date()));
            temp.put(Constant.WS_KEY_USAGE_HOUR, SmartERMobileUtility.getCurrentHour());
            temp.put(Constant.WS_KEY_WM_USAGE, entity.getWmUsage());
            result.put(temp);
        }

        return result;
    }

    //  encrypt password
    public static String encryptPwd(String pwd) {
        String result = "";

        try{
            // Create MessageDigest instance for MD5
            MessageDigest md = MessageDigest.getInstance("MD5");
            //Add password bytes to digest
            md.update(pwd.getBytes());
            //Get the hash's bytes
            byte[] bytes = md.digest();
            //This bytes[] has bytes in decimal format;
            //Convert it to hexadecimal format
            StringBuilder sb = new StringBuilder();
            for(int i=0; i< bytes.length ;i++)
            {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Get complete hashed password in hex format
            result = sb.toString();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
