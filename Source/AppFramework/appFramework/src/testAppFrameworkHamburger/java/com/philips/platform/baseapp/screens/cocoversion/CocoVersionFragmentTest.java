package com.philips.platform.baseapp.screens.cocoversion;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

/**
 * Created by philips on 4/20/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class CocoVersionFragmentTest extends TestCase {
    private HamburgerActivity hamburgerActivity = null;
    private CocoVersionFragment cocoVersionFragment;
    CocoVersionAdapter.CocoInfoViewHolder candyViewHolder;
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
        candyViewHolder = cocoVersionAdapter.onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application), 0);


    }


    @Test
    public void testadapterSize()
    {

        assertEquals(8,cocoVersionAdapter.getItemCount());
    }
}