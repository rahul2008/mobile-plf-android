package com.philips.amwelluapp.welcome;

import android.app.Activity;
import android.os.Bundle;

import com.philips.amwelluapp.BuildConfig;
import com.philips.amwelluapp.LibraryProjectTestRunner;
import com.philips.amwelluapp.activity.PTHLaunchActivity;
import com.philips.amwelluapp.CustomTestRunner;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(LibraryProjectTestRunner.class) @Config(constants = BuildConfig.class, sdk = 21)
public class PTHWelcomeFragmentTest {
    private PTHLaunchActivity mActivity;
    private PTHWelcomeFragment mWelcomeFragment;

    @Mock
    Bundle bundle;

    @Before
    public void setUp() throws Exception {
      //  mActivity = Robolectric.setupActivity(PTHLaunchActivity.class);
        mActivity = Robolectric.buildActivity(PTHLaunchActivity.class).create().resume().get();
        mWelcomeFragment = new PTHWelcomeFragment();
    }

    @Test
    public void getFragmentLauncher() throws Exception {
        FragmentLauncher launcher = mWelcomeFragment.getFragmentLauncher();
        assertThat(launcher).isNotNull();
        assertThat(launcher).isInstanceOf(FragmentLauncher.class);
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