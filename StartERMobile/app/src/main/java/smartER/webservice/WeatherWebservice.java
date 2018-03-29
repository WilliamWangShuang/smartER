package smartER.webservice;

import org.json.JSONException;
import org.json.JSONObject;
import com.example.william.starter_mobile.Constant;
import java.io.IOException;

public class WeatherWebservice {
    // Original data retrieve from weather service
    private static JSONObject massInfoByWS = null;

    // Initial original data
    private static void getCurrentWeatherInfo() throws IOException, JSONException {
        // Call the service to get data
        massInfoByWS = webservice.requestWebService(Constant.WEATHER_WS_URL);
    }

    // Get current temperature
    public static double getCurrentTemperature () throws IOException, JSONException {
        getCurrentWeatherInfo();
        JSONObject mainInfo = massInfoByWS.getJSONObject(Constant.WS_KEY_WEATHER_MAIN);
        double rough_degree = mainInfo.getDouble(Constant.WS_KEY_TEMP) - 273.16;
        return Math.round(rough_degree * 100.0) / 100.0;
    }
}
