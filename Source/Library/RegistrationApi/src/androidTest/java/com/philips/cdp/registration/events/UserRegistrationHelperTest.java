package com.philips.cdp.registration.events;

import android.content.Context;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.listener.UserRegistrationListener;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.junit.Assert.*;

/**
 * Created by 310243576 on 8/18/2016.
 */
public class UserRegistrationHelperTest extends InstrumentationTestCase{

    @Mock
    UserRegistrationHelper mUserRegistrationHelper;

    @Mock
    Context context;

    @Before
    public void setUp() throws Exception {
        // Necessary to get Mockito framework working
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
//        MockitoAnnotations.initMocks(this);
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