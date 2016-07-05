package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class AppIndentityDemoPage extends AppCompatActivity{

    AppIdentityInterface mAppIdentityInterface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfraInterface appInfra = AppInfraApplication.gAppInfra;
        mAppIdentityInterface = appInfra.getAppIdentity();
        Log.i("getAppLocalizedNAme", ""+mAppIdentityInterface.getAppLocalizedNAme());
        Log.i("getSector", ""+mAppIdentityInterface.getSector());
        Log.i("getMicrositeId", ""+mAppIdentityInterface.getMicrositeId());
        Log.i("getAppName", ""+mAppIdentityInterface.getAppName());
        Log.i("getAppState", ""+mAppIdentityInterface.getAppState());
        Log.i("getAppVersion", ""+mAppIdentityInterface.getAppVersion());
//        mAppIdentityManager.loadJSONFromAsset();
        AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppIndentityDemoPage", "Key", "AppIndentityVAlue");

    }
}
