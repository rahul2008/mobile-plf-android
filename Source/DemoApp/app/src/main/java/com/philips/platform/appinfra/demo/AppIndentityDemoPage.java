package com.philips.platform.appinfra.demo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;
import java.util.Map;

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
        AppTaggingInterface mAppTaggingInterface;
        mAppTaggingInterface = AppInfraApplication.gAppInfra.getTagging().createInstanceForComponent("AppIndentityID", "AppIndentityIDVersion");

        mAppTaggingInterface.trackPageWithInfo("AppIndentityPageDemoPage", "AppIndentityKEy", "AppIndentityValue");


        try {
            ((TextView) findViewById(R.id.appNameValue)).setText(mAppIdentityInterface.getAppName());

        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

        ((TextView) findViewById(R.id.localizedAppValue)).setText(mAppIdentityInterface.getLocalizedAppName());
        Log.i("getAppLocalizedNAme", "" + mAppIdentityInterface.getLocalizedAppName());


        try {

            ((TextView) findViewById(R.id.appVersionValue)).setText(mAppIdentityInterface.getAppVersion());
            if (mAppIdentityInterface.getAppState() != null) {
                ((TextView) findViewById(R.id.appStateValue)).setText(mAppIdentityInterface.getAppState().toString());
                Log.i("getAppState", "" + mAppIdentityInterface.getAppState());
            }
        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }


        try {
            ((TextView) findViewById(R.id.micrositeIdValue)).setText(mAppIdentityInterface.getMicrositeId());
            Log.i("getMicrositeId", "" + mAppIdentityInterface.getMicrositeId());

        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

        try {

            ((TextView) findViewById(R.id.sectorValue)).setText(mAppIdentityInterface.getSector());

        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

        try {

            ((TextView) findViewById(R.id.servicediscoveryvalue)).setText(mAppIdentityInterface.getServiceDiscoveryEnvironment());

        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }


//        appInfra.getTagging().createInstanceForComponent("AppIdentityID", "AppIdentityVersion");


    //        mAppIdentityManager.loadJSONFromAsset();
//    Map testHash = new HashMap<String, String>();
//    testHash.put("Test","Test");
//    AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppIndentityDemoPage",testHash);

}



