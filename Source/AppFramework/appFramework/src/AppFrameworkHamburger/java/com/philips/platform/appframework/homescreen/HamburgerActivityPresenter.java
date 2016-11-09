/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.support.annotation.NonNull;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.HamburgerAppState;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.BaseAppState;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.FragmentView;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
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
public class HamburgerActivityPresenter extends UIBasePresenter implements UIStateListener {

    public static final int MENU_OPTION_HOME = 0;
    private FragmentView fragmentView;
    private FragmentLauncher fragmentLauncher;
    private BaseState baseState;


    /*Event ID */
    final int MENU_OPTION_SETTINGS = 1;
    final int MENU_OPTION_SHOP = 2;
    final int MENU_OPTION_SUPPORT = 3;
    final int MENU_OPTION_ABOUT = 4;
    final int MENU_OPTION_DATA_SYNC = 5;
    final int MENU_OPTION_PR = 6;

    /* event to state map */
    final String HOME_SETTINGS = "settings";
    final String HOME_IAP = "iap";
    final String HOME_SUPPORT = "support";
    final String SHOPPING_CART = "shopping_cart";
    final String HOME_ABOUT = "about";
    final String HOME_FRAGMENT = "home_fragment";
    final String DATA_SYNC = "data_sync";
    final String SUPPORT_PR = "pr";

    public HamburgerActivityPresenter(final FragmentView fragmentView) {
        super(fragmentView);
        this.fragmentView = fragmentView;
        setState(HamburgerAppState.HAMBURGER_HOME);
    }

    /**
     * This methods handles all click events done on hamburger menu
     * Any changes for hamburger menu options should be made here
     */
    @Override
    public void onClick(int componentID) {
        AppFrameworkApplication appFrameworkApplication = getApplicationContext();
        String eventState = getEventState(componentID);
        baseState = appFrameworkApplication.getTargetFlowManager().getNextState(HamburgerAppState.HAMBURGER_HOME, eventState);
        baseState.setPresenter(this);
        baseState.setUiStateData(setStateData(componentID));
        fragmentLauncher = getFragmentLauncher();
        if (baseState instanceof SupportFragmentState) {
            ((SupportFragmentState) baseState).registerUIStateListener(this);
        }
        baseState.navigate(fragmentLauncher);
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) fragmentView.getFragmentActivity().getApplicationContext();
    }

    // TODO: Deepthi, when we already know that we need to set certain data for each menu click and that too for which component, why do we ask flow manager to give only state?
    // So here atleast i see launch type is already causing problems + data models was anyways a concern.
    protected UIStateData setStateData(final int componentID) {
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
                uiStateDataModel.setCtnList(getCtnList());
                return uiStateDataModel;
            case MENU_OPTION_PR:
                ProductRegistrationState.ProductRegistrationData prStateDataModel = new ProductRegistrationState().new ProductRegistrationData();
                prStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.productselection_ctnlist))));
                return prStateDataModel;
            case MENU_OPTION_DATA_SYNC:
                UIStateData syncStateData = new UIStateData();
                syncStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                return syncStateData;
            default:
                homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                return homeStateData;
        }
    }

    @NonNull
    protected ArrayList<String> getCtnList() {
        return new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.iap_productselection_ctnlist)));
    }

    protected FragmentLauncher getFragmentLauncher() {
        fragmentLauncher = new FragmentLauncher(fragmentView.getFragmentActivity(), fragmentView.getContainerId(), fragmentView.getActionBarListener());
        return fragmentLauncher;
    }

    @Override
    public void onLoad() {

    }

    // TODO: Deepthi, This seems to be hardcoded without even checking the uistate, we are taking decision here.
    // cant we move this to json ?
    @Override
    public void onStateComplete(BaseState baseState) {
        this.baseState = getApplicationContext().getTargetFlowManager().getNextState(BaseAppState.SUPPORT, SUPPORT_PR);
        this.baseState.setUiStateData(setStateData(MENU_OPTION_PR));
        this.baseState.setPresenter(this);
        fragmentLauncher = getFragmentLauncher();
        this.baseState.navigate(fragmentLauncher);
    }

    // TODO: Deepthi, is this expected? deviation from ios i think.
    public String getEventState(int componentID) {

        switch (componentID) {
            case MENU_OPTION_HOME:
                return HOME_FRAGMENT;
            case MENU_OPTION_SETTINGS:
                return HOME_SETTINGS;
            case MENU_OPTION_SHOP:
                return HOME_IAP;
            case MENU_OPTION_SUPPORT:
                return HOME_SUPPORT;
            case MENU_OPTION_ABOUT:
                return HOME_ABOUT;
            case Constants.UI_SHOPPING_CART_BUTTON_CLICK:
                return SHOPPING_CART;
            case MENU_OPTION_PR:
                return SUPPORT_PR;
            case MENU_OPTION_DATA_SYNC:
                return DATA_SYNC;
            default:
                return HOME_FRAGMENT;
        }
    }
}
