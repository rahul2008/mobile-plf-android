
package com.philips.platform.mya.demouapp;


import android.content.Context;

import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * Myaccount Demo App Launch input
 */
public class MyaDemouAppLaunchInput extends UappLaunchInput {
    public Context getContext() {
        return context;
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }
}
