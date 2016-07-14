package com.philips.platform.appinfra.demo;

import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.InternationalizationInterface;

/**
 * Created by 310238655 on 6/2/2016.
 */
public class LocalMainDemoPage extends AppCompatActivity {

    InternationalizationInterface mappIdentityinterface = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localmain);
        AppInfraInterface appInfra = AppInfraApplication.gAppInfra;
        mappIdentityinterface = appInfra.getLocal();

        AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("LocalMainDemoPage", "SDKEy", "SDValue");
//        ((TextView)findViewById(R.id.countryValue)).setText(mappIdentityinterface.getCountry());
        ((TextView)findViewById(R.id.localValue)).setText(mappIdentityinterface.getUILocal());

//        Log.i("TAG-Local-Country", ""+mappIdentityinterface.getCountry());
        Log.i("TAG-Local-language", ""+mappIdentityinterface.getUILocal());
    }
}
