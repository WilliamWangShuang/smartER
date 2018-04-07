package smartER.webservice;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;
import java.io.IOException;
import java.util.Date;
import smartER.db.SmartERDbUtility;

public class SmartERUsageWebservice {
    private String syncOneRecordResult;

    // call RESTful web service to do the POST request
    public String syncOneRecord2SeverDb(){
        syncOneRecordResult = "";
        SyncOneRecordFactorial smartERUsageFactorial = new SyncOneRecordFactorial();
        smartERUsageFactorial.execute();

        // return result to main activity
        return syncOneRecordResult;
    }

    // parse JSONObject for one usage data
    private JSONObject parseJsonObjForOneData(Context context) throws IOException, JSONException{
        JSONObject result = new JSONObject();
        // get user profile json object
        JSONObject userProfileJsonObject = SmartERUserWebservice.findCurrentUserById();

        // get appliance usage from SQLite db
        SmartERDbUtility smartERDbUtility = new SmartERDbUtility(context);
        SmartERDbUtility.AppUsageEntity appUsageEntity = smartERDbUtility.getCurrentHourAppUsage(SmartERMobileUtility.getCurrentHour(), SmartERMobileUtility.getResId());

        // parse result Json object
        result.put(Constant.WS_KEY_AC_USAGE, appUsageEntity.getAcUsage());
        result.put(Constant.WS_KEY_FRIDGE_USAGE, appUsageEntity.getFirdgeUsage());
        result.put(Constant.WS_KEY_RESID, userProfileJsonObject);
        result.put(Constant.WS_KEY_TEMPERATURE, SmartERMobileUtility.getCurrentTemp());
        result.put(Constant.WS_KEY_USAGE_DATE, new Date());
        result.put(Constant.WS_KEY_USAGE_HOUR, SmartERMobileUtility.getCurrentHour());
        result.put(Constant.WS_KEY_WM_USAGE, appUsageEntity.getWmUsage());

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
            syncOneRecordResult = result;
        }
    }
}
