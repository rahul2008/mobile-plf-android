
/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.listener;

import android.app.Activity;

import com.philips.platform.uappframework.listener.UappListener;

import java.io.Serializable;

public interface UserRegistrationListener extends UappListener, Serializable {

    long serialVersionUID = 1240231269747142242L;

    void onUserRegistrationComplete(Activity activity);

    void onPrivacyPolicyClick(Activity activity);

    void onTermsAndConditionClick(Activity activity);

    void onUserLogoutSuccess();

    void onUserLogoutFailure();

    void onUserLogoutSuccessWithInvalidAccessToken();

}
