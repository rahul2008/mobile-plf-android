/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.app.Application;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.appinfra.tagging.ApplicationLifeCycleHandler;

import java.net.URL;

/**
 * Created by deepakpanigrahi on 5/18/16.
 */
public class AppInfraApplication extends Application {
     public static AppTaggingInterface mAIAppTaggingInterface;
     public static AppInfraInterface gAppInfra;
    @Override
    public void onCreate() {
        super.onCreate();

        //gAppInfra = new AppInfra.Builder().build(getApplicationContext());
        //AppInfraSingleton appInfraSingleton ;




        AppInfraSingleton.setInstance(gAppInfra=new AppInfra.Builder().build(getApplicationContext()));
        gAppInfra=AppInfraSingleton.getInstance();


       /* LoggingInterface overridenLogger=null;
        AppInfraSingleton.setInstance(gAppInfra=new AppInfra.Builder().setLogging(overridenLogger).build(getApplicationContext()));*/


        mAIAppTaggingInterface = gAppInfra.getTagging().createInstanceForComponent("Component name","Component ID");
        mAIAppTaggingInterface.setPreviousPage("SomePreviousPage");
        gAppInfra.getServiceDiscovery().getServiceUrlWithLanguagePreference("userreg.janrain.api", new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
            @Override
            public void onSuccess(URL url) {
                Log.i("SUCCESS ***", ""+url);
            }

            @Override
            public void onError(ERRORVALUES error, String message) {
                Log.i("ERRORVALUES ***", ""+message);
            }
        });

        ApplicationLifeCycleHandler handler = new ApplicationLifeCycleHandler(mAIAppTaggingInterface);
        registerActivityLifecycleCallbacks(handler);
        registerComponentCallbacks(handler);

    }

}
