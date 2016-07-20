package com.philips.platform.appframework.settingscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.cocointerface.UICoCoUserRegImpl;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIStateBase;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/5/2016.
 */
public class SettingsFragmentPresenter implements UIBasePresenter,UICoCoUserRegImpl.SetStateCallBack {
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
        appFrameworkApplication.getFlowManager().navigateState(UIStateBase.UI_REGISTRATION_STATE, context);
    }

    @Override
    public void setNextState(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        appFrameworkApplication.getFlowManager().navigateState(UIStateBase.UI_HOME_STATE, context);
    }
}
