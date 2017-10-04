package com.philips.cdp2.ews.demoapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.cdp2.ews.helper.EWSLaunchHelper;

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
                new EWSLaunchHelper(this).launch();
                break;
            default:
                break;
        }
    }
}
