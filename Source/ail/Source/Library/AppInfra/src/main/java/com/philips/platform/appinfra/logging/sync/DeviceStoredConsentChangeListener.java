package com.philips.platform.appinfra.logging.sync;

/**
 * Created by abhishek on 5/21/18.
 */

public interface DeviceStoredConsentChangeListener {
    void onDeviceStorageConsentChanged(String consentType,boolean status);
}
