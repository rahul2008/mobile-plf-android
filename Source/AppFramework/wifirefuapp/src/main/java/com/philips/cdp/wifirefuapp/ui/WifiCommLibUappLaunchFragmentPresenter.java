package com.philips.cdp.wifirefuapp.ui;

import android.content.Context;

import com.philips.cdp.wifirefuapp.pojo.PairDevicePojo;
import com.philips.cdp.wifirefuapp.states.CheckDevicePairedStatusState;
import com.philips.cdp.wifirefuapp.states.StateContext;

public class WifiCommLibUappLaunchFragmentPresenter {

    FragmentViewListener fragmentViewListener;
    StateContext stateContext;
    Context context;

    public WifiCommLibUappLaunchFragmentPresenter(FragmentViewListener fragmentViewListener){
        this.fragmentViewListener = fragmentViewListener;

    }


    public void onPairDevice(PairDevicePojo pairDevicePojo) {
        stateContext = new StateContext();
        stateContext.setState(new CheckDevicePairedStatusState(pairDevicePojo,getActivityContext()));
        stateContext.start();
    }

    public Context getActivityContext(){
        return fragmentViewListener.getActivityContext();
    }


}
