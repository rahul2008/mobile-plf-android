package com.philips.platform.baseapp.screens.cocoversion;

import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

/**
 * Created by philips on 4/20/17.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class CocoVersionFragmentTest extends TestCase {
    private HamburgerActivity hamburgerActivity = null;
    private CocoVersionFragment cocoVersionFragment;
    private ArrayList<CocoVersionItem> chapterArrayList;
    CocoVersionAdapter.CocoInfoViewHolder candyViewHolder;
    CocoVersionAdapter  cocoVersionAdapter;


    @Before
    public void setUp() throws Exception {
        super.setUp();
        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().get();
        cocoVersionFragment = new CocoVersionFragment();
        hamburgerActivity.getSupportFragmentManager().beginTransaction().add(cocoVersionFragment, "CoCoVersion").commit();
        RecyclerView recyclerView = (RecyclerView) cocoVersionFragment.getView().findViewById(R.id.coco_version_view);
        cocoVersionAdapter = (CocoVersionAdapter) recyclerView.getAdapter();
        candyViewHolder = cocoVersionAdapter.onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application), 0);


    }

    @Test
    public void testcocoVersionFragment() {

        assertNotNull(cocoVersionFragment);
    }

    @Test
    public void testShowCoCoName() {
        cocoVersionAdapter.onBindViewHolder(candyViewHolder, 0);
        assertEquals("AppInfra", candyViewHolder.CocoName.getText().toString());

    }

    @Test
    public void testShowCoCoVersion() {

        cocoVersionAdapter.onBindViewHolder(candyViewHolder, 2);
        assertEquals("3.7.5-SNAPSHOT", candyViewHolder.CocoVersion.getText().toString());

    }

    @Test
    public void testadapterSize()
    {

        assertEquals(8,cocoVersionAdapter.getItemCount());
    }
}