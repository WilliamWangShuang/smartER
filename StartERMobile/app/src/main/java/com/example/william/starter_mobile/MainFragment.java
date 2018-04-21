package com.example.william.starter_mobile;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import smartER.webservice.Receivers.AppDataGenerator;
import smartER.webservice.Receivers.CurrentTempReceiver;
import smartER.webservice.Receivers.ResetCtxBasedValuesReceiver;
import smartER.webservice.Receivers.Sync24HourUsageDateReceiver;
import smartER.webservice.SmartERUsageWebservice;

public class MainFragment extends Fragment {
    private View vMainFragment;
    // SmartERUsage webservice
    private SmartERUsageWebservice smartERUsageWebservice;
    // textView for temperature
    private TextView tvTemp;
    // current temperature receiver
    private CurrentTempReceiver currentTempReceiver;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMainFragment = inflater.inflate(R.layout.main_fragment, container, false);

        return vMainFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initial SmartERUsage ws
        smartERUsageWebservice = new SmartERUsageWebservice();

        // get view of current temperature
        tvTemp = getActivity().findViewById(R.id.tvTemp);

        // Set background thread to get update temperature
        currentTempReceiver = new CurrentTempReceiver(getActivity());
        // define broadReceiver onReceive action to update view of currenet temperature timely
        BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tvTemp.setText(intent.getExtras().getString("currTemp"));
            }
        };
        getActivity().registerReceiver(broadcastReceiver, new IntentFilter("currTempIntentBroadcasting"));
        // register click event to button sycn_one_data
        Button btnSyncOneData=(Button)getActivity().findViewById(R.id.btn_syncOneData);
        //registering with onclicklistener
        btnSyncOneData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // POST to sync one record to system
                smartERUsageWebservice.syncOneRecord2SeverDb(v);

            }
        });
    }
}
