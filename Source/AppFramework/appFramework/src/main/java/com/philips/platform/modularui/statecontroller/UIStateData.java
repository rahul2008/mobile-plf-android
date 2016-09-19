package com.philips.platform.modularui.statecontroller;

import android.support.v4.app.FragmentActivity;

import com.philips.platform.uappframework.listener.ActionBarListener;

public class UIStateData {
    private FragmentActivity mFragmentActivity;
    private ActionBarListener mActionBarListner;
    private int containerID;

    public int getContainerID() {
        return containerID;
    }

    public void setContainerID(int containerID) {
        this.containerID = containerID;
    }

    public FragmentActivity getFragmentActivity() {
        return mFragmentActivity;
    }

    public void setFragmentActivity(FragmentActivity mFragmentActivity) {
        this.mFragmentActivity = mFragmentActivity;
    }

    public ActionBarListener getActionBarListner() {
        return mActionBarListner;
    }

    public void setActionBarListner(ActionBarListener mActionBarListner) {
        this.mActionBarListner = mActionBarListner;
    }

}
