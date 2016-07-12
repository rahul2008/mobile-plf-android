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
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.consumercare.ConsumerCareLauncher;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by 310213373 on 7/4/2016.
 */

public class UICoCoConsumerCareImpl implements UICoCoInterface, MainMenuListener,
        ProductMenuListener, SocialProviderListener {
    private ActionbarUpdateListener c = null;
    private ArrayList<String> mCtnList = null;
    private FragmentActivity mFragmentActivity = null;
    private ActionbarUpdateListener mActionBarClickListener = null;
    private ActionbarUpdateListener actionBarClickListener;
    private ConsumerCareLauncher mConsumerCareFragment = null;

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

        DigitalCareConfigManager.getInstance().setAppTaggingInputs(true, "App_ID_101", "AppName", "CurrentPageName");

        // Initializing DigitalCare Component.
        DigitalCareConfigManager.getInstance().initializeDigitalCareLibrary(
                mFragmentActivity);

        // Set DigitalCareLibrary Listeners
        DigitalCareConfigManager.getInstance().registerMainMenuListener(this);
        DigitalCareConfigManager.getInstance()
                .registerProductMenuListener(this);
        DigitalCareConfigManager.getInstance().registerSocialProviderListener(this);

        DigiCareLogger.enableLogging();
        mConsumerCareFragment = new ConsumerCareLauncher();

    }


    @Override
    public void runCoCo(Context context) {
        mConsumerCareFragment.initCC(mFragmentActivity, actionBarClickListener);
    }

    @Override
    public void unloadCoCo() {
        DigitalCareConfigManager.getInstance().unregisterMainMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterProductMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterSocialProviderListener(this);
    }


    @Override
    public boolean onMainMenuItemClicked(String s) {
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
