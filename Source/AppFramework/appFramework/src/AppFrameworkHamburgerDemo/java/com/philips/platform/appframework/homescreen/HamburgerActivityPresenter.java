/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.homescreen;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.appframework.flowmanager.base.UIStateData;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.FragmentView;
import com.philips.platform.baseapp.base.UIBasePresenter;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * This class id used for loading various fragments that are supported by home activity ,
 * based on user selection this class loads the next state of the application.
 */
public class HamburgerActivityPresenter extends UIBasePresenter {
    public static final String TAG = HamburgerActivityPresenter.class.getSimpleName();

    private FragmentView fragmentView;
    private FragmentLauncher fragmentLauncher;
    private BaseState baseState;

    public HamburgerActivityPresenter(final FragmentView fragmentView) {
        super(fragmentView);
        this.fragmentView = fragmentView;
        setState(AppStates.HAMBURGER_HOME);
    }

    /**
     * This methods handles all click events done on hamburger menu
     * Any changes for hamburger menu options should be made here
     */
    @Override
    public void onEvent(int componentID) {
        String eventState = getEventState(componentID);

        try {
            BaseFlowManager targetFlowManager = getApplicationContext().getTargetFlowManager();
            if(targetFlowManager == null){
                Toast.makeText(fragmentView.getFragmentActivity(), fragmentView.getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
                return;
            }
            baseState = targetFlowManager.getNextState(targetFlowManager.getState(AppStates.HAMBURGER_HOME), eventState);
            if (null != baseState) {
                baseState.setUiStateData(setStateData(baseState.getStateID()));
                fragmentLauncher = getFragmentLauncher();
                baseState.navigate(fragmentLauncher);
            }
        }  catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.d(TAG, e.getMessage());
            Toast.makeText(fragmentView.getFragmentActivity(), fragmentView.getFragmentActivity().getString(R.string.RA_something_wrong), Toast.LENGTH_SHORT).show();
        }
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) fragmentView.getFragmentActivity().getApplicationContext();
    }

    @NonNull
    protected ArrayList<String> getCtnList() {
        return new ArrayList<>(Arrays.asList(fragmentView.getFragmentActivity().getResources().getStringArray(R.array.iap_productselection_ctnlist)));
    }

    protected FragmentLauncher getFragmentLauncher() {
        fragmentLauncher = new FragmentLauncher(fragmentView.getFragmentActivity(), fragmentView.getContainerId(), fragmentView.getActionBarListener());
        return fragmentLauncher;
    }

    // TODO: Deepthi, is this expected? deviation from ios i think. - (Rakesh -As disscussed. Needed to convert int ID for views into Strings )
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
            // Commented as part of Plan A removal.
           /* case Constants.UI_SHOPPING_CART_BUTTON_CLICK:
                return SHOPPING_CART;*/
            case MENU_OPTION_PR:
                return SUPPORT_PR;
            case MENU_OPTION_DATA_SYNC:
                return HOME_DATA_SYNC;
            case MENU_OPTION_CONNECTIVITY:
                return CONNECTIVITY;
            case MENU_OPTION_COCOVERSION:
                return COCO_VERSION_INFO;
            case MENU_OPTION_TEST:
                return TESTMICROAPP;
            default:
                return HOME_FRAGMENT;
        }
    }

    protected UIStateData setStateData(final String componentID) {
        switch (componentID) {
            case AppStates.HOME_FRAGMENT:
                UIStateData homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                return homeStateData;
            case AppStates.SETTINGS:
                UIStateData settingsStateData = new UIStateData();
                settingsStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                return settingsStateData;
            case AppStates.IAP:
                UIStateData iapStateData = new UIStateData();
                iapStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                return iapStateData;
            case AppStates.SUPPORT:
                UIStateData supportStateData = new UIStateData();
                supportStateData.setFragmentLaunchType(Constants.CLEAR_TILL_HOME);
                return supportStateData;
            case AppStates.ABOUT:
                UIStateData aboutStateData = new UIStateData();
                aboutStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                return aboutStateData;
            // Commented as part of Plan A removal.
            /*case Constants.UI_SHOPPING_CART_BUTTON_CLICK:
                IAPState.InAppStateData uiStateDataModel = new IAPState().new InAppStateData();
                uiStateDataModel.setIapFlow(IAPState.IAP_SHOPPING_CART_VIEW);
                uiStateDataModel.setCtnList(getCtnList());
                return uiStateDataModel;*/
            case AppStates.PR:
                UIStateData prStateDataModel = new UIStateData();
                return prStateDataModel;
            case AppStates.DATA_SYNC:
                UIStateData syncStateData = new UIStateData();
                syncStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                return syncStateData;
            case AppStates.CONNECTIVITY:
                UIStateData connectivityStateData = new UIStateData();
                connectivityStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                return connectivityStateData;
            case AppStates.COCO_VERSION_INFO:
                UIStateData CocoVersioStateData = new UIStateData();
                CocoVersioStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                return CocoVersioStateData;
            case AppStates.TESTMICROAPP:
                UIStateData testStateData=new UIStateData();
                testStateData.setFragmentLaunchType(Constants.ADD_FROM_HAMBURGER);
                return testStateData;

            default:
                homeStateData = new UIStateData();
                homeStateData.setFragmentLaunchType(Constants.ADD_HOME_FRAGMENT);
                return homeStateData;
        }
    }
}
