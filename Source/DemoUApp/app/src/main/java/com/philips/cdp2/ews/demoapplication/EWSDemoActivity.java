package com.philips.cdp2.ews.demoapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import java.util.Locale;
import java.util.Map;

import static com.philips.platform.uappframework.launcher.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT;

public class EWSDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner configSpinner;
    private static final String WAKEUP_LIGHT = "wl";
    private static final String AIRPURIFIER = "ap";
    private static final String DEFAULT = "Default";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewsdemo);
        findViewById(R.id.btnLaunchEws).setOnClickListener(this);

        configSpinner = (Spinner) findViewById(R.id.configurationSelection);
        configSpinner.setOnItemSelectedListener(itemSelectedListener);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.configurations));
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        configSpinner.setAdapter(aa);
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
                new ContentConfiguration(createBaseContentConfiguration(),
                                        createHappyFlowConfiguration(),
                                        createTroubleShootingConfiguration()));
    }

    @NonNull
    private Map<String, String> createProductMap() {
        Map<String, String> productKeyMap = new HashMap<>();
        productKeyMap.put(EWSInterface.PRODUCT_NAME, getString(R.string.lbl_devicename));
        return productKeyMap;
    }

    @NonNull
    private BaseContentConfiguration createBaseContentConfiguration(){
        if (isDefaultValueSelected()){
            return new BaseContentConfiguration();
        }else{
            return new BaseContentConfiguration(R.string.lbl_devicename, R.string.lbl_appname);
        }
    }

    @NonNull
    private HappyFlowContentConfiguration createHappyFlowConfiguration(){
        if(isDefaultValueSelected()){
            return new HappyFlowContentConfiguration.Builder().build();
        }else{
            return new HappyFlowContentConfiguration.Builder()
                    .setGettingStartedScreenTitle(R.string.label_ews_get_started_title)
                    .setSetUpScreenTitle(R.string.lbl_setup_screen_title)
                    .setSetUpScreenBody(R.string.lbl_setup_screen_body)
                    .setSetUpVerifyScreenTitle(R.string.lbl_setup_verifyscreen_title)
                    .setSetUpVerifyScreenQuestion(R.string.lbl_setup_verifyscreen_body)
                    .setSetUpVerifyScreenYesButton(R.string.lbl_setup_verifyscreen_yesbutton)
                    .setSetUpVerifyScreenNoButton(R.string.lbl_setup_verifyscreen_nobutton)
                    .build();
        }
    }

    private void updateCurrentContent(String currentContent) {
        try {
            Configuration config = new Configuration(getResources().getConfiguration());
            config.setLocale(new Locale(currentContent));
            getResources().getConfiguration().updateFrom(config);
        } catch (Exception e) {
            Log.e(EWSDemoActivity.class.getName(), e.toString());
        }
    }

    private boolean isDefaultValueSelected(){
        if (configSpinner.getSelectedItem().equals(DEFAULT)){
            return true;
        }
        return false;
    }

    private AdapterView.OnItemSelectedListener itemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            switch (position){
                case 1:
                    updateCurrentContent(WAKEUP_LIGHT);
                    break;
                case 2:
                    updateCurrentContent(AIRPURIFIER);
                    break;
                case 0:
                default:
                    updateCurrentContent("");
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // do nothing
        }
    };

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