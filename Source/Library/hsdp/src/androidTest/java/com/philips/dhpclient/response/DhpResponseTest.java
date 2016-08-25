package com.philips.dhpclient.response;

import android.test.InstrumentationTestCase;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpResponseTest extends InstrumentationTestCase{

    DhpResponse mDhpResponse;
    DhpResponse mDhpResponse1;

    Map<String, Object> mRawResponse;
    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mRawResponse = new HashMap<String,Object>();
        mDhpResponse = new DhpResponse(mRawResponse);
        mDhpResponse1 = new DhpResponse(mRawResponse);

    }

    public void testDhpResponse(){
        assertNotNull(mDhpResponse);
        mDhpResponse = new DhpResponse("sample",mRawResponse);
        assertNotNull(mDhpResponse);
        assertTrue(mDhpResponse.equals(mDhpResponse));
        assertFalse(mDhpResponse.equals(mDhpResponse1));
        assertFalse(mDhpResponse.equals(mRawResponse));
        assertFalse(mDhpResponse.equals(null));
        assertNotNull(mDhpResponse.hashCode());
        assertNotNull(mDhpResponse.toString());
    }
}