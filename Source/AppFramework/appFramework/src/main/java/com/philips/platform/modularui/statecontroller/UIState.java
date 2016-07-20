package com.philips.platform.modularui.statecontroller;

/**
 * Created by 310240027 on 6/16/2016.
 */
public class UIState extends UIStateBase {

    UIBaseNavigator navigator;
    @UIStateBase.UIStateDef
    int stateID;

    @Override
    public UIBaseNavigator getNavigator() {
        return navigator;
    }

    @Override
    public void setNavigator(UIBaseNavigator navigator) {
        this.navigator = navigator;
    }

    @Override
    public int getStateID() {
        return stateID;
    }

    @Override
    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

}
