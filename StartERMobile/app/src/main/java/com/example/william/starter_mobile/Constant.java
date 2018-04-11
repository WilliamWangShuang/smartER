package com.example.william.starter_mobile;

public class Constant {
    // web service URL
    public static String WEATHER_WS_URL = "http://api.openweathermap.org/data/2.5/weather?id=7839805&APPID=8a0223cc559ebf08920e4cbf1279facd";
    public static String SMARTER_WS_ELECTRICITY_URL = "http://10.0.2.2:8080/SmartER-war/webresources/smarterentities.electricity";
    public static String FIND_USER_BY_ID_WS = "http://10.0.2.2:8080/SmartER-war/webresources/smarterentities.resident/";
    public static String CREATE_MULTIPLE_DATA_URL = "http://10.0.2.2:8080/SmartER-war/webresources/smarterentities.electricity/createMulitple";
    public static String MAP_WS_URL = "http://www.mapquestapi.com/geocoding/v1/address?key=HqENWLsTVnZCbCt0w8oWyn7NsAK7zVxp&thumbMaps=false&location=";

    public static String WS_KEY_EXCEPTION = "Exception";
    public static String MSG_401 = "401 Unauthorized. Please check your identity";
    public static String MSG_404 = "404 Web resource not found";
    public static String MSG_500 = "500 Internal error when request web resource";
    public static String DATE_FORMAT = "yyyy-MM-dd";
    public static String SERVER_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
    public static String SUCCESS_MSG = "Success";

    // json key for weather ws
    public static String WS_KEY_WEATHER_MAIN = "main";
    public static String WS_KEY_TEMP ="temp";

    // json key for user ws
    public static String WS_KEY_ADDRESS = "address";
    public static String WS_KEY_DOB ="dob";
    public static String WS_KEY_EMAIL ="email";
    public static String WS_KEY_ENERGY_PROVIDER ="energyprovider";
    public static String WS_KEY_FIRST_NAME ="firstname";
    public static String WS_KEY_MOBILE ="mobile";
    public static String WS_KEY_NO_OF_RESIDENT ="numberofresident";
    public static String WS_KEY_POSTCODE ="postcode";
    public static String WS_KEY_RESID ="resid";
    public static String WS_KEY_SURE_NAME ="surename";

    // json key for usage ws
    public static String WS_KEY_AC_USAGE = "acusage";
    public static String WS_KEY_FRIDGE_USAGE = "fridgeusage";
    public static String WS_KEY_WM_USAGE = "wmusage";
    public static String WS_KEY_TEMPERATURE = "temperature";
    public static String WS_KEY_USAGE_DATE = "usagedate";
    public static String WS_KEY_USAGE_HOUR = "usagehour";

    // json key for map ws
    public static String WS_KEY_MAP_RESULT = "results";
    public static String WS_KEY_MAP_LOCATION ="locations";
    public static String WS_KEY_MAP_LATLNG = "latLng";
    public static String WS_KEY__MAP_LAT = "lat";
    public static String WS_KEY_MAP_LNG = "lng";
}
