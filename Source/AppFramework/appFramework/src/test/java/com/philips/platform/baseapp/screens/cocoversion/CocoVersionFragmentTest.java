/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.cocoversion;

import android.support.v7.widget.RecyclerView;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class CocoVersionFragmentTest extends TestCase {
    private HamburgerActivity hamburgerActivity = null;
    private CocoVersionFragment cocoVersionFragment;
 //  CocoVersionAdapter.CocoInfoViewHolder candyViewHolder;
    CocoVersionAdapter  cocoVersionAdapter;
    private ActivityController<TestActivity> activityController;

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        cocoVersionFragment=null;
        activityController=null;
        hamburgerActivity=null;
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        activityController= Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity=activityController.create().start().get();
        cocoVersionFragment = new CocoVersionFragment();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(cocoVersionFragment, "CoCoVersion").commit();
        RecyclerView recyclerView = (RecyclerView) cocoVersionFragment.getView().findViewById(R.id.coco_version_view);
        cocoVersionAdapter = (CocoVersionAdapter) recyclerView.getAdapter();
      //  candyViewHolder = cocoVersionAdapter.onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application), 0);
//

    }


    @Test
    public void testadapterSize()
    {

        assertEquals(8,cocoVersionAdapter.getItemCount());
    }
}