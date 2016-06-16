package com.philips.cdp.appframework.statemachine;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 310240027 on 6/14/2016.
 */
public class UIState {
    @IntDef({UI_SPLASH_STATE_ONE, UI_WELCOME_STATE_ONE,UI_WELCOME_STATE_TWO,UI_WELCOME_STATE_THREE, UI_REGISTRATION_STATE_ONE,UI_HOME_STATE_ONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NavigationMode {}

    public static final int UI_SPLASH_STATE_ONE = 1001;
    public static final int UI_WELCOME_STATE_ONE = 1002;
    public static final int UI_WELCOME_STATE_TWO = 1003;
    public static final int UI_WELCOME_STATE_THREE = 1004;
    public static final int UI_HOME_STATE_ONE = 1005;
    public static final int UI_REGISTRATION_STATE_ONE = 1006;

    @IntDef({UI_ONCLICK_ACTION, UI_ONSWIPE_ACTION,UI_ON_PRESS_ACTION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActivityAction {}

    public static final int UI_ONCLICK_ACTION = 2001;
    public static final int UI_ONSWIPE_ACTION = 2002;
    public static final int UI_ON_PRESS_ACTION = 2003;
}
