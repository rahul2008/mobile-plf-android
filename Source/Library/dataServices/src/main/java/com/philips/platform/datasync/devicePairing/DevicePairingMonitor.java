package com.philips.platform.datasync.devicePairing;

import android.support.annotation.NonNull;

import com.philips.platform.core.events.DevicePairingErrorResponseEvent;
import com.philips.platform.core.events.DevicePairingResponseEvent;
import com.philips.platform.core.events.GetPairedDeviceRequestEvent;
import com.philips.platform.core.events.GetPairedDevicesResponseEvent;
import com.philips.platform.core.events.PairDevicesRequestEvent;
import com.philips.platform.core.events.UnPairDeviceRequestEvent;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.monitors.EventMonitor;
import com.philips.platform.core.trackers.DataServicesManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

public class DevicePairingMonitor extends EventMonitor {
    private DevicePairingController mDevicePairingController;
    private DevicePairingListener mDevicePairingListener;

    @Inject
    public DevicePairingMonitor(@NonNull DevicePairingController devicePairingController) {
        mDevicePairingController = devicePairingController;
        DataServicesManager.getInstance().getAppComponent().injectDevicePairingMonitor(this);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(PairDevicesRequestEvent pairDevicesRequestEvent) {
        mDevicePairingListener = pairDevicesRequestEvent.getDevicePairingListener();
        UCoreDevicePair uCoreDevicePair = new UCoreDevicePair();
        uCoreDevicePair.setDeviceId(pairDevicesRequestEvent.getDeviceId());
        uCoreDevicePair.setDeviceType(pairDevicesRequestEvent.getDeviceType());
        uCoreDevicePair.setStandardObservationNames(pairDevicesRequestEvent.getStandardObservationNames());
        uCoreDevicePair.setSubjectIds(pairDevicesRequestEvent.getSubjectIds());
        uCoreDevicePair.setRelationshipType(pairDevicesRequestEvent.getRelationshipType());
        mDevicePairingController.pairDevices(uCoreDevicePair);
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(UnPairDeviceRequestEvent unPairDeviceRequestEvent) {
        mDevicePairingListener = unPairDeviceRequestEvent.getDevicePairingListener();
        mDevicePairingController.unPairDevice(unPairDeviceRequestEvent.getDeviceId());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(GetPairedDeviceRequestEvent getPairedDeviceRequestEvent) {
        mDevicePairingListener = getPairedDeviceRequestEvent.getDevicePairingListener();
        mDevicePairingController.getPairedDevices();
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final DevicePairingResponseEvent devicePairingResponseEvent) {
        mDevicePairingListener.onResponse(devicePairingResponseEvent.isSuccess());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final DevicePairingErrorResponseEvent devicePairingErrorResponseEvent) {
        mDevicePairingListener.onError(devicePairingErrorResponseEvent.getDataServicesError());
    }

    @Subscribe(threadMode = ThreadMode.ASYNC)
    public void onEventAsync(final GetPairedDevicesResponseEvent getPairedDevicesResponseEvent) {
        mDevicePairingListener.onGetPairedDevicesResponse(getPairedDevicesResponseEvent.getPairedDevices());
    }
}
