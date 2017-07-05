/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.devicepair.states;

import com.philips.cdp.devicepair.pojo.PairDevice;
import com.philips.cdp.devicepair.ui.DeviceStatusListener;
import com.philips.cdp.devicepair.utils.Utility;
import com.philips.platform.core.listeners.SubjectProfileListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DataServicesError;
import com.philips.platform.datasync.subjectProfile.UCoreSubjectProfile;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.List;

class GetSubjectProfileState extends AbstractBaseState implements SubjectProfileListener {

    private PairDevice pairDevice;
    private FragmentLauncher context;
    private DeviceStatusListener mDeviceStatusListener;

    GetSubjectProfileState(PairDevice pairDevice, DeviceStatusListener listener, FragmentLauncher context) {
        super(context);
        this.context = context;
        this.pairDevice = pairDevice;
        this.mDeviceStatusListener = listener;
    }

    @Override
    public void start(StateContext stateContext) {
        if (Utility.isOnline(context.getFragmentActivity())) {
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

        context.getFragmentActivity().runOnUiThread(new Runnable() {
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
            stateContext.setState(new PairDeviceState(pairDevice, list, mDeviceStatusListener, context));
        } else {
            stateContext.setState(new CheckConsentState(pairDevice, mDeviceStatusListener, context));
        }

        stateContext.start();
    }
}
