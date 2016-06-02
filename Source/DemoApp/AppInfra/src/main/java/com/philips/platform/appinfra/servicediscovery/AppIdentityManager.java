package com.philips.platform.appinfra.servicediscovery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.BuildConfig;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class AppIdentityManager implements AppIdentityInterface {

    AppInfra mAppInfra;
    Context context;

    public AppIdentityManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

    }

       public String loadJSONFromAsset() {
        String json = null;
        try {

            InputStream is = context.getAssets().open("AppIdentity.json");

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");


        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

    @Override
    public void configureAppIdentity(String configFilePath) {

    }
}
