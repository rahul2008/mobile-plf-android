package com.philips.platform.modularui.statecontroller;

import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 6/16/2016.
 */
public abstract class UIStateBase {
    UIBaseNavigator navigator;
    @UIConstants.UIStateDef
    int stateID;

    public UIBaseNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(UIBaseNavigator navigator) {
        this.navigator = navigator;
    }

    @UIConstants.UIStateDef
    public int getStateID() {
        return stateID;
    }

    @UIConstants.UIStateDef
    public void setStateID(int stateID) {
        this.stateID = stateID;
    }
}
