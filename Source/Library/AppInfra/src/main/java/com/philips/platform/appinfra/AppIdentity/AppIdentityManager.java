/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.appidentity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.R;
import com.philips.platform.appinfra.logging.LoggingInterface;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;


public class AppIdentityManager implements AppIdentityInterface {

    AppInfra mAppInfra;
    Context context;

    public String mAppName;
    public String mAppVersion;
    public String mServiceDiscoveryEnvironment;
    public String mLocalizedAppName;
    public String micrositeId;
    public String sector;
    public String mAppState;



    @Override
    public String getAppName() {
        return mAppName;
    }


    @Override
    public String getAppVersion() {
        return mAppVersion;
    }

    @Override
    public AppState getAppState() {
        AppState mAppStateEnum = null;
        if(mAppState.equalsIgnoreCase("DEVELOPMENT")){
            mAppStateEnum =  AppState.DEVELOPMENT;
        }
        if(mAppState.equalsIgnoreCase("TEST")){
            mAppStateEnum =  AppState.TEST;
        }
        if(mAppState.equalsIgnoreCase("STAGING")){
            mAppStateEnum =  AppState.STAGING;
        }
        if(mAppState.equalsIgnoreCase("ACCEPTANCE")){
            mAppStateEnum = AppState.ACCEPTANCE;
        }
        if(mAppState.equalsIgnoreCase("PRODUCTION")){
            mAppStateEnum = AppState.PRODUCTION;
        }
        if(mAppStateEnum != null){
            return mAppStateEnum;
        }

        return mAppStateEnum;
    }

    @Override
    public String getServiceDiscoveryEnvironment() {
        return mServiceDiscoveryEnvironment;
    }


    @Override
    public String getLocalizedAppName() {
        return mLocalizedAppName;
    }


    @Override
    public String getMicrositeId() {
        return micrositeId;
    }


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

   // Refactored to support unit test
    protected String getJsonStringFromAsset(){
        String json = null;
        InputStream is = null;
        try {
            is = context.getAssets().open("AppIdentity.json");
            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");
        } catch (Exception e) {
                e.printStackTrace();
        }
            return json;
    }

    public String  loadJSONFromAsset() {
        String json = null;
        try {

            json= getJsonStringFromAsset();
            if(json != null){
                try {
                    JSONObject obj = new JSONObject(json);
                     micrositeId= obj.getString("micrositeId");
                     sector = obj.getString("sector");
                    mServiceDiscoveryEnvironment= obj.getString("ServiceDiscoveryEnvironment");
                    mAppState = obj.getString("AppState");

                    if(mServiceDiscoveryEnvironment.equalsIgnoreCase("DEVELOPMENT")&& mServiceDiscoveryEnvironment.length()==11 || mServiceDiscoveryEnvironment.equalsIgnoreCase("TEST")&& mServiceDiscoveryEnvironment.length()==4 || mServiceDiscoveryEnvironment.equalsIgnoreCase("STAGING")&& mServiceDiscoveryEnvironment.length()==7 || mServiceDiscoveryEnvironment.equalsIgnoreCase("ACCEPTANCE")&& mServiceDiscoveryEnvironment.length()==10 ||mServiceDiscoveryEnvironment.equalsIgnoreCase("PRODUCTION")&& mServiceDiscoveryEnvironment.length()==10){

                        mServiceDiscoveryEnvironment= obj.getString("ServiceDiscoveryEnvironment");

                    }else{
                        mServiceDiscoveryEnvironment= null;
                        Assert.fail("Please set Config File (AppIdentity.json) to proper data");
                    }
                    if(mAppState.equalsIgnoreCase("DEVELOPMENT")&& mAppState.length()==11 || mAppState.equalsIgnoreCase("TEST")&& mAppState.length()==4 || mAppState.equalsIgnoreCase("STAGING")&& mAppState.length()==7 || mAppState.equalsIgnoreCase("ACCEPTANCE")&& mAppState.length()==10 ||mAppState.equalsIgnoreCase("PRODUCTION")&& mAppState.length()==10){

                        mAppState= obj.getString("AppState");

                    }else{
                        mAppState= null;
                        Assert.fail("Please set Config File (AppIdentity.json) to proper data");
                    }
                    try {
                        PackageInfo pInfo = null;
                        pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);

                        mAppName = context.getApplicationInfo().loadLabel(context.getPackageManager()).toString();

                        /* Vertical App should have this string defined for all supported language files
                        *  default <string name="localized_commercial_app_name">AppInfra DemoApp localized</string>
                        * */
                        mLocalizedAppName = context.getResources().getString(R.string.localized_commercial_app_name);

                        mAppVersion = String.valueOf(pInfo.versionName);

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }


                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,"micrositeId",""+getMicrositeId());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,"sector",""+getSector());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,"AppState",""+getAppState());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,"AppName",""+getAppName());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,"AppVersion",""+getAppVersion());
                    mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.INFO,"AppLocalizedNAme",""+getLocalizedAppName());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }




        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }

}
