package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIStateBase;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class HomeActivityPresenter implements UIBasePresenter {
    UIBaseNavigator uiBaseNavigator;
    AppFrameworkApplication appFrameworkApplication;
    @Override
    public void onClick(int componentID, Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();

        switch (componentID){
            case 0: appFrameworkApplication.getFlowManager().navigateState(UIStateBase.UI_HOME_FRAGMENT_STATE,context);
                break;
            case 1: appFrameworkApplication.getFlowManager().navigateState(UIStateBase.UI_SUPPORT_FRAGMENT_STATE,context);
                break;
            case 2: appFrameworkApplication.getFlowManager().navigateState(UIStateBase.UI_SETTINGS_FRAGMENT_STATE,context);
                break;
            case 3: appFrameworkApplication.getFlowManager().navigateState(UIStateBase.UI_DEBUG_FRAGMENT_STATE,context);
                break;
            default:appFrameworkApplication.getFlowManager().navigateState(UIStateBase.UI_HOME_FRAGMENT_STATE,context);
        }
    }

    @Override
    public void onLoad(Context context) {

    }

}
