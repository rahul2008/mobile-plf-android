package com.philips.platform.udi;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.philips.platform.uid.utils.UIDActivity;

public class UDIActivity extends UIDActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.udi_activity);
    }
}
