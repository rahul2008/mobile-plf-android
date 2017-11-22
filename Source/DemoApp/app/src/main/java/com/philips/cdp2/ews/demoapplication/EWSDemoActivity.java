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
        findViewById(R.id.btnLaunchEws).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLaunchEws:
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
                new ContentConfiguration(new BaseContentConfiguration(),
                        new HappyFlowContentConfiguration.Builder().build(),
                        new TroubleShootContentConfiguration.Builder().build()));
    }

    @NonNull
    private Map<String, String> createProductMap() {
        Map<String, String> productKeyMap = new HashMap<>();
        productKeyMap.put(EWSInterface.PRODUCT_NAME, getString(R.string.lbl_devicename));
        return productKeyMap;
    }


}