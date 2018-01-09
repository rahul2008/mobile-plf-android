package com.philips.cdp.prodreg.launcher;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;

/**
 * It is used to pass proposition application context to Product Registration component
 */
public class PRSettings extends UappSettings {

    /**
     * creates instance of PRSettings with proposition application context
     * @param applicationContext - Context applicationContext
     */
    public PRSettings(final Context applicationContext) {
        super(applicationContext);
    }
}
