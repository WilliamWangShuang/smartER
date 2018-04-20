package smartER.Factories;

import android.os.AsyncTask;
import android.util.Log;

import com.example.william.starter_mobile.SmartERMobileUtility;

public class ResetCtxBasedValueFactorial extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... params) {
        Log.d("SmartERDebug","****Reset context values****");

        // Every 24 hours, reset context value those which are used to work as the base of generating apps usage
        SmartERMobileUtility.resetCtxBasedValue();

        return null;
    }

    @Override
    protected void onPostExecute(Void result) {
        Log.d("SmartERDebug", "reset context data finish.");
    }
}
