package com.philips.cl.di.reg.handlers;

import org.json.JSONObject;

public interface SocialProviderLoginHandler {
	public void onLoginSuccess();
	public void onLoginFailedWithError(int error);
	public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,String socialRegistrationToken);
	public void onLoginFailedWithMergeFlowError(String mergeToken,String existingProvider, String conflictingIdentityProvider,
	            String conflictingIdpNameLocalized, String existingIdpNameLocalized);
	public void onContinueSocialProviderLoginSuccess();
	public void onContinueSocialProviderLoginFailure(int error);
}
