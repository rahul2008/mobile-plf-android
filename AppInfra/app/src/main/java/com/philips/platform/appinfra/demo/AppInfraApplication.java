package com.philips.platform.appinfra.demo;

import android.app.Application;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

/**
 * Created by deepakpanigrahi on 5/18/16.
 */
public class AppInfraApplication extends Application {
     public static AIAppTaggingInterface mAIAppTaggingInterface;
    @Override
    public void onCreate() {
        super.onCreate();

      AppInfra  ai = new AppInfra.Builder().build(getApplicationContext());

        mAIAppTaggingInterface = ai.getTagging().createInstanceForComponent("Component name","Component ID");

        int x = 10;
        if(x==10){
            int y = x;
        }


    }
}
