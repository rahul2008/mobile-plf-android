package com.philips.cdp.registration.Configuration;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.configuration.Development;
import com.philips.cdp.registration.configuration.Flow;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310243576 on 8/5/2016.
 */
public class DevelopmentTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    private Development development;

    public DevelopmentTest() {
        super(RegistrationActivity.class);
        development = new Development();
    }
//    private String sharedKey;
//    private String secretKey;
//    private String baseURL;

    public void testSharedKey(){
        development.setSharedKey(null);
        assertEquals(null, development.getSharedKey());

        development.setSharedKey("");
        assertEquals("",development.getSharedKey());

        development.setSharedKey("2ded3e3");
        assertEquals("2ded3e3",development.getSharedKey());
    }
    public void testSecretKey(){
        development.setSecretKey(null);
        assertEquals(null, development.getSecretKey());

        development.setSecretKey("");
        assertEquals("",development.getSecretKey());

        development.setSecretKey("2ded3e3");
        assertEquals("2ded3e3",development.getSecretKey());

    }
    public void testBaseURL(){
        development.setBaseURL(null);
        assertEquals(null, development.getBaseURL());

        development.setBaseURL("");
        assertEquals("",development.getBaseURL());

        development.setBaseURL("2ded3e3");
        assertEquals("2ded3e3",development.getBaseURL());

    }

}
