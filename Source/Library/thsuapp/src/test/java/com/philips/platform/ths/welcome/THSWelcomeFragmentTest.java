package com.philips.platform.ths.welcome;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.activity.THSLaunchActivity;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ProgressBar;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSWelcomeFragmentTest {
    private THSLaunchActivity mActivity;
    private WelcomeFragmentMock mWelcomeFragment;

    @Mock
    Button buttonInitMock;

    @Mock
    Bundle bundle;

    @Mock
    AWSDK awsdkMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    User userMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    ProgressBar progressBar;

    @Mock
    THSWelcomePresenter presenterMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    FragmentActivity activityMock;


    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);
        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);

        THSManager.getInstance().setAppInfra(appInfraInterface);

        mWelcomeFragment = new WelcomeFragmentMock();
        mWelcomeFragment.setActionBarListener(actionBarListenerMock);
        mWelcomeFragment.mPTHBaseFragmentProgressBar = progressBar;

        THSManager.getInstance().TEST_FLAG = true;
        THSManager.getInstance().setUser(userMock);

        when(userMock.getHsdpUUID()).thenReturn("123");
        when(userMock.getHsdpAccessToken()).thenReturn("123");
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
    public void hideProgressBar() throws Exception {
        mWelcomeFragment.hideProgressBar();
    }

    @Test
    public void handleBackEvent() throws Exception {

        Assert.assertEquals(mWelcomeFragment.handleBackEvent(), false);
    }

    @Test
    public void getContainerID() {
        mWelcomeFragment.setFragmentLauncher(fragmentLauncherMock);
        int id = mWelcomeFragment.getContainerID();
        assertThat(id).isNotNull();
        assertThat(id).isInstanceOf(Integer.class);
    }

    @Test
    public void onClickappointments() throws Exception {
        SupportFragmentTestUtil.startFragment(mWelcomeFragment);
        mWelcomeFragment.presenter = presenterMock;
        mWelcomeFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mWelcomeFragment.getView().findViewById(R.id.appointments);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.appointments);
    }

    @Test
    public void onClickvisit_history() throws Exception {
        SupportFragmentTestUtil.startFragment(mWelcomeFragment);
        mWelcomeFragment.presenter = presenterMock;
        mWelcomeFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mWelcomeFragment.getView().findViewById(R.id.visit_history);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.visit_history);
    }

    @Test
    public void onClickhow_it_works() throws Exception {
        SupportFragmentTestUtil.startFragment(mWelcomeFragment);
        mWelcomeFragment.presenter = presenterMock;
        mWelcomeFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mWelcomeFragment.getView().findViewById(R.id.how_it_works);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.how_it_works);
    }

    @Test
    public void onClickths_start() throws Exception {
        SupportFragmentTestUtil.startFragment(mWelcomeFragment);
        mWelcomeFragment.presenter = presenterMock;
        mWelcomeFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mWelcomeFragment.getView().findViewById(R.id.ths_start);
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.ths_start);
    }

    @Test
    public void updateViewtest() throws Exception {
        SupportFragmentTestUtil.startFragment(mWelcomeFragment);
        View viewById = mWelcomeFragment.getView().findViewById(R.id.how_it_works);
        assertThat(viewById.isEnabled());
        viewById = mWelcomeFragment.getView().findViewById(R.id.appointments);
        assertThat(viewById.isEnabled());
        viewById = mWelcomeFragment.getView().findViewById(R.id.visit_history);
        assertThat(viewById.isEnabled());
    }

    @Test
    public void testFinishAffinity(){
        SupportFragmentTestUtil.startFragment(mWelcomeFragment);
        mWelcomeFragment.finishActivityAffinity();
        boolean isAlive  = mWelcomeFragment.getActivity().isDestroyed();
        assertThat(isAlive);
    }

    @Test
    public void testDestroy(){
        SupportFragmentTestUtil.startFragment(mWelcomeFragment);
        mWelcomeFragment.onDestroy();
        boolean isAlive  = mWelcomeFragment.getActivity().isDestroyed();
        assertThat(isAlive);
    }
}
