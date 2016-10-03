/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.statecontroller.UIStateListener;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
import com.philips.platform.modularui.stateimpl.DebugTestFragmentState;
import com.philips.platform.modularui.stateimpl.HomeFragmentState;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
import com.philips.platform.modularui.stateimpl.SettingsFragmentState;
import com.philips.platform.modularui.stateimpl.SupportFragmentState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class id used for loading various fragments that are supported by home activity ,
 * based on user selection this class loads the next state of the application.
 *
 */
public class HomeActivityPresenter extends UIBasePresenter implements UIStateListener {

    HomeActivityPresenter(){
        setState(UIState.UI_HOME_STATE);
    }
    private AppFrameworkApplication appFrameworkApplication;
    private FragmentLauncher fragmentLauncher;
    private UIState uiState;
    private final int MENU_OPTION_HOME = 0;
    private final int MENU_OPTION_SETTINGS = 1;
    private final int MENU_OPTION_SHOP = 2;
    private final int MENU_OPTION_SUPPORT = 3;
    private final int MENU_OPTION_ABOUT = 4;
    private final int MENU_OPTION_DEBUG = 5;
    private Context activityContext;

/**
 * This methods handles all click events done on hamburger menu
 * Any changes for hamburger menu options should be made here
 */
    @Override
    public void onClick(int componentID, Context context) {
         activityContext = context;
        appFrameworkApplication = (AppFrameworkApplication) context.getApplicationContext();
        switch (componentID){
            case MENU_OPTION_HOME: uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                uiState.setUiStateData(homeStateData);
                fragmentLauncher = new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context);
                break;
            case MENU_OPTION_SETTINGS: uiState = new SettingsFragmentState(UIState.UI_SETTINGS_FRAGMENT_STATE);
                UIStateData settingsStateData = new UIStateData();
                settingsStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                uiState.setUiStateData(settingsStateData);
                fragmentLauncher = new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context);
                break;
            case MENU_OPTION_SHOP: uiState = new IAPState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
                fragmentLauncher = new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context);
                IAPState.InAppStateData iapStateData = new IAPState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE).new InAppStateData();
                iapStateData.setIapFlow(IAPState.IAP_CATALOG_VIEW);
                iapStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                uiState.setUiStateData(iapStateData);
                break;
            case MENU_OPTION_SUPPORT: uiState = new SupportFragmentState(UIState.UI_SUPPORT_FRAGMENT_STATE);
                fragmentLauncher = new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context);
                SupportFragmentState.ConsumerCareData supportStateData =  new SupportFragmentState(UIState.UI_SUPPORT_FRAGMENT_STATE).new ConsumerCareData();
                supportStateData.setCtnList(new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.productselection_ctnlist))));
                supportStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                uiState.setUiStateData(supportStateData);
                break;
            case MENU_OPTION_ABOUT:
                uiState=new AboutScreenState(UIState.UI_ABOUT_SCREEN_STATE);
                UIStateData aboutStateData = new UIStateData();
                aboutStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                uiState.setUiStateData(aboutStateData);
                fragmentLauncher = new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context);
                break;
 			case MENU_OPTION_DEBUG:
                uiState = new DebugTestFragmentState(UIState.UI_DEBUG_FRAGMENT_STATE);
                UIStateData debugStateData = new UIStateData();
                debugStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                uiState.setUiStateData(debugStateData);
                fragmentLauncher = new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context);
                break;
            case Constants.UI_SHOPPING_CART_BUTTON_CLICK:
                uiState = new IAPState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE);
                fragmentLauncher = new FragmentLauncher((HomeActivity)context,R.id.frame_container,(HomeActivity)context);
                IAPState.InAppStateData uiStateDataModel = new IAPState(UIState.UI_IAP_SHOPPING_FRAGMENT_STATE).new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_SHOPPING_CART_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                uiState.setUiStateData(uiStateDataModel);
                break;
            default:uiState = new HomeFragmentState(UIState.UI_HOME_FRAGMENT_STATE);
        }
        uiState.setPresenter(this);
        if(uiState instanceof SupportFragmentState){
            ((SupportFragmentState)uiState).registerUIStateListener(this);
        }
        appFrameworkApplication.getFlowManager().navigateToState(uiState,fragmentLauncher);
    }

    @Override
    public void onLoad(Context context) {

    }

    @Override
    public void onStateComplete(UIState uiState) {
        appFrameworkApplication = (AppFrameworkApplication) activityContext.getApplicationContext();
        this.uiState = new ProductRegistrationState(UIState.UI_PROD_REGISTRATION_STATE);
        fragmentLauncher = new FragmentLauncher((HomeActivity)activityContext,R.id.frame_container,(HomeActivity)activityContext);
        ProductRegistrationState.ProductRegistrationData uiStateDataModel = new ProductRegistrationState(UIState.UI_PROD_REGISTRATION_STATE).new ProductRegistrationData();
        uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(activityContext.getResources().getStringArray(R.array.productselection_ctnlist))));
        this.uiState.setUiStateData(uiStateDataModel);
        this.uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(this.uiState, fragmentLauncher);
    }
}
