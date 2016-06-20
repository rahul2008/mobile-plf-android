package com.philips.cdp.appframework.modularui;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 310240027 on 6/20/2016.
 */
public class UIStateDefintions {
    @IntDef({UI_SPLASH_STATE_ONE, UI_WELCOME_STATE_ONE,UI_WELCOME_STATE_TWO,UI_WELCOME_STATE_THREE, UI_REGISTRATION_STATE_ONE,UI_HOME_STATE_ONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIStateDef {}

    public static final int UI_SPLASH_STATE_ONE = 1001;
    public static final int UI_WELCOME_STATE_ONE = 1002;
    public static final int UI_WELCOME_STATE_TWO = 1003;
    public static final int UI_WELCOME_STATE_THREE = 1004;
    public static final int UI_HOME_STATE_ONE = 1005;
    public static final int UI_REGISTRATION_STATE_ONE = 1006;
}
