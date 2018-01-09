/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */
package com.philips.cdp.registration.listener;

import android.app.Activity;

/**
 * UI Registtaion Event Listener
 * @since 1.0.0
 */
public interface UserRegistrationUIEventListener {
    /**
     * When user complete the registion process completed
     *
     * @param activity launching activity as callback
     *                 @since 1.0.0
     */
    void onUserRegistrationComplete(Activity activity);

    /**
     * Privecy policy link clicked callback
     *
     * @param activity launching activity as callback
     *                 @since 1.0.0
     */
    void onPrivacyPolicyClick(Activity activity);

    /**
     * On terms and condition clicked callback.
     *
     * @param activity launching activity as callback
     *                 @since 1.0.0
     */
    void onTermsAndConditionClick(Activity activity);

}
