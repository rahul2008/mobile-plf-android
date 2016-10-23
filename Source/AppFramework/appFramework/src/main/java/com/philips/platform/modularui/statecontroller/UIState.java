/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import android.content.Context;
import android.support.annotation.IntDef;

import com.philips.platform.uappframework.launcher.UiLauncher;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

abstract public class UIState {

    public static final int UI_WELCOME_REGISTRATION_STATE = 1001;
    public static final int UI_SPLASH_STATE = 1002;
    public static final int UI_SPLASH_UNREGISTERED_STATE = 1003;
    public static final int UI_SPLASH_REGISTERED_STATE = 1004;
    public static final int UI_SPLASH_DONE_PRESSED_STATE = 1005;
    public static final int UI_WELCOME_STATE = 1006;
    public static final int UI_HOME_STATE = 1007;
    public static final int UI_USER_REGISTRATION_STATE = 1008;
    public static final int UI_HOME_FRAGMENT_STATE = 1009;
    public static final int UI_SETTINGS_FRAGMENT_STATE = 1010;
    public static final int UI_SUPPORT_FRAGMENT_STATE = 1011;
    public static final int UI_DEBUG_FRAGMENT_STATE = 1012;
    public static final int UI_PROD_REGISTRATION_STATE = 1013;
    public static final int UI_IAP_SHOPPING_FRAGMENT_STATE = 1015;
    public static final int UI_ABOUT_SCREEN_STATE = 1016;
    public static final int UI_HOME_TABBED_STATE = 1017;

    /**
     * This class defines constants for each state ,
     * Any new state should be added here and its constant should be defined here
     * Constants for each state,Values for the states start from 1001 and continues further.
     */
    @IntDef({UI_WELCOME_REGISTRATION_STATE, UI_SPLASH_STATE, UI_SPLASH_UNREGISTERED_STATE, UI_SPLASH_REGISTERED_STATE, UI_SPLASH_DONE_PRESSED_STATE,
            UI_WELCOME_STATE, UI_USER_REGISTRATION_STATE, UI_HOME_STATE,
            UI_HOME_FRAGMENT_STATE, UI_SETTINGS_FRAGMENT_STATE, UI_SUPPORT_FRAGMENT_STATE, UI_DEBUG_FRAGMENT_STATE, UI_PROD_REGISTRATION_STATE, UI_IAP_SHOPPING_FRAGMENT_STATE, UI_ABOUT_SCREEN_STATE, UI_HOME_TABBED_STATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIStateDef {
    }
    @UIState.UIStateDef
    int stateID;
    private UIBasePresenter uiBasePresenter;
    private UIStateData uiStateData;

    /**
     * AppFlowState constructor
     *
     * @param stateID pass the state Id
     */
    public UIState(@UIState.UIStateDef int stateID) {
        this.stateID = stateID;
    }

    /**
     * getter for state Id
     *
     * @return stateID
     */
    @UIState.UIStateDef
    public int getStateID() {
        return stateID;
    }

    /**
     * setter for state ID
     *
     * @param stateID requirs the state ID
     */

    public void setStateID(@UIState.UIStateDef int stateID) {
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
