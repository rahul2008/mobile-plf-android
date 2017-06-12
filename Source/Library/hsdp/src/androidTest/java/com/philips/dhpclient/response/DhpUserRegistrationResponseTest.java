
package com.philips.dhpclient.response;

import android.support.multidex.MultiDex;
import android.test.InstrumentationTestCase;

import org.junit.Before;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310243576 on 8/24/2016.
 */
public class DhpUserRegistrationResponseTest extends InstrumentationTestCase{

    DhpUserRegistrationResponse mDhpUserRegistrationResponse;
    DhpUserRegistrationResponse mDhpUserRegistrationResponse1;

    Map<String, Object> mRawResponse;

    @Before
    public void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());

        mRawResponse = new HashMap<String,Object>();
        mDhpUserRegistrationResponse = new DhpUserRegistrationResponse("sample",mRawResponse);
        mDhpUserRegistrationResponse1 = new DhpUserRegistrationResponse("sample",mRawResponse);


    }

    public void testDhpUserRegistrationResponse(){
        assertNotNull(mDhpUserRegistrationResponse);
        mDhpUserRegistrationResponse = new DhpUserRegistrationResponse("sample","sample",mRawResponse);
        assertNotNull(mDhpUserRegistrationResponse);
        assertTrue(mDhpUserRegistrationResponse.equals(mDhpUserRegistrationResponse));
        assertFalse(mDhpUserRegistrationResponse.equals(mDhpUserRegistrationResponse1));
        assertFalse(mDhpUserRegistrationResponse.equals(mRawResponse));
        assertFalse(mDhpUserRegistrationResponse.equals(null));


        assertNotNull(mDhpUserRegistrationResponse.hashCode());
    }
}
