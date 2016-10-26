/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.HamburgerAppState;
import com.philips.platform.appframework.utility.Constants;
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
public class HomeActivityPresenter extends UIBasePresenter implements UIStateListener {

    public static final int MENU_OPTION_HOME = 0;
    private final int PRODUCT_REGISTRATION = 6;
    private final String HOME_FRAGMENT = "home_fragment";
    private FragmentView fragmentView;
    private AppFrameworkApplication appFrameworkApplication;
    private FragmentLauncher fragmentLauncher;
    private BaseState baseState;
    private String HOME_SETTINGS = "settings";
    private String HOME_IAP = "iap";
    private String HOME_SUPPORT = "support";
    private String HOME_ABOUT = "about";
    private String SUPPORT_PR = "pr";

    public HomeActivityPresenter(final FragmentView fragmentView) {
        super(fragmentView);
        this.fragmentView = fragmentView;
        setState(BaseState.UI_HOME_STATE);
    }

    /**
     * This methods handles all click events done on hamburger menu
     * Any changes for hamburger menu options should be made here
     */
    @Override
    public void onClick(int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) fragmentView.getFragmentActivity().getApplicationContext();
        String eventState = getEventState(componentID);
        baseState = appFrameworkApplication.getTargetFlowManager().getNextState(HamburgerAppState.HOME, eventState);
        baseState.setPresenter(this);
        baseState.setUiStateData(setStateData(componentID));
        fragmentLauncher = getFragmentLauncher();
        if (baseState instanceof SupportFragmentState) {
            ((SupportFragmentState) baseState).registerUIStateListener(this);
        }
        baseState.navigate(fragmentLauncher);
    }

    // TODO: Deepthi, when we already know that we need to set certain data for each menu click and that too for which component, why do we ask flow manager to give only state?
    // So here atleast i see launch type is already causing problems + data models was anyways a concern.
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

    // TODO: Deepthi, This seems to be hardcoded without even checking the uistate, we are taking decision here.
    // cant we move this to json ?
    @Override
    public void onStateComplete(BaseState baseState) {
        appFrameworkApplication = (AppFrameworkApplication) fragmentView.getFragmentActivity().getApplicationContext();
        this.baseState = appFrameworkApplication.getTargetFlowManager().getNextState(HamburgerAppState.SUPPORT, SUPPORT_PR);
        ProductRegistrationState.ProductRegistrationData uiStateDataModel = new ProductRegistrationState().new ProductRegistrationData();
        uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.productselection_ctnlist))));
        this.baseState.setUiStateData(uiStateDataModel);
        this.baseState.setPresenter(this);
        this.baseState.navigate(fragmentLauncher);
    }

    // TODO: Deepthi, is this expected? deviation from ios i think.
    public String getEventState(int componentID) {
        final int MENU_OPTION_SETTINGS = 1;
        final int MENU_OPTION_SHOP = 2;
        final int MENU_OPTION_SUPPORT = 3;
        final int MENU_OPTION_ABOUT = 4;
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
                return HOME_SUPPORT;
            case PRODUCT_REGISTRATION:
                return SUPPORT_PR;
            default:
                return HOME_FRAGMENT;
        }
    }
}
