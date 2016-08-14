/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.inapppurchase.InAppPurchasesHistoryFragment;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.statecontroller.UIState;

public class InAppPurchaseHistoryFragmentState extends UIState {
    private UICoCoInterface uiCoCoInAppPurchase;

    public InAppPurchaseHistoryFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        InAppPurchasesHistoryFragment iap = new InAppPurchasesHistoryFragment();
        ((AppFrameworkBaseActivity) context).showFragment(iap, InAppPurchasesHistoryFragment.TAG);
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity) context).popBackTillHomeFragment();
    }
}
