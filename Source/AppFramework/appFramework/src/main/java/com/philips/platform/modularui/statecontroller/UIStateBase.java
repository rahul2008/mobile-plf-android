package com.philips.platform.modularui.statecontroller;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 310240027 on 6/16/2016.
 */
public abstract class UIStateBase {
    /**
     * Constants for each state
     */
    @IntDef({UI_SPLASH_UNREGISTERED_STATE,UI_SPLASH_REGISTERED_STATE,UI_SPLASH_DONE_PRESSED_STATE,
            UI_WELCOME_STATE, UI_REGISTRATION_STATE, UI_HOME_STATE,
            UI_HOME_FRAGMENT_STATE, UI_SETTINGS_FRAGMENT_STATE, UI_SUPPORT_FRAGMENT_STATE, UI_DEBUG_FRAGMENT_STATE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIStateDef {
    }

    public static final int UI_SPLASH_UNREGISTERED_STATE = 1003;
    public static final int UI_SPLASH_REGISTERED_STATE = 1004;
    public static final int UI_SPLASH_DONE_PRESSED_STATE = 1005;
    public static final int UI_WELCOME_STATE = 1006;
    public static final int UI_HOME_STATE = 1007;
    public static final int UI_REGISTRATION_STATE = 1008;
    public static final int UI_HOME_FRAGMENT_STATE = 1009;
    public static final int UI_SETTINGS_FRAGMENT_STATE = 1010;
    public static final int UI_SUPPORT_FRAGMENT_STATE = 1011;
    public static final int UI_DEBUG_FRAGMENT_STATE = 1012;
    UIBaseNavigator navigator;
    @UIStateBase.UIStateDef
    int stateID;

    public UIBaseNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(UIBaseNavigator navigator) {
        this.navigator = navigator;
    }

    @UIStateBase.UIStateDef
    public int getStateID() {
        return stateID;
    }

    @UIStateBase.UIStateDef
    public void setStateID(int stateID) {
        this.stateID = stateID;
    }
}
