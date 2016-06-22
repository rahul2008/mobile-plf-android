package com.philips.cdp.appframework.modularui;

/**
 * Created by 310240027 on 6/16/2016.
 */
public abstract class UIStateBase {
    UIBaseNavigation navigator;
    @UIConstants.UIStateDef int stateID;
    UIBaseLogic logic;

    public UIBaseNavigation getNavigator() {
        return navigator;
    }

    public void setNavigator(UIBaseNavigation navigator) {
        this.navigator = navigator;
    }

    @UIConstants.UIStateDef public int getStateID() {
        return stateID;
    }

    @UIConstants.UIStateDef public void setStateID(int stateID) {
        this.stateID = stateID;
    }
}
