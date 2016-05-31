/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils.test;



import android.test.ActivityInstrumentationTestCase2;

import com.janrain.android.Jump;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.controller.RegisterSocial;
import com.philips.cdp.registration.controller.RegisterTraditional;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.handlers.SocialProviderLoginHandler;
import com.philips.cdp.registration.handlers.TraditionalRegistrationHandler;
import com.philips.cdp.registration.handlers.UpdateUserRecordHandler;
import com.philips.cdp.registration.ui.traditional.RegistrationActivity;

import org.mockito.Mockito;

import static org.mockito.Mockito.mock;

public class UserTest extends ActivityInstrumentationTestCase2<RegistrationActivity> {

	UserTest mUserTest = null;

	public UserTest() {
		super(RegistrationActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		System.setProperty("dexmaker.dexcache", getInstrumentation()
				.getTargetContext().getCacheDir().getPath());
		mUserTest = mock(UserTest.class);
	}
	
	public void testUser() throws Exception {

		User result = new User(getInstrumentation().getTargetContext());
		assertNotNull(result);
	}

	/*public void testRegisterUserInfoForTraditionalIsOnSuccess() {

		User mockUser = Mockito.mock(User.class);
		TraditionalRegistrationHandler regHandler = Mockito
				.mock(TraditionalRegistrationHandler.class);

		UpdateUserRecordHandler updateHandler = Mockito
				.mock(UpdateUserRecordHandler.class);

		DIUserProfile diUserProfile = Mockito.mock(DIUserProfile.class);
		mockUser.registerUserInfoForTraditional("a","b","c",true,false,regHandler);
		mockUser.registerUserInfoForSocial("a","b","c","a@a.com",true,false,regHandler);
		//mockUser.registerNewUserUsingTraditional(diUserProfile,regHandler);
		//mockUser.registerNewUserUsingTraditional(setValuesForTraditionalLogin("Sampath", "sampath.kumar@yahoo.com", "Sams@1234", true, false), regHandler);

		Jump.SignInResultHandler mockJump = Mockito
				.mock(Jump.SignInResultHandler.class);
		mockJump.onSuccess();
		RegisterTraditional handler = new RegisterTraditional(regHandler,
				getInstrumentation().getTargetContext(), updateHandler);

		handler.onSuccess();
		Mockito.verify(regHandler, Mockito.atLeast(1)).onRegisterSuccess();
		Mockito.verify(mockUser, Mockito.atLeast(1)).registerNewUserUsingTraditional(diUserProfile,regHandler);
	}

	public void testRegisterUserInfoForSocialIsOnSuccess() {

		String SOCIAL_REG_TOKEN = "socialRegistrationToken";
		User mockUser = Mockito.mock(User.class);
		SocialProviderLoginHandler socialRegHandler = Mockito
				.mock(SocialProviderLoginHandler.class);

		UpdateUserRecordHandler updateHandler = Mockito
				.mock(UpdateUserRecordHandler.class);

		mockUser.completeSocialProviderLogin(setValuesForSocialLogin("SamPath", "Sam", "kumar",
						"sampath1421@gmail.com", true, false, socialRegHandler,
						SOCIAL_REG_TOKEN), socialRegHandler, SOCIAL_REG_TOKEN);

		Jump.SignInResultHandler mockJump = Mockito
				.mock(Jump.SignInResultHandler.class);
		mockJump.onSuccess();
		RegisterSocial handler = new RegisterSocial(
				socialRegHandler, getInstrumentation().getTargetContext(),
				updateHandler);

		handler.onSuccess();
		Mockito.verify(socialRegHandler, Mockito.atLeast(1))
				.onContinueSocialProviderLoginSuccess();

	}*/
	
/*	public void testForgotPasswordForEmailNull() throws Exception {

		User user = new User(getInstrumentation().getTargetContext());
		user.mEmail = "sampath1421@gmail.com";
		user.mPassword = "Sams1234";
		user.mGivenName = "sampatnkumar";
		user.mDisplayName = "sam";
		String emailAddress = null;

		ForgotPasswordHandler forgotpasswordhandler = new ForgotPasswordHandler() {

			@Override
			public void onSendForgotPasswordSuccess() {

			}

			@Override
			public void onSendForgotPasswordFailedWithError(int error) {

			}
		};
		user.forgotPassword(emailAddress, forgotpasswordhandler);
	}*/
	
	private DIUserProfile setValuesForTraditionalLogin(String mGivenName, String mUserEmail, String password, boolean olderThanAgeLimit, 
			boolean isReceiveMarketingEmail){
		
		DIUserProfile profile = new DIUserProfile();
		profile.setGivenName(mGivenName);
		profile.setEmail(mUserEmail);
		profile.setPassword(password);
		profile.setOlderThanAgeLimit(olderThanAgeLimit);
		profile.setReceiveMarketingEmail(isReceiveMarketingEmail);
		
		return profile;
		
	}
	
	private DIUserProfile setValuesForSocialLogin (String mGivenName, String mDisplayName, String mFamilyName, String mUserEmail, boolean olderThanAgeLimit, boolean isReceiveMarketingEmail,
			SocialProviderLoginHandler socialProviderLoginHandler,
			String socialRegistrationToken){
		
		DIUserProfile profile = new DIUserProfile();
		profile.setGivenName(mGivenName);
		profile.setDisplayName(mDisplayName);
		profile.setFamilyName(mFamilyName);
		profile.setEmail(mUserEmail);
		profile.setOlderThanAgeLimit(olderThanAgeLimit);
		profile.setReceiveMarketingEmail(isReceiveMarketingEmail);
		
		return profile;
	}
}
