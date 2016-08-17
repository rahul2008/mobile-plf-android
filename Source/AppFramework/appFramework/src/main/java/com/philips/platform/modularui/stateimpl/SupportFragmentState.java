/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.MainMenuListener;
import com.philips.cdp.digitalcare.productdetails.ProductMenuListener;
import com.philips.cdp.digitalcare.social.SocialProviderListener;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.launchertype.FragmentLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;

import java.util.ArrayList;
import java.util.Arrays;

public class SupportFragmentState extends UIState implements MainMenuListener,
        ProductMenuListener, SocialProviderListener {
    private ArrayList<String> mCtnList = null;
    private FragmentActivity mFragmentActivity = null;
    private Context mContext;
    private ActionbarUpdateListener actionBarClickListenerCC;
    private int containerID;

    public SupportFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        mFragmentActivity = (HomeActivity) context;
        mContext = context;
        actionBarClickListenerCC = (HomeActivity) context;
        if(context instanceof HomeActivity){
            containerID = R.id.frame_container;
        }
        loadPlugIn();
        runCoCo();
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity) context).popBackTillHomeFragment();
    }

    public interface SetStateCallBack {
        void setNextState(Context contexts);
    }

    SetStateCallBack setStateCallBack;

    public void registerForNextState(SetStateCallBack setStateCallBack) {
        this.setStateCallBack = (SetStateCallBack) getPresenter();
    }

    public void loadPlugIn() {
        if (mCtnList == null) {
            mCtnList = new ArrayList<String>(Arrays.asList(mFragmentActivity.getResources().getStringArray(R.array.productselection_ctnlist)));
        }
        String[] ctnList = new String[mCtnList.size()];
        for (int i = 0; i < mCtnList.size(); i++) {
            ctnList[i] = mCtnList.get(i);
        }
        ProductModelSelectionType productsSelection = new com.philips.cdp.productselection
                .productselectiontype.HardcodedProductList(ctnList);
        productsSelection.setCatalog(Catalog.CARE);
        productsSelection.setSector(Sector.B2C);
        DigitalCareConfigManager.getInstance().initializeDigitalCareLibrary(
                mFragmentActivity);
        DigitalCareConfigManager.getInstance().registerMainMenuListener(this);
        DigitalCareConfigManager.getInstance()
                .registerProductMenuListener(this);
        DigitalCareConfigManager.getInstance().registerSocialProviderListener(this);
        DigiCareLogger.enableLogging();
    }


    public void runCoCo() {
        if (mCtnList == null) {
            mCtnList = new ArrayList<>(Arrays.asList(mContext.getResources().getStringArray(R.array.productselection_ctnlist)));
        }
        if (mCtnList != null) {
            String[] ctnList = new String[mCtnList.size()];
            for (int i = 0; i < mCtnList.size(); i++) {
                ctnList[i] = mCtnList.get(i);
            }
            ProductModelSelectionType productsSelection = new com.philips.cdp.productselection
                    .productselectiontype.HardcodedProductList(ctnList);
            productsSelection.setCatalog(Catalog.CARE);
            productsSelection.setSector(Sector.B2C);

            FragmentLauncher fragLauncher = new FragmentLauncher(mFragmentActivity, containerID, actionBarClickListenerCC);
            fragLauncher.setAnimation(0, 0);
            DigitalCareConfigManager.getInstance().invokeDigitalCare(fragLauncher, productsSelection);
        }
    }

    public void unloadCoCo() {
        DigitalCareConfigManager.getInstance().unregisterMainMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterProductMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterSocialProviderListener(this);
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
}
