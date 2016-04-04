
package com.philips.cdp.registration.coppa.listener;

import android.app.Activity;

public interface UserRegistrationCoppaListener {

    void onUserRegistrationComplete(Activity activity);

    void onPrivacyPolicyClick(Activity activity);

    void onTermsAndConditionClick(Activity activity);

    void onUserLogoutSuccess();

    void onUserLogoutFailure();

    void onUserLogoutSuccessWithInvalidAccessToken();

}
