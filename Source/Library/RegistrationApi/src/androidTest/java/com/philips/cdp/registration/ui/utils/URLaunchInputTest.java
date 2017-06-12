package com.philips.cdp.registration.ui.utils;

import android.app.Activity;
import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;

import org.junit.Before;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;


public class URLaunchInputTest extends RegistrationApiInstrumentationBase {
    Context mContext;
    URLaunchInput mURLaunchInput;

    @Before
    public void setUp() throws Exception {
          super.setUp();
        mContext = getInstrumentation().getTargetContext();
        mURLaunchInput = new URLaunchInput();
    }
    @Test
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
        mURLaunchInput.setRegistrationFunction(RegistrationFunction.SignIn);
        assertNotNull(mURLaunchInput.getRegistrationFunction());

        mURLaunchInput.setRegistrationFunction(RegistrationFunction.Registration);
        assertNotNull(mURLaunchInput.getRegistrationFunction());
        mURLaunchInput.enableAddtoBackStack(true);
        assertTrue(mURLaunchInput.isAddtoBackStack());
        mURLaunchInput.enableAddtoBackStack(false);
        assertFalse(mURLaunchInput.isAddtoBackStack());
    }
@Test
    public void testIsAccountSettingsTrue(){
        mURLaunchInput.setEndPointScreen(RegistrationLaunchMode.ACCOUNT_SETTINGS);
        assertEquals(RegistrationLaunchMode.ACCOUNT_SETTINGS,mURLaunchInput.getEndPointScreen());
    }
}