/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.StringDef;

import com.philips.platform.uappframework.launcher.UiLauncher;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

abstract public class BaseState {

    /**
     * This class defines constants for each state ,
     * Any new state should be added here and its constant should be defined here
     * Constants for each state is defined in the BaseAppState class as static strings.
     */

    @StringDef({BaseAppState.ABOUT, BaseAppState.DEBUG,BaseAppState.HOME_FRAGMENT,BaseAppState.IAP,BaseAppState.PR,BaseAppState.REGISTRATION,BaseAppState.SETTINGS,
            BaseAppState.SPLASH, BaseAppState.SUPPORT,BaseAppState.WELCOME,BaseAppState.DATA_SYNC})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIStateDef {}



    @BaseState.UIStateDef
    String stateID;
    private UIBasePresenter uiBasePresenter;
    private UIStateData uiStateData;

    /**
     * AppFlowState constructor
     *
     * @param stateID pass the state Id
     */
    public BaseState(@BaseState.UIStateDef String stateID) {
        this.stateID = stateID;
    }

    /**
     * getter for state Id
     *
     * @return stateID
     */
    @BaseState.UIStateDef
    public String getStateID() {
        return stateID;
    }

    /**
     * setter for state ID
     *
     * @param stateID requirs the state ID
     */

    public void setStateID(@BaseState.UIStateDef String stateID) {
        this.stateID = stateID;
    }

    /**
     * For navigating from one state to other
     *
     * @param uiLauncher requires the UiLauncher object
     */
    public abstract void navigate(UiLauncher uiLauncher);

    /**
     * For initialising the component
     *
     * @param context
     */

    public abstract void init(Context context);

    /**
     * to get the presenter object
     *
     * @return
     */
    public UIBasePresenter getPresenter() {
        return uiBasePresenter;
    }

    /**
     * to set the presenter
     *
     * @param uiBasePresenter
     */

    public void setPresenter(UIBasePresenter uiBasePresenter) {
        this.uiBasePresenter = uiBasePresenter;
    }

    public UIStateData getUiStateData() {
        return uiStateData;
    }

    public void setUiStateData(UIStateData uiStateData) {
        this.uiStateData = uiStateData;
    }
}
