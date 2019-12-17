/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demoapp;

import android.app.Application;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demoapp.R;
import com.philips.platform.appinfra.demoapp.abtesting.AbTestingImpl;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.tagging.ApplicationLifeCycleHandler;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
//import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;

public class AppInfraApplication extends Application {
    private AppInfraInterface gAppInfra;
    private ArrayList<ConsentDefinition> consentDefinitions=new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        //LeakCanary.install(this);

        AbTestingImpl abTestingImpl = new AbTestingImpl();
        abTestingImpl.initFireBase();

        AppInfra.Builder builder = new AppInfra.Builder();
        builder.setAbTesting(abTestingImpl);
        long current_time = System.currentTimeMillis();
        Log.i("time","Time before ail build "+ current_time);
        gAppInfra = builder.build(getApplicationContext());
        abTestingImpl.initAbTesting(gAppInfra);
        abTestingImpl.enableDeveloperMode(true);
        long end_time = System.currentTimeMillis();
        Log.i("time","Time after ail build "+ end_time);
        Log.i("time","Application Diff is " + (end_time - current_time));
        consentDefinitions.add(getCloudConsentDefinition());
        gAppInfra.getConsentManager().registerConsentDefinitions(consentDefinitions);
        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler((AppInfra) gAppInfra);
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
    }

    public ConsentDefinition getCloudConsentDefinition() {
        int text = R.string.ail_cloud_consent_title;
        int helpText = R.string.ail_cloud_consent_help;
        final ArrayList<String> types = new ArrayList<>();
        types.add(AppInfraLogging.CLOUD_CONSENT);
        return new ConsentDefinition(text, helpText, types, 1);
    }

    public AppInfraInterface getAppInfra() {
        return gAppInfra;
    }

}
