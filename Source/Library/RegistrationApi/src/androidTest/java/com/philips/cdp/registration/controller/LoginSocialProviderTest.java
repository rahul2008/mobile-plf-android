package com.philips.cdp.registration.controller;

import android.content.Context;

import com.philips.cdp.registration.RegistrationApiInstrumentationBase;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;

import org.json.JSONObject;
import org.junit.Before;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by 310243576 on 8/26/2016.
 */
public class LoginSocialProviderTest extends RegistrationApiInstrumentationBase {

    LoginSocialProvider mLoginSocialProvider;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        Context context =  getInstrumentation()
                .getTargetContext();;

        SocialProviderLoginHandler mSocialProviderLoginHandler= new SocialProviderLoginHandler() {
            @Override
            public void onLoginSuccess() {

            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }

            @Override
            public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {

            }

            @Override
            public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {

            }

            @Override
            public void onContinueSocialProviderLoginSuccess() {

            }

            @Override
            public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };

        UpdateUserRecordHandler mUpdateUserRecordHandler = new UpdateUserRecordHandler() {
            @Override
            public void updateUserRecordLogin() {

            }

            @Override
            public void updateUserRecordRegister() {

            }
        };
        mLoginSocialProvider = new LoginSocialProvider(mSocialProviderLoginHandler,context,mUpdateUserRecordHandler);
    }


    public void testLoginSocialProvider() {

        assertNotNull(mLoginSocialProvider);
//        mLoginSocialProvider.onSuccess();
        mLoginSocialProvider.onCode("sample");
        mLoginSocialProvider.onFlowDownloadFailure();
    }
}