package com.philips.cdp.wifirefuapp.ui;

import android.content.Context;

import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.cdp.wifirefuapp.states.CheckDevicePairedStatusState;
import com.philips.cdp.wifirefuapp.states.StateContext;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class LaunchFragmentPresenter {

    LaunchFragmentViewListener fragmentViewListener;
    StateContext stateContext;
    Context context;

    public LaunchFragmentPresenter(LaunchFragmentViewListener fragmentViewListener){
        this.fragmentViewListener = fragmentViewListener;

    }


    public void onPairDevice(PairDevicePojo pairDevicePojo) {
        stateContext = new StateContext();
        stateContext.setState(new CheckDevicePairedStatusState(pairDevicePojo,getFragmentLauncher()));
        stateContext.start();
    }

    public Context getActivityContext(){
        return fragmentViewListener.getActivityContext();
    }
    public FragmentLauncher getFragmentLauncher(){
        return fragmentViewListener.getFragmentLauncher();
    }

}
