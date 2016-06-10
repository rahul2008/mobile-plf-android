package com.philips.cdp.dicommclientsample;

import com.philips.cdp.dicommclient.cpp.KpsConfigurationInfo;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
class SampleKpsConfigurationInfo extends KpsConfigurationInfo {

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
        return "FI-AIR-AND";
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
        return "FI-AIR-AND-DEV";
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
        return "https://www.bat.ecdinterface.philips.com/DevicePortalICPRequestHandler/RequestHandler.ashx";
    }
}
