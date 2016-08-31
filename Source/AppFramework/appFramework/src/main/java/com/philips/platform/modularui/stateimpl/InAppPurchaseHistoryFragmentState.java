/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.Fragments.BaseAnimationSupportFragment;
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

public class InAppPurchaseHistoryFragmentState extends UIState {

    Context mContext;
    private FragmentActivity fragmentActivity;
    private int containerID;
    private ArrayList<String> mCtnList = null;
    private TextView mTitleTextView;
    private ImageView mBackImage;
    private ImageView mCartIcon;
    private TextView mCountText;
    private ActionBarListener actionBarListener;

    public InAppPurchaseHistoryFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        mContext = context;
        if(context instanceof HomeActivity) {
            fragmentActivity = (HomeActivity) context;
            containerID = R.id.frame_container;
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
        FragmentLauncher fragLauncher = new FragmentLauncher(fragmentActivity, containerID, new ActionBarListener() {
            @Override
            public void updateActionBar(@StringRes int i, boolean b) {

            }

            @Override
            public void updateActionBar(String s, boolean b) {

            }
        });
        try {
            iapInterface.launch(fragLauncher, iapLaunchInput);
        } catch (RuntimeException e) {
            showIAPToast(IAPConstant.IAP_ERROR_NO_CONNECTION);
        }
    }

    private void showIAPToast(int errorCode) {
        String errorText = mContext.getResources().getString(R.string.iap_unknown_error);
        if (IAPConstant.IAP_ERROR_NO_CONNECTION == errorCode) {
        } else if (IAPConstant.IAP_ERROR_CONNECTION_TIME_OUT == errorCode) {
        } else if (IAPConstant.IAP_ERROR_AUTHENTICATION_FAILURE == errorCode) {
        } else if (IAPConstant.IAP_ERROR_INSUFFICIENT_STOCK_ERROR == errorCode) {
        }

        if (null != mContext) {
            Toast toast = Toast.makeText(mContext, errorText, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
        }
    }

    public void addFragment(BaseAnimationSupportFragment newFragment,
                            String newFragmentTag) {
        newFragment.setActionBarListener(actionBarListener);
        FragmentTransaction transaction = fragmentActivity.getSupportFragmentManager().beginTransaction();
        transaction.replace(containerID, newFragment, newFragmentTag);
        transaction.addToBackStack(newFragmentTag);
        transaction.commitAllowingStateLoss();

    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBack();
    }
}
