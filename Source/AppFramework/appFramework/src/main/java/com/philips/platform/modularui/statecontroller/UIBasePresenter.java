package com.philips.platform.modularui.statecontroller;

import android.content.Context;

/**
 * Created by 310240027 on 7/4/2016.
 */
abstract public class UIBasePresenter {
    public abstract void onClick(int componentID, Context context);
    public abstract void onLoad(Context context);
    public void setState(int stateID){

    }
}
