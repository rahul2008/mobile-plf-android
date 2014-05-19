package com.philips.cl.di.reg.controller;

import android.content.Context;
import com.janrain.android.Jump;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;

public class ContinueSocialProviderLogin implements Jump.SignInResultHandler,Jump.SignInCodeHandler {
	private SocialProviderLoginHandler socialProviderLoginHandler;
	
	private Context context;

	public ContinueSocialProviderLogin(
			SocialProviderLoginHandler socialProviderLoginHandler,
			Context context) {
		this.socialProviderLoginHandler = socialProviderLoginHandler;
		this.context = context;
	}

	public void onSuccess() {
		Jump.saveToDisk(context);
		this.socialProviderLoginHandler.onContinueSocialProviderLoginSuccess();
	}

	public void onCode(String code) {
	}

	public void onFailure(SignInError error) {
		FailureErrorMaping ea = new FailureErrorMaping(error, null, null);
		int errorCondition = ea.checkSignInError();
		this.socialProviderLoginHandler.onContinueSocialProviderLoginFailure(errorCondition);
	}
}
