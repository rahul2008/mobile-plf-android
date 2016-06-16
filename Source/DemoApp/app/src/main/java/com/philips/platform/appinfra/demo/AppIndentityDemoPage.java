package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.appinfra.AppIdentity.AppIdentityManager;
import com.philips.platform.appinfra.AppInfra;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class AppIndentityDemoPage extends AppCompatActivity{

    AppIdentityManager mAppIdentityManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfra appInfra = AppInfraApplication.gAppInfra;
        mAppIdentityManager = (AppIdentityManager)appInfra.getAppIdentity();
        mAppIdentityManager.loadJSONFromAsset();

    }
}
