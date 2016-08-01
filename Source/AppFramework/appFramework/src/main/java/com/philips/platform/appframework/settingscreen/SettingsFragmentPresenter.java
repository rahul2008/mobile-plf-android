package com.philips.platform.appframework.settingscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.cocointerface.UICoCoUserRegImpl;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class SettingsFragmentPresenter extends UIBasePresenter implements UICoCoUserRegImpl.SetStateCallBack {

    SettingsFragmentPresenter(){
        setState(UIState.UI_SETTINGS_FRAGMENT_STATE);
    }

    AppFrameworkApplication appFrameworkApplication;
    UICoCoUserRegImpl uiCoCoUserReg;
    @Override
    public void onClick(int componentID, Context context) {
    }

    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiCoCoUserReg = (UICoCoUserRegImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_USER_REGISTRATION);
        uiCoCoUserReg.registerForNextState(this);
        uiCoCoUserReg.setFragActivity((HomeActivity)context);
        uiCoCoUserReg.setFragmentContainer(R.id.frame_container);
        appFrameworkApplication.getFlowManager().navigateToState(UIState.UI_REGISTRATION_STATE, context, this);
    }

    @Override
    public void setNextState(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        appFrameworkApplication.getFlowManager().navigateToState(UIState.UI_HOME_STATE, context, this);
    }
}
