package com.philips.platform.modularui.util;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 310240027 on 6/20/2016.
 */
public class UIConstants {

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
    public static final String DONE_PRESSED = "donePressed";
}
