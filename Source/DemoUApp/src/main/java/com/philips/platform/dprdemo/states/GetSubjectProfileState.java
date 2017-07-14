/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.dprdemo.states;

import android.app.Activity;

import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.dprdemo.pojo.PairDevice;
import com.philips.platform.dprdemo.ui.IDevicePairingListener;
import com.philips.platform.dprdemo.utils.Utility;

import java.util.List;

class GetSubjectProfileState extends AbstractBaseState implements SubjectProfileListener {

    private PairDevice pairDevice;
    private Activity mActivity;
    private IDevicePairingListener mDeviceStatusListener;

    GetSubjectProfileState(PairDevice pairDevice, IDevicePairingListener listener, Activity activity) {
        super(activity);
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
        showProgressDialog("Fetching Subject Profile...");
        DataServicesManager.getInstance().getSubjectProfiles(this);
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
    public void onGetSubjectProfiles(List<UCoreSubjectProfile> list) {
        dismissProgressDialog();
        StateContext stateContext = new StateContext();
        if (list.size() > 0) {
            stateContext.setState(new PairDeviceState(pairDevice, list, mDeviceStatusListener, mActivity));
        } else {
            stateContext.setState(new CheckConsentState(mDeviceStatusListener, mActivity));
        }

        stateContext.start();
    }
}
