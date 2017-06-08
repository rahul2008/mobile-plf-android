package com.philips.cdp.wifirefuapp.observablemodules;

import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.utils.DataServicesError;

import java.util.List;

/**
 * Created by philips on 6/7/17.
 */

public class DevicePairingObservable implements DevicePairingListener{
    @Override
    public void onResponse(boolean b) {

    }

    @Override
    public void onError(DataServicesError dataServicesError) {

    }

    @Override
    public void onGetPairedDevicesResponse(List<String> list) {

    }

}
