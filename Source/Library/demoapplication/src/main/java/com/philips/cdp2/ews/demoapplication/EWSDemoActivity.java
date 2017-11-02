package com.philips.cdp2.ews.demoapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.configuration.TroubleShootContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSDependencies;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.cdp2.ews.tagging.Tag;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.uappframework.launcher.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT;

public class EWSDemoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewsdemo);
        findViewById(R.id.launchEWS).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.launchEWS:
                launchEwsUApp();
                break;
            default:
                break;
        }
    }

    private void launchEwsUApp() {
        AppInfraInterface appInfra = new AppInfra.Builder().build(getApplicationContext());
        EWSInterface ewsInterface = new EWSInterface();
        ewsInterface.init(createUappDependencies(appInfra, createProductMap()), new UappSettings(getApplicationContext()));
        ewsInterface.launch(new ActivityLauncher(SCREEN_ORIENTATION_PORTRAIT, -1), new EWSLauncherInput());
    }

    @NonNull
    private UappDependencies createUappDependencies(AppInfraInterface appInfra,
                                                    Map<String, String> productKeyMap) {
        return new EWSDependencies(appInfra, productKeyMap,
                new ContentConfiguration(createBaseContentConfiguration(),
                                        createHappyFlowConfiguration(),
                                        createTroubleShootingConfiguration()));
    }

    @NonNull
    private Map<String, String> createProductMap() {
        Map<String, String> productKeyMap = new HashMap<>();
        productKeyMap.put(EWSInterface.PRODUCT_NAME, Tag.VALUE.PRODUCT_NAME_SOMNEO);
        return productKeyMap;
    }

    @NonNull
    private BaseContentConfiguration createBaseContentConfiguration(){
        return new BaseContentConfiguration(R.string.lbl_devicename, R.string.lbl_appname);
    }

    @NonNull
    private HappyFlowContentConfiguration createHappyFlowConfiguration(){
        return new HappyFlowContentConfiguration.Builder()
                .setGettingStartedScreenTitle(R.string.lbl_connectwithdevice)
                .setSetUpScreenTitle(R.string.lbl_ews_02_01_title)
                .setSetUpScreenBody(R.string.lbl_ews_02_01_body)
                .build();
    }

    @NonNull
    private TroubleShootContentConfiguration createTroubleShootingConfiguration(){
        return new TroubleShootContentConfiguration.Builder()
                .setConnectWrongPhoneTitle(R.string.lbl_ews_H_03_01_title)
                .setConnectWrongPhoneBody(R.string.lbl_ews_H_03_01_body)
                .setConnectWrongPhoneImage(R.drawable.ic_ews_enable_ap_mode)
                .setConnectWrongPhoneQuestion(R.string.lbl_ews_H_03_01_question)

                .setResetConnectionTitle(R.string.lbl_ews_H_03_02_title)
                .setResetConnectionBody(R.string.lbl_ews_H_03_02_body)
                .setResetConnectionImage(R.drawable.ic_ews_enable_ap_mode)

                .setResetDeviceTitle(R.string.lbl_ews_H_03_03_title)
                .setResetDeviceBody(R.string.lbl_ews_H_03_03_body)
                .setResetDeviceImage(R.drawable.ic_ews_enable_ap_mode)

                .setSetUpAccessPointTitle(R.string.lbl_ews_H_03_04_title)
                .setSetUpAccessPointBody(R.string.lbl_ews_H_03_04_body)
                .setSetUpAccessPointImage(R.drawable.ic_ews_enable_ap_mode)
                .build();
    }
}