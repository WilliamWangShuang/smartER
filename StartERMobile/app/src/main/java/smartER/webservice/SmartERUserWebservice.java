package smartER.webservice;

import android.util.Log;

import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SmartERUserWebservice {
    // get profile of current login user
    public static JSONObject findCurrentUserById() throws IOException, JSONException {
        // result object
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = webservice.requestWebService(Constant.FIND_USER_BY_ID_WS + SmartERMobileUtility.getResId());
        } catch (Exception ex) {
            throw ex;
        }
        // return result
        return jsonObject;
    }

    // get profile of all users
    public static List<UserProfile> findAllUsers() throws IOException, JSONException, ParseException {
        List<UserProfile> users = new ArrayList<>();

        // ws query result object
        JSONArray jsonArray = new JSONArray();
        try {
            jsonArray = webservice.requestWebServiceArray(Constant.FIND_ALL_USERS);

            // construct return list
            int position = 0;
            while (position < jsonArray.length()) {
                JSONObject jsonObj = jsonArray.getJSONObject(position);
                UserProfile userProfile = new UserProfile(jsonObj);
                users.add(userProfile);
                position ++;
            }
        } catch (Exception ex) {
            throw ex;
        }

        //return result
        return users;
    }

    // inner class working as entity class for user
    public static class UserProfile {
        // resident id
        private int resId;
        // address
        private String address;
        // date of birth
        private Date dob;
        // email
        private String email;
        // energy provider
        private String energyProvider;
        // first name
        private String firstName;
        // phone number
        private String phone;
        // number of resident
        private int noOfResident;
        // postcode
        private String postCode;
        // sure name
        private String sureName;

        // default constructor
        public UserProfile() {}

        // constructor with Json param
        public UserProfile(JSONObject jsonObject) throws JSONException, ParseException {
            // set value to attributes from json object
            resId = jsonObject.getInt(Constant.WS_KEY_RESID);
            address = jsonObject.getString(Constant.WS_KEY_ADDRESS);
            Date date1=new SimpleDateFormat(Constant.DATE_FORMAT).parse(jsonObject.getString(Constant.WS_KEY_DOB).substring(0, 9));
            dob = date1;
            email = jsonObject.getString(Constant.WS_KEY_EMAIL);
            energyProvider = jsonObject.getString(Constant.WS_KEY_ENERGY_PROVIDER);
            firstName = jsonObject.getString(Constant.WS_KEY_FIRST_NAME);
            phone = jsonObject.getString(Constant.WS_KEY_MOBILE);
            noOfResident = jsonObject.getInt(Constant.WS_KEY_NO_OF_RESIDENT);
            postCode = jsonObject.getString(Constant.WS_KEY_POSTCODE);
            sureName = jsonObject.getString(Constant.WS_KEY_SURE_NAME);
        }

        // getters and setters

        public int getResId() {
            return resId;
        }

        public String getAddress() {
            return address;
        }

        public Date getDob() {
            return dob;
        }

        public String getEmail() {
            return email;
        }

        public String getEnergyProvider() {
            return energyProvider;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getPhone() {
            return phone;
        }

        public int getNoOfResident() {
            return noOfResident;
        }

        public String getPostCode() {
            return postCode;
        }

        public String getSureName() {
            return sureName;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setDob(Date dob) {
            this.dob = dob;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setEnergyProvider(String energyProvider) {
            this.energyProvider = energyProvider;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setNoOfResident(int noOfResident) {
            this.noOfResident = noOfResident;
        }

        public void setPostCode(String postCode) {
            this.postCode = postCode;
        }

        public void setSureName(String sureName) {
            this.sureName = sureName;
        }
    }
}
