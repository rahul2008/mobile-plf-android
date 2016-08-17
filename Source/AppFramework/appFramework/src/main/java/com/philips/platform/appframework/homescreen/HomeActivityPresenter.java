/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.statecontroller.UIBaseNavigator;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.InAppPurchaseFragmentState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.SettingsFragmentState;
import com.philips.platform.modularui.stateimpl.SupportFragmentState;

public class HomeActivityPresenter extends UIBasePresenter implements SupportFragmentState.SetStateCallBack {

    HomeActivityPresenter(){
        setState(UIState.UI_HOME_STATE);
    }
    UIBaseNavigator uiBaseNavigator;
    AppFrameworkApplication appFrameworkApplication;
    UIState uiState;
    @Override
    public void onClick(int componentID, Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        switch (componentID){
            case 0: uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
                break;
            case 1: uiState = new SupportFragmentState(UIState.UI_SUPPORT_FRAGMENT_STATE);
                // TODO: pass presenter interface as listener if required from respective state classes
                break;
            case 2: uiState = new InAppPurchaseFragmentState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
                break;
            case 3: uiState = new SettingsFragmentState(UIState.UI_SETTINGS_FRAGMENT_STATE);
                break;
 			case 4: uiState = new DebugTestFragmentState(UIState.UI_DEBUG_FRAGMENT_STATE);
                break;
            case 5: uiState=new AboutScreenState(UIState.UI_ABOUT_SCREEN_STATE);
                break;
            default:uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
        }
        uiState.setPresenter(this);
        if(uiState instanceof SupportFragmentState){
            ((SupportFragmentState)uiState).registerForNextState(this);
        }
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
    }

    @Override
    public void onLoad(Context context) {

    }

    @Override
    public void setNextState(Context context) {
        ((SupportFragmentState)uiState).unloadCoCo();
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new ProductRegistrationState(UIState.UI_PROD_REGISTRATION_STATE);
        uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
    }
}
