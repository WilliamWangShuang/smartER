package smartER.Factories;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.R;
import com.example.william.starter_mobile.SmartERMobileUtility;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapquest.mapping.maps.MapView;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.OnMapReadyCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import smartER.webservice.MapWebservice;
import smartER.webservice.SmartERUsageWebservice;
import smartER.webservice.SmartERUserWebservice;

public class MapFragmentFactorial extends AsyncTask<Void, Void, MapWebservice.ResidentMapEntity> {
    Bundle savedInstanceState = null;
    MapView mMapView = null;
    LatLng myLocation = null;
    MapWebservice.ResidentMapEntity residentInfo = null;
    MapboxMap mMapboxMap = null;
    List<SmartERUserWebservice.UserProfile> users = null;
    List<JSONObject> dataJson = null;

    // constructor
    public MapFragmentFactorial(MapView mMapView, Bundle savedInstanceState){
        this.savedInstanceState = savedInstanceState;
        this.mMapView = mMapView;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected MapWebservice.ResidentMapEntity doInBackground(Void... params) {
        Log.d("SmartERDebug","****Set map****");

        MapWebservice.ResidentMapEntity result = null;

        try {
            // call ws to get all users
            users = SmartERUserWebservice.findAllUsers();
            // get default view usage json data
            // TODO: should be the date before current date, here for demo purpose, set 2018-3-3
            //Calendar cal = Calendar.getInstance();
            //cal.add(Calendar.HOUR_OF_DAY, -24);
            //Date date = cal.getTime();
            Calendar cal = new GregorianCalendar(2018,2,3);
            Date date = cal.getTime();
            dataJson = SmartERUsageWebservice.getDailyTotalUsageOrHourlyUsagesForAllResident(Constant.MAP_VIEW_DAILY, date);
            Log.d("SmartERDebug", "dataJson size:" + dataJson.size());
            // call ws to generate all Latlng and usage(hourly / daily) info for all users.
            result = MapWebservice.getLatLngAndUsageByAddress(users, dataJson, Constant.MAP_VIEW_DAILY);
        } catch (IOException e) {
            Log.e("SmertERDebug", SmartERMobileUtility.getExceptionInfo(e));
        } catch (JSONException e) {
            Log.e("SmertERDebug", SmartERMobileUtility.getExceptionInfo(e));
        } catch (ParseException e) {
            Log.e("SmertERDebug", SmartERMobileUtility.getExceptionInfo(e));
        }

        return result;
    }

    @Override
    protected void onPostExecute(MapWebservice.ResidentMapEntity result) {
        // TODO: get my location
        myLocation = new LatLng();
        myLocation.setLatitude(-37.87649);
        myLocation.setLongitude(145.04543);

        Log.d("SmartERDebug", "result size:" + result.getResidentInfoList().size());
        residentInfo = result;
        Log.d("SmartERDebug", "*****" + residentInfo.getLatLngMap().entrySet().size());
        for (LatLng l : residentInfo.getLatLngMap().values()) {
            Log.d("SmartERDebug", "" + l.getLatitude() + l.getLongitude());
        }

        // synchronize map view
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
                mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));
                // add makers for all residents
                addMarker(mMapboxMap, residentInfo, Constant.MAP_VIEW_DAILY);
            }
        });
    }

    // add maker on map
    private void addMarker(MapboxMap mapboxMap, MapWebservice.ResidentMapEntity resInfo, String viewType) {
        // Create an Icon object for the marker to use
        IconFactory iconFactory = IconFactory.getInstance(SmartERMobileUtility.getmContext());
        Icon iconGreen = iconFactory.fromResource(R.drawable.marker_green);
        Icon iconRed = iconFactory.fromResource(R.drawable.marker_red);
        // set makers for all resident
        for (Map.Entry<Integer, LatLng> entity : resInfo.getLatLngMap().entrySet()) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(entity.getValue());
            MapWebservice.ResidentUsageInfoEntity residentInfo = SmartERMobileUtility.getReidentBasedOnLatLngMapKey(entity.getKey(), resInfo.getResidentInfoList());
            // if resident obj is null. it means for this resident, no usage data found matching the given time
            double totalUsage = residentInfo == null ? 0 : residentInfo.getTotalUsage();
            markerOptions.title("");
            markerOptions.snippet("Total Usage:" + (totalUsage == 0 ? "N.A." : totalUsage));
            // set marker color based on view type and usage
            if(Constant.MAP_VIEW_DAILY.equals(viewType)) {
                markerOptions.setIcon(totalUsage > 21 ? iconRed : iconGreen);
            } else if (Constant.MAP_VIEW_HOURLY.equals(viewType)) {
                markerOptions.setIcon(totalUsage > 1.5 ? iconRed : iconGreen);
            } else {
                markerOptions.setIcon(totalUsage > 21 ? iconRed : iconGreen);
            }
            mapboxMap.addMarker(markerOptions);
        }

    }
}
