package com.example.william.starter_mobile;

public class Constant {
    public static String WEATHER_WS_URL = "http://api.openweathermap.org/data/2.5/weather?id=7839805&APPID=8a0223cc559ebf08920e4cbf1279facd";
    public static String SMARTER_WS_ELECTRICITY_URL = " http://localhost:8080/SmartER-war/webresources/smarterentities.electricity";
    public static String WS_KEY_EXCEPTION = "Exception";
    public static String MSG_401 = "401 Unauthorized. Please check your identity";
    public static String MSG_404 = "404 Web resource not found";
    public static String MSG_500 = "500 Internal error when request web resource";
    public static String DATE_FORMAT = "yyyy-MM-dd";
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
}
