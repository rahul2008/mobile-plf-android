package com.philips.cdp.appframework.modularui;

import android.content.Context;

import com.philips.cdp.appframework.AppFrameworkBaseActivity;
import com.philips.cdp.appframework.R;
import com.philips.cdp.appframework.utility.SharedPreferenceUtility;

/**
 * Created by 310240027 on 6/27/2016.
 */
public class UIWelcomeScreenState implements UIBaseNavigation {
    @Override
    public int onClick(int componentID, Context context) {
        switch (componentID) {
            case R.id.start_registration_button:
                SharedPreferenceUtility.getInstance().writePreferenceInt(UIConstants.UI_START_STATUS, UIConstants.UI_WELCOME_STATE);
                AppFrameworkBaseActivity.setIntroScreenDonePressed();
                break;
            case R.id.appframework_skip_button:
                break;
        }
        return UIConstants.UI_REGISTRATION_STATE;
    }

    @Override
    public int onPageLoad(Context context) {
        return UIConstants.UI_REGISTRATION_STATE;
    }

    @Override
    public void setState() {
        UIFlowManager.currentState = UIFlowManager.getFromStateList(UIConstants.UI_WELCOME_STATE);
    }
}
