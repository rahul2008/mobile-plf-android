package com.philips.cdp.wifirefuapp.states;

import android.content.Context;

/**
 * Created by philips on 6/8/17.
 */

public abstract class BaseState {
    Context context;
    public BaseState(Context context){
        this.context =context;
    }
    abstract void start(StateContext stateContext);
}
