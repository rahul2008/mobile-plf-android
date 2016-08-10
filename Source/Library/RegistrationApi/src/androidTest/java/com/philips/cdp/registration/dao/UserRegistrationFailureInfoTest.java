package com.philips.cdp.registration.dao;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

/**
 * Created by 310243576 on 8/10/2016.
 */
public class UserRegistrationFailureInfoTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

    UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();


    public UserRegistrationFailureInfoTest() {
        super(RegistrationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());
    }

    public void testDisplayNameErrorMessage(){

        userRegistrationFailureInfo.setDisplayNameErrorMessage("sampleError");

        assertEquals("sampleError",userRegistrationFailureInfo.getDisplayNameErrorMessage());

    }
    public void testSocialOnlyError(){
        userRegistrationFailureInfo.setSocialOnlyError("sampleError");

        assertEquals("sampleError",userRegistrationFailureInfo.getSocialOnlyError());

    }
    public void testErrorDescription(){
        userRegistrationFailureInfo.setErrorDescription("sampleError");

        assertEquals("sampleError",userRegistrationFailureInfo.getErrorDescription());

    }
    public void testErrorCode(){
        userRegistrationFailureInfo.setErrorCode(1);

        assertEquals(1,userRegistrationFailureInfo.getErrorCode());

    }
    public void testFirstNameErrorMessage(){
        userRegistrationFailureInfo.setFirstNameErrorMessage("sampleError");

        assertEquals("sampleError",userRegistrationFailureInfo.getFirstNameErrorMessage());

    }
    public void testEmailErrorMessage(){
        userRegistrationFailureInfo.setEmailErrorMessage("sampleError");

        assertEquals("sampleError",userRegistrationFailureInfo.getEmailErrorMessage());
    }
    public void testPasswordErrorMessage(){
        userRegistrationFailureInfo.setPasswordErrorMessage("sampleError");

        assertEquals("sampleError",userRegistrationFailureInfo.getPasswordErrorMessage());
    }

}
