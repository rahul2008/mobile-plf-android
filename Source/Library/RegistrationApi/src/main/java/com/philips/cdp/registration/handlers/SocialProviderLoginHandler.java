
package com.philips.cdp.registration.handlers;

import org.json.JSONObject;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

public interface SocialProviderLoginHandler {

	public void onLoginSuccess();

	public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);

	public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,
	        String socialRegistrationToken);

	public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider,
	        String conflictingIdentityProvider, String conflictingIdpNameLocalized,
	        String existingIdpNameLocalized,String emailId);

	public void onContinueSocialProviderLoginSuccess();

	public void onContinueSocialProviderLoginFailure(
	        UserRegistrationFailureInfo userRegistrationFailureInfo);
}
