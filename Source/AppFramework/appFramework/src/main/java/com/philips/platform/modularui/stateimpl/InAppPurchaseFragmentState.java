/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.session.IAPListener;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.util.UIConstants;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.ArrayList;
import java.util.Arrays;

public class InAppPurchaseFragmentState extends UIState{

    Context mContext;
    private FragmentActivity fragmentActivity;
    private int containerID;
    private ArrayList<String> mCtnList = null;
    private ActionBarListener actionBarListener;
    public InAppPurchaseFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }
    private IAPListener iapListener;
    int iapFlowType;

    @Override
    public void navigate(Context context) {
        mContext = context;
        /*if(context instanceof HomeActivity) {
            fragmentActivity = (HomeActivity) context;
            containerID = R.id.frame_container;
            actionBarListener = (HomeActivity)context;
            iapListener = (HomeActivity)context;
        }*/

        fragmentActivity = getUiStateData().getFragmentActivity();
        containerID = getUiStateData().getContainerID();
        actionBarListener = getUiStateData().getActionBarListner();
        iapListener = ((InAppStateData)getUiStateData()).getIAPListener();
        iapFlowType = ((InAppStateData)getUiStateData()).getIapFlow();
        if(iapFlowType == UIConstants.IAP_CATALOG_VIEW){
            iapFlowType = IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW;
        }else if(iapFlowType == UIConstants.IAP_PURCHASE_HISTORY_VIEW){
            iapFlowType = IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW;
        }else if(iapFlowType == UIConstants.IAP_SHOPPING_CART_VIEW){
            iapFlowType = IAPLaunchInput.IAPFlows.IAP_SHOPPING_CART_VIEW;
        }
        if (mCtnList == null) {
            mCtnList = new ArrayList<>(Arrays.asList(fragmentActivity.getResources().getStringArray(R.array.iap_productselection_ctnlist)));
        }
        launchIAP();
    }

    private void launchIAP() {
        IAPInterface iapInterface = ((AppFrameworkApplication)mContext.getApplicationContext()).getIapInterface();
        IAPFlowInput iapFlowInput = new IAPFlowInput(mCtnList);
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(/*IAPLaunchInput.IAPFlows.IAP_PRODUCT_CATALOG_VIEW*/iapFlowType, iapFlowInput);
        iapLaunchInput.setIapListener(iapListener);
        FragmentLauncher fragLauncher = new FragmentLauncher(fragmentActivity, containerID,actionBarListener);
        try {
            iapInterface.launch(fragLauncher, iapLaunchInput);

        } catch (RuntimeException e) {
            Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }
    public class InAppStateData extends UIStateData {
        private IAPListener mIAPListener;
        private int iapFlow;

        public IAPListener getIAPListener() {
            return mIAPListener;
        }

        public void setIAPListener(IAPListener mIAPListener) {
            this.mIAPListener = mIAPListener;
        }

        public int getIapFlow() {
            return iapFlow;
        }

        public void setIapFlow(int iapFlow) {
            this.iapFlow = iapFlow;
        }

    }
}
