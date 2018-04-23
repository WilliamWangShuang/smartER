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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import smartER.Factories.AppGenerateFactorial;
import smartER.db.SmartERDbUtility;

public class AppDataGenerator extends BroadcastReceiver {

    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;

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
        AppGenerateFactorial f = new AppGenerateFactorial(context);
        f.execute();
    }
}
