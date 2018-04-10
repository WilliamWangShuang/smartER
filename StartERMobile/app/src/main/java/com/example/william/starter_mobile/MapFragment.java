package com.example.william.starter_mobile;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class MapFragment extends Fragment {
        View vMapFragment;

        @Nullable
        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            vMapFragment = inflater.inflate(R.layout.map_fragment, container, false);
            return vMapFragment;
        }
}
