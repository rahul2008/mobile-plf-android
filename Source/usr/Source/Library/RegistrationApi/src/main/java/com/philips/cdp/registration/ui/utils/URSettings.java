package com.philips.cdp.registration.ui.utils;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * It passes the proposition Application context to UR component
 * @since 1.0.0
 */
public class URSettings extends UappSettings {

    /**
     * creates instance of URSettings with application context
     * @param applicationContext application context
     * @since 1.0.0
     */
    public URSettings(Context applicationContext) {
        super(applicationContext);
    }

}
