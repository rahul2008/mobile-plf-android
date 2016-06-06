package com.philips.platform.appinfra.servicediscovery;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.BuildConfig;

import org.json.JSONException;
import org.json.JSONObject;

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
            if(json != null){
                try {
                    JSONObject obj = new JSONObject(json);
                    String str1= obj.getString("micrositeId");
                    String str2 = obj.getString("sector");
                    String str3= obj.getString("AppVersion");
                    String str4 = obj.getString("AppName");
                    String str5= obj.getString("AppState");
                    String str6 = obj.getString("LocalizedAppName");

                    Log.i("Obj tag1", str1);
                    Log.i("Obj tag2", str2);
                    Log.i("Obj tag1", str3);
                    Log.i("Obj tag2", str4);
                    Log.i("Obj tag1", str5);
                    Log.i("Obj tag2", str6);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }




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
