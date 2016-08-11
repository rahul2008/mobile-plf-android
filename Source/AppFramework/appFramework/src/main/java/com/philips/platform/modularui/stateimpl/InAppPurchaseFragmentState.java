/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.inapppurchase.InAppPurchasesFragment;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.statecontroller.UIState;

public class InAppPurchaseFragmentState extends UIState {
    private UICoCoInterface uiCoCoInAppPurchase;

    public InAppPurchaseFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        InAppPurchasesFragment iap = new InAppPurchasesFragment();
        ((AppFrameworkBaseActivity)context).showFragment( iap, iap.getClass().getSimpleName());
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }
}
