package com.philips.platform.baseapp.screens.introscreen.pager;

import android.content.res.Configuration;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
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
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(CustomRobolectricRunner.class)
@Config(application = TestAppFrameworkApplication.class, sdk=25)
public class WelcomeVideoPagerFragmentTest {

    private WelcomeVideoPagerFragment welcomeVideoPagerFragment;
    private SplashFragmentTest.LaunchActivityMockAbstract launchActivity;
    private ImageView thumbNail, play;
    private TextureVideoView textureVideoView;
    private ActivityController<SplashFragmentTest.LaunchActivityMockAbstract> activityController;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private static WelcomeVideoFragmentContract.Presenter presenter;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        presenter = mock(WelcomeVideoPresenter.class);
        activityController = Robolectric.buildActivity(SplashFragmentTest.LaunchActivityMockAbstract.class);
        launchActivity = activityController.create().start().resume().visible().get();
        welcomeVideoPagerFragment = new WelcomeVideoPagerFragmentMock();
        launchActivity.getSupportFragmentManager().beginTransaction().add(welcomeVideoPagerFragment, null).commit();
        thumbNail = (ImageView) welcomeVideoPagerFragment.getView().findViewById(R.id.thumb_nail);
        textureVideoView = (TextureVideoView) welcomeVideoPagerFragment.getView().findViewById(R.id.onboarding_video);
        play = (ImageView) welcomeVideoPagerFragment.getView().findViewById(R.id.onboarding_play_button);
    }

    @Test
    public void onFetchErrorTest() {
        welcomeVideoPagerFragment.onFetchError();
        assertTrue(thumbNail.getVisibility() == View.VISIBLE);
    }

    @Test
    public void testVideoViewClickWithPlayVisible() {
        textureVideoView.performClick();
        assertTrue(welcomeVideoPagerFragment.isVideoPlaying());
    }

    @Test
    public void testVideoViewClickWithPlayInVisible() {
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
        verify(presenter, times(1)).fetchVideoDataSource();
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
        presenter = null;
        textureVideoView = null;
        activityController = null;
    }

    public static class WelcomeVideoPagerFragmentMock extends WelcomeVideoPagerFragment {
        @Override
        protected WelcomeVideoFragmentContract.Presenter getWelcomeVideoPagerPresenter() {
            return presenter;
        }
    }
}


