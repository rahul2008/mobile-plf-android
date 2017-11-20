package com.philips.cdp2.ews.settingdeviceinfo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.cdp.dicommclient.port.DICommPortListener;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.lan.context.LanTransportContext;
import com.philips.cdp2.ews.appliance.ApplianceSessionDetailsInfo;
import com.philips.cdp2.ews.appliance.EWSGenericAppliance;

import javax.inject.Inject;
import javax.inject.Named;

public class DeviceFriendlyNameFetcher {

    public interface Callback {
        void onFriendlyNameFetchingSuccess(@NonNull String friendlyName);

        void onFriendlyNameFetchingFailed();
    }

    @NonNull private final EWSGenericAppliance appliance;

    @Nullable private Callback callback;

    @NonNull private final ApplianceSessionDetailsInfo applianceSessionDetailsInfo;

    @NonNull private final DICommPortListener<DevicePort> portListener = new DICommPortListener<DevicePort>() {
        @Override
        public void onPortUpdate(DevicePort devicePort) {
            handleSuccessfulGetProps(devicePort);
        }

        @Override
        public void onPortError(DevicePort devicePort, Error error, @Nullable String s) {
            notifyFailure();
        }
    };

    @Inject
    public DeviceFriendlyNameFetcher(@NonNull @Named("ews.temporary.appliance") EWSGenericAppliance appliance,
                                     @NonNull ApplianceSessionDetailsInfo applianceSessionDetailsInfo) {
        this.appliance = appliance;
        this.applianceSessionDetailsInfo = applianceSessionDetailsInfo;
    }

    public void fetchFriendlyName() {
        DevicePort devicePort = appliance.getDevicePort();
        devicePort.addPortListener(portListener);
        devicePort.reloadProperties();
    }

    public void clear() {
        callback = null;
    }

    private void notifyFailure() {
        if (callback != null) {
            callback.onFriendlyNameFetchingFailed();
        }
    }

    private void handleSuccessfulGetProps(@Nullable DevicePort devicePort) {
        if (callback == null) return;
        if (devicePort != null && devicePort.getPortProperties() != null) {
            String pin = LanTransportContext.readPin(appliance);
            applianceSessionDetailsInfo.setAppliancePin(pin);
            //secureStorageUtility.storeString(SecureStorageUtility.APPLIANCE_PIN, pin);
            callback.onFriendlyNameFetchingSuccess(devicePort.getPortProperties().getName());
        } else {
            callback.onFriendlyNameFetchingFailed();
        }
    }

    public void setNameFetcherCallback(@NonNull DeviceFriendlyNameFetcher.Callback callback) {
        this.callback = callback;
    }

    @VisibleForTesting(otherwise = VisibleForTesting.NONE)
    @Nullable
    Callback getCallback() {
        return callback;
    }
}