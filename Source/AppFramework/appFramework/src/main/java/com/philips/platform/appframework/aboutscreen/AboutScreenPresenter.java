package com.philips.platform.appframework.aboutscreen;

import android.content.Context;

import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Created by 310213373 on 8/10/2016.
 */
public class AboutScreenPresenter  extends UIBasePresenter {

    AboutScreenPresenter(){
        setState(UIState.UI_ABOUT_SCREEN_STATE);
    }
    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {

    }  }