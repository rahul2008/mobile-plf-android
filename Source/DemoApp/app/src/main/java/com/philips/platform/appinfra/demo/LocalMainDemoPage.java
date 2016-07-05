package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.LocalInterface;

/**
 * Created by 310238655 on 6/2/2016.
 */
public class LocalMainDemoPage extends AppCompatActivity {

    LocalInterface mappIdentityinterface = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfraInterface appInfra = AppInfraApplication.gAppInfra;
        mappIdentityinterface = appInfra.getLocal();
        Log.i("TAG-Local-Country", ""+mappIdentityinterface.getCountry());
        Log.i("TAG-Local-language", ""+mappIdentityinterface.getlocal());
    }
}
