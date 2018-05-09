/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.appframework.connectivitypowersleep.insights;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.MomentInsight;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.core.datatypes.Insight;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.trackers.DataServicesManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

/**
 * Created by philips on 9/5/17.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class InsightsFragmentTest {

    private ActivityController<TestActivity> activityController;
    private HamburgerActivity testActivity;
    private InsightsFragment insightsFragment;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private static InsightsContract.Action presenter;

    @Before
    public void setUp() {
        activityController = Robolectric.buildActivity(TestActivity.class);
        testActivity = activityController.create().start().get();
        insightsFragment = new InsightsFragmentMock();
        testActivity.getSupportFragmentManager().beginTransaction().add(insightsFragment, null).commit();
    }

    @Test
    public void testFragmentWithValidData() {
        RecyclerView recyclerView = (RecyclerView) insightsFragment.getView().findViewById(R.id.insights_recycler_view);
        assertEquals(0, recyclerView.getAdapter().getItemCount());
    }

    @Test
    public void onInsightLoadSuccessWithEmptyListTest() {
        insightsFragment.onInsightLoadSuccess(new ArrayList<Insight>());
        assertTrue(insightsFragment.getView().findViewById(R.id.insights_error).getVisibility() == View.VISIBLE);
    }

    @Test
    public void onInsightLoadSuccessWithNonEmptyListTest() {
        List<Insight> insightList = new ArrayList<Insight>();
        Insight insight = new MomentInsight();
        insightList.add(insight);
        insightsFragment.onInsightLoadSuccess(insightList);
        assertFalse(insightsFragment.getView().findViewById(R.id.insights_error).getVisibility() == View.VISIBLE);
    }

    @Test
    public void onInsightLoadErrorTest() {
        insightsFragment.onInsightLoadError("error");
        assertTrue(insightsFragment.getView().findViewById(R.id.insights_error).getVisibility() == View.VISIBLE);
    }

    @Test
    public void testGetActionBarTitle() {
        assertEquals(testActivity.getString(R.string.RA_DLS_ps_tips_title), insightsFragment.getActionbarTitle());
    }


    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        presenter = null;
        insightsFragment = null;
        testActivity = null;
        activityController = null;
    }

    public static class InsightsFragmentMock extends InsightsFragment {

        @Override
        protected InsightsContract.Action getInsightsPresenter() {
            return presenter;
        }

    }
}
