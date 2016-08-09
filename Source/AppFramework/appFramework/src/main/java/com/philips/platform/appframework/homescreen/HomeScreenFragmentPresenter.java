/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;

public class HomeScreenFragmentPresenter extends UIBasePresenter {

    public HomeScreenFragmentPresenter(){
        setState(UIState.UI_HOME_FRAGMENT_STATE);
    }
    @Override
    public void onClick(int componentID, Context context) {

    }

    @Override
    public void onLoad(Context context) {

    }
}
