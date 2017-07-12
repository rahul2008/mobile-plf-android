/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.dprdemo.states;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

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

        FragmentTransaction fragmentTransaction = ((FragmentActivity)mActivity).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(getActiveFragment().getId(), createProfileFragment, CreateSubjectProfileFragment.TAG);
        fragmentTransaction.addToBackStack(CreateSubjectProfileFragment.TAG);
        fragmentTransaction.commit();
    }

    public Fragment getActiveFragment() {
        if (((FragmentActivity)mActivity).getSupportFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }

        String tag = ((FragmentActivity)mActivity).getSupportFragmentManager().getBackStackEntryAt(((FragmentActivity)mActivity).getSupportFragmentManager().getBackStackEntryCount() - 1).getName();
        return ((FragmentActivity)mActivity).getSupportFragmentManager().findFragmentByTag(tag);
    }
}
