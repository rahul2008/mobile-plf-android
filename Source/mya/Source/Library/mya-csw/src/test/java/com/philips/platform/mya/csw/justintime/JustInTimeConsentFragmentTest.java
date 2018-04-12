package com.philips.platform.mya.csw.justintime;

import com.philips.platform.mya.csw.BuildConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class JustInTimeConsentFragmentTest {

    private JustInTimeConsentFragment fragment;

    @Before
    public void setUp() throws Exception {
        fragment = new JustInTimeConsentFragment();
        SupportFragmentTestUtil.startFragment(fragment);
    }

    @Test
    public void givenFragmentStarted_whenX_thenY() {
        assertNotNull(fragment);
    }
}