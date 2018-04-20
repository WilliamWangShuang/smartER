package smartER.Factories;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import smartER.db.SmartERDbUtility;
import smartER.webservice.SmartERUserWebservice;
import smartER.webservice.webservice;

// async factory class to do post task which is for sync one record to server db
public class SyncOneRecordFactorial extends AsyncTask<Void, Void, String> {
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
            JSONObject jsonParam = SmartERMobileUtility.parseJsonObjForOneData();
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
