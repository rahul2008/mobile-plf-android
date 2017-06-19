package com.philips.cdp.wifirefuapp.ui;

import android.content.Context;

import com.philips.cdp.wifirefuapp.pojo.PairDevice;
import com.philips.cdp.wifirefuapp.states.GetPairedDevicesState;
import com.philips.cdp.wifirefuapp.states.StateContext;
import com.philips.cdp.wifirefuapp.states.UnPairDeviceState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class LaunchFragmentPresenter {
    LaunchFragmentViewListener fragmentViewListener;
    StateContext stateContext;

    public LaunchFragmentPresenter(LaunchFragmentViewListener fragmentViewListener) {
        this.fragmentViewListener = fragmentViewListener;
    }

    public void pairDevice(PairDevice pairDevice, DeviceStatusListener deviceStatusListener) {
        stateContext = new StateContext();
        stateContext.setState(new GetPairedDevicesState(pairDevice, getFragmentLauncher(), deviceStatusListener));
        stateContext.start();
    }

    public void unPairDevice(String deviceID, DeviceStatusListener deviceStatusListener) {
        stateContext = new StateContext();
        stateContext.setState(new UnPairDeviceState(deviceID, deviceStatusListener, getFragmentLauncher()));
        stateContext.start();
    }

    public Context getActivityContext() {
        return fragmentViewListener.getActivityContext();
    }

    public FragmentLauncher getFragmentLauncher() {
        return fragmentViewListener.getFragmentLauncher();
    }
}
