
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.handlers;

import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;

import org.json.JSONObject;

/**
 * Social provider login handler interface
 */
public interface SocialProviderLoginHandler {

    /**
     * {@code onLoginSuccess} method to on login success
     */
    void onLoginSuccess();

    /**
     * {@code userRegistrationFailureInfo} method ton login failed with error
     *
     * @param userRegistrationFailureInfo user registration failure info
     */
    void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo);

    /**
     * {@code onLoginFailedWithTwoStepError} method to on login failed with tow step error
     *
     * @param prefilledRecord         pre-filled record json object
     * @param socialRegistrationToken social registration token
     */
    void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,
                                       String socialRegistrationToken);

    /**
     * {@code onLoginFailedWithMergeFlowError} method to On login failed with merge flow error
     *
     * @param mergeToken                  merge token
     * @param existingProvider            existing provider
     * @param conflictingIdentityProvider conflicting identity provider
     * @param conflictingIdpNameLocalized conflicting idp name localized
     * @param existingIdpNameLocalized    existing idp name localized
     * @param emailId                     email id
     */
    void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider,
                                         String conflictingIdentityProvider, String conflictingIdpNameLocalized,
                                         String existingIdpNameLocalized, String emailId);

    /**
     * {@code onContinueSocialProviderLoginSuccess} method to on contiues social provider login success
     */
    void onContinueSocialProviderLoginSuccess();

    /**
     * {@code userRegistrationFailureInfo} method to on continue social provider login failure
     *
     * @param userRegistrationFailureInfo user registration failure info
     */
    void onContinueSocialProviderLoginFailure(
            UserRegistrationFailureInfo userRegistrationFailureInfo);
}