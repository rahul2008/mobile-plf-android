package com.philips.platform.modularui.util;

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
    @IntDef({UI_SPLASH_UNREGISTERED_STATE,UI_SPLASH_REGISTERED_STATE,UI_SPLASH_DONE_PRESSED_STATE,
             UI_WELCOME_STATE, UI_REGISTRATION_STATE, UI_HOME_STATE,
            UI_HOME_FRAGMENT_STATE, UI_SETTINGS__FRAGMENT_STATE, UI_SUPPORT_FRAGMENT_STATE, UI_DEBUG_FRAGMENT_STATE})
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
    public static final int UI_SETTINGS__FRAGMENT_STATE = 1010;
    public static final int UI_SUPPORT_FRAGMENT_STATE = 1011;
    public static final int UI_DEBUG_FRAGMENT_STATE = 1012;

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
