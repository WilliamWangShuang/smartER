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
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

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
    // textView for current date
    private TextView tvCurrDateTime;
    // textView for first name
    private TextView tvFirstName;
    // current temperature receiver
    private CurrentTempReceiver currentTempReceiver;
    // textView for current total usage message
    private TextView tvCurrentUsageTotal;
    // image view
    private ImageView imgUsage;

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
        // get current date text view
        tvCurrDateTime = getActivity().findViewById(R.id.home_date_time);
        // set current date time and show on page
        SimpleDateFormat f = new SimpleDateFormat(Constant.DATE_TIME_FORMAT_ON_PAGE);
        tvCurrDateTime.setText(f.format(Calendar.getInstance().getTime()));
        //get first name text view
        tvFirstName = getActivity().findViewById(R.id.home_firstName);
        // set first name
        tvFirstName.setText(SmartERMobileUtility.getFirstName());
        // get usage total message textView
        tvCurrentUsageTotal = getActivity().findViewById(R.id.tvUsageMsg);
        // get usage image view
        imgUsage = getActivity().findViewById(R.id.imgUsage);

        // display positive or negative usage info message when switching between pages
        displayPosOrNegUsageInfo(SmartERMobileUtility.getTotalCurrHourUsage());

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

        // define broadReceiver onReceive action to update usage info of currenet hour when new hour data generated
        BroadcastReceiver currHourTotalUsageReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                double totalUsage = intent.getExtras().getDouble("currHourTotalUsage");
                displayPosOrNegUsageInfo(totalUsage);
            }
        };
        getActivity().registerReceiver(currHourTotalUsageReceiver, new IntentFilter("currHourTotalUsage"));

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

    private void displayPosOrNegUsageInfo(Double currHourUsage) {
        int currentH = Calendar.getInstance().get(Calendar.HOUR);
        int currentDayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        double totalUsage = currHourUsage;

        // if peak time and weekday, set relevant appearance based on total usage. Otherwise, set positive appearance
        if (1 <= currentDayOfWeek && currentDayOfWeek <= 7 && 9 <= currentH && currentH <= 22) {
            if (totalUsage > 1.5) {
                tvCurrentUsageTotal.setText(R.string.negative_msg);
                imgUsage.setImageResource(R.drawable.negative);
            } else {
                tvCurrentUsageTotal.setText(R.string.positive_msg);
                imgUsage.setImageResource(R.drawable.positive);
            }
        } else{
            tvCurrentUsageTotal.setText(R.string.positive_msg);
            imgUsage.setImageResource(R.drawable.positive);
        }
    }

}
