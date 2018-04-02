package com.example.william.starter_mobile;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_getRandomDouble(){
        double min = 0.3;
        double max = 0.8;
        double generate = SmartERMobileUtility.getRandomDoubleNumber(0.3, 0.8);
        System.out.print("output:" + generate);
        assertTrue(generate >= min && generate <= max);
    }
}