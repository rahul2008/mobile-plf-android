/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.microapp;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;

import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;


/**
 * EWSLauncherInput class is used to provide support to use EWS library as fragment launcher.
 */
public class EWSLauncherInput extends UappLaunchInput {

    private static FragmentManager fragmentManager;

    @IdRes private static int containerFrameId;

    /**
     * Default constructor
     * create EWSLauncherInput object
     */
    public EWSLauncherInput() {
    }

    /**
     * Handles close button click for Fragment
     */
    public void handleCloseButtonClick(){
        BaseFragment baseFragment = (BaseFragment) fragmentManager.findFragmentById(containerFrameId);
        baseFragment.handleCancelButtonClicked();
    }

    /**
     * Set fragmentManager for Fragment launcher
     * @param fragManager FragmentManager
     */
    void setFragmentManager(FragmentManager fragManager) {
        fragmentManager = fragManager;
    }

    /**
     * Set containerFrameID for Fragment launcher
     * @param containerFrmId ContainerFrameId
     */
    void setContainerFrameId(int containerFrmId) {
        containerFrameId = containerFrmId;
    }

    public static int getContainerFrameId() {
        return containerFrameId;
    }

    public static FragmentManager getFragmentManager() {
        return fragmentManager;
    }
}
