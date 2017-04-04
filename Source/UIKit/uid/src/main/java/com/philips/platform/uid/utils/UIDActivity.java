package com.philips.platform.uid.utils;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

public class UIDActivity extends AppCompatActivity {

    private Resources uidResources;

    @Override
    public Resources getResources() {
        if (uidResources == null) {
            uidResources = new UIDResources(super.getResources());
        }
        return uidResources == null ? super.getResources() : uidResources;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getDelegate().onConfigurationChanged(newConfig);
        if (uidResources != null) {
            // The real (and thus managed) resources object was already updated
            // by ResourcesManager, so pull the current metrics from there.
            final DisplayMetrics newMetrics = super.getResources().getDisplayMetrics();
            uidResources.updateConfiguration(newConfig, newMetrics);
        }
    }
}
