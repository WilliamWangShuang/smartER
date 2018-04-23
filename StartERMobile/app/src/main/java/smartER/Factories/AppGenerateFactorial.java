package smartER.Factories;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import smartER.db.SmartERDbUtility;

public class AppGenerateFactorial extends AsyncTask<Void, Void, Void> {
    // db helper
    private SmartERDbUtility dbHelper;
    // current temperature
    private double currTemp;
    // washing machine start work time used in thread
    private int wsStartTime;
    // air conditioner working time used in thread
    private ArrayList<Integer> acWorkTime;
    // is continue work flag for washing machine used in thread
    private boolean isContinueWork;
    // total usage for current hour
    private double total;
    // current context
    Context mContext;

    public AppGenerateFactorial(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        // set db helper
        dbHelper = new SmartERDbUtility(SmartERMobileUtility.getmContext());
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
        int currentH = SmartERMobileUtility.getCurrentHour(); // gets hour in 24h format

        // load fridge generated data in array
        double currentHourFridgeUsage = generateFridgeData();
        // load washing machine generated data in array
        double currentHourWSUsage = (currentH >= wsStartTime && currentH < wsStartTime + 3) && isContinueWork ? generateWSHourlyUData() : 0.0;
        // load air conditioner data in array
        double currentHourACusage = acWorkTime.contains(currentH) && currTemp > 20.0 ? generateACHourlyUData() : 0.0;

        // get total usage for current hour
        total = currentHourFridgeUsage + currentHourWSUsage + currentHourACusage;

        // insert hourly usage into SQLite table
        DateFormat df = new SimpleDateFormat(Constant.DATE_FORMAT);
        long rowId = dbHelper.insertAppUsage(df.format(new Date()), currentH, currentHourFridgeUsage, currentHourWSUsage, currentHourACusage, (int)currTemp);
        Log.d("SmartERDebug", "new row id:" + rowId);
        System.out.println("fridge:" + currentHourFridgeUsage + ",ws:" + currentHourWSUsage + ",ac:" + currentHourACusage);

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //mContext.sendBroadcast(new Intent("startGenerateAppDataSignal"));
        Log.d("SmartERDebug", "generate data for apps thread finish.");
        Intent backToActivityIntent = new Intent("currHourTotalUsage");
        backToActivityIntent.putExtra("currHourTotalUsage", total);
        mContext.sendBroadcast(backToActivityIntent);
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
}
