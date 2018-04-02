package smartER.webservice.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.william.starter_mobile.SmartERMobileUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AppDataGenerator extends BroadcastReceiver {

    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;
    // array store current 24 hour fridge usage
    private double firdge24HourUsage[];
    // array store current 24 hour washing machine usage
    private double ws24HourUsage[];
    // array store current 24 hour air conditioner usage
    private double ac24HourUsage[];
    // washing machine start work time;
    private int wsStartWorkTime;
    // air conditioner work time
    ArrayList<Integer> workTime;

    public AppDataGenerator() {}

    public AppDataGenerator(Context context){
        // initial arrays of applicance usage
        firdge24HourUsage = new double[24];
        ws24HourUsage = new double[24];
        ac24HourUsage = new double[24];
        // initial air conditioner work time
        workTime = new ArrayList<Integer>();

        // Generate a washing machine start work time
        wsStartWorkTime = SmartERMobileUtility.getRandomIntegerNumber(6, 18);
        // Generate air conditioner work time
        int generatedWorkTime = SmartERMobileUtility.getRandomIntegerNumber(0, 25);
        int workCount = 1;
        while (9 <= generatedWorkTime && generatedWorkTime <= 11 && workCount <= 10) {
            workTime.add(generatedWorkTime);
            workCount ++;
        }

        // Initial alarm manager used to set repeat clock
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Define which intent to be broadcast to context
        i = new Intent(context, AppDataGenerator.class);
        // get current pending board cast intent in context
        pi = PendingIntent.getBroadcast(context,0, i,0);
        // Set repeater do the job of the intent every 24 hours (86400000 milliseconds)
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),3600000, pi);
    }

    // when this receiver receive the captured intent broadcasting to do the work in this onReceive method
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("SmartERDebug","****Set App generator****");
        // get current system time hour
        Date date = new Date();   // current date
        Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
        calendar.setTime(date);   // assigns calendar to given date
        int currentH = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format

        // load fridge generated data in array
        firdge24HourUsage[currentH] = generateFridgeData();
        // load washing machine generated data in array
        ws24HourUsage[currentH] = (currentH >= wsStartWorkTime && currentH < wsStartWorkTime + 3) ? generateWSHourlyUData() : 0.0;
        // load air conditioner data in array
        ac24HourUsage[currentH] = (workTime.contains(currentH)) ? generateACHourlyUData() : 0.0;

        System.out.println("fridge:" + firdge24HourUsage[currentH] + "ws:" + ws24HourUsage[currentH] + "ac:" + ac24HourUsage[currentH]);
    }

    public double[] getFirdge24HourUsage() {
        return firdge24HourUsage;
    }

    public double[] getWs24HourUsage() {
        return ws24HourUsage;
    }

    public double[] getAc24HourUsage() {
        return ac24HourUsage;
    }

    // Generate firdge hourly usage between 0.3 kwh and 0.8 kwh
    private double generateFridgeData() {
        return SmartERMobileUtility.getRandomDoubleNumber(0.3, 0.8);
    }

    // Generate washing machine hourly usage between 0.4 kwh and 1.3 kwh
    private double generateWSHourlyUData() {
        return SmartERMobileUtility.getRandomDoubleNumber(0.4, 1.3);
    }

    // Generate air conditioner hourly usage between 1.0 kwh and 5.0 kwh
    private double generateACHourlyUData() {
        return SmartERMobileUtility.getRandomDoubleNumber(1.0, 5.0);
    }
}
