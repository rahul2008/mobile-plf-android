/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.ths.onboarding;

import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.consumer.Consumer;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class OnBoardingFragmentTest {

    private TestOnBoardingFragment mOnBoardingFragment;

    @Mock
    private
    AWSDK awsdkMock;

    @Mock
    private
    THSConsumer thsConsumerMock;

    @Mock
    private
    FragmentLauncher fragmentLauncherMock;

    @Mock
    private
    Consumer consumerMock;

    @Mock
    private
    AppInfraInterface appInfraInterface;

    @Mock
    private
    AppTaggingInterface appTaggingInterface;

    @Mock
    private
    LoggingInterface loggingInterface;

    @Mock
    private
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    private
    ActionBarListener actionBarListenerMock;

    @Mock
    private
    OnBoardingPresenter onBoardingPresenterMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        THSManager.getInstance().setAwsdk(awsdkMock);

        THSManager.getInstance().setThsConsumer(thsConsumerMock);
        THSManager.getInstance().setThsParentConsumer(thsConsumerMock);
        when(thsConsumerMock.getConsumer()).thenReturn(consumerMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);


        mOnBoardingFragment = new TestOnBoardingFragment();
        mOnBoardingFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mOnBoardingFragment);
    }

    @Test
    public void shouldHideAgreeButton_DependingOnFragmentArguments() throws Exception {
        THSManager.getInstance().setOnBoradingABFlow(THSConstants.THS_ONBOARDING_ABFLOW2);

        mOnBoardingFragment = new TestOnBoardingFragment();
        mOnBoardingFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mOnBoardingFragment);

        mOnBoardingFragment.onBoardingPresenter = onBoardingPresenterMock;
        mOnBoardingFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mOnBoardingFragment.getView().findViewById(R.id.btn_continue);

        assertThat(viewById.getVisibility()).isEqualTo(View.GONE);
    }

    @Test
    public void onClick() throws Exception {
        mOnBoardingFragment.onBoardingPresenter = onBoardingPresenterMock;
        mOnBoardingFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mOnBoardingFragment.getView().findViewById(R.id.breif_2);
        viewById.performClick();
        verify(onBoardingPresenterMock).onEvent(anyInt());
    }

    @Test
    public void onClick_btn_take_tour() throws Exception {
        mOnBoardingFragment.onBoardingPresenter = onBoardingPresenterMock;
        mOnBoardingFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mOnBoardingFragment.getView().findViewById(R.id.btn_continue);
        viewById.performClick();
        verify(onBoardingPresenterMock).onEvent(anyInt());
    }

}
