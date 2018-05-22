/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.aildemolaunch;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.CloudConsentProvider;
import com.philips.platform.appinfra.tagging.ApplicationLifeCycleHandler;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;

public class AppInfraApplication extends Application {
    private AppInfraInterface gAppInfra;
    private ArrayList<ConsentDefinition> consentDefinitions=new ArrayList<>();


    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
        AppInfra.Builder builder = new AppInfra.Builder();
        gAppInfra = builder.build(getApplicationContext());
        consentDefinitions.add(CloudConsentProvider.getCloudConsentDefinition());
        gAppInfra.getConsentManager().registerConsentDefinitions(consentDefinitions);
        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler((AppInfra) gAppInfra);
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);
    }

    public AppInfraInterface getAppInfra() {
        return gAppInfra;
    }

}
