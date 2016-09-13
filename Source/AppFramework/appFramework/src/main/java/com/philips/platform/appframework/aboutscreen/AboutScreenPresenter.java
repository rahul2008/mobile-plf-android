package com.philips.platform.appframework.aboutscreen;

import android.content.Context;

import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;

/**
 * Presenter defines the methods that can be called on loading about screen
 *The click operation and screen load logic can be written in this class
 *
 */
public class AboutScreenPresenter  extends UIBasePresenter {

    AboutScreenPresenter(){
        setState(UIState.UI_ABOUT_SCREEN_STATE);
    }

    /**
     * For click event of about screen
     * @param  componentID : the component ID
     * @param  context : Requires context
     * returns: void
     *
     */
    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {

    }  }