package com.philips.cdp.registration.events;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.listener.UserRegistrationListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;


public class UserRegistrationHelperTest extends RegistrationApiInstrumentationBase {

    @Mock
    UserRegistrationHelper mUserRegistrationHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
           super.setUp();

        assertNotNull(mUserRegistrationHelper.getInstance());

        mUserRegistrationHelper = mUserRegistrationHelper.getInstance();
        context = getInstrumentation().getTargetContext();

    }
    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(mUserRegistrationHelper);
        mUserRegistrationHelper.getInstance();
        UserRegistrationListener observer = new UserRegistrationListener() {
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
        mUserRegistrationHelper.registerEventNotification(observer);
        mUserRegistrationHelper.unregisterEventNotification(observer);
        mUserRegistrationHelper.notifyOnUserLogoutSuccess();
        mUserRegistrationHelper.notifyOnUserLogoutFailure();
        mUserRegistrationHelper.notifyOnLogoutSuccessWithInvalidAccessToken();
    }
}