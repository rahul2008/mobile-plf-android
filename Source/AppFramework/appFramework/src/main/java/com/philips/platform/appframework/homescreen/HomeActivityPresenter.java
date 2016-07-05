package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.modularui.navigatorimpl.DebugTestFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.HomeFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.SettingsFragmentNavigator;
import com.philips.platform.modularui.navigatorimpl.SupportFragmentNavigator;
import com.philips.platform.modularui.statecontroller.SomeInterface;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;

/**
 * Created by 310240027 on 7/4/2016.
 */
public class HomeActivityPresenter implements UIBasePresenter {
    UIBaseNavigator uiBaseNavigator;
    @Override
    public void onClick(int componentID, Context context) {


        switch (componentID){
            case 0: uiBaseNavigator = new HomeFragmentNavigator();
                break;
            case 1: uiBaseNavigator = new SupportFragmentNavigator();
                break;
            case 2: uiBaseNavigator = new SettingsFragmentNavigator();
                break;
            case 3: uiBaseNavigator = new DebugTestFragmentNavigator();
                break;
            default: uiBaseNavigator = new HomeFragmentNavigator();
        }

        uiBaseNavigator.loadScreen(context);
    }

    public void onLoad(SomeInterface someInterface){
            someInterface.showFragmentCallBack(uiBaseNavigator.loadFragment());
    }
}
