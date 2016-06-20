package com.philips.cdp.appframework.modularui;

/**
 * Created by 310240027 on 6/16/2016.
 */
public abstract class UIStateBase {
    UIBaseNavigation navigator;
    @UIFlowManager.UIStateDef int stateID;
    UIBaseLogic logic;
}
