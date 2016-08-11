/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.statecontroller;

import android.content.Context;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

abstract public class UIState {

    UIBasePresenter uiBasePresenter;
    /**
     * Constants for each state
     */
    @IntDef({UI_WELCOME_REGISTRATION_STATE,UI_SPLASH_STATE,UI_SPLASH_UNREGISTERED_STATE,UI_SPLASH_REGISTERED_STATE,UI_SPLASH_DONE_PRESSED_STATE,
            UI_WELCOME_STATE, UI_USER_REGISTRATION_STATE, UI_HOME_STATE,
            UI_HOME_FRAGMENT_STATE, UI_SETTINGS_FRAGMENT_STATE, UI_SUPPORT_FRAGMENT_STATE, UI_DEBUG_FRAGMENT_STATE, UI_PROD_REGISTRATION_STATE, UI_IAP_SHOPPING_FRAGMENT_STATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIStateDef {
    }

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

    @UIState.UIStateDef
    int stateID;

    public UIState(@UIState.UIStateDef int stateID){
        this.stateID = stateID;
    }

    @UIState.UIStateDef
    public int getStateID() {
        return stateID;
    }

    @UIState.UIStateDef
    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    protected abstract void navigate(Context context);

    public abstract void back(Context context);

    public void setPresenter(UIBasePresenter uiBasePresenter){
        this.uiBasePresenter = uiBasePresenter;
    }

}
