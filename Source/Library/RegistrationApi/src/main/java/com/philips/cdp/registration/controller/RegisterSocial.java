
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.controller;

import android.content.Context;
import android.util.Log;

import com.janrain.android.Jump;
import com.janrain.android.capture.*;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.dao.*;
import com.philips.cdp.registration.events.JumpFlowDownloadStatusListener;
import com.philips.cdp.registration.handlers.*;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.utils.*;

import org.json.*;

public class RegisterSocial implements SocialProviderLoginHandler,Jump.SignInResultHandler,
        Jump.SignInCodeHandler,JumpFlowDownloadStatusListener {

	private String LOG_TAG = "RegisterSocial";
	private SocialProviderLoginHandler mSocialProviderLoginHandler;

	private Context mContext;

	private UpdateUserRecordHandler mUpdateUserRecordHandler;

	public RegisterSocial(SocialProviderLoginHandler socialProviderLoginHandler,
						  Context context, UpdateUserRecordHandler updateUserRecordHandler) {
		mSocialProviderLoginHandler = socialProviderLoginHandler;
		mContext = context;
		mUpdateUserRecordHandler = updateUserRecordHandler;
	}

	public void onSuccess() {
		Jump.saveToDisk(mContext);
		User user = new User(mContext);
		mUpdateUserRecordHandler.updateUserRecordRegister();

		if (RegistrationConfiguration.getInstance().isHsdpFlow() && (user.isEmailVerified() || user.isMobileVerified())) {
			HsdpUser hsdpUser = new HsdpUser(mContext);
			String emailOrMobile;
			if (FieldsValidator.isValidEmail(user.getEmail())){
				emailOrMobile=	user.getEmail();
			}else{
				emailOrMobile=user.getMobile();
			}
			hsdpUser.socialLogin(emailOrMobile, user.getAccessToken(),Jump.getRefreshSecret(), new SocialLoginHandler() {

				@Override
				public void onLoginSuccess() {
					ThreadUtils.postInMainThread(mContext,()->
					mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess());
				}

				@Override
				public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
					AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
					ThreadUtils.postInMainThread(mContext,()->
					mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
				}
			});
		}else{
			ThreadUtils.postInMainThread(mContext,()->
			mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess());
		}
	}

	public void onCode(String code) {
	}

	public void onFailure(SignInError error) {
		UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
		userRegistrationFailureInfo.setError(error.captureApiError);
		handleInvalidInputs(error.captureApiError, userRegistrationFailureInfo);
		userRegistrationFailureInfo.setErrorCode(error.captureApiError.code);
		userRegistrationFailureInfo.setErrorDescription(error.captureApiError.error_description);
		AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
		ThreadUtils.postInMainThread(mContext,()->
		mSocialProviderLoginHandler.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo));
	}

	private void handleInvalidInputs(CaptureApiError error,
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		if (null != error && null != error.error
		        && error.error.equals(RegConstants.INVALID_FORM_FIELDS)) {
			try {
				JSONObject object = error.raw_response;
				JSONObject jsonObject = (JSONObject) object.get(RegConstants.INVALID_FIELDS);
				if (jsonObject != null) {

					if (!jsonObject.isNull(RegConstants.SOCIAL_REGISTRATION_EMAIL_ADDRESS)) {
						userRegistrationFailureInfo.setEmailErrorMessage(getErrorMessage(jsonObject
						        .getJSONArray(RegConstants.SOCIAL_REGISTRATION_EMAIL_ADDRESS)));
					}
					if (!jsonObject.isNull(RegConstants.SOCIAL_REGISTRATION_DISPLAY_NAME)) {
						userRegistrationFailureInfo
						        .setDisplayNameErrorMessage(getErrorMessage(jsonObject
						                .getJSONArray(RegConstants.SOCIAL_REGISTRATION_DISPLAY_NAME)));
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
	}

	private String getErrorMessage(JSONArray jsonArray)
	        throws JSONException {
		if (null == jsonArray) {
			return null;
		}
		return (String) jsonArray.get(0);
	}

	private JSONObject mUser;
	private String mUserRegistrationToken;

	private void registerNewUser(final JSONObject user, final String userRegistrationToken){
		mUser = user;
		mUserRegistrationToken = userRegistrationToken;
		if (!UserRegistrationInitializer.getInstance().isJumpInitializated()) {
			UserRegistrationInitializer.getInstance().registerJumpFlowDownloadListener(this);
		} else {
			Jump.registerNewUser(user, userRegistrationToken, this);
			return;

		}
		if(!UserRegistrationInitializer.getInstance().isRegInitializationInProgress()){
			RegistrationHelper.getInstance().initializeUserRegistration(mContext);
		}

	}

	@Override
	public void onFlowDownloadSuccess() {
		Jump.registerNewUser(mUser, mUserRegistrationToken, this);
		UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

	}

	@Override
	public void onFlowDownloadFailure() {
		if (mSocialProviderLoginHandler != null) {
			UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
			userRegistrationFailureInfo.setErrorDescription(mContext.getString(R.string.reg_JanRain_Server_Connection_Failed));
			userRegistrationFailureInfo.setErrorCode(RegConstants.REGISTER_SOCIAL_FAILED_SERVER_ERROR);
			ThreadUtils.postInMainThread(mContext,()->
			mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
		}
		UserRegistrationInitializer.getInstance().unregisterJumpFlowDownloadListener();

	}


	public void registerUserForSocial(final String givenName, final String displayName, final String familyName,
									  final String userEmail, final boolean olderThanAgeLimit, final boolean isReceiveMarketingEmail,
									  final String socialRegistrationToken) {
		DIUserProfile profile = new DIUserProfile();
		profile.setGivenName(givenName);
		profile.setDisplayName(displayName);
		profile.setFamilyName(familyName);
		if (FieldsValidator.isValidEmail(userEmail)){
			profile.setEmail(userEmail);

		}else {
			// check mobile umber validation
			profile.setMobile(userEmail);

		}
		profile.setOlderThanAgeLimit(olderThanAgeLimit);
		profile.setReceiveMarketingEmail(isReceiveMarketingEmail);
		completeSocialProviderLogin(profile, this, socialRegistrationToken);
	}


	private void completeSocialProviderLogin(DIUserProfile diUserProfile,
											 SocialProviderLoginHandler socialProviderLoginHandler, String socialRegistrationToken) {
		String familyName = "";
		if (diUserProfile != null) {
			familyName = diUserProfile.getFamilyName();
			JSONObject newUser = new JSONObject();
			try {
				newUser.put("email", diUserProfile.getEmail()).put("mobileNumber", diUserProfile.getMobile()).put("givenName", diUserProfile.getGivenName())
						.put("familyName", familyName).put("password", diUserProfile.getPassword())
						.put("displayName", diUserProfile.getDisplayName())
						.put("olderThanAgeLimit", diUserProfile.getOlderThanAgeLimit())
						.put("receiveMarketingEmail", diUserProfile.getReceiveMarketingEmail());

			} catch (JSONException e) {
				Log.e(LOG_TAG, "On completeSocialProviderLogin,Caught JSON Exception");
			}

			registerNewUser(newUser, socialRegistrationToken);
		} else {
			UserRegistrationFailureInfo userRegistrationFailureInfo = new UserRegistrationFailureInfo();
			userRegistrationFailureInfo.setErrorCode(RegConstants.DI_PROFILE_NULL_ERROR_CODE);
			userRegistrationFailureInfo.setErrorDescription(AppTagingConstants.NETWORK_ERROR);
			socialProviderLoginHandler
					.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo);
			AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
		}
	}



	@Override
	public void onLoginSuccess() {
		handleOnLoginSuccess();
	}


	@Override
	public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
		ThreadUtils.postInMainThread(mContext,()->
		mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
	}

	@Override
	public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord, String socialRegistrationToken) {
		ThreadUtils.postInMainThread(mContext,()->
		mSocialProviderLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord, socialRegistrationToken));
	}

	@Override
	public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider, String conflictingIdentityProvider, String conflictingIdpNameLocalized, String existingIdpNameLocalized, String emailId) {
		ThreadUtils.postInMainThread(mContext,()->
		mSocialProviderLoginHandler.onLoginFailedWithMergeFlowError(mergeToken, existingProvider, conflictingIdentityProvider, conflictingIdpNameLocalized, existingIdpNameLocalized, emailId));
	}

	@Override
	public void onContinueSocialProviderLoginSuccess() {
		ThreadUtils.postInMainThread(mContext,()->
		mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess());
	}

	@Override
	public void onContinueSocialProviderLoginFailure(UserRegistrationFailureInfo userRegistrationFailureInfo) {
		AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.JANRAIN);
		ThreadUtils.postInMainThread(mContext,()->
		mSocialProviderLoginHandler.onContinueSocialProviderLoginFailure(userRegistrationFailureInfo));
	}


	private void handleOnLoginSuccess() {
		boolean isEmailVerified;
		CaptureRecord captured = Jump.getSignedInUser();
		if (captured == null || (captured != null && captured.isNull("emailVerified"))) {
			isEmailVerified = false;
		} else {
			isEmailVerified = true;
		}
		if (RegistrationConfiguration.getInstance().isHsdpFlow() && isEmailVerified) {

			HsdpUser hsdpUser = new HsdpUser(mContext);
			try {
				hsdpUser.socialLogin(captured.getString("email"), captured.getAccessToken(),Jump.getRefreshSecret(), new SocialLoginHandler() {

					@Override
					public void onLoginSuccess() {
						ThreadUtils.postInMainThread(mContext,()->
						mSocialProviderLoginHandler.onLoginSuccess());
					}

					@Override
					public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
						AppTaggingErrors.trackActionRegisterError(userRegistrationFailureInfo, AppTagingConstants.HSDP);
						ThreadUtils.postInMainThread(mContext,()->
						mSocialProviderLoginHandler.onLoginFailedWithError(userRegistrationFailureInfo));
					}
				});
			} catch (JSONException e) {
				e.printStackTrace();
			}

		} else {
			ThreadUtils.postInMainThread(mContext,()->
			mSocialProviderLoginHandler.onLoginSuccess());
		}
	}
}
