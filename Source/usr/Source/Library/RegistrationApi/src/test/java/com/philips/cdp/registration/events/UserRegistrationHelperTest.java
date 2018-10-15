package com.philips.cdp.registration.events;

import android.content.Context;

import com.philips.cdp.registration.listener.UserRegistrationListener;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserRegistrationHelperTest extends TestCase {

    @Mock
    UserRegistrationHelper mUserRegistrationHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();

        assertNotNull(mUserRegistrationHelper.getInstance());

        mUserRegistrationHelper = mUserRegistrationHelper.getInstance();
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