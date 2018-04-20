package smartER.Factories;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.william.starter_mobile.SmartERMobileUtility;

import smartER.webservice.WeatherWebservice;

public class WeatherFactorial extends AsyncTask<Void, Void, String> {
    private String tvTempTxt;
    private Context mContext;

    public WeatherFactorial(Context context) {
        mContext = context;
    }

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
