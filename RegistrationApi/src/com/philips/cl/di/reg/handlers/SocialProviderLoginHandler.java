package com.philips.cl.di.reg.handlers;

import org.json.JSONObject;

import com.philips.cl.di.reg.dao.SignInSocialFailureInfo;

public interface SocialProviderLoginHandler {
	public void onLoginSuccess();
	public void onLoginFailedWithError(SignInSocialFailureInfo signInSocialFailureInfo);
	public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,String socialRegistrationToken);
	public void onLoginFailedWithMergeFlowError(String mergeToken,String existingProvider, String conflictingIdentityProvider,
	            String conflictingIdpNameLocalized, String existingIdpNameLocalized);
	public void onContinueSocialProviderLoginSuccess();
	public void onContinueSocialProviderLoginFailure(SignInSocialFailureInfo signInSocialFailureInfo);
}
