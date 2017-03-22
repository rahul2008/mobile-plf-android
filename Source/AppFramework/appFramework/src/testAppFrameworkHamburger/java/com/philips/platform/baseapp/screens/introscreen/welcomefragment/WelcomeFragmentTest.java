package com.philips.platform.baseapp.screens.introscreen.welcomefragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.GradleRunner;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.TestAppFrameworkApplication;
import com.philips.platform.baseapp.screens.introscreen.LaunchActivity;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static junit.framework.Assert.assertNotNull;

/**
 * Created by philips on 3/21/17.
 */
@RunWith(GradleRunner.class)
@Config(manifest=Config.NONE,constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 21)
public class WelcomeFragmentTest {
    private WelcomeFragmentTestMock welcomeFragment;

    @Before
    public void setUp(){
        welcomeFragment =  new WelcomeFragmentTestMock();
        SupportFragmentTestUtil.startFragment(welcomeFragment);
    }

    @Test
    public void testWelcomeFragmentLaunch(){
        assertNotNull(welcomeFragment);
    }

    public static class WelcomeFragmentTestMock extends WelcomeFragment{
        View view;
        @Override
        protected void startLogging() {

        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view  = (LayoutInflater.from(getFragmentActivity())).inflate(R.layout.af_welcome_fragment, null);
            //View view = inflater.inflate(R.layout.af_welcome_fragment, container, false);
            return view;
        }
    }


}
