package com.philips.cdp.registration.ui.utils;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * It passes the proposition Application context to USR component
 * @since 1.0.0
 */
public class URSettings extends UappSettings {

    private static final long serialVersionUID = 1128016096756071385L;

    /**
     * Creates instance of URSettings with application context
     * @param applicationContext application context
     * @since 1.0.0
     */
    public URSettings(Context applicationContext) {
        super(applicationContext);
    }

}
