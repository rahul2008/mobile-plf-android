/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.CcDependencies;
import com.philips.cdp.digitalcare.CcInterface;
import com.philips.cdp.digitalcare.CcLaunchInput;
import com.philips.cdp.digitalcare.CcSettings;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.CcListener;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.ArrayList;
import java.util.Arrays;

public class SupportFragmentState extends UIState implements CcListener {
    private ArrayList<String> mCtnList = null;
    private FragmentActivity mFragmentActivity = null;
    private Context mContext;
    private int containerID;
    private CcSettings ccSettings;
    private CcLaunchInput ccLaunchInput;
    private ActionBarListener actionBarListener;

    public SupportFragmentState(@UIStateDef int stateID) {
        super(stateID);
    }

    @Override
    public void navigate(Context context) {
        mFragmentActivity = (HomeActivity) context;
        mContext = context;
        actionBarListener  = (HomeActivity) context;
        if(context instanceof HomeActivity){
            containerID = R.id.frame_container;
        }
        DigitalCareConfigManager.getInstance().registerCcListener(this);
        runCC();
    }

    void runCC()
    {
        if (mCtnList == null) {
            mCtnList = new ArrayList<String>(Arrays.asList(mFragmentActivity.getResources().getStringArray(R.array.productselection_ctnlist)));
        }
        String[] ctnList = new String[mCtnList.size()];
        for (int i = 0; i < mCtnList.size(); i++) {
            ctnList[i] = mCtnList.get(i);
        }
        ProductModelSelectionType productsSelection = new com.philips.cdp.productselection.productselectiontype.HardcodedProductList(ctnList);
        productsSelection.setCatalog(Catalog.CARE);
        productsSelection.setSector(Sector.B2C);
        com.philips.platform.uappframework.launcher.FragmentLauncher launcher =
                new com.philips.platform.uappframework.launcher.FragmentLauncher
                        (mFragmentActivity, containerID,actionBarListener);
       // launcher.setCustomAnimation(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
        CcInterface ccInterface = new CcInterface();

        if (ccSettings == null) ccSettings = new CcSettings(mContext);
        if (ccLaunchInput == null) ccLaunchInput = new CcLaunchInput();
        ccLaunchInput.setProductModelSelectionType(productsSelection);
        ccLaunchInput.setConsumerCareListener(this);
        CcDependencies ccDependencies = new CcDependencies(AppInfraSingleton.getInstance());

        ccInterface.init(ccDependencies, ccSettings);
        ccInterface.launch(launcher, ccLaunchInput);
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



    /*public void loadPlugIn() {
        if (mCtnList == null) {
            mCtnList = new ArrayList<String>(Arrays.asList(mFragmentActivity.getResources().getStringArray(R.array.productselection_ctnlist)));
        }
        String[] ctnList = new String[mCtnList.size()];
        for (int i = 0; i < mCtnList.size(); i++) {
            ctnList[i] = mCtnList.get(i);
        }
        *//*ProductModelSelectionType productsSelection = new com.philips.cdp.productselection
                .productselectiontype.HardcodedProductList(ctnList);
        productsSelection.setCatalog(Catalog.CARE);
        productsSelection.setSector(Sector.B2C);
       AppInfraInterface gAppInfra = AppInfraSingleton.getInstance();
        DigitalCareConfigManager.getInstance().initializeDigitalCareLibrary(mContext, gAppInfra);
        DigitalCareConfigManager.getInstance().registerCcListener(this);*//*
        *//*DigitalCareConfigManager.getInstance()
                .registerProductMenuListener(this);
        DigitalCareConfigManager.getInstance().registerSocialProviderListener(this);*//*
        //DigiCareLogger.();
        //TODO
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
    }*/

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
}
