
package com.philips.cdp.registration.listener;

import android.app.Activity;

public interface UserRegistrationListener {

    void onUserRegistrationComplete(Activity activity);

    void onPrivacyPolicyClick(Activity activity);

    void onTermsAndConditionClick(Activity activity);

    void onUserLogoutSuccess();

    void onUserLogoutFailure();

    void onUserLogoutSuccessWithInvalidAccessToken();

}
