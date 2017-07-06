/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.states;

import android.app.Activity;

import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.dprdemo.ui.DeviceStatusListener;
import com.philips.platform.dprdemo.utils.Utility;

import java.util.List;

public class UnPairDeviceState extends AbstractBaseState implements DevicePairingListener {
    private String mDeviceID;
    private DeviceStatusListener mDeviceStatusListener;
    private Activity mActivity;

    public UnPairDeviceState(String deviceID, DeviceStatusListener deviceStatusListener, Activity activity) {
        super(activity);
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
        showProgressDialog("UnPairing device...");
        DataServicesManager.getInstance().unPairDevice(mDeviceID, this);
    }

    @Override
    public void onResponse(boolean b) {
        dismissProgressDialog();

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onDeviceUnPaired(mDeviceID);
            }
        });
    }

    @Override
    public void onError(final DataServicesError dataServicesError) {
        dismissProgressDialog();

        mActivity.runOnUiThread(new Runnable() {
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
