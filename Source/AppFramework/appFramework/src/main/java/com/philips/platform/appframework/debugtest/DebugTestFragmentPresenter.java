package com.philips.platform.appframework.debugtest;

import android.content.Context;

import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class DebugTestFragmentPresenter extends UIBasePresenter {

    DebugTestFragmentPresenter(){
        setState(UIState.UI_DEBUG_FRAGMENT_STATE);
    }
    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {

    }
}
