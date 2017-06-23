package com.philips.cdp.devicepair.ui;

import java.util.List;

public interface DeviceStatusListener {
    void onGetPairedDevices(List<String> pairedDeviceList);

    void onDevicePaired(String pairedDeviceID);

    void onDeviceUnPaired(String unPairedDeviceID);

    void onError(String errorMessage);

    void onInternetError();
}
