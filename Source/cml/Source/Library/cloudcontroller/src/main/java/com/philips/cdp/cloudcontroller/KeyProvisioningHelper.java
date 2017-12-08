/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.cloudcontroller;

import android.util.Log;

import com.philips.cdp.cloudcontroller.api.util.LogConstants;
import com.philips.icpinterface.configuration.KeyProvisioningConfiguration;
import com.philips.icpinterface.data.NVMComponentInfo;

/**
 * This class provide interface to set ICP Client configuration.
 * configuration parameters set by the application.
 * Set the necessary parameters, as per the request.
 */
class KeyProvisioningHelper extends KeyProvisioningConfiguration {

    private final KpsConfigurationInfo mKpsConfigurationInfo;

    public KeyProvisioningHelper(KpsConfigurationInfo kpsConfigurationInfo) {
        this.mKpsConfigurationInfo = kpsConfigurationInfo;

        // General configuration
        this.ICPClientPriority = kpsConfigurationInfo.getICPClientPriority();
        this.ICPClientStackSize = kpsConfigurationInfo.getICPClientStackSize();
        this.HTTPTimeout = kpsConfigurationInfo.getHttpTimeout();
        this.FilterString = kpsConfigurationInfo.getFilterString();
        this.MaxNrOfRetry = kpsConfigurationInfo.getMaxNumberOfRetries();

        // Specific configuration
        this.ICPClientBootStrapID = mKpsConfigurationInfo.getBootStrapId();
        this.ICPClientBootStrapKey = mKpsConfigurationInfo.getBootStrapKey();
        this.ICPClientBootStrapProductId = mKpsConfigurationInfo.getProductId();
        this.ICPClientproductVersion = mKpsConfigurationInfo.getProductVersion();
        this.ICPClientproductCountry = mKpsConfigurationInfo.getCountryCode();
        this.ICPClientproductLanguage = mKpsConfigurationInfo.getLanguageCode();

        this.ICPClientComponentCount = mKpsConfigurationInfo.getComponentCount();
        NVMComponentInfo appComponentInfo = new NVMComponentInfo();
        appComponentInfo.componentID = mKpsConfigurationInfo.getComponentId();
        appComponentInfo.componentVersion = mKpsConfigurationInfo.getAppVersion();
        this.ICPClientNVMComponents = new NVMComponentInfo[]{appComponentInfo};

        this.ICPClientdevicePortalURL1 = mKpsConfigurationInfo.getDevicePortUrl();

        Log.i(LogConstants.ICPCLIENT, "Created new KeyProvisioningInfo with locale: " + mKpsConfigurationInfo.getLanguageCode());
    }
}