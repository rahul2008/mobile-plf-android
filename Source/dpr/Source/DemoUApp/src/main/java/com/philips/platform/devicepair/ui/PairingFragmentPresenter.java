/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.ui;

import android.app.Activity;

import com.philips.platform.devicepair.pojo.PairDevice;
import com.philips.platform.devicepair.states.GetSubjectProfileState;
import com.philips.platform.devicepair.states.StateContext;
import com.philips.platform.devicepair.states.UnPairDeviceState;

class PairingFragmentPresenter {

    private Activity mActivity;

    PairingFragmentPresenter(Activity activity) {
        this.mActivity = activity;
    }

    void pairDevice(PairDevice pairDevice, IDevicePairingListener deviceStatusListener) {
        StateContext stateContext = new StateContext();
        stateContext.setState(new GetSubjectProfileState(pairDevice, deviceStatusListener, mActivity));
        stateContext.start();
    }

    void unPairDevice(String deviceID, IDevicePairingListener deviceStatusListener) {
        StateContext stateContext = new StateContext();
        stateContext.setState(new UnPairDeviceState(deviceID, deviceStatusListener, mActivity));
        stateContext.start();
    }

}
