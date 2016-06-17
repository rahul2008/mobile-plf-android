package com.philips.cdp.appframework.modularui;

import android.app.Activity;

import java.util.Map;

/**
 * Created by 310240027 on 6/16/2016.
 */
public abstract class UIStateBase {
    Map<Integer, Activity> activityMap;
    UIBaseNavigation navigator;
    @UIFlowManager.UIStateDef int stateID;
    UIBaseLogic logic;
}
