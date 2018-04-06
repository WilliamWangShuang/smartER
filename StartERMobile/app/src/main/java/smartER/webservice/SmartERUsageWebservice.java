package smartER.webservice;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;

import java.io.IOException;

public class SmartERUsageWebservice {
    // call RESTful web service to do the POST request
    public String syncOneRecord2SeverDb(JSONObject jsonParam) throws IOException {
        // process result
        String result = "";

        try {
            result = webservice.postWebService(Constant.SMARTER_WS_ELECTRICITY_URL, jsonParam);
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }

    // parse JSONObject for one usage data
    public JSONObject parseJsonObjForOneData(){
        // TODO: format a json object that used to invoke sync one data in main activity
        JSONObject jsonObject = null;

        return jsonObject;
    }
}
