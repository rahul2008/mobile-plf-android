package com.philips.cdp.appframework.modularui;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 310240027 on 6/20/2016.
 */
public class UIConstants {
    @IntDef({UI_SPLASH_STATE_ONE, UI_SPLASH_STATE_TWO, UI_SPLASH_STATE_THREE, UI_WELCOME_STATE_ONE, UI_WELCOME_STATE_TWO, UI_WELCOME_STATE_THREE, UI_REGISTRATION_STATE_ONE, UI_HOME_STATE_ONE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UIStateDef {
    }

    public static final int UI_SPLASH_STATE_ONE = 1001;
    public static final int UI_SPLASH_STATE_TWO = 1002;
    public static final int UI_SPLASH_STATE_THREE = 1003;
    public static final int UI_WELCOME_STATE_ONE = 1004;
    public static final int UI_WELCOME_STATE_TWO = 1005;
    public static final int UI_WELCOME_STATE_THREE = 1006;
    public static final int UI_HOME_STATE_ONE = 1007;
    public static final int UI_REGISTRATION_STATE_ONE = 1008;

    @IntDef({UI_COCO_USER_REGISTRATION, UI_COCO_PRODUCT_REGISTRATION, UI_COCO_IN_APP_PURCHASE, UI_COCO_CONSUMER_CARE, UI_COCO_APP_CHASSIS})
    @Retention(RetentionPolicy.SOURCE)
    public @interface UICoCoConstants {
    }

    public static final int UI_COCO_USER_REGISTRATION = 2001;
    public static final int UI_COCO_PRODUCT_REGISTRATION = 2002;
    public static final int UI_COCO_IN_APP_PURCHASE = 2003;
    public static final int UI_COCO_CONSUMER_CARE = 2004;
    public static final int UI_COCO_APP_CHASSIS = 2005;
}
