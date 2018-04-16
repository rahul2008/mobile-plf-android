package com.philips.platform.mya.csw;

import android.content.Context;

import com.philips.platform.uid.thememanager.ThemeUtils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*" })
@PrepareForTest(ThemeUtils.class)
public class CswActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();


    @Test
    public void test() {

        mockStatic(ThemeUtils.class);
        when(ThemeUtils.getThemeResourceID((Context) any())).thenReturn(R.style.AccentAqua_UltraLight);

        ActivityController<CswActivity> activityController = Robolectric.buildActivity(CswActivity.class);

        CswActivity startedActivity = activityController.create().start().restart().get();
        assertNotNull(startedActivity);
    }
}