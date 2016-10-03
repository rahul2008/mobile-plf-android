/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;

import com.philips.cdp.digitalcare.CcDependencies;
import com.philips.cdp.digitalcare.CcInterface;
import com.philips.cdp.digitalcare.CcLaunchInput;
import com.philips.cdp.digitalcare.CcSettings;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.CcListener;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.appframework.AppFrameworkApplication;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.modularui.statecontroller.UIStateListener;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;
/**
 * This class contains all initialization & Launching details of Consumer Care
 */
public class SupportFragmentState extends UIState implements CcListener {
    private Context activityContext;
    private CcSettings ccSettings;
    private CcLaunchInput ccLaunchInput;
    private FragmentLauncher fragmentLauncher;
    private UIStateListener supportListener;

    public SupportFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    /**
     * UIState overridden methods
     * @param uiLauncher requires the UiLauncher object
     */
    @Override
    public void navigate(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
        this.activityContext = fragmentLauncher.getFragmentActivity();
        DigitalCareConfigManager.getInstance().registerCcListener(this);
        ((AppFrameworkBaseActivity)activityContext).handleFragmentBackStack(null,null,getUiStateData().getFragmentLaunchState());
        launchCC();
    }

    @Override
    public void handleBack(final Context context) {
        ((AppFrameworkBaseActivity) context).popBackTillHomeFragment();
    }

    @Override
    public void init(Context context) {

    }

    void launchCC()
    {
        String[] ctnList = new String[((ConsumerCareData)getUiStateData()).getCtnList().size()];
        ctnList = ((ConsumerCareData)getUiStateData()).getCtnList().toArray(ctnList);

        ProductModelSelectionType productsSelection = new com.philips.cdp.productselection.productselectiontype.HardcodedProductList(ctnList);
        productsSelection.setCatalog(Catalog.CARE);
        productsSelection.setSector(Sector.B2C);
        CcInterface ccInterface = new CcInterface();

        if (ccSettings == null) ccSettings = new CcSettings(activityContext);
        if (ccLaunchInput == null) ccLaunchInput = new CcLaunchInput();
        ccLaunchInput.setProductModelSelectionType(productsSelection);
        ccLaunchInput.setConsumerCareListener(this);
        CcDependencies ccDependencies = new CcDependencies(AppFrameworkApplication.appInfra);

        ccInterface.init(ccDependencies, ccSettings);
        ccInterface.launch(fragmentLauncher, ccLaunchInput);
    }

    /**
     * Registering for UIStateListener callbacks
     * @param uiStateListener
     */
    public void registerUIStateListener(UIStateListener uiStateListener) {
        this.supportListener = (UIStateListener) getPresenter();
    }


    /**
     * CcListener interface implementation methods
     * @param s
     * @return
     */
    @Override
    public boolean onMainMenuItemClicked(String s) {
        if (s.equalsIgnoreCase("product_registration")) {
            supportListener.onStateComplete(new SupportFragmentState(UIState.UI_SUPPORT_FRAGMENT_STATE));
            return true;
        }
        return false;
    }

    @Override
    public boolean onProductMenuItemClicked(String s) {
        return false;
    }

    @Override
    public boolean onSocialProviderItemClicked(String s) {
        return false;
    }

    /**
     * Data Model for CoCo is defined here to have minimal import files.
     */
    public class ConsumerCareData extends UIStateData {
        private ArrayList<String> ctnList = null;

        public ArrayList<String> getCtnList() {
            return ctnList;
        }

        public void setCtnList(ArrayList<String> ctnList) {
            this.ctnList = ctnList;
        }
    }
}
