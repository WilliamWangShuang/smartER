package smartER.webservice;

import android.util.Log;

import com.example.william.starter_mobile.Constant;
import com.mapbox.mapboxsdk.geometry.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MapWebservice {
    // Original data retrieve from weather service
    private static JSONObject mapInfoByWS = null;

    // get latitude by address
    public static ResidentMapEntity getLatLngAndUsageByAddress(List<SmartERUserWebservice.UserProfile> users, List<JSONObject> usageDataJsons, String viewType) throws IOException, JSONException {
        // start to construct latLng map of all residents
        Map<Integer, LatLng> LatLngProviders = new LinkedHashMap<>();
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
            LatLngProviders.put(users.get(position).getResId(), latLng);
            position++;
        }

        // start to construct resident info list
        List<ResidentUsageInfoEntity> residentUsageInfoEntities = new ArrayList<>();
        for (JSONObject jsonObj : usageDataJsons) {
            // get resident id from json object
            int resid = jsonObj.getInt(Constant.WS_KEY_RESID);
            // get totalUsage from json object
            double totalUsage = jsonObj.getDouble(Constant.WS_KEY_MAP_TOTAL_USAGE);
            // declare hour
            int hour = 0;
            if (Constant.MAP_VIEW_DAILY.equals(viewType)) {
                // set hour to -1 due to daily view
                hour = -1;
            } else if (Constant.MAP_VIEW_HOURLY.equals(viewType)){
                // get hour from json
                hour = jsonObj.getInt(Constant.WS_KEY_MAP_TIME);
            }
            // construct resident info entity
            ResidentUsageInfoEntity resEntity = new ResidentUsageInfoEntity(resid, hour, totalUsage);
            // add it into list
            residentUsageInfoEntities.add(resEntity);
        }

        // construct return result
        ResidentMapEntity result = new ResidentMapEntity(LatLngProviders, residentUsageInfoEntities);
        return result;
    }

    // the map entity result class used to transfer latLng info and user usage info for all residents
    public static class ResidentMapEntity {
        // list of locations of all residents
        private Map<Integer, LatLng> latLngMap;
        // list of usage info for all residents
        private List<ResidentUsageInfoEntity> residentInfoList;

        // constructors
        public ResidentMapEntity() {}

        public ResidentMapEntity(Map<Integer, LatLng> latLngMap, List<ResidentUsageInfoEntity> residentInfoList) {
            this.latLngMap = latLngMap;
            this.residentInfoList = residentInfoList;
        }

        // getters
        public Map<Integer, LatLng> getLatLngMap() {
            return latLngMap;
        }
        public List<ResidentUsageInfoEntity> getResidentInfoList() { return residentInfoList; }
    }

    // class to store user usage info
    public static class ResidentUsageInfoEntity {
        private int resId;
        // the hour of the resident total usage. If daily view, set to -1
        private int hour;
        // total usage. If hourly view, represents hourly total. If daily view, represent daily total.
        private double totalUsage;

        // constructors
        public ResidentUsageInfoEntity() {}

        public ResidentUsageInfoEntity(int resid, int hour, double totalUsage) {
            this.resId = resid;
            this.hour = hour;
            this.totalUsage = totalUsage;
        }

        // getters
        public int getResId() {
            return resId;
        }

        public int getHour() {
            return hour;
        }

        public double getTotalUsage() {
            return totalUsage;
        }
    }
}
