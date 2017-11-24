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
 * This class is used to providing support to use EWS library as fragment launcher
 */
public class EWSLauncherInput extends UappLaunchInput {
    private FragmentManager fragmentManager;
    @IdRes private int containerFrameId;

    /**
     * Default constructor
     * create EWSLauncherInput object
     */
    public EWSLauncherInput() {
    }


    /**
     * create EWSLauncherInput object
     * @param fragmentManager
     * @param containerFrameId
     */
    public EWSLauncherInput(FragmentManager fragmentManager, int containerFrameId) {
        this.fragmentManager = fragmentManager;
        this.containerFrameId = containerFrameId;
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
     * @param fragmentManager
     */
    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    /**
     * Set containerFrameID for Fragment launcher
     * @param containerFrameId
     */
    public void setContainerFrameId(int containerFrameId) {
        this.containerFrameId = containerFrameId;
    }
}
