package smartER.Factories;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.william.starter_mobile.MainActivity;
import com.example.william.starter_mobile.R;
import com.example.william.starter_mobile.SmartERMobileUtility;
import com.mapbox.mapboxsdk.geometry.LatLng;

import smartER.webservice.MapWebservice;
import smartER.webservice.SmartERUserWebservice;

public class LoginFactorial  extends AsyncTask<Void, Void, Void> {
    private TextView tvUserName;
    private TextView tvPwd;
    private Activity loginActivity;
    // indicator if find user successfully
    private boolean isFound;
    // incdicator if errror occur
    private boolean isError;
    // user info variable
    SmartERUserWebservice.UserProfile userProfile;

    public LoginFactorial(Activity loginActivity, TextView tvUserName, TextView tvPwd) {
        this.tvUserName = tvUserName;
        this.tvPwd = tvPwd;
        this.loginActivity = loginActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        isFound = false;
        isError = false;
        userProfile = null;
    }

    @Override
    protected Void doInBackground(Void... params) {
        Log.d("SmartERDebug","****login logic start****");
        // get input username and pwd
        String usernameFromUI = tvUserName.getText().toString();
        String pwdFromUI = SmartERMobileUtility.encryptPwd(tvPwd.getText().toString());

        // initial resident info by calling webservice
        try {
            userProfile = SmartERUserWebservice.findUserByUsernameAndPwd(usernameFromUI, pwdFromUI);
            Log.d("SmartERDebug", "is user profile null:" + (userProfile == null));
            isFound = userProfile != null;
            isError = false;
        } catch (NullPointerException ex) {
            isFound = false;
            isError = false;
            Log.d("SmartERDebug", "No resident found by input username and password.");
            Log.d("SmartERDebug", SmartERMobileUtility.getExceptionInfo(ex));
        } catch (Exception ex) {
            isFound = false;
            isError = true;
            Log.e("SmartERDebug", "Error occured during login: " + ex.getMessage());
            Log.e("SmartERDebug", SmartERMobileUtility.getExceptionInfo(ex));
        }

        // if succeed to find user. initial application-level value and go to main activity
        if (isFound) {
            // set application-level latitude and longtitude for current user
            LatLng latLng = MapWebservice.getLatLngByAddress(userProfile.getAddress());
            SmartERMobileUtility.setLatitude(latLng.getLatitude());
            SmartERMobileUtility.setLongtiude(latLng.getLongitude());
            h.sendEmptyMessage(2);
        } else {
            if (isError) {
                // if error happens, toast notification message
                h.sendEmptyMessage(0);
            } else {
                // if no exception, means no resident found by input usearname and password.
                h.sendEmptyMessage(1);
            }
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        //mContext.sendBroadcast(new Intent("startGenerateAppDataSignal"));
        Log.d("SmartERDebug", "login finish.");
    }

    // create a handler to toast message on main thread according to the post result
    @SuppressLint("HandlerLeak")
    Handler h = new Handler() {
        public void handleMessage(Message msg){
            if(msg.what == 0)
                Toast.makeText(loginActivity, "Exception occurred when sign in. Try again. If not solved, remove the shit app.", Toast.LENGTH_LONG).show();
            else if(msg.what == 1) {
                TextView tvMsg = loginActivity.findViewById(R.id.lblErrorMsg);
                tvMsg.setText("No accound found. Check your usearname and password.");
            } else {
                // get resident info
                int resid = userProfile.getResId();
                String address = userProfile.getAddress();
                String firstName = userProfile.getFirstName();

                // set resident info to application level
                SmartERMobileUtility.setResId(resid);
                SmartERMobileUtility.setAddress(address);
                SmartERMobileUtility.setFirstName(firstName);

                // refresh message textview
                TextView tvMsg = loginActivity.findViewById(R.id.lblErrorMsg);
                tvMsg.setText("");

                // go to main activity
                Intent intent = new Intent(loginActivity, MainActivity.class);
                loginActivity.startActivityForResult(intent, 1);
            }

        }
    };
}
