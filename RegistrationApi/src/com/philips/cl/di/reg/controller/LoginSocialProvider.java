package com.philips.cl.di.reg.controller;

import org.json.JSONObject;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.engage.session.JRProvider;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;

public class LoginSocialProvider implements Jump.SignInResultHandler,
		Jump.SignInCodeHandler {
	private Context mContext;
	private SocialProviderLoginHandler mSocialLoginHandler;
	private String mMergeToken;
	private int mGetError = 1;
	private UpdateUserRecordHandler mUpdateUserRecordHandler;

	public LoginSocialProvider(SocialProviderLoginHandler socialLoginHandler,
			Context context,UpdateUserRecordHandler updateUserRecordHandler) {
		mSocialLoginHandler = socialLoginHandler;
		mContext = context;
		mUpdateUserRecordHandler =updateUserRecordHandler ;
	}

	@Override
	public void onSuccess() {
		Jump.saveToDisk(mContext);
		mUpdateUserRecordHandler.updateUserRecordLogin();
		mSocialLoginHandler.onLoginSuccess();
	}

	@Override
	public void onCode(String code) {

	}

	@Override
	public void onFailure(SignInError error) {

		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null,
				null);
		mGetError = errorMapping.checkSignInError();

		if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
				&& error.captureApiError.isMergeFlowError()) {

			mMergeToken = error.captureApiError.getMergeToken();
			final String existingProvider = error.captureApiError
					.getExistingAccountIdentityProvider();
			String conflictingIdentityProvider = error.captureApiError
					.getConflictingIdentityProvider();
			String conflictingIdpNameLocalized = JRProvider
					.getLocalizedName(conflictingIdentityProvider);
			String existingIdpNameLocalized = JRProvider
					.getLocalizedName(conflictingIdentityProvider);
			mSocialLoginHandler.onLoginFailedWithMergeFlowError(mMergeToken,
					existingProvider, conflictingIdentityProvider,
					conflictingIdpNameLocalized, existingIdpNameLocalized);
		} else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
				&& error.captureApiError.isTwoStepRegFlowError()) {

			JSONObject prefilledRecord = error.captureApiError
					.getPreregistrationRecord();
			String socialRegistrationToken = error.captureApiError
					.getSocialRegistrationToken();
			mSocialLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord,
					socialRegistrationToken);

		} else {
			mSocialLoginHandler.onLoginFailedWithError(mGetError);
		}

	}

}
