package com.philips.platform.uappdemo.screens.dls;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.philips.platform.uappdemo.UappDemoInterface;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uid.thememanager.UIDHelper;


public class DLSActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(UappDemoInterface.DLS_THEME);
        UIDHelper.init(UappDemoInterface.THEME_CONFIGURATION);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dls);
    }
}
