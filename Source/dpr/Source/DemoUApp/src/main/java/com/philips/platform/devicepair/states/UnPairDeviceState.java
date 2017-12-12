/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.states;

import android.app.Activity;

import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.devicepair.ui.IDevicePairingListener;
import com.philips.platform.devicepair.utils.Utility;

import java.util.List;

public class UnPairDeviceState extends AbstractBaseState implements com.philips.platform.core.listeners.DevicePairingListener {
    private String mDeviceID;
    private IDevicePairingListener mDeviceStatusListener;
    private Activity mActivity;

    public UnPairDeviceState(String deviceID, IDevicePairingListener deviceStatusListener, Activity activity) {
        super();
        mActivity = activity;
        mDeviceID = deviceID;
        mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    void start(StateContext stateContext) {
        if (Utility.isOnline(mActivity)) {
            unPairDevice();
        } else {
            mDeviceStatusListener.onInternetError();
        }
    }

    private void unPairDevice() {
        DataServicesManager.getInstance().unPairDevice(mDeviceID, this);
    }

    @Override
    public void onResponse(boolean b) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onDeviceUnPaired(mDeviceID);
            }
        });
    }

    @Override
    public void onError(final DataServicesError dataServicesError) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataServicesError.getErrorCode() == 404) {
                    mDeviceStatusListener.onError(dataServicesError.getErrorMessage());
                    mDeviceStatusListener.onDeviceUnPaired(mDeviceID);
                } else {
                    mDeviceStatusListener.onError(dataServicesError.getErrorMessage());
                }
            }
        });
    }

    @Override
    public void onGetPairedDevicesResponse(List<String> list) {
    }
}
