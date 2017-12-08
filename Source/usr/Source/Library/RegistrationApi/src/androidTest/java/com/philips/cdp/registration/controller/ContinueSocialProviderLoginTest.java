/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.support.multidex.MultiDex;
import android.test.ActivityInstrumentationTestCase2;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import org.json.JSONObject;
import org.junit.Test;


public class ContinueSocialProviderLoginTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {
    RegisterSocial continueSocialProviderLogin;

    public ContinueSocialProviderLoginTest() {
        super(RegistrationActivity.class);
    }
    @Override
    protected void setUp() throws Exception {
        MultiDex.install(getInstrumentation().getTargetContext());
        super.setUp();
        System.setProperty("dexmaker.dexcache", getInstrumentation()
                .getTargetContext().getCacheDir().getPath());

        continueSocialProviderLogin = new RegisterSocial(new SocialProviderLoginHandler() {
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
    @Test
    public void test_onSuccess(){
       /* RegisterSocial continuesocialLogin = Mockito.mock(RegisterSocial.class);
        UpdateUserRecordHandler updateUserRecordHandler = Mockito.mock(UpdateUserRecordHandler.class);
        continueSocialProviderLogin.onSuccess();

        Mockito.verify(continueSocialProviderLogin,Mockito.atLeast(1)).onSuccess();*/
        // Mockito.verify(updateUserRecordHandler,Mockito.atLeast(1)).updateUserRecordRegister();

    }

}
