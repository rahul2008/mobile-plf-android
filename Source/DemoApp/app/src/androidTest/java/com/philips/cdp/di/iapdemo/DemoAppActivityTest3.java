package com.philips.cdp.di.iapdemo;


import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class DemoAppActivityTest3 {

    @Rule
    public ActivityTestRule<DemoAppActivity> mActivityTestRule = new ActivityTestRule<>(DemoAppActivity.class);

    @Test
    public void demoAppActivityTest3() {
    }

}
