package com.philips.platform.baseapp.screens.introscreen.pager;

import android.view.View;
import android.widget.ImageView;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.baseapp.screens.dataservices.utility.Utility;
import com.philips.platform.baseapp.screens.splash.SplashFragmentTest;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class WelcomeVideoPagerFragmentTest {

    private WelcomeVideoPagerFragment welcomeVideoPagerFragment;
    private SplashFragmentTest.LaunchActivityMockAbstract launchActivity;
    private ImageView thumbNail, play;
    private TextureVideoView textureVideoView;
    private ActivityController<SplashFragmentTest.LaunchActivityMockAbstract> activityController;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        activityController = Robolectric.buildActivity(SplashFragmentTest.LaunchActivityMockAbstract.class);
        launchActivity = activityController.create().start().resume().visible().get();
        welcomeVideoPagerFragment =  new WelcomeVideoPagerFragment();
        launchActivity.getSupportFragmentManager().beginTransaction().add(welcomeVideoPagerFragment,null).commit();
        thumbNail = (ImageView) welcomeVideoPagerFragment.getView().findViewById(R.id.thumb_nail);
        textureVideoView = (TextureVideoView) welcomeVideoPagerFragment.getView().findViewById(R.id.onboarding_video);
        play = (ImageView) welcomeVideoPagerFragment.getView().findViewById(R.id.onboarding_play_button);
    }

    @Test
    public void onFetchErrorTest(){
        welcomeVideoPagerFragment.onFetchError();
        assertTrue(thumbNail.getVisibility() == View.VISIBLE);
    }

    @Test
    public void testVideoViewClickWithPlayVisible(){
        textureVideoView.performClick();
        assertTrue(welcomeVideoPagerFragment.isVideoPlaying());
    }

    @Test
    public void testVideoViewClickWithPlayInVisible(){
        play.setVisibility(View.INVISIBLE);
        textureVideoView.performClick();
        assertFalse(welcomeVideoPagerFragment.isVideoPlaying());
    }

    @Test
    public void onVideoPrepared() {
        welcomeVideoPagerFragment.onVideoPrepared();
        assertFalse(welcomeVideoPagerFragment.getView().findViewById(R.id.onboarding_video_progress_bar).getVisibility() == View.VISIBLE);
    }

    @Test
    public void onVideoEndTest() {
        welcomeVideoPagerFragment.onVideoEnd();
        assertFalse(welcomeVideoPagerFragment.isVideoPlaying());
    }

    @Test
    public void onVideoPlayTest() {
        welcomeVideoPagerFragment.onVideoPlay();
        assertEquals(View.GONE, thumbNail.getVisibility());
    }

    @Test
    public void thumbNailClickTest() {
        thumbNail.setVisibility(View.VISIBLE);
        thumbNail.performClick();
        assertTrue(welcomeVideoPagerFragment.isVideoPlaying());
    }

    @Test
    public void setUserVisibleHintForTrueConditionTest() {
        welcomeVideoPagerFragment.onVideoPlay();
        welcomeVideoPagerFragment.setUserVisibleHint(true);
        assertTrue(welcomeVideoPagerFragment.isVideoPlaying());
    }

    @Test
    public void setUserVisibleHintForFalseConditionTest() {
        welcomeVideoPagerFragment.setUserVisibleHint(false);
        assertFalse(welcomeVideoPagerFragment.isVideoPlaying());
    }

    @After
    public void tearDown() {
        activityController.pause().stop().destroy();
        welcomeVideoPagerFragment = null;
        launchActivity = null;
        thumbNail = null;
        play = null;
        textureVideoView = null;
        activityController = null;
    }
}


