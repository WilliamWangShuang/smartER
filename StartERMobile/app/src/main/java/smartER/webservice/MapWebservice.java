package smartER.webservice;

import android.util.Log;

import com.example.william.starter_mobile.Constant;
import com.mapbox.mapboxsdk.geometry.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;

public class MapWebservice {
    // Original data retrieve from weather service
    private static JSONObject mapInfoByWS = null;

    // get latitude by address
    public static LatLng getLatLngByAddress(String address) throws IOException, JSONException {
        LatLng latLng = new LatLng();
        // encode address
        address = address.replaceAll(" ", "%20");

        // Call the service to get data
        JSONObject jsonFromWs = webservice.requestWebService(Constant.MAP_WS_URL + address);

        // get latLng json from the ws result
        JSONArray jsonNodeResult = jsonFromWs.getJSONArray(Constant.WS_KEY_MAP_RESULT);
        JSONObject jsonNodeResultFirstObject = jsonNodeResult.getJSONObject(0);
        JSONArray jsonLocation = jsonNodeResultFirstObject.getJSONArray(Constant.WS_KEY_MAP_LOCATION);
        JSONObject jsonLatLng = jsonLocation.getJSONObject(0).getJSONObject(Constant.WS_KEY_MAP_LATLNG);

        // set latlng value
        latLng.setLatitude(jsonLatLng.getDouble(Constant.WS_KEY__MAP_LAT));
        latLng.setLongitude(jsonLatLng.getDouble(Constant.WS_KEY_MAP_LNG));

        return latLng;
    }
}
