package com.philips.platform.mya;

import android.content.Context;

import com.philips.platform.uappframework.uappinput.UappSettings;


public class MyaSettings extends UappSettings {
    public String applicationName;
    public String propositionName;

    public MyaSettings(Context applicationContext) {
        super(applicationContext);
    }

    public MyaSettings(Context applicationContext, String applicationName, String propositionName) {
        super(applicationContext);
        this.applicationName = applicationName;
        this.propositionName = propositionName;
    }
}
