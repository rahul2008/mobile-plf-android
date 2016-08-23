package com.philips.cdp.registration.coppa.event;

import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.coppa.listener.UserRegistrationCoppaListener;

import org.junit.Test;

/**
 * Created by 310243576 on 8/20/2016.
 */
public class UserRegistrationCoppaHelperTest extends InstrumentationTestCase{

    UserRegistrationCoppaHelper mUserRegistrationCoppaHelper;




    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        mUserRegistrationCoppaHelper = mUserRegistrationCoppaHelper.getInstance();

    }
    @Test
    public void testGetInstance() throws Exception {
        assertNotNull(mUserRegistrationCoppaHelper);

    }

    @Test
    public void testRegisterEventNotification() throws Exception {
        UserRegistrationCoppaListener mUserRegistrationCoppaListener = new UserRegistrationCoppaListener() {

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
         mUserRegistrationCoppaHelper.registerEventNotification(mUserRegistrationCoppaListener);
    }

    @Test
    public void testUnregisterEventNotification() throws Exception {
        UserRegistrationCoppaListener userRegistrationCoppaListener = new UserRegistrationCoppaListener() {


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
        mUserRegistrationCoppaHelper.unregisterEventNotification(userRegistrationCoppaListener);

    }

    @Test
    public void testNotifyonUserRegistrationCompleteEventOccurred() throws Exception {
        mUserRegistrationCoppaHelper.notifyOnLogoutSuccessWithInvalidAccessToken();

    }





    @Test
    public void testNotifyOnUserLogoutSuccess() throws Exception {
 mUserRegistrationCoppaHelper.notifyOnUserLogoutSuccess();
    }

    @Test
    public void testNotifyOnUserLogoutFailure() throws Exception {
        mUserRegistrationCoppaHelper.notifyOnUserLogoutFailure();
    }

    @Test
    public void testNotifyOnLogoutSuccessWithInvalidAccessToken() throws Exception {
        mUserRegistrationCoppaHelper.notifyOnLogoutSuccessWithInvalidAccessToken();
    }
}