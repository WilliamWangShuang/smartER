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
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import smartER.Factories.RegisterFactorial;

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

    // get resident by username
    public static JSONArray findUserByEmail(String email) throws IOException, JSONException {
        // result object
        JSONArray jsonObject = new JSONArray();
        try {
            jsonObject = webservice.requestWebServiceArray(Constant.FIND_USER_BY_EMAIL_WS + email);
        } catch (NullPointerException ex) {
            // if null pointer exception, means no use exist in db with same email, then return null
            return null;
        } catch (Exception ex) {
            throw ex;
        }
        // return result
        return jsonObject;
    }

    // get resident credential by user name
    public static JSONArray findCredentialByUsername(String username) throws IOException, JSONException {
        // result object
        JSONArray jsonObject = new JSONArray();
        try {
            jsonObject = webservice.requestWebServiceArray(Constant.FIND_CRENDENTIAL_BY_USERNAME_WS + username);
        } catch (NullPointerException ex){
            // if json content is 404, means no use exist in db with same user name, then return null
            return null;
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

    // find resident by username and passwords
    public static UserProfile findUserByUsernameAndPwd(String username, String pwd) throws IOException, JSONException, ParseException {
        UserProfile result = null;
        JSONObject credentialJson = null;

        try {
            // get json array from ws
            JSONArray jsonArray = webservice.requestWebServiceArray(Constant.FIND_USER_CREDENTIAL + username + "/" + pwd);
            Log.d("SmartERDebug", "json array length:" + jsonArray.length());
            // get use credential by username and pwd
            if (jsonArray.length() > 0)
                credentialJson = jsonArray.getJSONObject(0);
            else
                credentialJson = null;

            if (credentialJson != null) {
                // get resident json object
                JSONObject residentJsonObj = credentialJson.getJSONObject(Constant.WS_KEY_RESID);
                result = new UserProfile(residentJsonObj);
            }
        } catch (Exception ex) {
            throw ex;
        }
        return result;
    }

    // post to server to store register user
    public static boolean saveRegisterResident(RegisterFactorial.RegisterInfoUI registerInfoUI) throws JSONException, IOException, ParseException {
        boolean isSuccessSave = false;
        // construct resident json
        JSONObject jsonResident = new JSONObject();
        jsonResident.put(Constant.WS_KEY_ADDRESS, registerInfoUI.getAddress());
        SimpleDateFormat f = new SimpleDateFormat(Constant.DATE_FORMAT);
        SimpleDateFormat f1 = new SimpleDateFormat(Constant.SERVER_DATE_FORMAT);
        jsonResident.put(Constant.WS_KEY_DOB, f1.format(f.parse(registerInfoUI.getDob())));
        jsonResident.put(Constant.WS_KEY_EMAIL, registerInfoUI.getEmail());
        jsonResident.put(Constant.WS_KEY_ENERGY_PROVIDER, registerInfoUI.getEnergyProvider());
        jsonResident.put(Constant.WS_KEY_FIRST_NAME, registerInfoUI.getFirstName());
        jsonResident.put(Constant.WS_KEY_SURE_NAME, registerInfoUI.getSureName());
        jsonResident.put(Constant.WS_KEY_MOBILE, registerInfoUI.getPhone());
        jsonResident.put(Constant.WS_KEY_NO_OF_RESIDENT, Integer.parseInt(registerInfoUI.getResidentNumber()));
        jsonResident.put(Constant.WS_KEY_POSTCODE, Integer.parseInt(registerInfoUI.getPostcode()));
        Log.d("SmartERDebug", "parsed resident json to post:" + jsonResident.toString());
        // call ws to save
        String saveResidentResult = webservice.postWebService(Constant.SAVE_RESIDENT_URL, jsonResident);
        // if save resident successfully, start save credential
        if (Constant.SUCCESS_MSG.equals(saveResidentResult)) {
            // get new resid based on inerted resident's email
            JSONArray jsonArray = webservice.requestWebServiceArray(Constant.FIND_USER_BY_EMAIL_WS + registerInfoUI.getEmail());
            JSONObject residJson = (JSONObject)jsonArray.get(0);
            // construct credential JSON
            JSONObject credentialJson = new JSONObject();
            credentialJson.put(Constant.WS_KEY_CREDENTIAL_PASSWORDHASH, SmartERMobileUtility.encryptPwd(registerInfoUI.getPwd()));
            credentialJson.put(Constant.WS_KEY_RESID, residJson);
            credentialJson.put(Constant.WS_KEY_CREDENTIAL_REGISTER_DATE, f1.format(Calendar.getInstance().getTime()));
            credentialJson.put(Constant.WS_KEY_CREDENTIAL_USERNAME, registerInfoUI.getUserName());
            Log.d("SmartERDebug", "parsed credential json to post:" + credentialJson.toString());
            // call ws to save
            String saveCredentialResult = webservice.postWebService(Constant.SAVE_CREDENTIAO_URL, credentialJson);

            if (Constant.SUCCESS_MSG.equals(saveCredentialResult)) {
                // set application level variable resid
                SmartERMobileUtility.setResId(residJson.getInt(Constant.WS_KEY_RESID));
                isSuccessSave = true;
            }
        }

        return isSuccessSave;
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
