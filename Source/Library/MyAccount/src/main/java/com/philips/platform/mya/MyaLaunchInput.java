package com.philips.platform.mya;

import android.content.Context;

import com.philips.cdp.registration.configuration.RegistrationLaunchMode;
import com.philips.cdp.registration.listener.UserRegistrationUIEventListener;
import com.philips.cdp.registration.settings.RegistrationFunction;
import com.philips.cdp.registration.ui.utils.RegistrationContentConfiguration;
import com.philips.cdp.registration.ui.utils.UIFlow;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * This class is used to provide input parameters and customizations for myaccount.
 */

public class MyaLaunchInput extends UappLaunchInput {
    public Context getContext() {
        return context;
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * Get status of is current fragment need to add to backstack or no.
     *
     * @return true if need to add to fragment back stack
     */
    public boolean isAddtoBackStack() {
        return isAddToBackStack;
    }


    /**
     * Enable  add to back stack for current fragment.
     *
     * @param isAddToBackStack
     */
    public void addToBackStack(boolean isAddToBackStack) {
        this.isAddToBackStack = isAddToBackStack;
    }

    private boolean isAddToBackStack;
}
