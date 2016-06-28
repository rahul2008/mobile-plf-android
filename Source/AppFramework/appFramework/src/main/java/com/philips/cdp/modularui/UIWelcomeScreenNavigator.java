package com.philips.cdp.modularui;

import android.content.Context;

import com.philips.cdp.appframework.AppFrameworkBaseActivity;
import com.philips.cdp.appframework.R;
import com.philips.cdp.appframework.utility.SharedPreferenceUtility;

/**
 * Created by 310240027 on 6/27/2016.
 */
public class UIWelcomeScreenNavigator implements UIBaseNavigation {
    @Override
    public UIStateBase onClick(int componentID, Context context) {
        switch (componentID) {
            case R.id.start_registration_button:
                SharedPreferenceUtility.getInstance().writePreferenceInt(UIConstants.UI_START_STATUS, UIConstants.UI_WELCOME_STATE);
                AppFrameworkBaseActivity.setIntroScreenDonePressed();
                break;
            case R.id.appframework_skip_button:
                break;
        }
        return  UIStateFactory.getInstance().createUIState(UIConstants.UI_REGISTRATION_STATE);
    }

    @Override
    public UIStateBase onPageLoad(Context context) {
        return (UIStateManager.getInstance().getFromStateList(UIConstants.UI_REGISTRATION_STATE));
    }

    @Override
    public void setState() {
        UIStateManager.getInstance().setCurrentState(UIStateManager.getInstance().getFromStateList(UIConstants.UI_WELCOME_STATE));
    }
}
