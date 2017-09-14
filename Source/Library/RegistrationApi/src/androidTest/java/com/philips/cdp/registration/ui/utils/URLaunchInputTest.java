package com.philips.cdp.registration.ui.utils;

import android.app.*;
import android.content.*;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.listener.*;
import com.philips.cdp.registration.settings.*;

import org.junit.*;

import static android.support.test.InstrumentationRegistry.*;
import static junit.framework.Assert.*;


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

}