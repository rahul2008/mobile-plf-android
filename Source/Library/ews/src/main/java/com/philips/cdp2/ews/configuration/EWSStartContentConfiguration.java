package com.philips.cdp2.ews.configuration;

import android.support.annotation.StringRes;

import java.io.Serializable;

public class EWSStartContentConfiguration implements Serializable {

    @StringRes
    private int title;

    @StringRes
    private int appName;


    public EWSStartContentConfiguration(@StringRes int title, @StringRes int appName) {
        this.title = title;
        this.appName = appName;
    }

    public int getTitle() {
        return title;
    }

    public int getAppName() {
        return appName;
    }

}
