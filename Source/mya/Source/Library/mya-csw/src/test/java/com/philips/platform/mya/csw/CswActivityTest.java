package com.philips.platform.mya.csw;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.platform.mya.csw.utils.CswTestApplication;
import com.philips.platform.mya.csw.utils.ShadowXIConTextView;
import com.philips.platform.uid.thememanager.ThemeUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, application = CswTestApplication.class, shadows = {ShadowXIConTextView.class})
@PowerMockIgnore({ "org.mockito.*", "org.robolectric.*", "android.*", "com.sun.org.apache.xerces.internal.jaxp.*" })
@PrepareForTest({ThemeUtils.class, FontLoader.class})
public class CswActivityTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    @Mock
    private FontLoader fontLoaderMock;

    @Before
    public void setUp() {
        initMocks(this);
        // Init statics
        mockStatic(ThemeUtils.class);
        when(ThemeUtils.getThemeResourceID((Context) any())).thenReturn(R.style.AccentAqua_UltraLight);
        mockStatic(FontLoader.class);
        when(FontLoader.getInstance()).thenReturn(fontLoaderMock);
    }

    @Test
    public void test() {
        Intent testIntent = new Intent();
        testIntent.putExtras(new Bundle());
        testIntent.putExtras(CswConstants.DLS_THEME, )

        // Create builder subject under test
        ActivityController<CswActivity> activityController = Robolectric.buildActivity(CswActivity.class, testIntent);
        CswActivity startedActivity = activityController.create().start().resume().get();

        assertNotNull(startedActivity);
    }
}