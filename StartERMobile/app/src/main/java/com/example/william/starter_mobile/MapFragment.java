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
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapbox.mapboxsdk.telemetry.MqEventManager;
import com.mapquest.android.commoncore.util.VolleyUtil;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import org.json.JSONException;
import java.io.IOException;
import smartER.webservice.MapWebservice;

public class MapFragment extends Fragment {
    View vMapFragment;
    private MapboxMap mMapboxMap;
    private MapView mMapView;
    // my address geographical position
    private LatLng myLocation;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMapFragment = inflater.inflate(R.layout.map_fragment, container, false);

        return vMapFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // create map view
        Activity activity = getActivity();

        mMapView = (MapView)activity.findViewById(R.id.mapquestMapView);
        MapFragmentFactorial mapFragmentFactorial = new MapFragmentFactorial(mMapView, savedInstanceState);
        mapFragmentFactorial.execute();
    }

    private class MapFragmentFactorial extends AsyncTask<Void, Void, LatLng> {
        Bundle savedInstanceState = null;
        MapView mMapView = null;
        LatLng myLocation = null;

        // constructor
        public MapFragmentFactorial(MapView mMapView, Bundle savedInstanceState){
            this.savedInstanceState = savedInstanceState;
            this.mMapView = mMapView;
            this.mMapView.onCreate(savedInstanceState);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected LatLng doInBackground(Void... params) {
            Log.d("SmartERDebug","****Set map****");

            LatLng result = new LatLng();
            // TODO: my address - test purpose
            String address = "14 Brixton ave,Eltham North,VIC";
            try {
                result = MapWebservice.getLatLngByAddress(address);
            } catch (IOException e) {
                Log.e("SmertERDebug", SmartERMobileUtility.getExceptionInfo(e));
            } catch (JSONException e) {
                Log.e("SmertERDebug", SmartERMobileUtility.getExceptionInfo(e));
            }

            return result;
        }

        @Override
        protected void onPostExecute(LatLng result) {
            myLocation = result;

            // synchronize map view
            mMapView.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(MapboxMap mapboxMap) {
                    mMapboxMap = mapboxMap;
                    mMapboxMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 11));
                    addMarker(mMapboxMap);
                }
            });
        }

        // add maker on map
        private void addMarker(MapboxMap mapboxMap) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(myLocation);
            markerOptions.title("Test Data");
            markerOptions.snippet("Welcome!");
            mapboxMap.addMarker(markerOptions);
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
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        if(outState != null) {
            Log.d("SmaertERDebug", "on save instance state...");
            super.onSaveInstanceState(outState);
            mMapView.onSaveInstanceState(outState);
        }
    }
}
