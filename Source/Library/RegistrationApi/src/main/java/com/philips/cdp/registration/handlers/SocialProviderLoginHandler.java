
package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

import org.json.JSONObject;

public interface SocialProviderLoginHandler {

    void onLoginSuccess();

    void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);

    void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,
                                       String socialRegistrationToken);

    void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider,
                                         String conflictingIdentityProvider, String conflictingIdpNameLocalized,
                                         String existingIdpNameLocalized, String emailId);

    void onContinueSocialProviderLoginSuccess();

    void onContinueSocialProviderLoginFailure(
            UserRegistrationFailureInfo userRegistrationFailureInfo);
}
