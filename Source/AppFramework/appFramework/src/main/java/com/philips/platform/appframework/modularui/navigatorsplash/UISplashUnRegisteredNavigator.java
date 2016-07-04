package com.philips.platform.appframework.modularui.navigatorsplash;

import android.content.Context;

import com.philips.platform.appframework.modularui.statecontroller.UIStateManager;
import com.philips.platform.appframework.modularui.factorymanager.UIStateFactory;
import com.philips.platform.appframework.modularui.statecontroller.UIBaseNavigation;
import com.philips.platform.appframework.modularui.statecontroller.UIStateBase;
import com.philips.platform.appframework.modularui.util.UIConstants;

/**
 * Created by 310240027 on 6/21/2016.
 */
public class UISplashUnRegisteredNavigator implements UIBaseNavigation {
    @Override
    public UIStateBase onClick(int componentID, Context context) {
        return null;
    }

    @Override
    public UIStateBase onPageLoad(Context context) {
        return UIStateFactory.getInstance().createUIState(UIConstants.UI_WELCOME_STATE);
    }

    @Override
    public void setState() {
        UIStateManager.getInstance().setCurrentState(UIStateManager.getInstance().getStateMap(UIConstants.UI_SPLASH_UNREGISTERED_STATE));
    }
}
