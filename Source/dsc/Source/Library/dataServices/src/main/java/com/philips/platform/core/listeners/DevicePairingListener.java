package com.philips.platform.core.listeners;

import com.philips.platform.core.utils.DataServicesError;

import java.util.List;

public interface DevicePairingListener {
    void onResponse(boolean status);

    void onError(DataServicesError dataServicesError);

    void onGetPairedDevicesResponse(List<String> pairedDevices);
}
