package com.philips.platform.modularui.navigatorwelcome;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.modularui.factorymanager.UIStateFactory;
import com.philips.platform.modularui.statecontroller.UIBaseNavigation;
import com.philips.platform.modularui.statecontroller.UIStateBase;
import com.philips.platform.modularui.statecontroller.UIStateManager;
import com.philips.platform.modularui.util.UIConstants;
import com.philips.platform.appframework.utility.SharedPreferenceUtility;

/**
 * Created by 310240027 on 6/27/2016.
 */
public class UIWelcomeScreenNavigator implements UIBaseNavigation {
    @Override
    public UIStateBase onClick(int componentID, Context context) {
        switch (componentID) {
            case R.id.start_registration_button:
                SharedPreferenceUtility.getInstance().writePreferenceInt(UIConstants.UI_START_STATUS, UIConstants.UI_SPLASH_DONE_PRESSED_STATE);
                AppFrameworkBaseActivity.setIntroScreenDonePressed();
                break;
            case R.id.appframework_skip_button:
                break;
        }
        return  UIStateFactory.getInstance().createUIState(UIConstants.UI_REGISTRATION_STATE);
    }

    @Override
    public UIStateBase onPageLoad(Context context) {
        return UIStateFactory.getInstance().createUIState(UIConstants.UI_HAMBURGER_STATE);
    }

    @Override
    public void setState() {
        UIStateManager.getInstance().setCurrentState(UIStateManager.getInstance().getStateMap(UIConstants.UI_WELCOME_STATE));
    }
}
