package com.philips.platform.csw;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.util.List;

/**
 * This class is used to provide input parameters and customizations for myaccount.
 */

public class CswLaunchInput extends UappLaunchInput {

    private ConsentBundleConfig config;
    private final Context context;

    public CswLaunchInput(ConsentBundleConfig config, Context context) {
        this.config = config;
        this.context = context;
    }

    public ConsentBundleConfig getConfig() {
        return config;
    }

    public Context getContext() {
        return context;
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
