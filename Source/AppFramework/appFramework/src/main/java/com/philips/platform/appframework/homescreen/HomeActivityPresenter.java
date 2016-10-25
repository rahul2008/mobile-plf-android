/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.FlowManager;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.flowmanager.jsonstates.AppStates;
import com.philips.platform.flowmanager.jsonstates.EventStates;
import com.philips.platform.modularui.statecontroller.FragmentView;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.statecontroller.UIStateListener;
import com.philips.platform.modularui.stateimpl.IAPState;
import com.philips.platform.modularui.stateimpl.ProductRegistrationState;
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
    private final int PRODUCT_REGISTRATION = 6;
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
        EventStates eventStates = getEventState(componentID);
        uiState = appFrameworkApplication.getTargetFlowManager().getNextState(AppStates.HOME, eventStates);
        uiState.setPresenter(this);
        uiState.setUiStateData(setStateData(componentID));
        fragmentLauncher = getFragmentLauncher();
        if (uiState instanceof SupportFragmentState) {
            ((SupportFragmentState) uiState).registerUIStateListener(this);
        }
        uiState.navigate(fragmentLauncher);
    }

    protected UIStateData setStateData(final int componentID) {
        final int MENU_OPTION_SETTINGS = 1;
        final int MENU_OPTION_SHOP = 2;
        final int MENU_OPTION_SUPPORT = 3;
        final int MENU_OPTION_ABOUT = 4;
        switch (componentID) {
            case MENU_OPTION_HOME:
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                return homeStateData;
            case MENU_OPTION_SETTINGS:
                UIStateData settingsStateData = new UIStateData();
                settingsStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                return settingsStateData;
            case MENU_OPTION_SHOP:
                IAPState.InAppStateData iapStateData = new IAPState().new InAppStateData();
                iapStateData.setIapFlow(IAPState.IAP_CATALOG_VIEW);
                iapStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                return iapStateData;
            case MENU_OPTION_SUPPORT:
                SupportFragmentState.ConsumerCareData supportStateData = new SupportFragmentState().new ConsumerCareData();
                supportStateData.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.productselection_ctnlist))));
                supportStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                return supportStateData;
            case MENU_OPTION_ABOUT:
                UIStateData aboutStateData = new UIStateData();
                aboutStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                return aboutStateData;
            case Constants.UI_SHOPPING_CART_BUTTON_CLICK:
                IAPState.InAppStateData uiStateDataModel = new IAPState().new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_SHOPPING_CART_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                return uiStateDataModel;
            default:
                homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                return homeStateData;
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
        this.uiState = appFrameworkApplication.getTargetFlowManager().getNextState(AppStates.SUPPORT, EventStates.SUPPORT_PR);
        ProductRegistrationState.ProductRegistrationData uiStateDataModel = new ProductRegistrationState().new ProductRegistrationData();
        uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.productselection_ctnlist))));
        this.uiState.setUiStateData(uiStateDataModel);
        this.uiState.setPresenter(this);
        this.uiState.navigate(fragmentLauncher);
    }

    public EventStates getEventState(int componentID) {
        final int MENU_OPTION_SETTINGS = 1;
        final int MENU_OPTION_SHOP = 2;
        final int MENU_OPTION_SUPPORT = 3;
        final int MENU_OPTION_ABOUT = 4;
        switch (componentID) {
            case MENU_OPTION_HOME:
                return EventStates.HOME_FRAGMENT;
            case MENU_OPTION_SETTINGS:
                return EventStates.HOME_SETTINGS;
            case MENU_OPTION_SHOP:
                return EventStates.HOME_IAP;
            case MENU_OPTION_SUPPORT:
                return EventStates.HOME_SUPPORT;
            case MENU_OPTION_ABOUT:
                return EventStates.HOME_ABOUT;
            case Constants.UI_SHOPPING_CART_BUTTON_CLICK:
                return EventStates.HOME_SUPPORT;
            case PRODUCT_REGISTRATION:
                return EventStates.SUPPORT_PR;
            default:
                return EventStates.HOME_FRAGMENT;
        }
    }
}
