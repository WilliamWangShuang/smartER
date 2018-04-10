package com.example.william.starter_mobile;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import smartER.webservice.Receivers.*;
import smartER.webservice.SmartERUsageWebservice;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get view of current temperature
        tvTemp = findViewById(R.id.tvTemp);
        // keep context
        SmartERMobileUtility.setmContext(this);
        // TODO: should be put in login logic
        // set resident ID after login
        SmartERMobileUtility.setResId(3);
        // initial SmartERUsage ws
        smartERUsageWebservice = new SmartERUsageWebservice();

        // Initial context value for those which are used to work as the base of generating apps usage
        SmartERMobileUtility.resetCtxBasedValue();
        // Set background thread to get update temperature
        currentTempReceiver = new CurrentTempReceiver(this);
        // Set receiver to generate context based data for generating app usage data every 24 hours
        resetCtxBasedValuesReceiver = new ResetCtxBasedValuesReceiver(this);
        // Set applicance generated data every hour
        appDataGenerator = new AppDataGenerator(this);
        // Set sync 24-hour data receiver
        sync24HourUsageDateReceiver  = new Sync24HourUsageDateReceiver(this);

        // define broadReceiver onReceive action to update view of currenet temperature timely
        BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                tvTemp.setText(intent.getExtras().getString("currTemp"));
                // update global variable - currentTemp
                SmartERMobileUtility.setCurrentTemp(Double.parseDouble(intent.getExtras().getString("currTemp")));
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("currTempIntentBroadcasting"));

        // register click event to button sycn_one_data
        Button btnSyncOneData=(Button)findViewById(R.id.btn_syncOneData);
        //registering with onclicklistener
        btnSyncOneData.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // POST to sync one record to system
                smartERUsageWebservice.syncOneRecord2SeverDb(v);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
