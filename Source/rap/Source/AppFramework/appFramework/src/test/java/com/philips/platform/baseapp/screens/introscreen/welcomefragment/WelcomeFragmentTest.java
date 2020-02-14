/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.baseapp.screens.introscreen.welcomefragment;

import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.base.AbstractUIBasePresenter;
import com.philips.platform.baseapp.screens.splash.SplashFragmentTest;
import com.philips.platform.uid.view.widget.Button;
import com.shamanland.fonticon.FontIconView;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class WelcomeFragmentTest {
    private SplashFragmentTest.LaunchActivityMockAbstract launchActivity;
    private WelcomeFragmentMockAbstract welcomeFragment;
    private ViewPager pager;
    private ImageView rightArrow;
    private Button environmentSelection;
    private ActivityController<SplashFragmentTest.LaunchActivityMockAbstract> activityController;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private static WelcomeFragmentPresenter presenter;

    @After
    public void tearDown(){
        activityController.pause().stop().destroy();
        welcomeFragment=null;
        pager=null;
        environmentSelection = null;
        launchActivity=null;
        presenter = null;
    }
    @Before
    public void setUp(){
        activityController=Robolectric.buildActivity(SplashFragmentTest.LaunchActivityMockAbstract.class);
        launchActivity=activityController.create().start().get();
//        launchActivity = Robolectric.buildActivity(SplashFragmentTest.LaunchActivityMockAbstract.class).create().start().get();
        welcomeFragment =  new WelcomeFragmentMockAbstract();
        launchActivity.getSupportFragmentManager().beginTransaction().add(welcomeFragment,null).commit();
        pager = (ViewPager) welcomeFragment.getView().findViewById(R.id.welcome_pager);
        environmentSelection = (Button) welcomeFragment.getView().findViewById(R.id.environment_selection);

    }

    @Test
    public void testArrowClicks(){
        pager.setCurrentItem(1);
        rightArrow = (ImageView) welcomeFragment.getView().findViewById(R.id.welcome_rightarrow);
        rightArrow.performClick();
        assertEquals(FontIconView.VISIBLE,rightArrow.getVisibility());

    }

    @Test
    public void testBackPressed(){
        pager.setCurrentItem(0);
        boolean handleBack = welcomeFragment.handleBackEvent();
        assertEquals(true,handleBack);
    }

    @Test
    public void testDoneVisible() {
        pager.setCurrentItem(8);
        rightArrow = (ImageView) welcomeFragment.getView().findViewById(R.id.welcome_rightarrow);
        assertFalse(View.VISIBLE == rightArrow.getVisibility());
    }

    @Test
    public void testEnvironmentSelectionVisibility() {
        pager.setCurrentItem(0);
        assertTrue(View.VISIBLE == environmentSelection.getVisibility());

        pager.setCurrentItem(1);
        assertFalse(View.VISIBLE == environmentSelection.getVisibility());
    }
    @Test
    public void testEnvironmentSelectionLongClick() {
        Button environmentSelection = (Button) welcomeFragment.getView().findViewById(R.id.environment_selection);
        environmentSelection.performLongClick();
        verify(presenter).onEvent(anyInt());
    }

    @Test
    public void testClearAdapter() {
        welcomeFragment.clearAdapter();
        assertNull(pager.getAdapter());
    }

    public static class WelcomeFragmentMockAbstract extends WelcomeFragment {
        View view;
        @Override
        protected void startLogging() {

        }

        @Override
        protected AbstractUIBasePresenter getWelcomePresenter() {
            return presenter;
        }
    }


}
