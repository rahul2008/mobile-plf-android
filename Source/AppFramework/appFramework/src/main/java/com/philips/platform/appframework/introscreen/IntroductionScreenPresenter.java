package com.philips.platform.appframework.introscreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.modularui.statecontroller.ShowFragmentCallBack;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class IntroductionScreenPresenter implements UIBasePresenter {
    AppFrameworkApplication appFrameworkApplication;

    @Override
    public void onClick(int componentID, Context context, ShowFragmentCallBack showFragmentCallBack) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        switch (componentID) {
            case R.id.appframework_skip_button:
                appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_REGISTRATION_STATE, context);
                break;
            case R.id.start_registration_button:
                appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_REGISTRATION_STATE, context);
                break;
        }

    }

    @Override
    public void onLoad(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        appFrameworkApplication.getFlowManager().navigateNextState(UIConstants.UI_HOME_STATE, context);
    }
}
