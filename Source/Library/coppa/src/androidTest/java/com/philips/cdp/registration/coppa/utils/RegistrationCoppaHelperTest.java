package com.philips.cdp.registration.coppa.utils;

import android.app.Activity;
import android.test.InstrumentationTestCase;

import org.junit.Test;

/**
 * Created by 310243576 on 8/25/2016.
 */
public class RegistrationCoppaHelperTest extends InstrumentationTestCase{

    RegistrationCoppaHelper mRegistrationCoppaHelper;




    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        mRegistrationCoppaHelper = mRegistrationCoppaHelper.getInstance();

    }

    @Test
    public void testRegistrationCoppaHelper(){
        UserRegistrationCoppaUIEventListener userRegistrationUIEventListener = new UserRegistrationCoppaUIEventListener() {
            @Override
            public void onUserRegistrationComplete(Activity activity) {

            }

            @Override
            public void onPrivacyPolicyClick(Activity activity) {

            }

            @Override
            public void onTermsAndConditionClick(Activity activity) {

            }
        };
        mRegistrationCoppaHelper.setUserRegistrationUIEventListener(userRegistrationUIEventListener);
        assertNotNull(mRegistrationCoppaHelper.getUserRegistrationUIEventListener());
        assertEquals(userRegistrationUIEventListener,mRegistrationCoppaHelper.getUserRegistrationUIEventListener());

            }

    public void testRegisterUserRegistrationListner(){
        UserRegistrationCoppaListener userRegistrationListener = new UserRegistrationCoppaListener() {
            @Override
            public void onUserLogoutSuccess() {

            }

            @Override
            public void onUserLogoutFailure() {

            }

            @Override
            public void onUserLogoutSuccessWithInvalidAccessToken() {

            }
        };
        mRegistrationCoppaHelper.registerUserRegistrationListener(userRegistrationListener);
        assertNotNull(mRegistrationCoppaHelper.getUserRegistrationListener());

    }

    public void testUnRegisterUserRegistrationListener(){
        UserRegistrationCoppaListener userRegistrationListener = new UserRegistrationCoppaListener() {
            @Override
            public void onUserLogoutSuccess() {

            }

            @Override
            public void onUserLogoutFailure() {

            }

            @Override
            public void onUserLogoutSuccessWithInvalidAccessToken() {

            }
        };
        mRegistrationCoppaHelper.unRegisterUserRegistrationListener(userRegistrationListener);
        assertNotNull(mRegistrationCoppaHelper.getUserRegistrationListener());
    }
}