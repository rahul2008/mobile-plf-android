/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.modularui.cocointerface.UICoCoConsumerCareImpl;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
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
import com.philips.platform.modularui.util.UIConstants;

public class HomeActivityPresenter extends UIBasePresenter implements UICoCoConsumerCareImpl.SetStateCallBack {

    HomeActivityPresenter(){
        setState(UIState.UI_HOME_STATE);
    }
    UIBaseNavigator uiBaseNavigator;
    AppFrameworkApplication appFrameworkApplication;
    UICoCoConsumerCareImpl uiCoCoConsumerCareImpl;
    UIState uiState;
    private final int MENU_OPTION_HOME = 0;
    private final int MENU_OPTION_SETTINGS = 1;
    private final int MENU_OPTION_SHOP = 2;
    private final int MENU_OPTION_SUPPORT = 3;
    private final int MENU_OPTION_ABOUT = 4;
    private final int MENU_OPTION_DEBUG = 5;

    @Override
    public void onClick(int componentID, Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiCoCoConsumerCareImpl = (UICoCoConsumerCareImpl) CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_CONSUMER_CARE);
        switch (componentID){
            case MENU_OPTION_HOME: uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
                break;
            case MENU_OPTION_SETTINGS: uiState = new SettingsFragmentState(UIState.UI_SETTINGS_FRAGMENT_STATE);
                break;
            case MENU_OPTION_SHOP: uiState = new InAppPurchaseFragmentState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
                break;
            case MENU_OPTION_SUPPORT: uiState = new SupportFragmentState(UIState.UI_SUPPORT_FRAGMENT_STATE);
                // TODO: pass presenter interface as listener if required from respective state classes
                uiCoCoConsumerCareImpl.registerForNextState(this);
                break;
            case MENU_OPTION_ABOUT:
                uiState=new AboutScreenState(UIState.UI_ABOUT_SCREEN_STATE);
                break;
 			case MENU_OPTION_DEBUG:
                uiState = new DebugTestFragmentState(UIState.UI_DEBUG_FRAGMENT_STATE);
                break;
            default:uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
        }
        uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
    }

    @Override
    public void onLoad(Context context) {

    }

    @Override
    public void setNextState(Context context) {
        uiCoCoConsumerCareImpl.unloadCoCo();
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new ProductRegistrationState(UIState.UI_PROD_REGISTRATION_STATE);
        uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState,context);
    }
}
