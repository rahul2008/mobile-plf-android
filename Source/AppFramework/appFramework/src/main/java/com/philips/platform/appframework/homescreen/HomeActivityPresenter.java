package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.statecontroller.ShowFragmentCallBack;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class HomeActivityPresenter implements UIBasePresenter {
    UIBaseNavigator uiBaseNavigator;
    AppFrameworkApplication appFrameworkApplication;
    @Override
    public void onClick(int componentID, Context context,ShowFragmentCallBack showFragmentCallBack) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();

        switch (componentID){
            case 0: appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_HOME_FRAGMENT_STATE,context);
                break;
            case 1: appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_SUPPORT_FRAGMENT_STATE,context);
                break;
            case 2: appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_SETTINGS_FRAGMENT_STATE,context);
                break;
            case 3: appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_DEBUG_FRAGMENT_STATE,context);
                break;
            default:appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_HOME_FRAGMENT_STATE,context);
        }
    }

    @Override
    public void onLoad(Context context) {

    }

}
