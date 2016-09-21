/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.util.UIConstants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;

public class IAPState extends UIState{

    private Context activityContext;
    private ArrayList<String> mCtnList = null;
    public IAPState(@UIStateDef int stateID) {
        super(stateID);
    }
    private int iapFlowType;
    private FragmentLauncher fragmentLauncher;

    @Override
    public void init(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
    }

    // TODO : Deepthi - M -swap the params for init and navigate further we may hv to inject dependencies and settings
    @Override
    public void navigate(Context context) {
        activityContext = context;
        // can use switch
        iapFlowType = ((InAppStateData)getUiStateData()).getIapFlow();
        if(iapFlowType == UIConstants.IAP_CATALOG_VIEW){
            iapFlowType = IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW;
        }else if(iapFlowType == UIConstants.IAP_PURCHASE_HISTORY_VIEW){
            iapFlowType = IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW;
        }else if(iapFlowType == UIConstants.IAP_SHOPPING_CART_VIEW){
            iapFlowType = IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW;
        }
        // get directly from data model
        if (mCtnList == null) {
            mCtnList = ((InAppStateData)getUiStateData()).getCtnList();
        }
        launchIAP();
    }

    private void launchIAP() {
        IAPInterface iapInterface = ((AppFrameworkApplication) activityContext.getApplicationContext()).getIapInterface();
        IAPFlowInput iapFlowInput = new IAPFlowInput(mCtnList);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(iapFlowType, iapFlowInput);
        iapLaunchInput.setIapListener((IAPListener) fragmentLauncher.getFragmentActivity());
        try {
            iapInterface.launch(fragmentLauncher, iapLaunchInput);

        } catch (RuntimeException e) {
            //TODO: Deepthi - M -  not to show toast msg from exception, we need to defined string messages for all errors
            Toast.makeText(activityContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }

    /**
     * Data Model for CoCo is defined here to have minimal import files.
     */
    public class InAppStateData extends UIStateData {
        private int iapFlow;
        private ArrayList<String> mCtnList = null;

        public ArrayList<String> getCtnList() {
            return mCtnList;
        }

        public void setCtnList(ArrayList<String> mCtnList) {
            this.mCtnList = mCtnList;
        }

        public int getIapFlow() {
            return iapFlow;
        }

        public void setIapFlow(int iapFlow) {
            this.iapFlow = iapFlow;
        }

    }
}
