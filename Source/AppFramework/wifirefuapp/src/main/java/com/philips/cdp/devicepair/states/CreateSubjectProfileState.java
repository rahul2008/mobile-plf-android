/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.devicepair.states;

import android.support.v4.app.FragmentTransaction;

import com.philips.cdp.devicepair.pojo.PairDevice;
import com.philips.cdp.devicepair.ui.CreateSubjectProfileFragment;
import com.philips.cdp.devicepair.ui.DeviceStatusListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class CreateSubjectProfileState extends AbstractBaseState {

    private FragmentLauncher context;
    private PairDevice pairDevice;
    private DeviceStatusListener mDeviceStatusListener;

    public CreateSubjectProfileState(PairDevice pairDevice, DeviceStatusListener deviceStatusListener, FragmentLauncher context) {
        super(context);
        this.context = context;
        this.pairDevice = pairDevice;
        this.mDeviceStatusListener = deviceStatusListener;
    }

    @Override
    void start(StateContext stateContext) {
        launchSubjectProfile();
    }

    private void launchSubjectProfile() {
        CreateSubjectProfileFragment createProfileFragment = new CreateSubjectProfileFragment();
        createProfileFragment.setFragmentLauncher(context);
        createProfileFragment.setDeviceDetails(pairDevice);
        createProfileFragment.setDeviceStatusListener(mDeviceStatusListener);

        FragmentTransaction fragmentTransaction = context.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(context.getParentContainerResourceID(), createProfileFragment, CreateSubjectProfileFragment.TAG);
        fragmentTransaction.addToBackStack(CreateSubjectProfileFragment.TAG);
        fragmentTransaction.commit();
    }
}
