package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class HomeScreenFragmentPresenter extends UIBasePresenter {

    HomeScreenFragmentPresenter(){
        setState(UIState.UI_HOME_FRAGMENT_STATE);
    }
    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {

    }
}
