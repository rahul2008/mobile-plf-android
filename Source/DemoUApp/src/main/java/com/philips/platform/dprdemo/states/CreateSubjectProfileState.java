/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.states;

import android.app.Activity;
import android.app.FragmentTransaction;

import com.philips.platform.dprdemo.R;
import com.philips.platform.dprdemo.pojo.PairDevice;
import com.philips.platform.dprdemo.ui.CreateSubjectProfileFragment;
import com.philips.platform.dprdemo.ui.DeviceStatusListener;


public class CreateSubjectProfileState extends AbstractBaseState {

    private Activity mActivity;
    private PairDevice pairDevice;
    private DeviceStatusListener mDeviceStatusListener;

    public CreateSubjectProfileState(PairDevice pairDevice, DeviceStatusListener deviceStatusListener, Activity activity) {
        super(activity);
        this.mActivity = activity;
        this.pairDevice = pairDevice;
        this.mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    void start(StateContext stateContext) {
        launchSubjectProfile();
    }

    private void launchSubjectProfile() {
        CreateSubjectProfileFragment createProfileFragment = new CreateSubjectProfileFragment();
        createProfileFragment.setDeviceDetails(pairDevice);
        createProfileFragment.setDeviceStatusListener(mDeviceStatusListener);

        int containerId = R.id.user_registration_frame_container;
        FragmentTransaction fragmentTransaction = mActivity.getFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerId, createProfileFragment, CreateSubjectProfileFragment.TAG);
        fragmentTransaction.addToBackStack(CreateSubjectProfileFragment.TAG);
        fragmentTransaction.commit();
    }
}
