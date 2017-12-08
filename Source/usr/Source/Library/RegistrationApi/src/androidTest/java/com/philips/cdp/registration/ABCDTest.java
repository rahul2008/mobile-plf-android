package com.philips.cdp.registration;

import android.support.multidex.MultiDex;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class ABCDTest extends RegistrationApiInstrumentationBase{

    ABCD abcd;
    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        abcd = abcd.getInstance();
        assertNotNull(abcd);

    }
    @Test
    public void testmP(){
        abcd.setmP("abcd");
        assertEquals("abcd",abcd.getmP());

    }
}