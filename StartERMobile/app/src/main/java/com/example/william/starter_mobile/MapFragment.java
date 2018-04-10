package com.example.william.starter_mobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.mapbox.mapboxsdk.MapboxAccountManager;
import com.mapquest.mapping.maps.OnMapReadyCallback;
import com.mapquest.mapping.maps.MapboxMap;
import com.mapquest.mapping.maps.MapView;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;

public class MapFragment extends Fragment {
    View vMapFragment;
    private MapboxMap mMapboxMap;
    private MapView mMapView;
    private LatLng myLocation; // = new LatLng(37.7749, -122.4194);

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMapFragment = inflater.inflate(R.layout.map_fragment, container, false);

        return vMapFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // start MapQuest account manager
        MapboxAccountManager.start(getActivity().getApplicationContext());
        // create map view
        mMapView = (MapView)getActivity().findViewById(R.id.mapquestMapView);
        mMapView.onCreate(savedInstanceState);

        // synchronize map view
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(MapboxMap mapboxMap) {
                mMapboxMap = mapboxMap;
            }
        });
    }

    @Override
    public void onResume() { super.onResume(); mMapView.onResume(); }

    @Override
    public void onPause()
    { super.onPause(); mMapView.onPause(); }

    @Override
    public void onDestroy()
    { super.onDestroy(); mMapView.onDestroy(); }

    @Override
    public void onSaveInstanceState(Bundle outState)
    { super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }
}
