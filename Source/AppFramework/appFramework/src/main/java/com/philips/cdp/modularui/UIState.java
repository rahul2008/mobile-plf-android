package com.philips.cdp.modularui;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIState extends UIStateBase {
    public UIState(UIBaseNavigation navigator, @UIConstants.UIStateDef int stateID) {
        this.navigator = navigator;
        this.stateID = stateID;
    }

}
