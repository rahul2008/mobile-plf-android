package com.philips.platform.appinfra.demo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class AppIndentityDemoPage extends AppCompatActivity {

    AppIdentityInterface mAppIdentityInterface = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appidentity);
        AppInfraInterface appInfra = AppInfraApplication.gAppInfra;
        mAppIdentityInterface = appInfra.getAppIdentity();

        ((TextView) findViewById(R.id.appNameValue)).setText(mAppIdentityInterface.getAppName());
        ((TextView) findViewById(R.id.localizedAppValue)).setText(mAppIdentityInterface.getAppLocalizedNAme());
        ((TextView) findViewById(R.id.appVersionValue)).setText(mAppIdentityInterface.getAppVersion());
        ((TextView) findViewById(R.id.appStateValue)).setText(mAppIdentityInterface.getAppState());
        ((TextView) findViewById(R.id.micrositeIdValue)).setText(mAppIdentityInterface.getMicrositeId());
        ((TextView) findViewById(R.id.sectorValue)).setText(mAppIdentityInterface.getSector());

        Log.i("getAppLocalizedNAme", "" + mAppIdentityInterface.getAppLocalizedNAme());
        Log.i("getSector", "" + mAppIdentityInterface.getSector());
        Log.i("getMicrositeId", "" + mAppIdentityInterface.getMicrositeId());
        Log.i("getAppName", "" + mAppIdentityInterface.getAppName());
        Log.i("getAppState", "" + mAppIdentityInterface.getAppState());
        Log.i("getAppVersion", "" + mAppIdentityInterface.getAppVersion());
//        mAppIdentityManager.loadJSONFromAsset();
        AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppIndentityDemoPage", "Key", "AppIndentityVAlue");


    }


}
