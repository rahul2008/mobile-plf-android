package com.philips.cdp.registration;

import android.test.InstrumentationTestCase;

import org.junit.Before;
/**
 * Created by 310230979  on 8/31/2016.
 */
public class ABCDTest extends InstrumentationTestCase{

    ABCD abcd;
    @Before
    public void setUp() throws Exception {
        abcd = abcd.getInstance();

    }
    public void testmP(){
        abcd.setmP("abcd");
        assertEquals("abcd",abcd.getmP());

    }
}