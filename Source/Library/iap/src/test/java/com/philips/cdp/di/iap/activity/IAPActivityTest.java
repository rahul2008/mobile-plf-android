package com.philips.cdp.di.iap.activity;

import com.philips.cdp.di.iap.BuildConfig;
import com.philips.cdp.di.iap.CustomRobolectricRunner;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class IAPActivityTest {
    IAPActivity activity;

    @Before
    public void setUp() {
        activity = Robolectric.buildActivity(IAPActivity.class).create().resume().get();
    }

}