package com.philips.cdp2.ews.demoapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.philips.cdp2.ews.demoapplication.EWSDemoUActivity;
import com.philips.platform.uappframework.launcher.ActivityLauncher;

import static com.philips.cdp2.ews.microapp.EWSUapp.SCREEN_ORIENTATION;

public class EWSDemoActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);
        findViewById(R.id.btn_launch_ews).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_launch_ews:
                launchEwsUApp();
                break;
            default:
                break;
        }
    }

    private void launchEwsUApp() {
        Intent intent = new Intent(this, EWSDemoUActivity.class);
        intent.putExtra(SCREEN_ORIENTATION, ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
}