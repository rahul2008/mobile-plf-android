package com.philips.platform.mya.csw.permission;

import com.philips.platform.mya.csw.BuildConfig;
import com.philips.platform.mya.csw.CswActivity;
import com.philips.platform.uid.thememanager.ThemeUtils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.assertNotNull;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
@PrepareForTest(ThemeUtils.class)
public class PermissionViewIntegrationTest {

    @Rule
    public PowerMockRule rule = new PowerMockRule();

    private PermissionView fragment;

    @Before
    public void setup() {
        initMocks(this);
        PowerMockito.mockStatic(ThemeUtils.class);

        fragment = new PermissionView();
        SupportFragmentTestUtil.startFragment(fragment, CswActivity.class);
    }

    @Test
    public void testX() {
        assertNotNull(fragment);
//        assertNotNull(activityController.get());
    }
}
