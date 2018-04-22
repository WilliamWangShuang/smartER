package com.example.william.starter_mobile;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class RegisterActivity extends AppCompatActivity   {

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private EditText edittext;
    private Spinner residentNumberSpinner;
    private Spinner energyProviderSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        myCalendar = Calendar.getInstance();
        edittext = (EditText) findViewById(R.id.Birthday);
        residentNumberSpinner = (Spinner)findViewById(R.id.register_number_of_resident);
        energyProviderSpinner = (Spinner)findViewById(R.id.register_energy_provider);

        // create a datepicker
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        // set datepicker to edit text on page
        edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // get list of No. of resident items from string.xml
        List<String> noOfResidentList = Arrays.asList(getResources().getStringArray(R.array.register_number_of_register));
        // get list of No. of resident items from string.xml
        List<String> energyProviderList = Arrays.asList(getResources().getStringArray(R.array.register_energy_provider));

        // Initializing an ArrayAdapter
        ArrayAdapter<String> noOfResidenetArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, noOfResidentList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        ArrayAdapter<String> energyProviderArrayAdapter = new ArrayAdapter<String>(this,R.layout.spinner_item, energyProviderList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        // spinner set adapeter
        residentNumberSpinner.setAdapter(noOfResidenetArrayAdapter);
        // spinner set adapeter
        energyProviderSpinner.setAdapter(energyProviderArrayAdapter);

    }

    private void updateLabel() {
        String myFormat = Constant.DATE_FORMAT; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }
}
