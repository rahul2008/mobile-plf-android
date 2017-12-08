/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
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
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.registration.dependantregistration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class OnBoardingFragmentTest {

    OnBoardingFragment mOnBoardingFragment;

    @Mock
    AWSDK awsdkMock;

    @Mock
    THSConsumer thsConsumerMock;

    @Mock
    FragmentLauncher fragmentLauncherMock;

    @Mock
    Consumer consumerMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
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


        mOnBoardingFragment = new OnBoardingFragment();
        mOnBoardingFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mOnBoardingFragment);
    }

    @Test
    public void onClick() throws Exception {
        mOnBoardingFragment.onBoardingPresenter = onBoardingPresenterMock;
        mOnBoardingFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mOnBoardingFragment.getView().findViewById(R.id.tv_skip);
        viewById.performClick();
        verify(onBoardingPresenterMock).onEvent(anyInt());
    }

    @Test
    public void onClick_btn_take_tour() throws Exception {
        mOnBoardingFragment.onBoardingPresenter = onBoardingPresenterMock;
        mOnBoardingFragment.setFragmentLauncher(fragmentLauncherMock);
        final View viewById = mOnBoardingFragment.getView().findViewById(R.id.btn_take_tour);
        viewById.performClick();
        verify(onBoardingPresenterMock).onEvent(anyInt());
    }

}