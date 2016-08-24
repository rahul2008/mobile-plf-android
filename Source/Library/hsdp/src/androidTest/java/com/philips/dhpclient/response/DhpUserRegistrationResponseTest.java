package com.philips.dhpclient.response;

import android.test.InstrumentationTestCase;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpUserRegistrationResponseTest extends InstrumentationTestCase{

    DhpUserRegistrationResponse mDhpUserRegistrationResponse;
    Map<String, Object> mRawResponse;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mRawResponse = new HashMap<String,Object>();
        mDhpUserRegistrationResponse = new DhpUserRegistrationResponse("sample",mRawResponse);

    }

    public void testDhpUserRegistrationResponse(){
        assertNotNull(mDhpUserRegistrationResponse);
        mDhpUserRegistrationResponse = new DhpUserRegistrationResponse("sample","sample",mRawResponse);
        assertNotNull(mDhpUserRegistrationResponse);
        assertFalse(mDhpUserRegistrationResponse.equals(mRawResponse));
        assertNotNull(mDhpUserRegistrationResponse.hashCode());
    }
}