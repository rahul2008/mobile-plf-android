package com.philips.cdp.prodreg.launcher;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * It is used to pass proposition application context to Product Registration component
 * @since 1.0.0
 */
public class PRSettings extends UappSettings {
    private static final long serialVersionUID = -6635233525340545678L;


    /**
     * creates instance of PRSettings with proposition application context
     * @param applicationContext - pass instance of application context
     * @since 1.0.0
     */
    public PRSettings(final Context applicationContext) {
        super(applicationContext);
    }
}
