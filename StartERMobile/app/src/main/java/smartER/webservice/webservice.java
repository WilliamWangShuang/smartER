package smartER.webservice;

import android.os.Build;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;
import com.example.william.starter_mobile.Constant;

public class webservice {

    // get a web service based on URL
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
            // set content type to JSON
            urlConnection.setRequestProperty("Content-Type", "application/json");
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
                    // get response stream from web service
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // put the stream content into string
                    responseFromWS = getResponseText(in);
                    // create result Json from the content string
                    resultJSONObj = new JSONObject(responseFromWS);
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

    // get a json array by ws URL
    public static JSONArray requestWebServiceArray(String serviceUrl) throws IOException, JSONException {
        disableConnectionReuseIfNecessary();

        HttpURLConnection urlConnection = null;
        JSONArray resultJSONArray = null;
        int statusCode;
        String exceptionJSON = "{%s:%s}";
        String responseFromWS = "";

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set content type to JSON
            urlConnection.setRequestMethod("GET");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            // handle issues
            statusCode = urlConnection.getResponseCode();

            // handle different web service response status
            switch (statusCode) {
                case HttpURLConnection.HTTP_UNAUTHORIZED:
                    resultJSONArray = new JSONArray(String.format(exceptionJSON, Constant.WS_KEY_EXCEPTION, Constant.MSG_401));
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    resultJSONArray = new JSONArray(String.format(exceptionJSON, Constant.WS_KEY_EXCEPTION, Constant.MSG_404));
                    break;
                case HttpURLConnection.HTTP_INTERNAL_ERROR:
                    resultJSONArray = new JSONArray(String.format(exceptionJSON, Constant.WS_KEY_EXCEPTION, Constant.MSG_500));
                    break;
                case HttpURLConnection.HTTP_OK:
                    // get response stream from web service
                    InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                    // put the stream content into string
                    responseFromWS = getResponseText(in);
                    // create result Json from the content string
                    resultJSONArray = new JSONArray(responseFromWS);
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

        return resultJSONArray;
    }

    // make a POST http request to a web service
    public static String postWebService(String serviceUrl, JSONObject jsonParam) throws IOException {
        // response result
        String result = "";

        // if json content is empty, driectly return success message
        if (jsonParam.length() == 0)
            return Constant.SUCCESS_MSG;

        // declare a url connection
        HttpURLConnection urlConnection=null;

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set http request is POST
            urlConnection.setRequestMethod("POST");
            // disable caches
            urlConnection.setUseCaches(false);
            // set time out in case net is slow
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            // set post request header
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // set post send true. allow to send to ws
            urlConnection.setDoOutput(true);

            // set stream sent to server
            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            outputPost.write(jsonParam.toString().getBytes());
            outputPost.flush();
            outputPost.close();

            // connect url
            urlConnection.connect();

            // get server response status
            int HttpResult = urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_NO_CONTENT){
                result = Constant.SUCCESS_MSG;
            }else{
                result = urlConnection.getResponseMessage();
            }
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            // close connection
            if(urlConnection!=null)
                urlConnection.disconnect();
            return result;
        }
    }

    // make a POST http request to a web service
    public static String postWebServiceSyncAllData(String serviceUrl, JSONArray jsonParam) throws IOException {
        // response result
        String result = "";
        // declare a url connection
        HttpURLConnection urlConnection=null;

        try {
            // create connection
            URL urlToRequest = new URL(serviceUrl);
            urlConnection = (HttpURLConnection)urlToRequest.openConnection();
            // set http request is POST
            urlConnection.setRequestMethod("POST");
            // disable caches
            urlConnection.setUseCaches(false);
            // set time out in case net is slow
            urlConnection.setConnectTimeout(10000);
            urlConnection.setReadTimeout(10000);
            // set post request header
            urlConnection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            // set post send true. allow to send to ws
            urlConnection.setDoOutput(true);

            // set stream sent to server
            OutputStream outputPost = new BufferedOutputStream(urlConnection.getOutputStream());
            outputPost.write(jsonParam.toString().getBytes());
            outputPost.flush();
            outputPost.close();

            // connect url
            urlConnection.connect();

            // get server response status
            int HttpResult = urlConnection.getResponseCode();
            if(HttpResult == HttpURLConnection.HTTP_NO_CONTENT){
                result = Constant.SUCCESS_MSG;
            }else{
                result = urlConnection.getResponseMessage();
            }
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw ex;
        } finally {
            // close connection
            if(urlConnection!=null)
                urlConnection.disconnect();
            return result;
        }
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
