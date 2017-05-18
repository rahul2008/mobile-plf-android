package com.philips.cdp.wifirefuapp;

import android.app.Application;

import com.philips.cdp.dicommclient.discovery.DICommClientWrapper;
import com.philips.cdp2.commlib.core.CommCentral;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;

public class WifiCommLibUappApplication extends Application {

    private CommCentral commCentral;

    @Override
    public void onCreate() {
        super.onCreate();

        final LanTransportContext lanTransportContext = new LanTransportContext(this);
        final SampleApplianceFactory applianceFactory = new SampleApplianceFactory(lanTransportContext);
        this.commCentral = new CommCentral(applianceFactory, lanTransportContext);

        if (DICommClientWrapper.getContext() == null) {
            DICommClientWrapper.initializeDICommLibrary(this, applianceFactory, null, null);
        }
    }

}
