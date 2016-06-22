/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appidentity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.R;

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

    public String mAppName;
    public String mAppVersion;
    public String mAppState;
    public String mAppLocalizedNAme;
    public String micrositeId;
    public String sector;

    /**
     * Gets app name.
     *
     * @return the app name
     */
    @Override
    public String getAppName() {
        return mAppName;
    }

    /**
     * Gets app version.
     *
     * @return the app version
     */
    @Override
    public String getAppVersion() {
        return mAppVersion;
    }

    /**
     * Gets app state.
     *
     * @return the app state
     */
    @Override
    public String getAppState() {
        return mAppState;
    }

    /**
     * Gets app localized name.
     *
     * @return the app localized name
     */
    @Override
    public String getAppLocalizedNAme() {
        return mAppLocalizedNAme;
    }

    /**
     * Gets microsite id.
     *
     * @return the microsite id
     */
    @Override
    public String getMicrositeId() {
        return micrositeId;
    }

    /**
     * Gets sector.
     *
     * @return the sector
     */
    @Override
    public String getSector() {
        return sector;
    }

    public AppIdentityManager(AppInfra aAppInfra) {
        mAppInfra = aAppInfra;
        context = mAppInfra.getAppInfraContext();
        // Class shall not presume appInfra to be completely initialized at this point.
        // At any call after the constructor, appInfra can be presumed to be complete.

        // Method Loads the json data and can be access through getters
        loadJSONFromAsset();
    }


       private String loadJSONFromAsset() {
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
                     micrositeId= obj.getString("micrositeId");
                     sector = obj.getString("sector");
                     mAppState= obj.getString("AppState");

                    try {
                        PackageInfo pInfo = null;
                        pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
                         mAppName = pInfo.versionName;

                        /* Vertical App should have this string defined for all supported language files
                        *  default <string name="localized_commercial_app_name">AppInfra DemoApp localized</string>
                        * */
                            mAppLocalizedNAme = context.getResources().getString(R.string.localized_commercial_app_name);

                        mAppVersion = String.valueOf(pInfo.versionCode);

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }


                    Log.i("micrositeId", ""+getMicrositeId());
                    Log.i("sector", ""+getSector());
                    Log.i("mAppState", ""+getAppState());
                    Log.i("mAppName", ""+getAppName());
                    Log.i("mAppVersion", ""+getAppVersion());
                    Log.i("mAppLocalizedNAme", ""+getAppLocalizedNAme());
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

}
