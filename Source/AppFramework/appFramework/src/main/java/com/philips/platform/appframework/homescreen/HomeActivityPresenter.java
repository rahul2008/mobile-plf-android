/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.modularui.statecontroller.CoCoListener;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.InAppPurchaseState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.SettingsFragmentState;
import com.philips.platform.modularui.stateimpl.SupportFragmentState;
import com.philips.platform.modularui.util.UIConstants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class id used for loading various fragments that are supported by home activity ,
 * based on user selection this class loads the next state of the application.
 *
 */
public class HomeActivityPresenter extends UIBasePresenter implements CoCoListener {

    HomeActivityPresenter(){
        setState(UIState.UI_HOME_STATE);
    }
    AppFrameworkApplication appFrameworkApplication;
    UIState uiState;
    private final int MENU_OPTION_HOME = 0;
    private final int MENU_OPTION_SETTINGS = 1;
    private final int MENU_OPTION_SHOP = 2;
    private final int MENU_OPTION_SUPPORT = 3;
    private final int MENU_OPTION_ABOUT = 4;
    private final int MENU_OPTION_DEBUG = 5;
/**
 * This methods handles all click events done on hamburger menu
 * Any changes for hamburger menu options shuld be made here
 */
    @Override
    public void onClick(int componentID, Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        switch (componentID){
            case MENU_OPTION_HOME: uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
                break;
            case MENU_OPTION_SETTINGS: uiState = new SettingsFragmentState(UIState.UI_SETTINGS_FRAGMENT_STATE);
                break;
            case MENU_OPTION_SHOP: uiState = new InAppPurchaseState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
                uiState.init(new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context));
                InAppPurchaseState.InAppStateData uiStateData = new InAppPurchaseState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE).new InAppStateData();
                uiStateData.setIapFlow(UIConstants.IAP_CATALOG_VIEW);
                uiState.setUiStateData(uiStateData);
                break;
            case MENU_OPTION_SUPPORT: uiState = new SupportFragmentState(UIState.UI_SUPPORT_FRAGMENT_STATE);
                uiState.init(new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context));
                SupportFragmentState.ConsumerCareData uistateDataModel =  new SupportFragmentState(UIState.UI_SUPPORT_FRAGMENT_STATE).new ConsumerCareData();
                uistateDataModel.setCtnList(new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.productselection_ctnlist))));
                uiState.setUiStateData(uistateDataModel);
                break;
            case MENU_OPTION_ABOUT:
                uiState=new AboutScreenState(UIState.UI_ABOUT_SCREEN_STATE);
                break;
 			case MENU_OPTION_DEBUG:
                uiState = new DebugTestFragmentState(UIState.UI_DEBUG_FRAGMENT_STATE);
                break;
            case UIConstants.UI_SHOPPING_CART_BUTTON_CLICK:
                uiState = new InAppPurchaseState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
                uiState.init(new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context));
                InAppPurchaseState.InAppStateData uiStateDataModel = new InAppPurchaseState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE).new InAppStateData();
                uiStateDataModel.setIapFlow(UIConstants.IAP_SHOPPING_CART_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                uiState.setUiStateData(uiStateDataModel);
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
    public void coCoCallBack(Context context) {
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        uiState = new ProductRegistrationState(UIState.UI_PROD_REGISTRATION_STATE);
        uiState.init(new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context));
        ProductRegistrationState.ProductRegistrationData uiStateDataModel = new ProductRegistrationState(UIState.UI_PROD_REGISTRATION_STATE).new ProductRegistrationData();
        uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.productselection_ctnlist))));
        uiState.setUiStateData(uiStateDataModel);
        uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(uiState, context);
    }
}
