/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.demoapplication;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSDependencies;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.uappinput.UappDependencies;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EWSDemoBaseActivity extends AppCompatActivity {

    protected static final String DEFAULT = "Default";
    public static final String SELECTED_CONFIG = "SelectedConfig";

    @NonNull
    protected UappDependencies createUappDependencies(AppInfraInterface appInfra, boolean isDefaultValueSelected) {
        return new EWSDependencies(appInfra, createProductMap(isDefaultValueSelected),
                new ContentConfiguration(createBaseContentConfiguration(isDefaultValueSelected),
                        createHappyFlowConfiguration(isDefaultValueSelected),
                        createTroubleShootingConfiguration(isDefaultValueSelected)));
    }

    @NonNull
    protected Map<String, String> createProductMap(boolean isDefaultValueSelected) {
        Map<String, String> productKeyMap = new HashMap<>();
        if(isDefaultValueSelected){
            productKeyMap.put(EWSInterface.PRODUCT_NAME, getString(R.string.ews_device_name_default));
        }else {
            productKeyMap.put(EWSInterface.PRODUCT_NAME, getString(R.string.ews_device_name));
        }
        return productKeyMap;
    }

    @NonNull
    private BaseContentConfiguration createBaseContentConfiguration(boolean isDefaultValueSelected){
        if (isDefaultValueSelected){
            return new BaseContentConfiguration();
        }else{
            return new BaseContentConfiguration(R.string.ews_device_name, R.string.ews_app_name);
        }
    }

    protected boolean isDefaultValueSelected(String selectedItem){
        return selectedItem.equals(DEFAULT);
    }

    @NonNull
    private HappyFlowContentConfiguration createHappyFlowConfiguration(boolean isDefaultValueSelected){
        if(isDefaultValueSelected){
            return new HappyFlowContentConfiguration.Builder().build();
        }else{
            return new HappyFlowContentConfiguration.Builder()
                    .setGettingStartedScreenTitle(R.string.label_ews_get_started_title)
                    .setSetUpScreenTitle(R.string.label_ews_plug_in_title)
                    .setSetUpScreenBody(R.string.label_ews_plug_in_body)
                    .setSetUpVerifyScreenTitle(R.string.label_ews_verify_ready_title)
                    .setSetUpVerifyScreenQuestion(R.string.label_ews_verify_ready_question)
                    .setSetUpVerifyScreenYesButton(R.string.button_ews_verify_ready_yes)
                    .setSetUpVerifyScreenNoButton(R.string.button_ews_verify_ready_no)
                    .build();
        }
    }

    protected void updateCurrentContent(String currentContent) {
        try {
            Locale locale = new Locale(currentContent);
            Locale.setDefault(locale);
            Resources res = getResources();
            Configuration config = new Configuration(res.getConfiguration());
            config.locale = locale;
            res.updateConfiguration(config, res.getDisplayMetrics());
        } catch (Exception e) {
            Log.e(EWSDemoActivity.class.getName(), e.toString());
        }
    }

    @NonNull
    private TroubleShootContentConfiguration createTroubleShootingConfiguration(boolean isDefaultValueSelected){
        if(isDefaultValueSelected){
            return new TroubleShootContentConfiguration.Builder().build();
        }else {
            return new TroubleShootContentConfiguration.Builder()
                    .setResetConnectionTitle(R.string.label_ews_support_reset_connection_title)
                    .setResetConnectionBody(R.string.label_ews_support_reset_connection_body)
                    .setResetConnectionImage(R.drawable.ic_ews_enable_ap_mode)

                    .setResetDeviceTitle(R.string.label_ews_support_reset_device_title)
                    .setResetDeviceBody(R.string.label_ews_support_reset_device_body)
                    .setResetDeviceImage(R.drawable.ic_ews_enable_ap_mode)

                    .setSetUpAccessPointTitle(R.string.label_ews_support_setup_access_point_title)
                    .setSetUpAccessPointBody(R.string.label_ews_support_setup_access_point_body)
                    .setSetUpAccessPointImage(R.drawable.ic_ews_enable_ap_mode)

                    .setConnectWrongPhoneTitle(R.string.label_ews_support_wrong_phone_title)
                    .setConnectWrongPhoneBody(R.string.label_ews_support_wrong_phone_body)
                    .setConnectWrongPhoneImage(R.drawable.ic_ews_enable_ap_mode)
                    .setConnectWrongPhoneQuestion(R.string.label_ews_support_wrong_phone_question)
                    .build();
        }
    }
}
