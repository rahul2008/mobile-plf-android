package com.philips.platform.ths.welcome;

import android.os.Bundle;

import com.americanwell.sdk.AWSDK;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.ProgressBar;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static org.assertj.core.api.Java6Assertions.assertThat;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSWelcomeFragmentTest {
    private THSLaunchActivity mActivity;
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
        THSManager.getInstance().setAwsdk(awsdkMock);
        mActivity = Robolectric.buildActivity(THSLaunchActivity.class).create().get();
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
        Assert.assertEquals(mWelcomeFragment.handleBackEvent(),true);
    }

    @Test
    public void getContainerID() {
        mWelcomeFragment.setFragmentLauncher(fragmentLauncherMock);
        int id = mWelcomeFragment.getContainerID();
        assertThat(id).isNotNull();
        assertThat(id).isInstanceOf(Integer.class);
    }

}