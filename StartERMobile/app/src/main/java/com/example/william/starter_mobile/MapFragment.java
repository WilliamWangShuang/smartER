package com.example.william.starter_mobile;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import org.json.JSONException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import smartER.webservice.MapWebservice;
import smartER.webservice.SmartERUserWebservice;

public class MapFragment extends Fragment {
    View vMapFragment;
    private MapView mMapView;
    // my address geographical position
    private LatLng myLocation;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMapFragment = inflater.inflate(R.layout.map_fragment, container, false);
        // create map view
        mMapView = (MapView)vMapFragment.findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);
        return vMapFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // start map view asynchronically
        MapFragmentFactorial mapFragmentFactorial = new MapFragmentFactorial(mMapView, savedInstanceState);
        mapFragmentFactorial.execute();
    }

    private class MapFragmentFactorial extends AsyncTask<Void, Void, List<LatLng>> {
        Bundle savedInstanceState = null;
        MapView mMapView = null;
        LatLng myLocation = null;
        List<LatLng> latLngList = null;
        MapboxMap mMapboxMap = null;
        List<SmartERUserWebservice.UserProfile> users = null;

        // constructor
        public MapFragmentFactorial(MapView mMapView, Bundle savedInstanceState){
            this.savedInstanceState = savedInstanceState;
            this.mMapView = mMapView;
            //this.mMapView.onCreate(savedInstanceState);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected List<LatLng> doInBackground(Void... params) {
            Log.d("SmartERDebug","****Set map****");

            List<LatLng> result = new ArrayList<>();

            try {
                // call ws to get all users
                users = SmartERUserWebservice.findAllUsers();
                // call ws to generate all Latlng info for all users.
                result = MapWebservice.getLatLngByAddress(users);
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
        protected void onPostExecute(List<LatLng> result) {
            // TODO: get my location
            myLocation = new LatLng();
            myLocation.setLatitude(-37.87649);
            myLocation.setLongitude(145.04543);

            latLngList = result;
            Log.d("SmartERDebug", "*****" + latLngList.size());
            for (LatLng l : latLngList) {
                Log.d("SmartERDebug", "" + l.getLatitude() + l.getLongitude());
            }

            // synchronize map view
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    mMapboxMap = mapboxMap;
                    mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));
                    // add makers for all residents
                    addMarker(mMapboxMap, latLngList);
                }
            });
        }

        // add maker on map
        private void addMarker(MapboxMap mapboxMap, List<LatLng> resPositions) {
            // set makers for all resident
            for (LatLng latLng : resPositions) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Test Data");
                markerOptions.snippet("Welcome!");
                mapboxMap.addMarker(markerOptions);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy()
    {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.d("SmartERDebug", "on save instance state...");
        super.onSaveInstanceState(outState);
    }
}
