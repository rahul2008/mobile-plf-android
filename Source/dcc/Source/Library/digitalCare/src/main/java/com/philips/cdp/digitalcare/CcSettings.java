package com.philips.cdp.digitalcare;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * the class which extends micro app settings
 *
 * Created by sampath.kumar on 8/17/2016.
 */
public class CcSettings extends UappSettings {
    /**
     * to set the context that will be used across the micro app
     * @param applicationContext
     * @since 1.0.0
     */
    public CcSettings(Context applicationContext) {
        super(applicationContext);
    }
}
