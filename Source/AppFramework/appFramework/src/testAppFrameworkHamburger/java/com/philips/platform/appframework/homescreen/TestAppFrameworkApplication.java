package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.GradleRunner;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertTrue;

@RunWith(GradleRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class TestAppFrameworkApplication extends AppFrameworkApplication {

    @Test
    public void shouldPass() {
        assertTrue(true);
    }

    @Override
    protected void attachBaseContext(Context base) {

        try {
            super.attachBaseContext(base);
        } catch (RuntimeException ignored) {
            // Multidex support doesn't play well with Robolectric yet
        }
    }

    @Override
    public void onCreate() {
    }
}