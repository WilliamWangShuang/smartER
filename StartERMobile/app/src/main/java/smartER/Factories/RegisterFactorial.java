package smartER.Factories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.william.starter_mobile.MainActivity;
import com.example.william.starter_mobile.R;
import com.example.william.starter_mobile.SmartERMobileUtility;

import org.json.JSONArray;
import org.json.JSONObject;

import smartER.webservice.SmartERUserWebservice;

public class RegisterFactorial  extends AsyncTask<Void, Void, Void> {
    // user name and email variables
    private String username;
    private String email;
    // email and username textviews
    private TextView tvUserName;
    private TextView tvEmail;
    // register activity
    private Activity activity;

    public RegisterFactorial(Activity activity, TextView tvUserName, TextView tvEmail) {
        this.activity = activity;
        this.tvUserName = tvUserName;
        this.tvEmail = tvEmail;
        this.username = tvUserName.getText().toString();
        this.email = tvEmail.getText().toString();
    }

    protected Void doInBackground(Void... params) {
        // check if email is already exist
        try {
            // get resident by email
            JSONArray userWithSameEmail = SmartERUserWebservice.findUserByEmail(email);
            // get resident by username
            JSONArray userWithSameUsername = SmartERUserWebservice.findCredentialByUsername(username);

            // check user with same email
            if (userWithSameEmail.length() > 0) {
                h.sendEmptyMessage(1);
            } else if (userWithSameUsername.length() > 0) {
                h.sendEmptyMessage(2);
            } else if (userWithSameEmail.length() == 0 && userWithSameUsername.length() == 0) {
                // if no user found with same email or username, can create new user

            }
        } catch (Exception ex) {
            Log.e("SmartERDebug", SmartERMobileUtility.getExceptionInfo(ex));
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //mContext.sendBroadcast(new Intent("startGenerateAppDataSignal"));
        Log.d("SmartERDebug", "register finish.");
    }

    // create a handler to toast message on main thread according to the post result
    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0) {

            } else if(msg.what == 1) {
                // if reisdent with same user exist in server db, server side validation not passed
                tvEmail.setBackgroundColor(activity.getResources().getColor(R.color.errorBackgound));
                ((TextView)activity.findViewById(R.id.lblEmailErrorMsg)).setText(activity.getResources().getString(R.string.register_email_exist_msg));
            } else {
                // if reisdent with same user exist in server db, server side validation not passed
                tvUserName.setBackgroundColor(activity.getResources().getColor(R.color.errorBackgound));
                ((TextView)activity.findViewById(R.id.lblUserNameErrorMsg)).setText(activity.getResources().getString(R.string.register_username_exist_msg));
            }

        }
    };

    public static class RegisterInfoUI {
        private String firstName;
        private String sureName;
        private String dob;
        private String address;
        private String postcode;
        private String phone;
        private String email;
        private String residentNumber;
        private String energyProvider;
        private String userName;
        private String pwd;
        private String repeatPwd;

        public RegisterInfoUI(String firstName, String sureName, String dob, String address, String postcode, String phone, String email, String residentNumber, String energyProvider, String userName, String pwd, String repeatPwd) {
            this.firstName = firstName;
            this.sureName = sureName;
            this.dob = dob;
            this.address = address;
            this.postcode = postcode;
            this.phone = phone;
            this.email = email;
            this.residentNumber = residentNumber;
            this.energyProvider = energyProvider;
            this.userName = userName;
            this.pwd = pwd;
            this.repeatPwd = repeatPwd;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getSureName() {
            return sureName;
        }

        public String getDob() {
            return dob;
        }

        public String getAddress() {
            return address;
        }

        public String getPostcode() {
            return postcode;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getResidentNumber() {
            return residentNumber;
        }

        public String getEnergyProvider() {
            return energyProvider;
        }

        public String getUserName() {
            return userName;
        }

        public String getPwd() {
            return pwd;
        }

        public String getRepeatPwd() {
            return repeatPwd;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setSureName(String sureName) {
            this.sureName = sureName;
        }

        public void setDob(String dob) {
            this.dob = dob;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setResidentNumber(String residentNumber) {
            this.residentNumber = residentNumber;
        }

        public void setEnergyProvider(String energyProvider) {
            this.energyProvider = energyProvider;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public void setRepeatPwd(String repeatPwd) {
            this.repeatPwd = repeatPwd;
        }
    }
}
