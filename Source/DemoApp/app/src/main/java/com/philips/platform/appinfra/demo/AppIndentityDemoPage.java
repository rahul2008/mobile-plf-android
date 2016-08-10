package com.philips.platform.appinfra.demo;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

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

        try {
            ((TextView) findViewById(R.id.appNameValue)).setText(mAppIdentityInterface.getAppName());
            ((TextView) findViewById(R.id.localizedAppValue)).setText(mAppIdentityInterface.getLocalizedAppName());
            ((TextView) findViewById(R.id.appVersionValue)).setText(mAppIdentityInterface.getAppVersion());
            ((TextView) findViewById(R.id.appStateValue)).setText(mAppIdentityInterface.getAppState().toString());
            ((TextView) findViewById(R.id.micrositeIdValue)).setText(mAppIdentityInterface.getMicrositeId());
            ((TextView) findViewById(R.id.sectorValue)).setText(mAppIdentityInterface.getSector());
            ((TextView) findViewById(R.id.servicediscoveryvalue)).setText(mAppIdentityInterface.getServiceDiscoveryEnvironment());


            Log.i("getAppLocalizedNAme", "" + mAppIdentityInterface.getLocalizedAppName());
            Log.i("getSector", "" + mAppIdentityInterface.getSector());
            Log.i("getMicrositeId", "" + mAppIdentityInterface.getMicrositeId());
            Log.i("getAppName", "" + mAppIdentityInterface.getAppName());
            Log.i("getAppState", "" + mAppIdentityInterface.getAppState());
            Log.i("getAppVersion", "" + mAppIdentityInterface.getAppVersion());

        } catch (Exception error) {
            System.out.println("ERROR" + " " + error);
            Toast.makeText(this, "Invalid Arguments", Toast.LENGTH_SHORT).show();
        }

//        appInfra.getTagging().createInstanceForComponent("AppIdentityID", "AppIdentityVersion");


//        mAppIdentityManager.loadJSONFromAsset();
        Map testHash = new HashMap<String, String>();
        testHash.put("Test", "Test");
        AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppIndentityDemoPage", testHash);


    }


}
