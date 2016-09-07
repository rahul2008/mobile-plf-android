package com.philips.cdp.registration.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;

import org.junit.Before;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 9/6/2016.
 */
public class URLaunchInputTest extends InstrumentationTestCase{
    Context mContext;
    URLaunchInput mURLaunchInput;

    @Before
    public void setUp() throws Exception {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
        super.setUp();
        mContext = getInstrumentation().getTargetContext();
        mURLaunchInput = new URLaunchInput();
    }
    public void testURLaunch(){

        UserRegistrationUIEventListener mUserRegistrationUIEventListener = new UserRegistrationUIEventListener() {
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
        mURLaunchInput.setUserRegistrationUIEventListener(mUserRegistrationUIEventListener);
        assertNotNull(mURLaunchInput.getUserRegistrationUIEventListener());
        mURLaunchInput.setAccountSettings(false);
        assertFalse(mURLaunchInput.isAccountSettings());
        mURLaunchInput.setAccountSettings(true);
        assertTrue(mURLaunchInput.isAccountSettings());

        mURLaunchInput.setRegistrationFunction(RegistrationFunction.SignIn);
        assertNotNull(mURLaunchInput.getRegistrationFunction());

        mURLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        assertNotNull(mURLaunchInput.getRegistrationFunction());
        mURLaunchInput.enableAddtoBackStack(true);
        assertTrue(mURLaunchInput.isAddtoBackStack());
        mURLaunchInput.enableAddtoBackStack(false);
        assertFalse(mURLaunchInput.isAddtoBackStack());
    }
}