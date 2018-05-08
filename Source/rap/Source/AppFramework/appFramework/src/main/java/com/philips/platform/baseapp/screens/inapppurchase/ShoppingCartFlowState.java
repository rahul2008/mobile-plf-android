/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.baseapp.screens.inapppurchase;

import android.widget.Toast;

import com.philips.cdp.di.iap.integration.IAPFlowInput;
import com.philips.cdp.di.iap.integration.IAPInterface;
import com.philips.cdp.di.iap.integration.IAPLaunchInput;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.screens.settingscreen.IndexSelectionListener;
import com.philips.platform.baseapp.screens.utility.CTNUtil;
import com.philips.platform.baseapp.screens.utility.Constants;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;
import java.util.Arrays;

public class ShoppingCartFlowState extends IAPRetailerFlowState{

    @Override
    public void updateDataModel() {
        RALog.d(TAG, "updateDataModel called ");
        setLaunchType(IAPState.IAP_SHOPPING_CART_VIEW);
        try {
            setCtnList(new ArrayList<>(Arrays.asList(CTNUtil.getCtnForCountry(getApplicationContext().getAppInfra().getServiceDiscovery().getHomeCountry()))));
        } catch (RuntimeException e) {
            RALog.e(TAG,e.getMessage());
            Toast.makeText(getApplicationContext(), R.string.RA_CTN_Null, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void navigate(UiLauncher uiLauncher) {
        RALog.d(TAG, " navigate ");
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        activityContext = fragmentLauncher.getFragmentActivity();
        ((AbstractAppFrameworkBaseActivity) activityContext).handleFragmentBackStack(null, null, Constants.BACK_BUTTON_CLICK_CONSTANT);
        updateDataModel();
        launchIAP();
    }

    @Override
    public void launchIAP() {
        RALog.d(TAG," launchIAP ");
        IAPInterface iapInterface = getIapInterface();
        IAPFlowInput iapFlowInput = new IAPFlowInput(getCtnList());
        IAPLaunchInput iapLaunchInput = new IAPLaunchInput();
        iapLaunchInput.setIAPFlow(getLaunchType(), iapFlowInput);
        iapLaunchInput.setIapListener(this);
        try {
            ((AbstractAppFrameworkBaseActivity) activityContext).hideProgressBar();
            iapInterface.launch(fragmentLauncher, iapLaunchInput);
        } catch (RuntimeException e) {
            //TODO: Deepthi - M -  not to show toast msg from exception, we need to defined string messages for all errors - (Had sent mail to Thiru long time ago. NO response. Will send another one to Bopanna)
            RALog.e(TAG,e.getMessage());
            if (activityContext instanceof IndexSelectionListener) {
                ((IndexSelectionListener) activityContext).updateSelectionIndex(0);
            }
        }
    }
}
