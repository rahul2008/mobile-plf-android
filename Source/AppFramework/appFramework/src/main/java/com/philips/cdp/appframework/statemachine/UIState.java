package com.philips.cdp.appframework.statemachine;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by 310240027 on 6/14/2016.
 */
public class UIState {

    @IntDef({UI_ONCLICK_ACTION, UI_ONSWIPE_ACTION,UI_ON_PRESS_ACTION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface ActivityAction {}

    public static final int UI_ONCLICK_ACTION = 2001;
    public static final int UI_ONSWIPE_ACTION = 2002;
    public static final int UI_ON_PRESS_ACTION = 2003;
}
