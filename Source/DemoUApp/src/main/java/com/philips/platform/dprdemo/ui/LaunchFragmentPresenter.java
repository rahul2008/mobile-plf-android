/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.ui;

import android.app.Activity;

import com.philips.platform.dprdemo.pojo.PairDevice;
import com.philips.platform.dprdemo.states.GetPairedDevicesState;
import com.philips.platform.dprdemo.states.StateContext;
import com.philips.platform.dprdemo.states.UnPairDeviceState;

class LaunchFragmentPresenter {

    private Activity mActivity;

    LaunchFragmentPresenter(Activity activity) {
        this.mActivity = activity;
    }

    void pairDevice(PairDevice pairDevice, DeviceStatusListener deviceStatusListener) {
        StateContext stateContext = new StateContext();
        stateContext.setState(new GetPairedDevicesState(pairDevice, mActivity, deviceStatusListener));
        stateContext.start();
    }

    void unPairDevice(String deviceID, DeviceStatusListener deviceStatusListener) {
        StateContext stateContext = new StateContext();
        stateContext.setState(new UnPairDeviceState(deviceID, deviceStatusListener, mActivity));
        stateContext.start();
    }
}
