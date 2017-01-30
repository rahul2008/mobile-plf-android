package com.philips.platform.appframework.homescreen;

import android.content.Context;

import com.philips.platform.baseapp.base.AppFrameworkApplication;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
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
}