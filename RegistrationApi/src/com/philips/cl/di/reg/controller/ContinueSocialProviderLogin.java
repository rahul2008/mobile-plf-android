package com.philips.cl.di.reg.controller;

import android.content.Context;

import com.janrain.android.Jump;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;
import com.philips.cl.di.reg.handlers.UpdateUserRecordHandler;

public class ContinueSocialProviderLogin implements Jump.SignInResultHandler,
		Jump.SignInCodeHandler {
	private SocialProviderLoginHandler mSocialProviderLoginHandler;
	private Context mContext;
	private UpdateUserRecordHandler mUpdateUserRecordHandler;

	public ContinueSocialProviderLogin(
			SocialProviderLoginHandler socialProviderLoginHandler,
			Context context,UpdateUserRecordHandler updateUserRecordHandler) {
		mSocialProviderLoginHandler = socialProviderLoginHandler;
		mContext = context;
		mUpdateUserRecordHandler = updateUserRecordHandler;
	}

	public void onSuccess() {
		Jump.saveToDisk(mContext);
		mUpdateUserRecordHandler.updateUserRecordRegister();
		mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess();
	}

	public void onCode(String code) {
	}

	public void onFailure(SignInError error) {
		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null,
				null);
		int getError = errorMapping.checkSignInError();
		mSocialProviderLoginHandler
				.onContinueSocialProviderLoginFailure(getError);
	}
}
