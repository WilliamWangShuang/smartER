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
import smartER.webservice.MapWebservice;

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
        //Activity activity = getActivity();
        //mMapView = (MapView)activity.findViewById(R.id.mapquestMapView);
        mMapView = (MapView)vMapFragment.findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState != null ? savedInstanceState.getBundle("mapViewSaveState") : null);
        return vMapFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // start map view asynchronically
        MapFragmentFactorial mapFragmentFactorial = new MapFragmentFactorial(mMapView, savedInstanceState);
        mapFragmentFactorial.execute();
    }

    private class MapFragmentFactorial extends AsyncTask<Void, Void, LatLng> {
        Bundle savedInstanceState = null;
        MapView mMapView = null;
        LatLng myLocation = null;
        MapboxMap mMapboxMap = null;

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
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        Log.d("SmartERDebug", "on save instance state...");
        final Bundle mapViewSaveState = new Bundle(outState);
        mMapView.onSaveInstanceState(mapViewSaveState);
        outState.putBundle("mapViewSaveState", mapViewSaveState);

        Bundle customBundle = new Bundle();
        outState.putBundle("ARG_CUSTOM_BUNDLE", customBundle);
        super.onSaveInstanceState(outState);
    }
}
