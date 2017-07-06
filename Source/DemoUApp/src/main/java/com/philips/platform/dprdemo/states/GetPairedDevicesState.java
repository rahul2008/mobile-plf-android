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
import com.philips.platform.dprdemo.pojo.PairDevice;
import com.philips.platform.dprdemo.ui.DeviceStatusListener;
import com.philips.platform.dprdemo.utils.Utility;

import java.util.List;

public class GetPairedDevicesState extends AbstractBaseState implements DevicePairingListener {

    private Activity mActivity;
    private PairDevice pairDevice;
    private DeviceStatusListener mDeviceStatusListener;
    private boolean mIsPair;

    public GetPairedDevicesState(PairDevice pairDevice, Activity activity, DeviceStatusListener deviceStatusListener) {
        super(activity);
        this.mActivity = activity;
        this.pairDevice = pairDevice;
        mDeviceStatusListener = deviceStatusListener;
        mIsPair = true;
    }

    public GetPairedDevicesState(Activity activity, DeviceStatusListener deviceStatusListener) {
        super(activity);
        this.mActivity = activity;
        mDeviceStatusListener = deviceStatusListener;
        mIsPair = false;
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
        showProgressDialog("Fetching paired devices...");
        DataServicesManager.getInstance().getPairedDevices(this);
    }

    @Override
    public void onResponse(boolean b) {
        dismissProgressDialog();
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
    public void onGetPairedDevicesResponse(final List<String> list) {
        dismissProgressDialog();

        if (mIsPair) {
            if (isDevicePaired(list)) {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDeviceStatusListener.onGetPairedDevices(list);
                    }
                });
            } else {
                StateContext stateContext = new StateContext();
                stateContext.setState(new GetSubjectProfileState(pairDevice, mDeviceStatusListener, mActivity));
                stateContext.start();
            }
        } else {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mDeviceStatusListener.onGetPairedDevices(list);
                }
            });
        }
    }

    private boolean isDevicePaired(List<String> list) {
        return list.contains(pairDevice.getDeviceID());
    }
}
