//package com.philips.cdp.di.iap.activity;
//
//import android.os.Bundle;
//
//import com.philips.cdp.di.iap.BuildConfig;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.robolectric.Robolectric;
//import org.robolectric.RobolectricGradleTestRunner;
//import org.robolectric.annotation.Config;
//import org.robolectric.util.ActivityController;
//
//import static org.mockito.MockitoAnnotations.initMocks;
//
///**
// * (C) Koninklijke Philips N.V., 2015.
// * All rights reserved.
// */
//@RunWith(RobolectricGradleTestRunner.class)
//@Config(constants = BuildConfig.class, sdk = 21)
//public class MainActivityTest {
//    private MainActivity activity;
//    @Mock
//    private Bundle mockBundle;
//    private ActivityController<MainActivity> activityController;
//
//    @Before
//    public void setup() throws Exception {
//        initMocks(this);
//        activityController = Robolectric.buildActivity(MainActivity.class);
//
//        activity = activityController.get();
//    }
//
//    @Test
//    public void testSetContenetView() {
//    }
//}