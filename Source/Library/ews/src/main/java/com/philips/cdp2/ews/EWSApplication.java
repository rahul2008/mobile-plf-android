/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews;

import android.app.Application;

import net.danlew.android.joda.JodaTimeAndroid;


public class EWSApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO fix the hashmap
        JodaTimeAndroid.init(this); // TODO this should be removed in favor or Java's date API
    }

}
