package com.philips.cdp.appframework.modularui;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 310240027 on 6/20/2016.
 */
public class UIConstants {

    /**
     * Constants for each state
     */
    @IntDef({UI_SPLASH_STATE,
             UI_WELCOME_STATE, UI_REGISTRATION_STATE, UI_HAMBURGER_STATE,
            UI_HAMBURGER_HOME_STATE_ONE, UI_HAMBURGER_SETTINGS_STATE_ONE, UI_HAMBURGER_SUPPORT_STATE_ONE, UI_HAMBURGER_DEBUG_STATE_STATE_ONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIStateDef {
    }

    public static final int UI_SPLASH_STATE = 1003;
    public static final int UI_WELCOME_STATE = 1006;
    public static final int UI_HAMBURGER_STATE = 1007;
    public static final int UI_REGISTRATION_STATE = 1008;
    public static final int UI_HAMBURGER_HOME_STATE_ONE = 1009;
    public static final int UI_HAMBURGER_SETTINGS_STATE_ONE = 1010;
    public static final int UI_HAMBURGER_SUPPORT_STATE_ONE = 1011;
    public static final int UI_HAMBURGER_DEBUG_STATE_STATE_ONE = 1012;

    /**
     * Constants for each CoCo
     */
    @IntDef({UI_COCO_USER_REGISTRATION, UI_COCO_PRODUCT_REGISTRATION, UI_COCO_IN_APP_PURCHASE, UI_COCO_CONSUMER_CARE, UI_COCO_APP_CHASSIS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UICoCoConstants {
    }

    public static final int UI_COCO_USER_REGISTRATION = 2001;
    public static final int UI_COCO_PRODUCT_REGISTRATION = 2002;
    public static final int UI_COCO_IN_APP_PURCHASE = 2003;
    public static final int UI_COCO_CONSUMER_CARE = 2004;
    public static final int UI_COCO_APP_CHASSIS = 2005;

    /**
     * Constants fo each UI screen
     */
    @IntDef({UI_SPLASH_SCREEN, UI_WELCOME_SCREEN, UI_HAMBURGER_SCREEN, UI_USER_REGISTRATION_SCREEN, UI_SETTINGS_SCREEN, UI_DEBUG_SCREEN, UI_SUPPORT_SCREEN,UI_HOME_SCREEN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIScreenConstants {
    }

    public static final int UI_SPLASH_SCREEN = 3001;
    public static final int UI_WELCOME_SCREEN = 3002;
    public static final int UI_HAMBURGER_SCREEN = 3003;
    public static final int UI_USER_REGISTRATION_SCREEN = 3004;
    public static final int UI_SETTINGS_SCREEN = 3005;
    public static final int UI_DEBUG_SCREEN = 3006;
    public static final int UI_SUPPORT_SCREEN = 3007;
    public static final int UI_HOME_SCREEN = 3008;

    //Start screen state constant
    public static final String UI_START_STATUS = "UI_SPLASH_STATE_ONE";
}
