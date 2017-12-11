/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.states;

import android.app.Activity;

import com.philips.platform.core.listeners.DevicePairingListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.devicepair.ui.IDevicePairingListener;
import com.philips.platform.devicepair.utils.Utility;

import java.util.List;

public class GetPairedDevicesState extends AbstractBaseState implements DevicePairingListener {

    private Activity mActivity;
    private IDevicePairingListener mDeviceStatusListener;

    public GetPairedDevicesState(Activity activity, IDevicePairingListener deviceStatusListener) {
        super();
        this.mActivity = activity;
        mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    public void start(StateContext stateContext) {
        if (Utility.isOnline(mActivity)) {
            getPairedDevices();
        } else {
            mDeviceStatusListener.onInternetError();
        }
    }

    private void getPairedDevices() {
        DataServicesManager.getInstance().getPairedDevices(this);
    }

    @Override
    public void onResponse(boolean b) {
    }

    @Override
    public void onError(final DataServicesError dataServicesError) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataServicesError.getErrorCode() == 401 || dataServicesError.getErrorCode() == 403)
                    mDeviceStatusListener.onAccessTokenExpiry();
                else
                    mDeviceStatusListener.onError(dataServicesError.getErrorMessage());
            }
        });
    }

    @Override
    public void onGetPairedDevicesResponse(final List<String> list) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onGetPairedDevices(list);
            }
        });
    }
}
