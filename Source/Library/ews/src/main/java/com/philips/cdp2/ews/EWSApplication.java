/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.core.configuration.RuntimeConfiguration;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.ews.appliance.BEApplianceFactory;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;

import net.danlew.android.joda.JodaTimeAndroid;


public class EWSApplication extends Application {

    @Nullable private AppInfraInterface appInfra;
    @Nullable private CommCentral commCentral;

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO fix the hashmap
        JodaTimeAndroid.init(this); // TODO this should be removed in favor or Java's date API
    }

    @NonNull
    public AppInfraInterface getAppInfra() {
        if (appInfra == null) {
            appInfra = createAppInfra(getApplicationContext());
        }
        return appInfra;
    }

    @NonNull
    public CommCentral getCommCentral() {
        if (commCentral == null) {
            commCentral = createCommCentral();
        }
        return commCentral;
    }

    @NonNull
    private CommCentral createCommCentral() {
        LanTransportContext lanTransportContext = new LanTransportContext(
                new RuntimeConfiguration(getApplicationContext(), appInfra));
        BEApplianceFactory factory = new BEApplianceFactory(lanTransportContext);
        return new CommCentral(factory, lanTransportContext);
    }

    @NonNull
    private AppInfra createAppInfra(@NonNull final Context context) {
        return new AppInfra.Builder().build(context);
    }
}
