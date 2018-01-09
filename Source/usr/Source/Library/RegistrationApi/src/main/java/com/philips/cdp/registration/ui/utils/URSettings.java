package com.philips.cdp.registration.ui.utils;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * It passes the proposition Application context to UR component
 */
public class URSettings extends UappSettings {

    /**
     * creates instance of URSettings with application context
     * @param applicationContext - Context applicationContext
     * @since 1.0.0
     */
    public URSettings(Context applicationContext) {
        super(applicationContext);
    }



}
