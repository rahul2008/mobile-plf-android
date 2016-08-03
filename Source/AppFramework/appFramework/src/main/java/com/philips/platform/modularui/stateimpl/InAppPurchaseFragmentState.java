package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.debugtest.DebugTestFragment;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appframework.inapppurchase.InAppPurchasesFragment;
import com.philips.platform.modularui.cocointerface.UICoCoInterface;
import com.philips.platform.modularui.factorymanager.CoCoFactory;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.util.UIConstants;

/**
 * Created by 310173741 on 1 Aug 2016.
 */
public class InAppPurchaseFragmentState extends UIState {
    private UICoCoInterface uiCoCoInAppPurchase;

    public InAppPurchaseFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
//        uiCoCoInAppPurchase = CoCoFactory.getInstance().getCoCo(UIConstants.UI_COCO_IN_APP_PURCHASE);
//        uiCoCoInAppPurchase.setActionbar((HomeActivity)context);
//        uiCoCoInAppPurchase.setFragActivity((HomeActivity)context);
//        uiCoCoInAppPurchase.loadPlugIn(context);
//        uiCoCoInAppPurchase.runCoCo(context);

        InAppPurchasesFragment iap = new InAppPurchasesFragment();
        ((AppFrameworkBaseActivity)context).showFragment( iap, iap.getClass().getSimpleName());
    }
}
