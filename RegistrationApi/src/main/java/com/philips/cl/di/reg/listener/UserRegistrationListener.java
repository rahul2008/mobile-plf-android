
package com.philips.cl.di.reg.listener;

import android.app.Activity;

public interface UserRegistrationListener {

    public void onUserRegistrationComplete(Activity activity);

    public void onPrivacyPolicyClick(Activity activity);

    public void onTermsAndConditionClick(Activity activity);

}
