package com.philips.dhpclient.response;

import android.test.InstrumentationTestCase;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpAuthenticationResponseTest extends InstrumentationTestCase {
    DhpAuthenticationResponse mDhpAuthenticationResponse;
    Map<String, Object> mRawResponse;


    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mDhpAuthenticationResponse = new DhpAuthenticationResponse(mRawResponse);


    }

    public void testDhpAuthenticationResponse()
    {
        assertNotNull(mDhpAuthenticationResponse);
        mDhpAuthenticationResponse = new DhpAuthenticationResponse("accessToken","refreshToken",1221,"userId",mRawResponse) ;
        assertNotNull(mDhpAuthenticationResponse);
        assertFalse(mDhpAuthenticationResponse.equals(mRawResponse));
        assertNotNull(mDhpAuthenticationResponse.hashCode());
        assertNotNull(mDhpAuthenticationResponse.toString());
    }



}