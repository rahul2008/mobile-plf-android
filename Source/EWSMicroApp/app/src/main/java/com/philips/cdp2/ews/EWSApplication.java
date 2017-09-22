/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.cdp2.ews;

import android.app.Application;

import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.ews.appliance.BEApplianceFactory;
import com.philips.cdp2.ews.common.util.Tagger;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;

import net.danlew.android.joda.JodaTimeAndroid;


public class EWSApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        // TODO clean up
        //TODO fix the hashmap
        JodaTimeAndroid.init(this); // TODO this should be removed in favor or Java's date API
        DICommClientWrapper.initializeDICommLibrary(getApplicationContext(),
                new BEApplianceFactory(new LanTransportContext(getApplicationContext())), null, null);

        AppInfraInterface appInfra = new AppInfra.Builder().build(getBaseContext());

        Tagger.init(this, appInfra.getTagging());
    }
}
