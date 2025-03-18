package com.example.mobilecalculator;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void getLastNum_handlesNumbersWithSign() {
        assertEquals("+456", MainActivity.getLastNum("764-(+456"));
    }

    @Test
    public void getLastNum_handlesEmpty() {
        assertNull(MainActivity.getLastNum(""));
    }

    @Test
    public void getLastNum_handlesSingleNumbers() {
        assertEquals("456", MainActivity.getLastNum("456"));
    }

    @Test
    public void getLastNum_handlesDecimalNumbers() {
        assertEquals("456.482", MainActivity.getLastNum("456.482"));
    }
}