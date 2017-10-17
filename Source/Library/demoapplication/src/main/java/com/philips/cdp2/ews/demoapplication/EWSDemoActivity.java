package com.philips.cdp2.ews.demoapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.cdp2.ews.configuration.EWSStartContentConfiguration;
import com.philips.cdp2.ews.helper.EWSLaunchHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EWSDemoActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewsdemo);
        findViewById(R.id.launchEWS).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.launchEWS:
                new EWSLaunchHelper(this) {

                    @Override
                    public Map<String, Serializable> getScreenConfigs() {
                       Map<String, Serializable> configurationMap = new HashMap();
                        configurationMap.put(EWSStartContentConfiguration.class.getName(),
                                new EWSStartContentConfiguration(R.string.lbl_appname, R.string.lbl_devicename));
                        return configurationMap;
                    }
                }.launch();
                break;
            default:
                break;
        }
    }
}
