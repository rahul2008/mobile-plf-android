/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.introscreen.welcomefragment;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.introscreen.pager.WelcomePagerAdapter;
import com.philips.platform.baseapp.screens.splash.SplashFragmentTest;
import com.shamanland.fonticon.FontIconView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;
import org.robolectric.shadows.ShadowLooper;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 25)
public class WelcomeFragmentTest {
    private SplashFragmentTest.LaunchActivityMockAbstract launchActivity;
    private ImageView logo;
    private WelcomeFragmentMockAbstract welcomeFragment;
    private ViewPager pager;
    private FontIconView leftArrow,rightArrow;

    @Before
    public void setUp(){

        launchActivity = Robolectric.buildActivity(SplashFragmentTest.LaunchActivityMockAbstract.class).create().start().get();
        welcomeFragment =  new WelcomeFragmentMockAbstract();
        launchActivity.getSupportFragmentManager().beginTransaction().add(welcomeFragment,null).commit();

    }

    @Test
    public void testWelcomeFragmentLaunch(){
        assertNotNull(welcomeFragment);
    }

    @Test
    public void testWelcomeFragmentViews(){
        TextView logo = (TextView) welcomeFragment.getView().findViewById(R.id.welcome_skip_button);
        assertEquals(welcomeFragment.getActivity().getResources().getString(R.string.RA_Skip_Button_Text),logo.getText().toString());
    }

    private void setAdapterForPager(){
        pager = (ViewPager) welcomeFragment.getView().findViewById(R.id.welcome_pager);
        ShadowLooper.pauseMainLooper();
        pager.setAdapter(new WelcomePagerAdapter(welcomeFragment.getActivity().getSupportFragmentManager()));
        ShadowApplication.getInstance().getForegroundThreadScheduler().advanceToLastPostedRunnable();
        assertNotNull(pager);
    }
    @Test
    public void testViewPager(){
        setAdapterForPager();
        pager.setCurrentItem(0);
        leftArrow = (FontIconView) welcomeFragment.getView().findViewById(R.id.welcome_leftarrow);
        rightArrow = (FontIconView) welcomeFragment.getView().findViewById(R.id.welcome_rightarrow);
        assertEquals(FontIconView.GONE,leftArrow.getVisibility());
        assertEquals(FontIconView.VISIBLE,rightArrow.getVisibility());
    }

    @Test
    public void testArrowClicks(){
        setAdapterForPager();
        pager.setCurrentItem(1);
        leftArrow = (FontIconView) welcomeFragment.getView().findViewById(R.id.welcome_leftarrow);
        rightArrow = (FontIconView) welcomeFragment.getView().findViewById(R.id.welcome_rightarrow);
        welcomeFragment.onClick(rightArrow);
        assertEquals(FontIconView.VISIBLE,leftArrow.getVisibility());
        assertEquals(FontIconView.VISIBLE,rightArrow.getVisibility());

    }

    @Test
    public void testBackPressed(){
        setAdapterForPager();
        pager.setCurrentItem(0);
        boolean handleBack = welcomeFragment.handleBackEvent();
        assertEquals(true,handleBack);
    }
    public static class WelcomeFragmentMockAbstract extends WelcomeFragment {
        View view;
        @Override
        protected void startLogging() {

        }
    }


}
