package com.philips.cdp.registration;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;
/**
 * Created by 310230979  on 8/31/2016.
 */
public class ABCDTest extends InstrumentationTestCase{

    ABCD abcd;
    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        abcd = abcd.getInstance();
        assertNotNull(abcd);

    }
    public void testmP(){
        abcd.setmP("abcd");
        assertEquals("abcd",abcd.getmP());

    }
}