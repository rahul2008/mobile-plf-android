package com.philips.cdp2.ews.demoapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.ContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.microapp.EWSDependencies;
import com.philips.cdp2.ews.microapp.EWSInterface;
import com.philips.cdp2.ews.microapp.EWSLauncherInput;
import com.philips.cdp2.ews.tagging.Actions;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.philips.platform.uappframework.launcher.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT;

public class EWSDemoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private static final String[] configurationOptions = { "Default", "Wakeup Light", "Air Purifier"};

    private Spinner configSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewsdemo);
        findViewById(R.id.launchEWS).setOnClickListener(this);

        configSpinner = (Spinner) findViewById(R.id.spinner);
        configSpinner.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,configurationOptions);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        configSpinner.setAdapter(aa);
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
                new ContentConfiguration(createBaseContentConfiguration(), createHappyFlowConfiguration()));
    }

    @NonNull
    private Map<String, String> createProductMap() {
        Map<String, String> productKeyMap = new HashMap<>();
        productKeyMap.put(EWSInterface.PRODUCT_NAME, Actions.Value.PRODUCT_NAME_SOMNEO);
        return productKeyMap;
    }

    @NonNull
    private BaseContentConfiguration createBaseContentConfiguration(){
        return new BaseContentConfiguration(R.string.lbl_devicename, R.string.lbl_appname);
    }

    @NonNull
    private HappyFlowContentConfiguration createHappyFlowConfiguration(){
        return new HappyFlowContentConfiguration.Builder()
                .setGettingStartedScreenTitle(R.string.label_ews_get_started_title)
                .setSetUpScreenTitle(R.string.lbl_setup_screen_title)
                .setSetUpScreenBody(R.string.lbl_setup_screen_body)
                .build();
    }

    public void updateCurrentContent(String currentContent) {
        try {
            Configuration config = new Configuration(getResources().getConfiguration());
            config.locale = new Locale(currentContent);
            getResources().updateConfiguration(config, getResources().getDisplayMetrics());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (position){
            case 0:
                updateCurrentContent("");
                break;
            case 1:
                updateCurrentContent("wl");
                break;
            case 2:
                updateCurrentContent("ap");
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}