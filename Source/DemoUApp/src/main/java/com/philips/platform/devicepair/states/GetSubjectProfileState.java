/* Copyright (c) Koninklijke Philips N.V., 2017
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.devicepair.states;

import android.app.Activity;

import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.devicepair.pojo.PairDevice;
import com.philips.platform.devicepair.ui.IDevicePairingListener;
import com.philips.platform.devicepair.utils.Utility;

import java.util.List;

public class GetSubjectProfileState extends AbstractBaseState implements SubjectProfileListener {

    private PairDevice pairDevice;
    private Activity mActivity;
    private IDevicePairingListener mDeviceStatusListener;

    public GetSubjectProfileState(PairDevice pairDevice, IDevicePairingListener listener, Activity activity) {
        super();
        this.mActivity = activity;
        this.pairDevice = pairDevice;
        this.mDeviceStatusListener = listener;
    }

    @Override
    public void start(StateContext stateContext) {
        if (Utility.isOnline(mActivity)) {
            getSubjectProfiles();
        } else {
            mDeviceStatusListener.onInternetError();
        }
    }

    private void getSubjectProfiles() {
        DataServicesManager.getInstance().getSubjectProfiles(this);
    }

    @Override
    public void onResponse(boolean b) {
    }

    @Override
    public void onError(final DataServicesError dataServicesError) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mDeviceStatusListener.onError(dataServicesError.getErrorMessage());
            }
        });
    }

    @Override
    public void onGetSubjectProfiles(List<UCoreSubjectProfile> list) {
        StateContext stateContext = new StateContext();
        if (list.size() > 0) {
            stateContext.setState(new PairDeviceState(pairDevice, list, mDeviceStatusListener, mActivity));
        } else {
            stateContext.setState(new CheckConsentState(mDeviceStatusListener, mActivity));
        }

        stateContext.start();
    }
}
