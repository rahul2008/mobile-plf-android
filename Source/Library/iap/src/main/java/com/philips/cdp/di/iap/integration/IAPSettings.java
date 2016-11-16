/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.integration;

import android.content.Context;

import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.uappframework.uappinput.UappSettings;

public class IAPSettings extends UappSettings {
    private boolean mUseLocalData;
    private String mProposition;
    private String mHostPort;

    public IAPSettings(Context applicationContext) {
        super(applicationContext);
        loadConfigParams();
    }

    private void loadConfigParams() {
        AppConfigurationInterface mConfigInterface = RegistrationHelper.getInstance().getAppInfraInstance().getConfigInterface();
        AppConfigurationInterface.AppConfigurationError configError = new AppConfigurationInterface.AppConfigurationError();

        mProposition = (String) mConfigInterface.getPropertyForKey("propositionid", "IAP", configError);

        if (configError.getErrorCode() != null) {
            IAPLog.e(IAPLog.LOG, "VerticalAppConfig ==loadConfigurationFromAsset " + configError.getErrorCode().toString());
        }
    }

    public boolean isUseLocalData() {
        return mUseLocalData;
    }

    public void setUseLocalData(boolean isLocalData) {
        mUseLocalData = isLocalData;
    }

    public String getProposition() {
        return mProposition;
    }

    public String getHostPort() {
        return mHostPort;
    }

    public void setHostPort(String hostPort) {
        mHostPort = hostPort;
    }
}
