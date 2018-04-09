package smartER.webservice.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import smartER.webservice.SmartERUsageWebservice;

public class Sync24HourUsageDateReceiver extends BroadcastReceiver {

    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;
    // web service object to do the function
    SmartERUsageWebservice smartERUsageWebservice;

    public Sync24HourUsageDateReceiver() {}

    public Sync24HourUsageDateReceiver(Context context){
        // Initial alarm manager used to set repeat clock
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Define which intent to be broadcast to context
        i = new Intent(context, Sync24HourUsageDateReceiver.class);
        // get current pending board cast intent in context
        pi = PendingIntent.getBroadcast(context,0, i,0);
        // Set repeater do the job of the intent every hour
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY, pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // create SmartER usage ws
        smartERUsageWebservice = new SmartERUsageWebservice();
        // do the sync logic
        smartERUsageWebservice.syncAllRecord2ServerDb();
    }
}
