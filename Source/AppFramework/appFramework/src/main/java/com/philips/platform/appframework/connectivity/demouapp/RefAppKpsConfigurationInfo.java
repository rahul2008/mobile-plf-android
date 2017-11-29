/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivity.demouapp;

import com.philips.cdp.cloudcontroller.KpsConfigurationInfo;

public class RefAppKpsConfigurationInfo extends KpsConfigurationInfo {
    // Example configuration for test environment in Europe

    @Override
    public String getBootStrapId() {
        return "000000ffe0000003";
    }

    @Override
    public String getBootStrapKey() {
        return "45240d84f206035f9f19856fd266e59b";
    }

    @Override
    public String getProductId() {
        return "FI-AIR_KPSPROV";
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