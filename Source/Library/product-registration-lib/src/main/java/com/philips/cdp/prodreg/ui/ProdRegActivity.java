package com.philips.cdp.prodreg.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp.registration.ui.utils.RegistrationLaunchHelper;

public class ProdRegActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RegistrationLaunchHelper.launchDefaultRegistrationActivity(this);
    }
}
