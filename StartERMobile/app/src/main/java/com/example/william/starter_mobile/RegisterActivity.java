package com.example.william.starter_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import net.hockeyapp.android.Strings;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import smartER.Factories.RegisterFactorial;

public class RegisterActivity extends AppCompatActivity   {

    private Calendar myCalendar;
    private DatePickerDialog.OnDateSetListener date;
    private EditText edittext;
    private Spinner residentNumberSpinner;
    private Spinner energyProviderSpinner;
    // declare message views
    private TextView msgFirstName;
    private TextView msgSureName;
    private TextView msgDOB;
    private TextView msgAddressName;
    private TextView msgPostcodeName;
    private TextView msgPhone;
    private TextView msgEmail;
    private TextView msgNoOfResident;
    private TextView msgEnergyProvider;
    private TextView msgUsername;
    private TextView msgPwd;
    private TextView msgConfirmPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        // initial views and variables
        myCalendar = Calendar.getInstance();
        edittext = (EditText) findViewById(R.id.Birthday);
        residentNumberSpinner = (Spinner)findViewById(R.id.register_number_of_resident);
        energyProviderSpinner = (Spinner)findViewById(R.id.register_energy_provider);
        ImageButton imgBtnBack = (ImageButton)findViewById(R.id.imgBtnBack);
        Button btnConfirmRegister = (Button)findViewById(R.id.btn_confirm_register);
        // initial message views
        msgFirstName = (TextView)findViewById(R.id.lblFirstErrorMsg);
        msgSureName = (TextView)findViewById(R.id.lblSureErrorMsg);
        msgDOB = (TextView)findViewById(R.id.lblDOBErrorMsg);
        msgAddressName = (TextView)findViewById(R.id.lblAddressErrorMsg);
        msgPostcodeName = (TextView)findViewById(R.id.lblPostcodeErrorMsg);
        msgPhone = (TextView)findViewById(R.id.lblPhoneErrorMsg);
        msgEmail = (TextView)findViewById(R.id.lblEmailErrorMsg);
        msgNoOfResident = (TextView)findViewById(R.id.lblNumberOfResidentErrorMsg);
        msgEnergyProvider = (TextView)findViewById(R.id.lblEnergyProviderErrorMsg);
        msgUsername = (TextView)findViewById(R.id.lblUserNameErrorMsg);
        msgPwd = (TextView)findViewById(R.id.lblPwdErrorMsg);
        msgConfirmPwd = (TextView)findViewById(R.id.lblConfirmPwdErrorMsg);

        // set back button listener
        imgBtnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // set confirm button listener
        btnConfirmRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = ((TextView)findViewById(R.id.register_firstname)).getText().toString();
                String sureName = ((TextView)findViewById(R.id.register_surename)).getText().toString();
                String dob = ((TextView)findViewById(R.id.Birthday)).getText().toString();
                String address = ((TextView)findViewById(R.id.register_address)).getText().toString();
                String postcode = ((TextView)findViewById(R.id.register_postcode)).getText().toString();
                String phone = ((TextView)findViewById(R.id.register_phone)).getText().toString();
                String email = ((TextView)findViewById(R.id.register_email)).getText().toString();
                String residentNumber = ((Spinner)findViewById(R.id.register_number_of_resident)).getSelectedItem().toString();
                String energyProvider = ((Spinner)findViewById(R.id.register_energy_provider)).getSelectedItem().toString();
                String userName = ((TextView)findViewById(R.id.register_username)).getText().toString();
                String pwd = ((TextView)findViewById(R.id.register_password)).getText().toString();
                String repeatPwd = ((TextView)findViewById(R.id.register_confirm_password)).getText().toString();

                // create UI info entity object
                RegisterFactorial.RegisterInfoUI registerInfoUI = new RegisterFactorial.RegisterInfoUI(firstName, sureName, dob, address, postcode, phone, email, residentNumber, energyProvider, userName, pwd, repeatPwd);

                // validate fields on UI
                boolean isDataValidate = true;

            }
        });

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

        // spinner set adapter
        residentNumberSpinner.setAdapter(noOfResidenetArrayAdapter);
        // spinner set adapter
        energyProviderSpinner.setAdapter(energyProviderArrayAdapter);

    }

    // validate UI fields
    private boolean validateUIFields(RegisterFactorial.RegisterInfoUI entity) {
        boolean result = false;
        // validate first name
        result = validateEmpty(entity.getFirstName(), Strings.get(R.string.register_first_name_empty_msg), msgFirstName);
        // validate first name
        result = validateEmpty(entity.getSureName(), Strings.get(R.string.register_sure_name_empty_msg), msgFirstName);
        return result;
    }

    // validate one field
    private boolean validateEmpty(String str, String message, TextView msgView) {
        boolean result = false;
        if (SmartERMobileUtility.isEmptyOrNull(str)){
            result = false;
            msgView.setText(message);
        } else {
            result = true;
            msgView.setText("");
        }

        // return validate result
        return result;
    }

    private void updateLabel() {
        String myFormat = Constant.DATE_FORMAT; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }
}
