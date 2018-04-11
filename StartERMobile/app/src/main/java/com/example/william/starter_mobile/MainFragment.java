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
    View vMainFragment;
    // current temperature receiver
    private CurrentTempReceiver currentTempReceiver;
    // applicance data generator receiver
    private AppDataGenerator appDataGenerator;
    // context based value reseter
    private ResetCtxBasedValuesReceiver resetCtxBasedValuesReceiver;
    // sync 24-hour data to server receiver
    private Sync24HourUsageDateReceiver sync24HourUsageDateReceiver;
    // SmartERUsage webservice
    private SmartERUsageWebservice smartERUsageWebservice;
    private TextView tvTemp;

    @Nullable
    @Override
    public View onCreateView (LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        vMainFragment = inflater.inflate(R.layout.main_fragment, container, false);

        return vMainFragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // get view of current temperature
        tvTemp = getActivity().findViewById(R.id.tvTemp);
        // keep context
        SmartERMobileUtility.setmContext(getActivity());
        // TODO: should be put in login logic
        // set resident ID after login
        SmartERMobileUtility.setResId(3);

        // Initial context value for those which are used to work as the base of generating apps usage
        SmartERMobileUtility.resetCtxBasedValue();
        // Set background thread to get update temperature
        currentTempReceiver = new CurrentTempReceiver(getActivity());
        // Set receiver to generate context based data for generating app usage data every 24 hours
        resetCtxBasedValuesReceiver = new ResetCtxBasedValuesReceiver(getActivity());
        // Set applicance generated data every hour
        appDataGenerator = new AppDataGenerator(getActivity());
        // Set sync 24-hour data receiver
        sync24HourUsageDateReceiver  = new Sync24HourUsageDateReceiver(getActivity());
        // initial SmartERUsage ws
        smartERUsageWebservice = new SmartERUsageWebservice();

        // define broadReceiver onReceive action to update view of currenet temperature timely
        BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tvTemp.setText(intent.getExtras().getString("currTemp"));
                // update global variable - currentTemp
                SmartERMobileUtility.setCurrentTemp(Double.parseDouble(intent.getExtras().getString("currTemp")));
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
