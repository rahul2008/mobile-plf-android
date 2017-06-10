package com.philips.cdp.wifirefuapp.states;

import android.os.Bundle;

import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.cdp.wifirefuapp.ui.CreateSubjectProfileFragment;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

/**
 * Created by philips on 6/8/17.
 */

public class CreateSubjectProfileState extends BaseState {

    private FragmentLauncher context;
    private PairDevicePojo pairDevicePojo;

    public CreateSubjectProfileState(PairDevicePojo pairDevicePojo,FragmentLauncher context) {
        super(context);
        this.context = context;
        this.pairDevicePojo = pairDevicePojo;
    }

    @Override
    void start(StateContext stateContext) {
        CreateSubjectProfileFragment createFrofileFragment = new CreateSubjectProfileFragment();
        createFrofileFragment.setFragmentLauncher(context);
        Bundle profileBundle = new Bundle();
        profileBundle.putString("DeviceID",pairDevicePojo.getDeviceID());
        profileBundle.putString("DeviceType",pairDevicePojo.getDeviceType());
        createFrofileFragment.setArguments(profileBundle);
        context.getFragmentActivity().getSupportFragmentManager().beginTransaction().replace(context.getParentContainerResourceID(),createFrofileFragment,"CreateSubjectProfileFragment").commit();

    }
}
