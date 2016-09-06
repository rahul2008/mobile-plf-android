/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * To launch In app purchase History
 */

public class InAppPurchaseHistoryFragmentState extends UIState {

    private Context mContext;
    private FragmentActivity fragmentActivity;
    private int containerId;
    private ArrayList<String> mCtnList;
    {
        mCtnList = null;
    }

    private ActionBarListener actionBarListener;

    public InAppPurchaseHistoryFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        mContext = context;
        if(context instanceof HomeActivity) {
            fragmentActivity = (HomeActivity) context;
            containerId = R.id.frame_container;
            actionBarListener = (HomeActivity)context;
        }
        if (mCtnList == null) {
            mCtnList = new ArrayList<String>(Arrays.asList(fragmentActivity.getResources().getStringArray(R.array.productselection_ctnlist)));
        }
        launchIAP();
    }

    private void launchIAP() {
        IAPInterface iapInterface = ((AppFrameworkApplication)mContext.getApplicationContext()).getIapInterface();;
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        IAPFlowInput iapFlowInput = new IAPFlowInput(mCtnList);
        iapLaunchInput.setIAPFlow(IAPLaunchInput.IAPFlows.IAP_PURCHASE_HISTORY_VIEW, iapFlowInput);
        FragmentLauncher fragLauncher = new FragmentLauncher(fragmentActivity, containerId, actionBarListener);
        try {
            iapInterface.launch(fragLauncher, iapLaunchInput);
        } catch (RuntimeException e) {
            showIapToast(IAPConstant.IAP_ERROR_NO_CONNECTION);
        }
    }

    private void showIapToast(int errorCode) {

        if (null != mContext) {
            Toast toast = Toast.makeText(mContext, "Error code "+ errorCode   , Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }


    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBack();
    }
}
