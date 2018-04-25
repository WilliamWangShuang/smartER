package com.example.william.starter_mobile;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.mapbox.mapboxsdk.MapboxAccountManager;

import smartER.webservice.Receivers.AppDataGenerator;
import smartER.webservice.Receivers.CurrentTempReceiver;
import smartER.webservice.Receivers.ResetCtxBasedValuesReceiver;
import smartER.webservice.Receivers.Sync24HourUsageDateReceiver;
import smartER.webservice.SmartERUsageWebservice;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    // applicance data generator receiver
    private AppDataGenerator appDataGenerator;
    // context based value reseter
    private ResetCtxBasedValuesReceiver resetCtxBasedValuesReceiver;
    // sync 24-hour data to server receiver
    private Sync24HourUsageDateReceiver sync24HourUsageDateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // start map box manager
        MapboxAccountManager.start(getApplicationContext());
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new MainFragment()).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // keep context
        SmartERMobileUtility.setmContext(this);

        // Initial context value for those which are used to work as the base of generating apps usage
        SmartERMobileUtility.resetCtxBasedValue();
        // Set receiver to generate context based data for generating app usage data every 24 hours
        resetCtxBasedValuesReceiver = new ResetCtxBasedValuesReceiver(this);
        // Set applicance generated data every hour
        appDataGenerator = new AppDataGenerator(this);
        // Set sync 24-hour data receiver
        sync24HourUsageDateReceiver  = new Sync24HourUsageDateReceiver(this);

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
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        // declare a fragement
        Fragment nextFragment = null;

        if (id == R.id.map_fragment) {
            // Handle map action
            nextFragment = new MapFragment();
        } else if (id == R.id.home_fragment) {
            // go back home page
            nextFragment = new MainFragment();
        } else if (id == R.id.linechart_fragment) {
            // go line chart page
            nextFragment = new LineChartFragment();
        } else if (id == R.id.barchart_fragment) {
            // go bar char page
            nextFragment = new BarChartFragment();
        } else if (id == R.id.piechart_fragment) {
            // go pie chart
            nextFragment = new PieChartFragment();
        }

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, nextFragment).commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
