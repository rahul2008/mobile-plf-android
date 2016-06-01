package com.philips.platform.appinfra.servicediscovery;

import android.app.Activity;
import android.os.Bundle;

import com.philips.platform.appinfra.BuildConfig;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class AppIdentityManager extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int versionCode = BuildConfig.VERSION_CODE;
        String versionName = BuildConfig.VERSION_NAME;
        String buildType = BuildConfig.BUILD_TYPE;
    }
}
