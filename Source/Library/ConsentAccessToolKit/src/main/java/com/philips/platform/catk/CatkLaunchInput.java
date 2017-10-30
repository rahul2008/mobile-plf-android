package com.philips.platform.catk;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * This class is used to provide input parameters and customizations for myaccount.
 */

public class CatkLaunchInput extends UappLaunchInput {

    public Context getContext() {
        return context;
    }

    private Context context;

    public void setContext(Context context) {
        this.context = context;
    }


}
