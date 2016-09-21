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
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;

public class SupportFragmentState extends UIState implements CcListener {
    private ArrayList<String> mCtnList = null;
    private Context mContext;
    private CcSettings ccSettings;
    private CcLaunchInput ccLaunchInput;
    private FragmentLauncher fragmentLauncher;

    public SupportFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }


    @Override
    public void init(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
    }

    @Override
    public void navigate(Context context) {
        mContext = context;
        DigitalCareConfigManager.getInstance().registerCcListener(this);
        runCC();
    }

    void runCC()
    {
        if (mCtnList == null) {
            mCtnList = ((ConsumerCareData)getUiStateData()).getCtnList();
        }
        String[] ctnList = new String[mCtnList.size()];
        for (int i = 0; i < mCtnList.size(); i++) {
            ctnList[i] = mCtnList.get(i);
        }
        ProductModelSelectionType productsSelection = new com.philips.cdp.productselection.productselectiontype.HardcodedProductList(ctnList);
        productsSelection.setCatalog(Catalog.CARE);
        productsSelection.setSector(Sector.B2C);
        CcInterface ccInterface = new CcInterface();

        if (ccSettings == null) ccSettings = new CcSettings(mContext);
        if (ccLaunchInput == null) ccLaunchInput = new CcLaunchInput();
        ccLaunchInput.setProductModelSelectionType(productsSelection);
        ccLaunchInput.setConsumerCareListener(this);
        CcDependencies ccDependencies = new CcDependencies(AppFrameworkApplication.gAppInfra);

        ccInterface.init(ccDependencies, ccSettings);
        ccInterface.launch(fragmentLauncher, ccLaunchInput);
    }
    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity) context).popBackTillHomeFragment();
    }

    @Override
    public void init(Context context) {

    }

    public interface SetStateCallBack {
        void setNextState(Context contexts);
    }

    SetStateCallBack setStateCallBack;

    public void registerForNextState(SetStateCallBack setStateCallBack) {
        this.setStateCallBack = (SetStateCallBack) getPresenter();
    }

    public void unloadCoCo() {
        DigitalCareConfigManager.getInstance().unRegisterCcListener(this);

    }

    @Override
    public boolean onMainMenuItemClicked(String s) {
        if (s.equalsIgnoreCase("product_registration")) {
            setStateCallBack.setNextState(mContext);
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
        private ArrayList<String> mCtnList = null;

        public ArrayList<String> getCtnList() {
            return mCtnList;
        }

        public void setCtnList(ArrayList<String> mCtnList) {
            this.mCtnList = mCtnList;
        }
    }
}
