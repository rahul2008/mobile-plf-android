/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils.test;


import android.app.Activity;
import android.content.Context;
import android.test.InstrumentationTestCase;

import com.janrain.android.Jump;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.controller.RegisterSocial;
import com.philips.cdp.registration.controller.RegisterTraditional;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.handlers.ForgotPasswordHandler;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;

import org.json.JSONObject;
import org.mockito.Mockito;

public class UserTest extends InstrumentationTestCase {

	User mUser = null;

//	public UserTest() {
	//	super(RegistrationActivity.class);
	//}
Context context;
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		context = getInstrumentation().getTargetContext();
		System.setProperty("dexmaker.dexcache", context.getCacheDir().getPath());
		mUser = new User(context);
		//getInstrumentation().get
	}
	
	public void testUser() throws Exception {

		User result = new User(getInstrumentation().getTargetContext());
		assertNotNull(result);
	}

	public void testRegisterUserInfoForTraditionalIsOnSuccess() throws  RuntimeException{


		TraditionalRegistrationHandler regHandler = new TraditionalRegistrationHandler() {
            @Override
            public void onRegisterSuccess() {

            }

            @Override
            public void onRegisterFailedWithFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };

		UpdateUserRecordHandler updateHandler = new UpdateUserRecordHandler() {
            @Override
            public void updateUserRecordLogin() {

            }

            @Override
            public void updateUserRecordRegister() {

            }
        };
        SocialProviderLoginHandler socialProviderLoginHandler = new SocialProviderLoginHandler() {
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

        mUser.loginUserUsingSocialProvider(null,null,
                socialProviderLoginHandler, "mergeToken");

		Jump.SignInResultHandler mockJump = new Jump.SignInResultHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(SignInError error) {

            }
        };
		mockJump.onSuccess();
		RegisterTraditional handler = new RegisterTraditional(regHandler,
				getInstrumentation().getTargetContext(), updateHandler);

		handler.onSuccess();

        TraditionalLoginHandler traditionalLoginHandler = new TraditionalLoginHandler() {
            @Override
            public void onLoginSuccess() {

            }

            @Override
            public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };
        try {
            mUser.loginUsingTraditional("sample","sample", null);
        }catch(Exception e){

        }
        try {
            mUser.loginUsingTraditional(null, null, null);
        }catch(Exception e){

        }
	}

	public void testRegisterUserInfoForSocialIsOnSuccess() {

		String SOCIAL_REG_TOKEN = "socialRegistrationToken";
		SocialProviderLoginHandler socialRegHandler = new SocialProviderLoginHandler() {
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

		UpdateUserRecordHandler updateHandler = new UpdateUserRecordHandler() {
            @Override
            public void updateUserRecordLogin() {

            }

            @Override
            public void updateUserRecordRegister() {

            }
        };


		Jump.SignInResultHandler mockJump = new Jump.SignInResultHandler() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(SignInError error) {

            }
        };
        mockJump.onSuccess();

	}
    public void testForgotPasswordForEmailNull() throws Exception {

		String emailAddress = null;

		ForgotPasswordHandler forgotpasswordhandler = new ForgotPasswordHandler() {

			@Override
			public void onSendForgotPasswordSuccess() {

			}

            @Override
            public void onSendForgotPasswordFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {

            }
        };
		mUser.forgotPassword(emailAddress, forgotpasswordhandler);
	}
	
}
