package com.philips.cdp.di.iap.activity;

import android.os.Build;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.BuildConfig;

import org.assertj.android.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowTypeface;

/**
 * Created by Apple on 30/06/16.
 */
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
@RunWith(RobolectricGradleTestRunner.class)
public class IAPActivityTest {

    ViewGroup mUPButtonLayout;
    IAPActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.buildActivity(IAPActivity.class).create().get();//new IAPActivity();
        activity.onResume();
    }

    @Test
    public void ensureActivityIsCreated() throws Exception {
        Assertions.assertThat(activity).isNotNull();
    }
}