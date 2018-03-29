package smartER.webservice;

import android.os.Build;
import android.util.Log;

import com.example.william.starter_mobile.R;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.Scanner;
import com.example.william.starter_mobile.Constant;

public class webservice {

    public static JSONObject requestWebService(String serviceUrl) throws IOException, JSONException {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        JSONObject resultJSONObj = null;
        int statusCode;
        String exceptionJSON = "{%s:%s}";
        String responseFromWS = "";

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();

            // handle issues
            statusCode = urlConnection.getResponseCode();

            // handle different web service response status
            switch (statusCode) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    resultJSONObj = new JSONObject(String.format(exceptionJSON, Constant.WS_KEY_EXCEPTION, Constant.MSG_401));
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    resultJSONObj = new JSONObject(String.format(exceptionJSON, Constant.WS_KEY_EXCEPTION, Constant.MSG_404));
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    resultJSONObj = new JSONObject(String.format(exceptionJSON, Constant.WS_KEY_EXCEPTION, Constant.MSG_500));
                    break;
                case HttpURLConnection.HTTP_OK:
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    responseFromWS = getResponseText(in);
                    resultJSONObj = new JSONObject(responseFromWS);
                    //resultJSONObj = new JSONObject("main:{temp:300.15,pressure:1007,humidity:74,temp_min:300.15,temp_max:300.15}");
                    break;
                default:
                    break;
            }
        } catch (MalformedURLException e) {
            // URL is invalid
            throw e;
        } catch (SocketTimeoutException e) {
            // data retrieval or connection timed out
            throw e;
        } catch (IOException e) {
            // could not read response body
            // (could not create input stream)
            throw e;
        } catch (JSONException e) {
            // response body is no valid JSON string
            throw e;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return resultJSONObj;
    }

    /**
     * required in order to prevent issues in earlier Android version.
     */
    private static void disableConnectionReuseIfNecessary() {
        // see HttpURLConnection API doc
        if (Integer.parseInt(Build.VERSION.SDK) < Build.VERSION_CODES.FROYO) {
            System.setProperty("http.keepAlive", "false");
        }
    }

    private static String getResponseText(InputStream inStream) {
        // very nice trick from
        // http://weblogs.java.net/blog/pat/archive/2004/10/stupid_scanner_1.html
        String str = new Scanner(inStream).useDelimiter("\\A").next();
        return str;
    }
}
