package smartER.Factories;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

import smartER.db.SmartERDbUtility;
import smartER.webservice.WeatherWebservice;
import smartER.webservice.webservice;

// async factory class to do post task which is for sync all record to server db
public class SyncAllRecordFactorial extends AsyncTask<Void, Void, String> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... params) {
        Log.d("SmartERDebug","****sync all record to server****");
        String result = "";

        try {
            // get current temperature
            double currTemp = WeatherWebservice.getCurrentTemperature();
            // update global variable - currentTemp
            SmartERMobileUtility.setCurrentTemp(currTemp);
            // parse POST Json
            JSONArray jsonParam = SmartERMobileUtility.parseJsonObjForAllData();
            Log.d("SmartERDebug", "parsed json to post:" + jsonParam.toString());
            if (jsonParam.length() != 0) {
                result = webservice.postWebServiceSyncAllData(Constant.CREATE_MULTIPLE_DATA_URL, jsonParam);
                // truncate SQLite
                SmartERDbUtility smartERDbUtility = new SmartERDbUtility(SmartERMobileUtility.getmContext());
                smartERDbUtility.truncateElectricityTable();
            }
        } catch (IOException ex) {
            Log.e("SmartERDebug", SmartERMobileUtility.getExceptionInfo(ex));
            result = SmartERMobileUtility.getExceptionInfo(ex);
        } catch (JSONException ex){
            Log.e("SmartERDebug", SmartERMobileUtility.getExceptionInfo(ex));
            result = SmartERMobileUtility.getExceptionInfo(ex);
        } catch (Exception ex) {
            Log.e("SmartERDebug", SmartERMobileUtility.getExceptionInfo(ex));
            result = SmartERMobileUtility.getExceptionInfo(ex);
        }

        return result;
    }

    @Override
    protected void onPostExecute(String result) {

    }
}
