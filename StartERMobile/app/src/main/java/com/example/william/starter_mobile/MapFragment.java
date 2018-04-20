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

import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import smartER.Factories.MapFragmentFactorial;
import smartER.webservice.MapWebservice;
import smartER.webservice.SmartERUsageWebservice;
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
