package com.philips.platform.mya.demoapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MyAccountDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account_demo);
    }

    public void launch(View view) {
        setContentView(com.philips.platform.mya.R.layout.consents);
    }
}
