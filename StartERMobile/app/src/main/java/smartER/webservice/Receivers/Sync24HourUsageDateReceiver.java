package smartER.webservice.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import smartER.db.SmartERDbUtility;
import smartER.webservice.SmartERUsageWebservice;

public class Sync24HourUsageDateReceiver extends BroadcastReceiver {

    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;

    public Sync24HourUsageDateReceiver() {}

    public Sync24HourUsageDateReceiver(Context context){
        // Initial alarm manager used to set repeat clock
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Define which intent to be broadcast to context
        i = new Intent(context, SmartERUsageWebservice.class);
        // get current pending board cast intent in context
        pi = PendingIntent.getBroadcast(context,0, i,0);
        // Set repeater do the job of the intent every hour
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Sync24HourUsageDateFactorial sync24HourUsageDateFactorial = new Sync24HourUsageDateFactorial();
        sync24HourUsageDateFactorial.execute();
    }

    private class Sync24HourUsageDateFactorial extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d("SmartERDebug","****Sync 24 hour data to server****");
            // TODO: call method in usage ws to do the sync logic
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //mContext.sendBroadcast(new Intent("startGenerateAppDataSignal"));
            Log.d("SmartERDebug", "generate data for apps thread finish.");
        }
    }
}
