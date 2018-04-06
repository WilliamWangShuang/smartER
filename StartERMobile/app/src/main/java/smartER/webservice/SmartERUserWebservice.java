package smartER.webservice;

import com.example.william.starter_mobile.Constant;
import com.example.william.starter_mobile.SmartERMobileUtility;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SmartERUserWebservice {
    // get profile of current login user
    public class UserProfile {
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
            resId = SmartERMobileUtility.getResId();
            address = jsonObject.getString(Constant.WS_KEY_ADDRESS);
            Date date1=new SimpleDateFormat(Constant.DATE_FORMAT).parse(jsonObject.getString(Constant.WS_KEY_DOB).substring(0, 9));
            dob = date1;
            //TODO
        }
    }
}
