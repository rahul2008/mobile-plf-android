package com.philips.platform.baseapp.screens.introscreen;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.appframework.R;
import com.philips.platform.baseapp.screens.introscreen.welcomefragment.WelcomeFragment;
import com.philips.platform.baseapp.screens.splash.SplashFragment;

/**
 * Created by philips on 3/22/17.
 */

public class WelcomeFragmentInstrumentationTest extends ActivityInstrumentationTestCase2<LaunchActivity> {
    private LaunchActivity launchActivityTest;
    private WelcomeFragment welcomeFragmentTest;
    private SplashFragment splashFragmentTest;

    public WelcomeFragmentInstrumentationTest() {
        super(LaunchActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        launchActivityTest = getActivity();
        welcomeFragmentTest = new WelcomeFragment();
        getActivity().getSupportFragmentManager().beginTransaction().add(welcomeFragmentTest, null).commit();
        getInstrumentation().waitForIdleSync();

    }

    public void testPreconditions() {
        // Try to add a message to add context to your assertions.
        // These messages will be shown if
        // a tests fails and make it easy to
        // understand why a test failed
        assertNotNull("mTestActivity is null", launchActivityTest);

    }
    public void testSplashFragment(){
        TextView logo = (TextView) welcomeFragmentTest.getView().findViewById(R.id.welcome_skip_button);
        assertEquals(welcomeFragmentTest.getActivity().getResources().getString(R.string.RA_Skip_Button_Text),logo.getText().toString());

    }
}
