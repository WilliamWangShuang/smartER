package com.example.william.starter_mobile;

import org.json.JSONException;
import org.junit.Test;
import static org.junit.Assert.*;
import smartER.webservice.*;

public class WeatherWSUnitTest {
    @Test
    public void testGetCurrentTemp(){
        double d = 303.16 - 273.16;
        try {
            assertEquals("Case fail: not expected result", d, WeatherWebservice.getCurrentTemperature());
        }
        catch(Exception e){
            e.printStackTrace();
            assertTrue("JSONException is thrown out", e instanceof JSONException);
        }
    }
}
