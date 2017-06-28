/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.devicepair.states;

import com.philips.cdp.devicepair.ui.DeviceStatusListener;
import com.philips.cdp.devicepair.utils.Utility;
import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

public class UnPairDeviceState extends AbstractBaseState implements DevicePairingListener {
    private String mDeviceID;
    private DeviceStatusListener mDeviceStatusListener;

    public UnPairDeviceState(String deviceID, DeviceStatusListener deviceStatusListener, FragmentLauncher context) {
        super(context);
        mDeviceID = deviceID;
        mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    void start(StateContext stateContext) {
        if (Utility.isOnline(context.getFragmentActivity())) {
            unPairDevice();
        } else {
            mDeviceStatusListener.onInternetError();
        }
    }

    private void unPairDevice() {
        showProgressDialog("UnPairing device...");
        DataServicesManager.getInstance().unPairDevice(mDeviceID, this);
    }

    @Override
    public void onResponse(boolean b) {
        dismissProgressDialog();

        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onDeviceUnPaired(mDeviceID);
            }
        });
    }

    @Override
    public void onError(final DataServicesError dataServicesError) {
        dismissProgressDialog();

        context.getFragmentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onError(dataServicesError.getErrorMessage());
            }
        });
    }

    @Override
    public void onGetPairedDevicesResponse(List<String> list) {
    }
}
