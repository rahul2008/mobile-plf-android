package com.philips.amwelluapp.welcome;

import com.philips.amwelluapp.BuildConfig;
import com.philips.amwelluapp.activity.PTHLaunchActivity;
import com.philips.amwelluapp.CustomTestRunner;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

@RunWith(CustomTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)
public class PTHWelcomeFragmentTest {
    private PTHLaunchActivity mActivity;
    private PTHWelcomeFragment mWelcomeFragment;

    @Before
    public void setUp() throws Exception {
      //  mActivity = Robolectric.buildActivity(PTHLaunchActivity.class).create().get();
 /*       mActivity = Robolectric.buildActivity( PTHLaunchActivity.class )
                .create()
                .resume()
                .get();*/
        mActivity = Robolectric.setupActivity(PTHLaunchActivity.class);
        mWelcomeFragment = new PTHWelcomeFragment();
        //SupportFragmentTestUtil.startFragment(mWelcomeFragment);
    }

    @Test
    public void getFragmentLauncher() throws Exception {
        FragmentLauncher launcher = mWelcomeFragment.getFragmentLauncher();
        //assertThat(launcher).isNotNull();
        //assertThat(launcher).isInstanceOf(FragmentLauncher.class);
    }

    @Test
    public void onCreate() throws Exception {

    }

    @Test
    public void onCreateView() throws Exception {

    }

    @Test
    public void setFragmentLauncher() throws Exception {

    }

    @Test
    public void finishActivityAffinity() throws Exception {

    }

    @Test
    public void getFragmentActivity() throws Exception {

    }

    @Test
    public void showProgressBar() throws Exception {

    }

    @Test
    public void hideProgressBar() throws Exception {

    }

    @Test
    public void handleBackEvent() throws Exception {

    }

}