package com.philips.cdp.registration.dao;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import static org.mockito.Mockito.mock;


public class UserRegistrationFailureInfoTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo(mock(Context.class));


    public UserRegistrationFailureInfoTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    }



    public void testErrorDescription(){
        userRegistrationFailureInfo.setErrorDescription("sampleError");

        assertEquals("sampleError",userRegistrationFailureInfo.getErrorDescription());

    }
    public void testErrorCode(){
        userRegistrationFailureInfo.setErrorCode(1);

        assertEquals(1,userRegistrationFailureInfo.getErrorCode());

    }

}
