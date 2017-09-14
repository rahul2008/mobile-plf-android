/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep.insights;

import android.support.v7.widget.RecyclerView;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

/**
 * Created by philips on 9/5/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class InsightsFragmentTest {

    private ActivityController<TestActivity> activityController;
    private HamburgerActivity testActivity;
    private InsightsFragment insightsFragment;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(TestActivity.class);
        testActivity = activityController.create().start().get();
        insightsFragment = new InsightsFragment();
        testActivity.getSupportFragmentManager().beginTransaction().add(insightsFragment, null).commit();
    }

    @Test
    public void testFragmentWithValidData() {
        RecyclerView recyclerView = (RecyclerView) insightsFragment.getView().findViewById(R.id.insights_recycler_view);
        assertEquals(3, recyclerView.getAdapter().getItemCount());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        insightsFragment = null;
        testActivity = null;
        activityController = null;
    }
}
