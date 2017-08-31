package com.philips.platform.uid.utils;

import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import com.philips.platform.uid.thememanager.ThemeUtils;

public class UIDActivity extends AppCompatActivity {

    private static final String TAG = UIDActivity.class.getSimpleName();
    private Resources uidResources;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (getThemeIDFromManifest() == 0) {
            setTheme(ThemeUtils.getThemeResourceID(getApplicationContext()));
            ThemeUtils.restoreAccentAndNavigation(this);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public Resources getResources() {
        if (uidResources == null) {
            uidResources = new UIDResources(super.getResources());
        }
        return uidResources;
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

    private int getThemeIDFromManifest() {
        int themeID = 0;
        try {
            ActivityInfo info = getPackageManager().getActivityInfo(getComponentName(), 0);
            themeID = info.theme;
        } catch (PackageManager.NameNotFoundException e) {
            UIDLog.e(TAG, "Unable to find activity info for " + e.getMessage());
        }
        return themeID;
    }
}
