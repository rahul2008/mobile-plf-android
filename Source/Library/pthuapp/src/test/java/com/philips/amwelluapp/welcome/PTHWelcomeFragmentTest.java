package com.philips.amwelluapp.welcome;

import android.os.Bundle;

import com.americanwell.sdk.AWSDK;
import com.philips.amwelluapp.ApplicationTestClass;
import com.philips.amwelluapp.BuildConfig;
import com.philips.amwelluapp.CustomRobolectricRunnerAmwel;
import com.philips.amwelluapp.activity.PTHLaunchActivity;
import com.philips.amwelluapp.base.PTHBaseFragment;
import com.philips.amwelluapp.utility.PTHManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.ProgressBar;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(CustomRobolectricRunnerAmwel.class)
@Config(constants = BuildConfig.class, sdk = 21, application = ApplicationTestClass.class, libraries = "../../../../../../../build/intermediates/exploded-aar/com.philips.cdp/uid/0.1.1-SNAPSHOT.20170623170225")
public class PTHWelcomeFragmentTest {
    private PTHLaunchActivity mActivity;
    private WelcomeFragmentMock mWelcomeFragment;

    @Mock
    Bundle bundle;

    @Mock
    AWSDK awsdkMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    ProgressBar progressBar;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        PTHManager.getInstance().setAwsdk(awsdkMock);
        mActivity = Robolectric.buildActivity(PTHLaunchActivity.class).create().get();
        mWelcomeFragment = new WelcomeFragmentMock();
        mWelcomeFragment.setActionBarListener(actionBarListenerMock);
      //  ProgressBarMock progressBar = new ProgressBarMock(mActivity.getApplicationContext());
        mWelcomeFragment.mPTHBaseFragmentProgressBar = progressBar;
    }

    @Test
    public void getFragmentLauncher() throws Exception {
        mWelcomeFragment.setFragmentLauncher(fragmentLauncherMock);
        FragmentLauncher launcher = mWelcomeFragment.getFragmentLauncher();
        assertThat(launcher).isNotNull();
        assertThat(launcher).isInstanceOf(FragmentLauncher.class);
    }

    @Test
    public void setFragmentLauncher() throws Exception {
        mWelcomeFragment.setFragmentLauncher(fragmentLauncherMock);
    }

    @Test
    public void finishActivityAffinity() throws Exception {
        SupportFragmentTestUtil.startFragment(mWelcomeFragment);
        mWelcomeFragment.finishActivityAffinity();
    }

    @Test
    public void getFragmentActivity() throws Exception {
        SupportFragmentTestUtil.startFragment(mWelcomeFragment);
        mWelcomeFragment.getFragmentActivity();
    }

    @Test
    public void showProgressBar() throws Exception {
        mWelcomeFragment.showProgressBar();
    }

    @Test
    public void hideProgressBar() throws Exception {
        mWelcomeFragment.hideProgressBar();
    }

    @Test
    public void handleBackEvent() throws Exception {
        mWelcomeFragment.handleBackEvent();
    }

    @Test
    public void getContainerID() {
        mWelcomeFragment.setFragmentLauncher(fragmentLauncherMock);
        mWelcomeFragment.getContainerID();
    }

}