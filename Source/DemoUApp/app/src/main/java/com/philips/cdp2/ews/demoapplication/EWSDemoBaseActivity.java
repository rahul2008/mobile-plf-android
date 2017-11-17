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
    protected UappDependencies createUappDependencies(AppInfraInterface appInfra,
                                                    Map<String, String> productKeyMap, boolean isDefaultValueSelected) {
        return new EWSDependencies(appInfra, productKeyMap,
                new ContentConfiguration(createBaseContentConfiguration(isDefaultValueSelected),
                        createHappyFlowConfiguration(isDefaultValueSelected),
                        createTroubleShootingConfiguration()));
    }

    @NonNull
    protected Map<String, String> createProductMap() {
        Map<String, String> productKeyMap = new HashMap<>();
        productKeyMap.put(EWSInterface.PRODUCT_NAME, getString(R.string.lbl_devicename));
        return productKeyMap;
    }

    @NonNull
    private BaseContentConfiguration createBaseContentConfiguration(boolean isDefaultValueSelected){
        if (isDefaultValueSelected){
            return new BaseContentConfiguration();
        }else{
            return new BaseContentConfiguration(R.string.lbl_devicename, R.string.lbl_appname);
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
                    .setSetUpScreenTitle(R.string.lbl_setup_screen_title)
                    .setSetUpScreenBody(R.string.lbl_setup_screen_body)
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
    private TroubleShootContentConfiguration createTroubleShootingConfiguration(){
        return new TroubleShootContentConfiguration.Builder()
                .setResetConnectionTitle(R.string.label_ews_support_reset_connection_title)
                .setResetConnectionBody(R.string.label_ews_support_reset_connection_body)
                .setResetConnectionImage(R.drawable.ic_ews_enable_ap_mode)

                .setResetDeviceTitle(R.string.label_ews_support_reset_device_title)
                .setResetDeviceBody(R.string.label_ews_support_reset_device_body)
                .setResetDeviceImage(R.drawable.ic_ews_enable_ap_mode)

                .setSetUpAccessPointTitle(R.string.label_ews_setup_access_point_mode_title)
                .setSetUpAccessPointBody(R.string.label_ews_setup_access_point_mode_body)
                .setSetUpAccessPointImage(R.drawable.ic_ews_enable_ap_mode)

                .setConnectWrongPhoneTitle(R.string.label_ews_connect_to_wrongphone_title)
                .setConnectWrongPhoneBody(R.string.label_ews_connect_to_wrongphone_body)
                .setConnectWrongPhoneImage(R.drawable.ic_ews_enable_ap_mode)
                .setConnectWrongPhoneQuestion(R.string.label_ews_connect_to_wrongphone_question)
                .build();
    }
}
