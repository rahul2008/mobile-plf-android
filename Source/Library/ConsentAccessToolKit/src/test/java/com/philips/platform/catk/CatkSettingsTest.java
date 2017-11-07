package com.philips.platform.catk;

import android.content.Context;

import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import static org.junit.Assert.*;


@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class CatkSettingsTest {

    @Test
    public void testStub() {

    }

/*
    @Test
    public void testCatkSettingContextnotNull() {
        CatkSettings catkSettings = new CatkSettings(ShadowApplication.getInstance().getApplicationContext());
        Context context = catkSettings.getContext();
        assertNotNull(context);
    }

    @Test
    public void testCatkSettingContextNull() {
        CatkSettings catkSettings = new CatkSettings(null);
        Context context = catkSettings.getContext();
        assertNull(context);
    }
    */
}