/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

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
        mAIAppTaggingInterface.setPreviousPage("SomeXpreviousPage");

    }

}
