package com.philips.cdp2.ews.microapp;

import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;

import com.philips.cdp2.ews.base.BaseFragment;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

public class EWSLauncherInput extends UappLaunchInput {
    private FragmentManager fragmentManager;
    @IdRes private int containerFrameId;

    public EWSLauncherInput() {
    }

    public void handleCloseButtonClick(){
        BaseFragment baseFragment = (BaseFragment) fragmentManager.findFragmentById(containerFrameId);
        baseFragment.handleCancelButtonClicked();
    }

    public void setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setContainerFrameId(int containerFrameId) {
        this.containerFrameId = containerFrameId;
    }
}
