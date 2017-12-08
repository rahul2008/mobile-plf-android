/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest;

import com.philips.cdp.cloudcontroller.KpsConfigurationInfo;

class CommlibUappKpsConfigurationInfo extends KpsConfigurationInfo {

    // Example configuration for test environment in Europe

    @Override
    public String getBootStrapId() {
        return "000000fff0000012";
    }

    @Override
    public String getBootStrapKey() {
        return "68b6717490cc1265a6e31884ad16e0e5";
    }

    @Override
    public String getProductId() {
        return "CDP_KPSPROV";
    }

    @Override
    public int getProductVersion() {
        return 0;
    }

    @Override
    public String getComponentId() {
        return "CDP-APP-AND";
    }

    @Override
    public int getComponentCount() {
        return 0;
    }

    @Override
    public String getAppId() {
        return "com.philips.cdp.dicommclientsample";
    }

    @Override
    public int getAppVersion() {
        return 0;
    }

    public String getAppType() {
        return "CDP-AND-DEV";
    }

    @Override
    public String getCountryCode() {
        return "NL";
    }

    @Override
    public String getLanguageCode() {
        return "nl";
    }

    @Override
    public String getDevicePortUrl() {
        return "https://www.uat.ecdinterface.philips.com/DevicePortalICPRequestHandler/RequestHandler.ashx";
    }
}
