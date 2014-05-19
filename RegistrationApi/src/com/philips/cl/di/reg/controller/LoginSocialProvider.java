package com.philips.cl.di.reg.controller;

import org.json.JSONObject;
import android.content.Context;
import com.janrain.android.Jump;
import com.janrain.android.engage.session.JRProvider;
import com.philips.cl.di.reg.errormapping.FailureErrorMaping;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;


public class LoginSocialProvider implements Jump.SignInResultHandler,Jump.SignInCodeHandler {
    private Context context;
	private SocialProviderLoginHandler socialLoginHandler;
	private String mergeToken;
	private int errorCondition = 0;

	public LoginSocialProvider(SocialProviderLoginHandler socialLoginHandler,Context context) {
		this.socialLoginHandler = socialLoginHandler;
		this.context = context;
	}

	@Override
	public void onSuccess() {
		Jump.saveToDisk(context);
		this.socialLoginHandler.onLoginSuccess();
	}

	@Override
	public void onCode(String code) {
		
	}

	@Override
	public void onFailure(SignInError error) {
		FailureErrorMaping errorMapping = new FailureErrorMaping(error, null,null);
		errorCondition = errorMapping.checkSignInError();

		if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
				&& error.captureApiError.isMergeFlowError()) {

			mergeToken = error.captureApiError.getMergeToken();
			final String existingProvider = error.captureApiError.getExistingAccountIdentityProvider();
			String conflictingIdentityProvider = error.captureApiError.getConflictingIdentityProvider();
			String conflictingIdpNameLocalized = JRProvider.getLocalizedName(conflictingIdentityProvider);
			String existingIdpNameLocalized = JRProvider.getLocalizedName(conflictingIdentityProvider);
			this.socialLoginHandler.onLoginFailedWithMergeFlowError(mergeToken,existingProvider, 
					conflictingIdentityProvider,conflictingIdpNameLocalized, existingIdpNameLocalized);
		}
       else if (error.reason == SignInError.FailureReason.CAPTURE_API_ERROR
				&& error.captureApiError.isTwoStepRegFlowError()) {

			JSONObject prefilledRecord = error.captureApiError.getPreregistrationRecord();
			String socialRegistrationToken = error.captureApiError.getSocialRegistrationToken();
			this.socialLoginHandler.onLoginFailedWithTwoStepError(prefilledRecord, socialRegistrationToken);
			
		} else {
			this.socialLoginHandler.onLoginFailedWithError(errorCondition);
		}
	}

}
