/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.flowmanager;

import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.utility.Constants;
import com.philips.platform.modularui.statecontroller.BaseState;
import com.philips.platform.modularui.statecontroller.FragmentView;
import com.philips.platform.modularui.statecontroller.UIBasePresenter;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.statecontroller.UIStateListener;
import com.philips.platform.modularui.stateimpl.AboutScreenState;
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
public class HomeTabbedActivityPresenter extends UIBasePresenter implements UIStateListener {

    private final int MENU_OPTION_HOME = 0;
    private final int MENU_OPTION_SUPPORT = 1;
    private final int MENU_OPTION_SETTINGS = 2;
    private final int MENU_OPTION_ABOUT = 3;

    private FragmentView fragmentView;
    private AppFrameworkApplication appFrameworkApplication;
    private FragmentLauncher fragmentLauncher;
    private BaseState baseState;

    public HomeTabbedActivityPresenter(final FragmentView fragmentView) {
        super(fragmentView);
        this.fragmentView = fragmentView;
        setState(BaseState.UI_HOME_TABBED_STATE);
    }

    /**
     * This methods handles all click events done on hamburger menu
     * Any changes for hamburger menu options should be made here
     */
    @Override
    public void onClick(int componentID) {
        appFrameworkApplication = (AppFrameworkApplication) fragmentView.getFragmentActivity().getApplicationContext();
        switch (componentID) {
            case MENU_OPTION_HOME:
                baseState = new HomeFragmentState();
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                baseState.setUiStateData(homeStateData);
                fragmentLauncher = new FragmentLauncher(fragmentView.getFragmentActivity(), fragmentView.getContainerId(), fragmentView.getActionBarListener());
                break;
            case MENU_OPTION_SETTINGS:
                baseState = new SettingsFragmentState();
                UIStateData settingsStateData = new UIStateData();
                settingsStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                baseState.setUiStateData(settingsStateData);
                fragmentLauncher = new FragmentLauncher(fragmentView.getFragmentActivity(), fragmentView.getContainerId(), fragmentView.getActionBarListener());
                break;
            case MENU_OPTION_SUPPORT:
                baseState = new SupportFragmentState();
                fragmentLauncher = new FragmentLauncher(fragmentView.getFragmentActivity(), fragmentView.getContainerId(), fragmentView.getActionBarListener());
                SupportFragmentState.ConsumerCareData supportStateData = new SupportFragmentState().new ConsumerCareData();
                supportStateData.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.productselection_ctnlist))));
                supportStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                baseState.setUiStateData(supportStateData);
                break;
            case MENU_OPTION_ABOUT:
                baseState = new AboutScreenState();
                UIStateData aboutStateData = new UIStateData();
                aboutStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                baseState.setUiStateData(aboutStateData);
                fragmentLauncher = new FragmentLauncher(fragmentView.getFragmentActivity(), fragmentView.getContainerId(), fragmentView.getActionBarListener());
                break;
            case Constants.UI_SHOPPING_CART_BUTTON_CLICK:
                baseState = new IAPState();
                fragmentLauncher = new FragmentLauncher(fragmentView.getFragmentActivity(), fragmentView.getContainerId(), fragmentView.getActionBarListener());
                IAPState.InAppStateData uiStateDataModel = new IAPState().new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_SHOPPING_CART_VIEW);
                uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.iap_productselection_ctnlist))));
                baseState.setUiStateData(uiStateDataModel);
                break;
            default:
                baseState = new HomeFragmentState();
        }
        baseState.setPresenter(this);
        if (baseState instanceof SupportFragmentState) {
            ((SupportFragmentState) baseState).registerUIStateListener(this);
        }
        appFrameworkApplication.getFlowManager().navigateToState(baseState, fragmentLauncher);
    }

    @Override
    public void onLoad() {

    }

    @Override
    public void onStateComplete(BaseState baseState) {
        appFrameworkApplication = (AppFrameworkApplication) fragmentView.getFragmentActivity().getApplicationContext();
        this.baseState = new ProductRegistrationState();
        fragmentLauncher = new FragmentLauncher(fragmentView.getFragmentActivity(), fragmentView.getContainerId(), fragmentView.getActionBarListener());
        ProductRegistrationState.ProductRegistrationData uiStateDataModel = new ProductRegistrationState().new ProductRegistrationData();
        uiStateDataModel.setCtnList(new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.productselection_ctnlist))));
        this.baseState.setUiStateData(uiStateDataModel);
        this.baseState.setPresenter(this);
        appFrameworkApplication.getFlowManager().navigateToState(this.baseState, fragmentLauncher);
    }
}
