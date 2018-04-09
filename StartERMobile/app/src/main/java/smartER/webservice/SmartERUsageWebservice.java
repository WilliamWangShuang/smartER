package smartER.webservice;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import smartER.db.SmartERDbUtility;

public class SmartERUsageWebservice {

    private SmartERDbUtility smartERDbUtility;

    // call RESTful web service to do the POST request
    public void syncOneRecord2SeverDb(View v) {
        SyncOneRecordFactorial smartERUsageFactorial = new SyncOneRecordFactorial();
        smartERUsageFactorial.execute();
    }

    // call RESTful web service to do the POST request to sync all data to server
    public void syncAllRecord2ServerDb() {
        SyncAllRecordFactorial syncAllRecordFactorial = new SyncAllRecordFactorial();
        syncAllRecordFactorial.execute();
    }

    // parse JSONObject for one usage data
    private JSONObject parseJsonObjForOneData(Context context) throws IOException, JSONException {
        JSONObject result = new JSONObject();
        // get user profile json object
        JSONObject userProfileJsonObject = SmartERUserWebservice.findCurrentUserById();

        // get appliance usage from SQLite db
        smartERDbUtility = new SmartERDbUtility(context);
        SmartERDbUtility.AppUsageEntity appUsageEntity = smartERDbUtility.getCurrentHourAppUsage(SmartERMobileUtility.getCurrentHour(), SmartERMobileUtility.getResId());

        // parse result Json object
        result.put(Constant.WS_KEY_AC_USAGE, appUsageEntity.getAcUsage());
        result.put(Constant.WS_KEY_FRIDGE_USAGE, appUsageEntity.getFirdgeUsage());
        result.put(Constant.WS_KEY_RESID, userProfileJsonObject);
        result.put(Constant.WS_KEY_TEMPERATURE, (int)SmartERMobileUtility.getCurrentTemp());
        SimpleDateFormat df =  new SimpleDateFormat(Constant.SERVER_DATE_FORMAT);
        result.put(Constant.WS_KEY_USAGE_DATE, df.format(new Date()));
        result.put(Constant.WS_KEY_USAGE_HOUR, SmartERMobileUtility.getCurrentHour());
        result.put(Constant.WS_KEY_WM_USAGE, appUsageEntity.getWmUsage());

        return result;
    }

    // parse Json object for all data in SQLite
    private JSONArray parseJsonObjForAllData(Context context) throws IOException, JSONException {
        JSONArray result = new JSONArray();
        // get user profile json object
        JSONObject userProfileJsonObject = SmartERUserWebservice.findCurrentUserById();

        // get SQLite data helper
        smartERDbUtility = new SmartERDbUtility(context);
        // get all data in SQLite
        List<SmartERDbUtility.AppUsageEntity> appUsageList = smartERDbUtility.getAllExistData();
        for (SmartERDbUtility.AppUsageEntity entity : appUsageList) {
            JSONObject temp = new JSONObject();
            temp.put(Constant.WS_KEY_AC_USAGE, entity.getAcUsage());
            temp.put(Constant.WS_KEY_FRIDGE_USAGE, entity.getFirdgeUsage());
            temp.put(Constant.WS_KEY_RESID, userProfileJsonObject);
            temp.put(Constant.WS_KEY_TEMPERATURE, (int)SmartERMobileUtility.getCurrentTemp());
            SimpleDateFormat df =  new SimpleDateFormat(Constant.SERVER_DATE_FORMAT);
            temp.put(Constant.WS_KEY_USAGE_DATE, df.format(new Date()));
            temp.put(Constant.WS_KEY_USAGE_HOUR, SmartERMobileUtility.getCurrentHour());
            temp.put(Constant.WS_KEY_WM_USAGE, entity.getWmUsage());
            result.put(temp);
        }

        return result;
    }

    // async factory class to do post task which is for sync one record to server db
    private class SyncOneRecordFactorial extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("SmartERDebug","****sync one record to server****");
            String result = "";
            // get context
            Context context = SmartERMobileUtility.getmContext();

            try {
                // parse POST Json
                JSONObject jsonParam = parseJsonObjForOneData(context);
                Log.d("SmartERDebug", "parsed json to post:" + jsonParam.toString());
                result = webservice.postWebService(Constant.SMARTER_WS_ELECTRICITY_URL, jsonParam);
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
            Log.d("SmartERDebug", result);
            Log.d("SmartERDebug", "sync one record to server finish.");
            //SmartERMobileUtility.setSyncOneRecordResult(result);

            if (Constant.SUCCESS_MSG.equals(result))
                h.sendEmptyMessage(0);
            else
                h.sendEmptyMessage(1);
        }

        // create a handler to toast message on main thread according to the post result
        @SuppressLint("HandlerLeak")
        Handler h = new Handler() {
            public void handleMessage(Message msg){
                if(msg.what == 0)
                    Toast.makeText(SmartERMobileUtility.getmContext(), "Success sync a record for current hour to server", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(SmartERMobileUtility.getmContext(),"Fail sync a record for current hour to server", Toast.LENGTH_LONG).show();
            }
        };
    }

    // async factory class to do post task which is for sync one record to server db
    private class SyncAllRecordFactorial extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            Log.d("SmartERDebug","****sync all record to server****");
            String result = "";
            // get context
            Context context = SmartERMobileUtility.getmContext();

            try {
                // parse POST Json
                JSONArray jsonParam = parseJsonObjForAllData(context);
                Log.d("SmartERDebug", "parsed json to post:" + jsonParam.toString());
                if (jsonParam.length() != 0) {
                    result = webservice.postWebServiceSyncAllData(Constant.CREATE_MULTIPLE_DATA_URL, jsonParam);
                    // truncate SQLite
                    smartERDbUtility = new SmartERDbUtility(SmartERMobileUtility.getmContext());
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
}
