/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

/**
 * Created by deepakpanigrahi on 5/18/16.
 */
public class AppInfraApplication extends Application {
     public static AIAppTaggingInterface mAIAppTaggingInterface;
    public static AppInfra gAppInfra;
    @Override
    public void onCreate() {
        super.onCreate();

        gAppInfra = new AppInfra.Builder().build(getApplicationContext());

        mAIAppTaggingInterface = gAppInfra.getTagging().createInstanceForComponent("Component name","Component ID");
        mAIAppTaggingInterface.setPreviousPage("SomeXpreviousPage");

    }

}
