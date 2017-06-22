package com.philips.platform.referenceapp.states;

import android.support.v4.app.FragmentTransaction;

import com.philips.platform.referenceapp.pojo.PairDevice;
import com.philips.platform.referenceapp.ui.CreateSubjectProfileFragment;
import com.philips.platform.referenceapp.ui.DeviceStatusListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class CreateSubjectProfileState extends BaseState {

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

    public void launchSubjectProfile() {
        CreateSubjectProfileFragment createProfileFragment = new CreateSubjectProfileFragment();
        createProfileFragment.setFragmentLauncher(context);
        createProfileFragment.setDeviceDetails(pairDevice);
        createProfileFragment.setDeviceStatusListener(mDeviceStatusListener);
        FragmentTransaction fragmentTransaction = context.getFragmentActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(context.getParentContainerResourceID(), createProfileFragment, "CreateSubjectProfileFragment");
        fragmentTransaction.addToBackStack("CreateSubjectProfileFragment");
        fragmentTransaction.commit();
    }
}
