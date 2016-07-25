package com.philips.platform.modularui.statecontroller;

import android.content.Context;

/**
 * Created by 310240027 on 6/16/2016.
 */
abstract public class UIState extends UIStateBase {

    public UIState(@UIStateBase.UIStateDef int stateID){
        this.stateID = stateID;
    }

    protected abstract void navigate(Context context);

}
