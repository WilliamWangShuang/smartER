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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import smartER.Factories.SyncAllRecordFactorial;
import smartER.Factories.SyncOneRecordFactorial;
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

    // call RESTful web serice to get daily total usage or hourly usages for all residents for a specific date
    public static List<JSONObject> getDailyTotalUsageOrHourlyUsagesForAllResident(String viewType, Date date) throws IOException, JSONException {
        List<JSONObject> result = new ArrayList<>();

        // convert date to URL param string
        DateFormat df = new SimpleDateFormat(Constant.DATE_FORMAT);
        String paramDate = df.format(date);
        // construct ws URL
        StringBuilder urlBuilder = new StringBuilder(Constant.MAP_WS_CHOOSE_VIEW);
        if (Constant.MAP_VIEW_HOURLY.equals(viewType)) {
            urlBuilder.append(paramDate);
            urlBuilder.append("/");
            urlBuilder.append(Constant.MAP_VIEW_HOURLY);
        } else if (Constant.MAP_VIEW_DAILY.equals(viewType)) {
            urlBuilder.append(paramDate);
            urlBuilder.append("/");
            urlBuilder.append(Constant.MAP_VIEW_DAILY);
        } else {
            urlBuilder.append(paramDate);
            urlBuilder.append("/");
            urlBuilder.append(Constant.MAP_VIEW_DAILY);
        }
        Log.d("SmartERDebug", "ws url for getting usage based on view type on map view:" + urlBuilder.toString());
        // call webservice to get reuslt from server
        JSONArray jsonArray = webservice.requestWebServiceArray(urlBuilder.toString());
        // construct return list
        int position = 0;
        while (position < jsonArray.length()) {
            JSONObject jsonObj = jsonArray.getJSONObject(position);
            result.add(jsonObj);
            position ++;
        }

        return result;
    }

    // call RESTful web serice to get all hourly usage for one resident for a specific date
    public static List<JSONObject> getHourlyUsageByResIdAndDate(int resid, Date date) throws IOException, JSONException {
        List<JSONObject> result = new ArrayList<>();

        // format date to string
        SimpleDateFormat f = new SimpleDateFormat(Constant.DATE_FORMAT);
        String urlParamDate = f.format(date);

        // construct url
        StringBuilder sBuilder = new StringBuilder(Constant.FIND_HOURLY_USAGE_BY_RESID_DATE);
        sBuilder.append(resid).append("/").append(urlParamDate).append("/").append(Constant.MAP_VIEW_HOURLY);
        // call ws to get query result
        JSONArray jsonArray = webservice.requestWebServiceArray(sBuilder.toString());

        // construct return result
        // construct return list
        int position = 0;
        while (position < jsonArray.length()) {
            JSONObject jsonObj = jsonArray.getJSONObject(position);
            result.add(jsonObj);
            position ++;
        }

        return result;
    }

    // call RESTful web service to get all monthly usage for one resident for a specific month, current year
    public static List<JSONObject> getMonthlyUsageByResIdAndMonth(int resid, int month) throws IOException, JSONException {
        List<JSONObject> result = new ArrayList<>();

        // construct url
        StringBuilder sBuilder = new StringBuilder(Constant.FIND_MONTHLY_USAGE_BY_RESID_MONTH);
        sBuilder.append(resid).append("/").append(month);
        // call ws to get query result
        JSONArray jsonArray = webservice.requestWebServiceArray(sBuilder.toString());

        // construct return result
        // construct return list
        int position = 0;
        while (position < jsonArray.length()) {
            JSONObject jsonObj = jsonArray.getJSONObject(position);
            result.add(jsonObj);
            position ++;
        }

        return result;
    }

    // call RESTful ws to get total usage in 24 hours for each appliance by resid and date
    public static JSONObject getAppUsageByResIdAndDate(int resid, String date)  throws IOException, JSONException {
        JSONObject result = new JSONObject();

        // create request url
        StringBuilder sp = new StringBuilder(Constant.FIND_APP_USAGE_BY_RESID_DATE_URL);
        sp.append(resid).append("/").append(date);
        Log.d("SmartERDebug", "getAppUsageByResIdAndDate url:" + sp.toString());
        //call ws to get request result
        result = webservice.requestWebService(sp.toString());

        return result;
    }
}
