package smartER.webservice.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.example.william.starter_mobile.SmartERMobileUtility;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import smartER.webservice.WeatherWebservice;

public class AppDataGenerator extends BroadcastReceiver {

    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;

    // current temperature
    private double currTemp;
    // washing machine start work time used in thread
    private int wsStartTime;
    // air conditioner working time used in thread
    private ArrayList<Integer> acWorkTime;
    // is continue work flag for washing machine used in thread
    private boolean isContinueWork;

    public AppDataGenerator() {}

    public AppDataGenerator(Context context){
        // Initial alarm manager used to set repeat clock
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Define which intent to be broadcast to context
        i = new Intent(context, AppDataGenerator.class);
        // get current pending board cast intent in context
        pi = PendingIntent.getBroadcast(context,0, i,0);
        // Set repeater do the job of the intent every hour
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),3600 * 1000, pi);
    }

    // when this receiver receive the captured intent broadcasting to do the work in this onReceive method
    @Override
    public void onReceive(Context context, Intent intent) {
        AppGenerateFactorial f = new AppGenerateFactorial();
        f.execute();
    }

    // Generate firdge hourly usage between 0.3 kwh and 0.8 kwh
    private double generateFridgeData() {
        return SmartERMobileUtility.getRandomDoubleNumber(0.3, 0.8);
    }

    // Generate washing machine hourly usage between 0.4 kwh and 1.3 kwh
    private double generateWSHourlyUData() {
        // generate random value to indicate if keep generating ws data or not. 1 means yes, and 0 means no.
        SmartERMobileUtility.setContinuGenerate(SmartERMobileUtility.getRandomIntegerNumber(0, 2) == 1);
        return SmartERMobileUtility.getRandomDoubleNumber(0.4, 1.3);
    }

    // Generate air conditioner hourly usage between 1.0 kwh and 5.0 kwh
    private double generateACHourlyUData() {
        return SmartERMobileUtility.getRandomDoubleNumber(1.0, 5.0);
    }

    private class AppGenerateFactorial extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // get washing machine start time
            wsStartTime = SmartERMobileUtility.getWsStartWorkTime();
            // get air conditioner working time
            acWorkTime = SmartERMobileUtility.getWorkTime();
            // get isContinue flag for washing machine
            isContinueWork = SmartERMobileUtility.isContinuGenerate();

            // get current temperature
            currTemp = SmartERMobileUtility.getCurrentTemp();
            Log.d("SmartERDebug","currT:"+currTemp);
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("SmartERDebug","****Set App generator****");

            // get current system time hour
            Date date = new Date();   // current date
            Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
            calendar.setTime(date);   // assigns calendar to given date
            int currentH = calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format

            // load fridge generated data in array
            double currentHourFridgeUsage = generateFridgeData();
            // load washing machine generated data in array
            double currentHourWSUsage = (currentH >= wsStartTime && currentH < wsStartTime + 3) && isContinueWork ? generateWSHourlyUData() : 0.0;
            // load air conditioner data in array
            double currentHourACusage = acWorkTime.contains(currentH) && currTemp > 20.0 ? generateACHourlyUData() : 0.0;

            System.out.println("fridge:" + currentHourFridgeUsage + ",ws:" + currentHourWSUsage + ",ac:" + currentHourACusage);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //mContext.sendBroadcast(new Intent("startGenerateAppDataSignal"));
            Log.d("SmartERDebug", "generate data for apps thread finish.");
        }
    }
}
