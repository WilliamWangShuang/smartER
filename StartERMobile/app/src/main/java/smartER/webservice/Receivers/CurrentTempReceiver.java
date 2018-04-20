package smartER.webservice.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.example.william.starter_mobile.SmartERMobileUtility;

import smartER.Factories.WeatherFactorial;
import smartER.webservice.WeatherWebservice;

public class CurrentTempReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;

    public CurrentTempReceiver() {

    }

    public CurrentTempReceiver(Context context){
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        i = new Intent(context, CurrentTempReceiver.class);
        pi = PendingIntent.getBroadcast(context,0, i,0);
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),5000, pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        WeatherFactorial f = new WeatherFactorial(context);
        f.execute();
    }
}
