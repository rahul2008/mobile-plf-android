package com.philips.platform.appinfra.demoapp;


import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import com.philips.platform.aildemo.AILDemouAppDependencies;
import com.philips.platform.aildemo.AILDemouAppInterface;
import com.philips.platform.aildemo.AILDemouAppSettings;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.appinfra.consentmanager.PostConsentCallback;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

public class AppInfraLaunchActivity extends AppCompatActivity {

    private Switch cloudLoggingConsentSwitch;

    private AppInfraInterface appInfra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_infra_launch);
        appInfra = ((AppInfraApplication) getApplication()).getAppInfra();
        Button button = findViewById(R.id.launch_demo_micro_app);
        button.setOnClickListener(v -> invokeMicroApp());
        cloudLoggingConsentSwitch = findViewById(R.id.cloud_logging_switch);

        cloudLoggingConsentSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            ConsentDefinition consentDefinition = appInfra.getConsentManager().getConsentDefinitionForType(AppInfraLogging.CLOUD_CONSENT);
            appInfra.getConsentManager().storeConsentState(consentDefinition, isChecked, new PostConsentCallback() {
                @Override
                public void onPostConsentFailed(ConsentError error) {
                    Log.v("SyncTesting", "Error while saving consent");
                }

                @Override
                public void onPostConsentSuccess() {
                    Log.v("SyncTesting", "Changed consent sucessfully");
                }
            });
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                appInfra.getConsentManager().fetchConsentTypeState(AppInfraLogging.CLOUD_CONSENT, new FetchConsentCallback() {
                    @Override
                    public void onGetConsentSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
                        if (consentDefinitionStatus != null && consentDefinitionStatus.getConsentState() != null) {
                            switch (consentDefinitionStatus.getConsentState()) {
                                case inactive:
                                case rejected:
                                    cloudLoggingConsentSwitch.setChecked(false);
                                    break;
                                case active:
                                    cloudLoggingConsentSwitch.setChecked(true);
                                    break;
                            }
                        }

                    }

                    @Override
                    public void onGetConsentFailed(ConsentError error) {
                        Log.v("LoggingActivity", "Getting consent failed");
                    }
                });
            }
        },2000);


    }


    private void invokeMicroApp() {
        AILDemouAppInterface uAppInterface = AILDemouAppInterface.getInstance();
        AppInfraApplication appInfraApplication = (AppInfraApplication) getApplication();
        uAppInterface.init(new AILDemouAppDependencies(appInfraApplication.getAppInfra()), new AILDemouAppSettings(getApplicationContext()));// pass App-infra instance instead of null
        uAppInterface.launch(new ActivityLauncher(this, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED, null, 0, null),null);// pass launch input if required
    }
}
