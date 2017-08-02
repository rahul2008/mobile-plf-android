package com.philips.platform.baseapp.screens.introscreen.pager;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.baseapp.screens.splash.SplashFragmentTest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

/**
 * Created by 310207283 on 7/31/2017.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class WelcomeAdapterTest {

    private WelcomePagerAdapter welcomePagerAdapter;
    private SplashFragmentTest.LaunchActivityMockAbstract launchActivity;

    @Before
    public void setUp(){
        launchActivity = Robolectric.buildActivity(SplashFragmentTest.LaunchActivityMockAbstract.class).create().start().resume().visible().get();
        welcomePagerAdapter =  new WelcomePagerAdapter(launchActivity.getSupportFragmentManager());
    }

    @Test
    public void getCountTest() {
        assertEquals(9, welcomePagerAdapter.getCount());
    }

    @Test
    public void getPageTitleTest() {
        assertEquals("Page 1", welcomePagerAdapter.getPageTitle(0));
    }

    @Test
    public void getItemForVideoFragmentTest() {
        assertTrue(welcomePagerAdapter.getItem(0) instanceof WelcomeVideoPagerFragment);
    }

    @Test
    public void getItemWelcomeScreen1Test() {
        assertTrue(welcomePagerAdapter.getItem(1) instanceof WelcomePagerFragment);
    }

    @Test
    public void getItemForInvalidPosition() {
        assertNull(welcomePagerAdapter.getItem(11));
    }

}
