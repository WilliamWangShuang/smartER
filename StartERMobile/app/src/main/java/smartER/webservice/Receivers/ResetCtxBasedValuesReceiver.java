package smartER.webservice.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.example.william.starter_mobile.SmartERMobileUtility;


public class ResetCtxBasedValuesReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;

    public ResetCtxBasedValuesReceiver() {}

    public ResetCtxBasedValuesReceiver(Context context) {
        // Initial alarm manager used to set repeat clock
        alarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        // Define which intent to be broadcast to context
        i = new Intent(context, ResetCtxBasedValuesReceiver.class);
        // get current pending board cast intent in context
        pi = PendingIntent.getBroadcast(context,0, i,0);
        // Set repeater do the job of the intent every hour
        alarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),24 * 3600 * 1000, pi);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ResetCtxBasedValueFactorial f = new ResetCtxBasedValueFactorial();
        f.execute();
    }

    private class ResetCtxBasedValueFactorial extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Log.d("SmartERDebug","****Reset context values****");

            // Every 24 hours, reset context value those which are used to work as the base of generating apps usage
            SmartERMobileUtility.resetCtxBasedValue();

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d("SmartERDebug", "reset context data finish.");
        }
    }
}
