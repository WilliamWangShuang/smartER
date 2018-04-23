package com.example.william.starter_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
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
    private TextView msgAddress;
    private TextView msgPostcode;
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
        msgAddress = (TextView)findViewById(R.id.lblAddressErrorMsg);
        msgPostcode = (TextView)findViewById(R.id.lblPostcodeErrorMsg);
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
                boolean isDataValidate = validateUIFields(registerInfoUI);
                Log.d("SmartERDebug", "UI fields validation:" + isDataValidate);
                // if UI validate passed, do server side validation and run logic flow
                if (isDataValidate) {
                    RegisterFactorial registerFactorial = new RegisterFactorial(RegisterActivity.this, (TextView)findViewById(R.id.register_username), (TextView)findViewById(R.id.register_email));
                    registerFactorial.execute();
                }
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
        result = validateEmpty(entity.getFirstName(), getResources().getString(R.string.register_first_name_empty_msg), msgFirstName);
        // validate sure name
        result = validateEmpty(entity.getSureName(), getResources().getString(R.string.register_sure_name_empty_msg), msgSureName) && result;
        // validate dob
        result = validateEmpty(entity.getDob(), getResources().getString(R.string.register_dob_empty_msg), msgDOB) && result;
        result = validateDobValue(entity.getDob(), getResources().getString(R.string.register_dob_format_msg)) && result;
        // validate address
        result = validateEmpty(entity.getAddress(), getResources().getString(R.string.register_address_empty_msg), msgAddress) && result;
        // validate postcode
        result = validateEmpty(entity.getPostcode(), getResources().getString(R.string.register_postcode_empty_msg), msgPostcode) && result;
        // validate phone
        result = validateEmpty(entity.getPhone(), getResources().getString(R.string.register_phone_empty_msg), msgPhone) && result;
        // validate email
        result = validateEmpty(entity.getEmail(), getResources().getString(R.string.register_email_empty_msg), msgEmail) && result;
        result = validateEmailFormat(entity.getEmail(), getResources().getString(R.string.register_email_format_msg)) && result;
        // validate number of resident
        result = validateSpinnerEmpty(entity.getResidentNumber(), residentNumberSpinner, getResources().getString(R.string.register_resident_number_empty_msg), msgNoOfResident) && result;
        // validate energy provider
        result = validateSpinnerEmpty(entity.getEnergyProvider(), energyProviderSpinner, getResources().getString(R.string.register_energy_provider_empty_msg), msgEnergyProvider) && result;
        // validate user name
        result = validateUserNameEmpty(entity.getUserName(), getResources().getString(R.string.register_username_empty_msg)) && result;
        // validate password
        result = validateEmpty(entity.getPwd(), getResources().getString(R.string.register_pwd_empty_msg), msgPwd) && result;
        result = validatePwdFormat(entity.getPwd(), getResources().getString(R.string.register_pwd_format_msg)) && result;
        // validate confirm pwd
        result = validateEmpty(entity.getRepeatPwd(), getResources().getString(R.string.register_confirm_pwd_msg), msgConfirmPwd) && result;

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

    // validate user name empty
    private boolean validateUserNameEmpty(String str, String message) {
        boolean result = false;
        if (SmartERMobileUtility.isEmptyOrNull(str)){
            result = false;
            msgUsername.setText(message);
        } else {
            result = true;
            msgUsername.setText("");
            ((TextView)findViewById(R.id.register_username)).setBackgroundColor(getResources().getColor(R.color.whiteBg));
        }

        // return validate result
        return result;
    }

    // validate one field
    private boolean validateSpinnerEmpty(String str, Spinner spinner, String message, TextView msgView) {
        boolean result = false;
        String firstValue = (String)spinner.getAdapter().getItem(0);
        if (firstValue.equals(str)){
            result = false;
            msgView.setText(message);
        } else {
            result = true;
            msgView.setText("");
        }

        // return validate result
        return result;
    }

    // validate email format
    private boolean validateEmailFormat(String str, String message) {
        boolean result = false;
        if(!str.matches("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*+@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$")){
            result = false;
            msgEmail.setText(message);
        } else {
            result = true;
            ((TextView)findViewById(R.id.register_email)).setBackgroundColor(getResources().getColor(R.color.whiteBg));
        }
        return result;
    }

    // valdate password format
    private boolean validatePwdFormat(String str, String message) {
        boolean result = false;
        // check if the string is a strong password. At least 8 length. Contains at least 1 special character, 1 lower&upper letter, and 1 number
        if(!str.trim().matches("^(?=.*[A-Z])(?=.*[!@#$&*])(?=.*[0-9])(?=.*[a-z]).{8}$")){
            result = false;
            msgPwd.setText(message);
        } else {
            result = true;
        }
        return result;
    }

    // validate dob value
    private boolean validateDobValue (String date, String message) {
        boolean result = false;
        SimpleDateFormat dateFormat = new SimpleDateFormat(Constant.DATE_FORMAT);
        try {
            Date dateFromUI = dateFormat.parse(date);
            if(dateFromUI.after(Calendar.getInstance().getTime())){
                result = false;
                msgDOB.setText(message);
            } else {
                result = true;
            }
        } catch (ParseException ex) {
            result = false;
            msgDOB.setText("Your input is not a valid date. Valid format: yyyy-MM-dd");
        }
        return result;
    }

    private void updateLabel() {
        String myFormat = Constant.DATE_FORMAT; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);

        edittext.setText(sdf.format(myCalendar.getTime()));
    }
}
