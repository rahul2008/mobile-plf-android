/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.launcher;

import android.content.Context;

import com.philips.platform.mya.MyaTabConfig;
import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.uappframework.uappinput.UappLaunchInput;

import java.io.Serializable;
import java.util.List;

/**
 * This class is used to provide input parameters and customizations for myaccount.
 *
 * @since 2017.5.0
 */

public class MyaLaunchInput extends UappLaunchInput implements Serializable {

    private static final long serialVersionUID = 1584414650805244556L;
    private transient Context context;
    private boolean isAddToBackStack;
    private MyaListener myaListener;
    private MyaTabConfig myaTabConfig;
    private List<String> settingsMenuList;
    private List<String> profileMenuList;
    public MyaLaunchInput() {
    }

    public MyaLaunchInput(Context context, MyaListener myaListener) {
        this.context = context;
        this.myaListener = myaListener;
    }


    public Context getContext() {
        return context;
    }

    /**
     * Get status of is current fragment need to add to backstack or no.
     *
     * @return true if need to add to fragment back stack
     * @since 2017.5.0
     */
    public boolean isAddtoBackStack() {
        return isAddToBackStack;
    }

    /**
     * Enable  add to back stack for current fragment.
     *
     * @param isAddToBackStack
     * @since 2017.5.0
     */
    public void addToBackStack(boolean isAddToBackStack) {
        this.isAddToBackStack = isAddToBackStack;
    }

    public MyaListener getMyaListener() {
        return myaListener;
    }

    public MyaTabConfig getMyaTabConfig() {
        return myaTabConfig;
    }

    public void setMyaTabConfig(MyaTabConfig myaTabConfig) {
        this.myaTabConfig = myaTabConfig;
    }

    public List<String> getSettingsMenuList() {
        return settingsMenuList;
    }

    public void setSettingsMenuList(List<String> settingsMenuList) {
        this.settingsMenuList = settingsMenuList;
    }

    public List<String> getProfileMenuList() {
        return profileMenuList;
    }

    public void setProfileMenuList(List<String> profileMenuList) {
        this.profileMenuList = profileMenuList;
    }
}
