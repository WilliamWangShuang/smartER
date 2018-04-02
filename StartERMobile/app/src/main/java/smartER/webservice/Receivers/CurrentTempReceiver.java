package smartER.webservice.Receivers;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import com.example.william.starter_mobile.SmartERMobileUtility;
import smartER.webservice.WeatherWebservice;

public class CurrentTempReceiver extends BroadcastReceiver {
    private AlarmManager alarmMgr;
    private Intent i;
    private PendingIntent pi;
    private String tvTempTxt;
    private Context mContext;

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
        mContext = context;
        WeatherFactorial f = new WeatherFactorial();
        f.execute();
    }

    private class WeatherFactorial extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String currTemp = "";
            try {
                currTemp = Double.toString(WeatherWebservice.getCurrentTemperature());
            } catch (Exception ex) {
                Log.e("WS_WEATHER_ERROR", SmartERMobileUtility.getExceptionInfo(ex));
            }
            return currTemp;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.d("SmartERDebug","****Set Alarm****");
            tvTempTxt = result;
            Intent backToActivityIntent = new Intent("currTempIntentBroadcasting");
            backToActivityIntent.putExtra("currTemp", tvTempTxt);
            mContext.sendBroadcast(backToActivityIntent);
        }
    }
}
