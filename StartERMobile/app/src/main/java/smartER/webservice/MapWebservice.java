package smartER.webservice;

import android.util.Log;

import com.example.william.starter_mobile.Constant;
import com.mapbox.mapboxsdk.geometry.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapWebservice {
    // Original data retrieve from weather service
    private static JSONObject mapInfoByWS = null;

    // get latitude by address
    public static List<LatLng> getLatLngByAddress(List<SmartERUserWebservice.UserProfile> users) throws IOException, JSONException {
        List<LatLng> latLngList = new ArrayList<LatLng>();
        // encode address and generate ws request URL
        StringBuilder urlBuilder = new StringBuilder(Constant.MAP_WS_MULTIPLE_LOCATION_URL);
        for (SmartERUserWebservice.UserProfile userProfile : users){
            userProfile.setAddress(userProfile.getAddress().replaceAll(" ", "%20").replaceAll("/", "%2F"));
            urlBuilder.append(Constant.MAP_WS_LOCATION_URL_PARAM);
            urlBuilder.append(userProfile.getAddress());
            urlBuilder.append(Constant.MAP_WS_POSTCODE_URL_PARAM);
            urlBuilder.append(userProfile.getPostCode());
        }

        // Call the service to get data
        JSONObject jsonObject = webservice.requestWebService(urlBuilder.toString());
        // get location result JSONArray
        JSONArray jsonArray = jsonObject.getJSONArray(Constant.WS_KEY_MAP_RESULT);

        // get latLng json from the ws result
        int position = 0;
        while (position < jsonArray.length()) {
            JSONObject jsonObj = jsonArray.getJSONObject(position);
            LatLng latLng = new LatLng();
            // get json object locations
            JSONArray jsonLocation = jsonObj.getJSONArray(Constant.WS_KEY_MAP_LOCATION);
            // get latlng json obj
            JSONObject jsonLatLng = jsonLocation.getJSONObject(0).getJSONObject(Constant.WS_KEY_MAP_LATLNG);
            // set latlng value
            latLng.setLatitude(jsonLatLng.getDouble(Constant.WS_KEY__MAP_LAT));
            latLng.setLongitude(jsonLatLng.getDouble(Constant.WS_KEY_MAP_LNG));
            latLngList.add(latLng);
            position++;
        }

        return latLngList;
    }
}
