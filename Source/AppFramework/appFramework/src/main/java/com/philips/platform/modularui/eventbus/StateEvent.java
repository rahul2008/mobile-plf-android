package com.philips.platform.modularui.eventbus;

import android.content.Context;

/**
 * Created by 310240027 on 7/7/2016.
 */
public class StateEvent {

    public int stateID;
    public Context context;
    public StateEvent(int stateID, Context context){
        this.stateID = stateID;
        this.context = context;
    }
    public int getStateID() {
        return stateID;
    }

    public void setStateID(int stateID) {
        this.stateID = stateID;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

}
