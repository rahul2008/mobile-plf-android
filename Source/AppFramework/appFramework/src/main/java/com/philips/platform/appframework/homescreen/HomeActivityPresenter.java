/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.datasevices.temperature.TemperatureTimeLineFragment;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.FragmentView;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.statecontroller.UIStateListener;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
import com.philips.platform.modularui.stateimpl.DataSyncScreenState;
import com.philips.platform.modularui.stateimpl.ConnectivityFragmentState;
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
 */
public class HomeActivityPresenter extends UIBasePresenter implements UIStateListener {

    public static final int MENU_OPTION_HOME = 0;
    private final int MENU_OPTION_SETTINGS = 1;
    private final int MENU_OPTION_SHOP = 2;
    private final int MENU_OPTION_SUPPORT = 3;
    private final int MENU_OPTION_ABOUT = 4;
    private final int MENU_OPTION_DEBUG = 5;
    private final int PRODUCT_REGISTRATION = 7;
    private final int MENU_OPTION_DATA_SYNC = 6;
    private FragmentView fragmentView;
    private AppFrameworkApplication appFrameworkApplication;
    private FragmentLauncher fragmentLauncher;
    private UIState uiState;

    public HomeActivityPresenter(final FragmentView fragmentView) {
        super(fragmentView);
        this.fragmentView = fragmentView;
        setState(UIState.UI_HOME_STATE);
    }

    /**
     * This methods handles all click events done on hamburger menu
     * Any changes for hamburger menu options should be made here
     */
    @Override
    public void onClick(int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) fragmentView.getFragmentActivity().getApplicationContext();
        uiState = getUiState(componentID);
        uiState.setPresenter(this);
        fragmentLauncher = getFragmentLauncher();
        if (uiState instanceof SupportFragmentState) {
            ((SupportFragmentState) uiState).registerUIStateListener(this);
        }
        appFrameworkApplication.getFlowManager().navigateToState(uiState, fragmentLauncher);
    }

    protected UIState getUiState(final int componentID) {
        switch (componentID) {
            case MENU_OPTION_HOME:
                uiState = new HomeFragmentState();
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                uiState.setUiStateData(homeStateData);
                break;
            case MENU_OPTION_SETTINGS:
                uiState = new SettingsFragmentState();
                UIStateData settingsStateData = new UIStateData();
                settingsStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                uiState.setUiStateData(settingsStateData);
                break;
            case MENU_OPTION_SHOP:
                uiState = new IAPState();
                IAPState.InAppStateData iapStateData = new IAPState().new InAppStateData();
                iapStateData.setIapFlow(IAPState.IAP_CATALOG_VIEW);
                iapStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                uiState.setUiStateData(iapStateData);
                break;
            case MENU_OPTION_SUPPORT:
                uiState = new SupportFragmentState();
                SupportFragmentState.ConsumerCareData supportStateData = new SupportFragmentState().new ConsumerCareData();
                supportStateData.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.productselection_ctnlist))));
                supportStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                uiState.setUiStateData(supportStateData);
                break;
            case MENU_OPTION_ABOUT:
                uiState = new AboutScreenState();
                UIStateData aboutStateData = new UIStateData();
                aboutStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                uiState.setUiStateData(aboutStateData);
                break;
            case MENU_OPTION_DEBUG:
                uiState = new DebugTestFragmentState();
                UIStateData debugStateData = new UIStateData();
                debugStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                uiState.setUiStateData(debugStateData);
                break;
            case Constants.UI_SHOPPING_CART_BUTTON_CLICK:
                uiState = new IAPState();
                IAPState.InAppStateData uiStateDataModel = new IAPState().new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_SHOPPING_CART_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                uiState.setUiStateData(uiStateDataModel);
                break;
            case MENU_OPTION_DATA_SYNC:
                uiState = new DataSyncScreenState();
                UIStateData syncStateData = new UIStateData();
                syncStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                uiState.setUiStateData(syncStateData);
                break;
            case PRODUCT_REGISTRATION:
                uiState = new ProductRegistrationState();
                break;
            case MENU_OPTION_CONNECTIVITY:
                uiState = new ConnectivityFragmentState();
                UIStateData connectivityStateData = new UIStateData();
                connectivityStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                uiState.setUiStateData(connectivityStateData);
                break;
            default:
                uiState = new HomeFragmentState();
        }
        return uiState;
    }

    public void showFragment(Fragment fragment, String fragmentTag) {
        int containerId = R.id.frame_container;

        try {
            FragmentTransaction fragmentTransaction = fragmentView.getFragmentActivity().getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(containerId, fragment, fragmentTag);
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            //Logger.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
    }

    protected FragmentLauncher getFragmentLauncher() {
        fragmentLauncher = new FragmentLauncher(fragmentView.getFragmentActivity(), fragmentView.getContainerId(), fragmentView.getActionBarListener());
        return fragmentLauncher;
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onStateComplete(UIState uiState) {
        appFrameworkApplication = (AppFrameworkApplication) fragmentView.getFragmentActivity().getApplicationContext();
        this.uiState = getUiState(PRODUCT_REGISTRATION);
        ProductRegistrationState.ProductRegistrationData uiStateDataModel = new ProductRegistrationState().new ProductRegistrationData();
        uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.productselection_ctnlist))));
        this.uiState.setUiStateData(uiStateDataModel);
        this.uiState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(this.uiState, fragmentLauncher);
    }
}
