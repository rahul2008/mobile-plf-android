package com.philips.cl.di.regsample.app.test;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cl.di.regsample.app.RegistrationSampleActivity;
import com.philips.dhpclient.DhpApiSigner;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 310190722 on 9/8/2015.
 */
public class DhpApiSignerTest extends ActivityInstrumentationTestCase2<RegistrationSampleActivity> {

    public DhpApiSignerTest() {
        super(RegistrationSampleActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    }

    public void testAuthorizationHeader() {
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("Content-Type", "application/json");
        headers.put("Content-Length", "10");
        headers.put("SignedDate", "2015-07-02T07:52:03.100+0000");

        String result = new DhpApiSigner("sharedKey", "secretKey").buildAuthorizationHeaderValue(
                "POST",
                "foo=bar&bar=foo",
                 headers,
                "http://user-registration-service.cloud.pcftest.com",
                "BLAA");
        String expected = "HmacSHA256;Credential:sharedKey;SignedHeaders:Content-Type,Content-Length,SignedDate,;Signature:xymOJ/t5bbgxmxqOf84ifd8U1w2H7WOITQkjB+zyZoY=";
        if(expected.toString().equals(result.toString().trim())){
            assertTrue(true);
        }else{
            assertTrue(false);
        }
    }
}
