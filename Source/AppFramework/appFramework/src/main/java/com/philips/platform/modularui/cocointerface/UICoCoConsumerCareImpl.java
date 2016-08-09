/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.cocointerface;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.MainMenuListener;
import com.philips.cdp.digitalcare.productdetails.ProductMenuListener;
import com.philips.cdp.digitalcare.social.SocialProviderListener;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.launchertype.FragmentLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class UICoCoConsumerCareImpl implements UICoCoInterface, MainMenuListener,
        ProductMenuListener, SocialProviderListener {

    private UICoCoConsumerCareImpl(){

    }

    private static UICoCoConsumerCareImpl instance = new UICoCoConsumerCareImpl();

    public static UICoCoConsumerCareImpl getInstance(){
        if(null == instance){
            instance = new UICoCoConsumerCareImpl();
        }
        return instance;
    }

    private ActionbarUpdateListener c = null;
    private ArrayList<String> mCtnList = null;
    private FragmentActivity mFragmentActivity = null;
    private ActionbarUpdateListener mActionBarClickListener = null;
    private ActionbarUpdateListener actionBarClickListener;
    private Context mContext;

    public interface SetStateCallBack{
        void setNextState(Context contexts);
    }
    SetStateCallBack setStateCallBack;

    public void registerForNextState(SetStateCallBack setStateCallBack){
        this.setStateCallBack = setStateCallBack;
    }
    @Override
    public void setActionbar(ActionbarUpdateListener acbl) {
        actionBarClickListener = acbl;
        mActionBarClickListener = actionBarClickListener;
    }
    @Override
    public void setFragActivity(FragmentActivity fragmentActivity) {

        mFragmentActivity = fragmentActivity;
    }

    @Override
    public void loadPlugIn(Context context) {
        mContext = context;

        if (mCtnList == null) {
            mCtnList = new ArrayList<String>(Arrays.asList(mFragmentActivity.getResources().getStringArray(R.array.productselection_ctnlist)));
        }

//        if (mCtnList != null) {
        String[] ctnList = new String[mCtnList.size()];
        for (int i = 0; i < mCtnList.size(); i++) {
            ctnList[i] = mCtnList.get(i);
        }

        ProductModelSelectionType productsSelection = new com.philips.cdp.productselection
                .productselectiontype.HardcodedProductList(ctnList);
        productsSelection.setCatalog(Catalog.CARE);
        productsSelection.setSector(Sector.B2C);


        PILLocaleManager localeManager = new PILLocaleManager(mFragmentActivity);
        localeManager.setInputLocale("en", "GB");

//        DigitalCareConfigManager.getInstance().setAppTaggingInputs(true, "App_ID_101", "AppName", "CurrentPageName");

        // Initializing DigitalCare Component.
        DigitalCareConfigManager.getInstance().initializeDigitalCareLibrary(
                mFragmentActivity);

        // Set DigitalCareLibrary Listeners
        DigitalCareConfigManager.getInstance().registerMainMenuListener(this);
        DigitalCareConfigManager.getInstance()
                .registerProductMenuListener(this);
        DigitalCareConfigManager.getInstance().registerSocialProviderListener(this);

        DigiCareLogger.enableLogging();


    }


    @Override
    public void runCoCo(Context context) {
        if (mCtnList == null) {
            mCtnList = new ArrayList<String>(Arrays.asList(mContext.getResources().getStringArray(R.array.productselection_ctnlist)));
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

            FragmentLauncher fragLauncher = new FragmentLauncher(mFragmentActivity, R.id.frame_container, actionBarClickListener);
            fragLauncher.setAnimation(0, 0);
            if(context instanceof HomeActivity){
                //((HomeActivity)context).getSupportFragmentManager().get
            }
            DigitalCareConfigManager.getInstance().invokeDigitalCare(fragLauncher, productsSelection);
        }
    }

    @Override
    public void unloadCoCo() {
        DigitalCareConfigManager.getInstance().unregisterMainMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterProductMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterSocialProviderListener(this);
    }


    @Override
    public boolean onMainMenuItemClicked(String s) {
        if(s.equalsIgnoreCase("product_registration")){
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
