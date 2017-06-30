package com.philips.platform.screens.dls;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.platform.uappdemo.UappDemoInterface;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.UIDActivity;


public class DLSActivity extends UIDActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        UIDHelper.init(UappDemoInterface.themeConfiguration);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dls);
    }
}
