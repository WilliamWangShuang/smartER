package com.example.william.starter_mobile;

import org.junit.Test;

import static org.junit.Assert.*;

public class SmartERMobileUtilityUnitTest {
    @Test
    public void test_getRandomDouble(){
        double min = 0.3;
        double max = 0.8;
        double generate = SmartERMobileUtility.getRandomDoubleNumber(0.3, 0.8);
        System.out.println("output:" + generate);
        assertTrue(generate >= min && generate <= max);
    }

    @Test
    public void testGetInt(){
        int min = 0;
        int max = 24;
        int generate = SmartERMobileUtility.getRandomIntegerNumber(min, max);
        System.out.println("output:" + generate);
        assertTrue(generate >= min && generate <= max);
    }
}
