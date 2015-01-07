package com.philips.cl.di.reg.controller;

import android.content.Context;
import com.janrain.android.Jump;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;

public class ContinueSocialProviderLogin implements Jump.SignInResultHandler,Jump.SignInCodeHandler {
	private SocialProviderLoginHandler mSocialProviderLoginHandler;
	private Context mContext;

	public ContinueSocialProviderLogin(SocialProviderLoginHandler socialProviderLoginHandler,
			Context context) {
		mSocialProviderLoginHandler = socialProviderLoginHandler;
		mContext = context;
	}

	public void onSuccess() {
		Jump.saveToDisk(mContext);
		mSocialProviderLoginHandler.onContinueSocialProviderLoginSuccess();
	}

	public void onCode(String code) {
	}

	public void onFailure(SignInError error) {
		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null, null);
		int getError = errorMapping.checkSignInError();
		mSocialProviderLoginHandler.onContinueSocialProviderLoginFailure(getError);
	}
}
