package com.philips.cdp.appframework.modularui;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIState extends UIStateBase {
    UIState(UIBaseNavigation navigator, @UIFlowManager.UIStateDef int stateID){
        this.navigator = navigator;
        this.stateID = stateID;
    }

}
