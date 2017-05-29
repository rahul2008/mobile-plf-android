/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.inapppurchase;

import android.content.Context;
import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPDependencies;
import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.integration.IAPListener;
import com.philips.cdp.di.iap.integration.IAPSettings;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.AppStates;
import com.philips.platform.appframework.flowmanager.base.BaseState;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.base.AppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;

/**
 * This class contains all initialization & Launching details of IAP
 */
public abstract class IAPState extends BaseState {
    public static final String TAG =  IAPState.class.getSimpleName();

    /**
     IAP flow constants, values for IAP views should start from 4000 series
     */
    public static final int IAP_CATALOG_VIEW = 4001;
    protected int launchType;
    protected ArrayList<String> ctnList = null;
    // public static final int IAP_PURCHASE_HISTORY_VIEW = 4002;
    // public static final int IAP_SHOPPING_CART_VIEW = 4003;
    private Context activityContext;
    private Context applicationContext;
    private IAPInterface iapInterface;
    private FragmentLauncher fragmentLauncher;

    public IAPState() {
        super(AppStates.IAP);
    }

    public IAPInterface getIapInterface() {
        return iapInterface;
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d(TAG," navigate ");
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        activityContext = fragmentLauncher.getFragmentActivity();
        ((AppFrameworkBaseActivity)activityContext).handleFragmentBackStack(null,null,getUiStateData().getFragmentLaunchState());
        updateDataModel();
        launchIAP();
    }

    private int getIAPFlowType(int iapFlowType){
        switch (iapFlowType){
            case IAPState.IAP_CATALOG_VIEW:return IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW;
            // case IAPState.IAP_PURCHASE_HISTORY_VIEW:return IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW;
            // case IAPState.IAP_SHOPPING_CART_VIEW:return IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW;
            default:return IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW;
        }
    }


    public ArrayList<String> getCtnList() {
        return ctnList;
    }

    public void setCtnList(ArrayList<String> ctnList) {
        this.ctnList = ctnList;
    }

    public int getLaunchType() {
        return launchType;
    }

    public void setLaunchType(int launchType) {
        this.launchType = getIAPFlowType(launchType);
    }

    private void launchIAP() {
        RALog.d(TAG," launchIAP ");
        IAPInterface iapInterface = getApplicationContext().getIap().getIapInterface();
        IAPFlowInput iapFlowInput = new IAPFlowInput(getCtnList());
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(getLaunchType(), iapFlowInput);
        iapLaunchInput.setIapListener((IAPListener) fragmentLauncher.getFragmentActivity());
        try {
            iapInterface.launch(fragmentLauncher, iapLaunchInput);

        } catch (RuntimeException e) {
            //TODO: Deepthi - M -  not to show toast msg from exception, we need to defined string messages for all errors - (Had sent mail to Thiru long time ago. NO response. Will send another one to Bopanna)
            RALog.e(TAG,e.getMessage());

            Toast.makeText(activityContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected AppFrameworkApplication getApplicationContext() {
        return (AppFrameworkApplication) activityContext.getApplicationContext();
    }

    @Override
    public void init(Context context) {
        RALog.d(TAG," init IAP ");
        applicationContext = context;
        iapInterface = new IAPInterface();
        IAPSettings iapSettings = new IAPSettings(applicationContext);
        IAPDependencies iapDependencies = new IAPDependencies(((AppFrameworkApplication)applicationContext).getAppInfra());
        iapSettings.setUseLocalData(true);
        iapInterface.init(iapDependencies, iapSettings);
    }


}
