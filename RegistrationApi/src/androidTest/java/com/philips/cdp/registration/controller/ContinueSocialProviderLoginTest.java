package com.philips.cdp.registration.controller;

import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import org.json.JSONObject;
import org.mockito.Mockito;

/**
 * Created by 310202337 on 11/27/2015.
 */
public class ContinueSocialProviderLoginTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {
    ContinueSocialProviderLogin continueSocialProviderLogin;

    public ContinueSocialProviderLoginTest() {
        super(RegistrationActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        continueSocialProviderLogin = new ContinueSocialProviderLogin(new SocialProviderLoginHandler() {
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
        }, getInstrumentation()
                .getTargetContext(), new UpdateUserRecordHandler() {
            @Override
            public void updateUserRecordLogin() {

            }

            @Override
            public void updateUserRecordRegister() {

            }
        });



    }

    public void test_onSuccess(){
       /* ContinueSocialProviderLogin continuesocialLogin = Mockito.mock(ContinueSocialProviderLogin.class);
        UpdateUserRecordHandler updateUserRecordHandler = Mockito.mock(UpdateUserRecordHandler.class);
        continueSocialProviderLogin.onSuccess();

        Mockito.verify(continueSocialProviderLogin,Mockito.atLeast(1)).onSuccess();*/
        // Mockito.verify(updateUserRecordHandler,Mockito.atLeast(1)).updateUserRecordRegister();

    }

}
