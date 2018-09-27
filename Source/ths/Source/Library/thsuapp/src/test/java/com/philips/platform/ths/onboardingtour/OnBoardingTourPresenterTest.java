/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.onboardingtour;

import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.onboarding.OnBoardingFragment;
import com.philips.platform.ths.registration.THSRegistrationFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.philips.platform.ths.onboardingtour.OnBoardingTourPresenter.ARG_PAGER_FLOW;
import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OnBoardingTourPresenterTest {

    private OnBoardingTourPresenter mOnBoardingTourPresenter;

    @Mock
    private
    OnBoardingTourFragment onBoardingTourFragmentMock;

    @Mock
    private AppInfraInterface appInfraInterface;

    @Mock
    private AppTaggingInterface appTaggingInterface;

    @Mock
    private AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    private LoggingInterface loggingInterface;

    @Mock
    private ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    private OnBoardingTourPagerAdapter onBoardingTourPagerAdapterMock;

    @Captor
    private ArgumentCaptor<Bundle> bundleCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mOnBoardingTourPresenter = new OnBoardingTourPresenter(onBoardingTourFragmentMock);

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        when(onBoardingTourFragmentMock.getOnBoardingTourPagerAdapter()).thenReturn(onBoardingTourPagerAdapterMock);
        when(onBoardingTourFragmentMock.getPagePosition()).thenReturn(1);
        when(onBoardingTourPagerAdapterMock.getPageTitle(1)).thenReturn("somethins");
    }

    @Test
    public void onEvent_welcome_rightarrow() throws Exception {
        mOnBoardingTourPresenter.onEvent(R.id.welcome_rightarrow);
    }

    @Test
    public void onEvent_welcome_leftarrow() throws Exception {
        mOnBoardingTourPresenter.onEvent(R.id.welcome_leftarrow);
    }

    @Test
    public void onEvent_welcome_welcome_skip_button() throws Exception {
        mOnBoardingTourPresenter.onEvent(R.id.welcome_skip_button);
        verifyOnBoardingLaunched();
    }

    @Test
    public void onEvent_btn_startnow() throws Exception {
        mOnBoardingTourPresenter.onEvent(R.id.btn_startnow);
        verifyOnBoardingLaunched();
    }

    @Test
    public void shouldLaunchWelcomeFragmentForReturningUser() throws Exception {
        THSManager.getInstance().setIsReturningUser(true);
        mOnBoardingTourPresenter.onEvent(R.id.btn_termsConditions);
        verify(onBoardingTourFragmentMock).addFragment(any(THSWelcomeFragment.class),eq(THSWelcomeFragment.TAG),(Bundle)isNull(),eq(false));
    }

    @Test
    public void shouldLaunchRegistrationFragmentForNewUser() throws Exception {
        THSManager.getInstance().setIsReturningUser(false);
        mOnBoardingTourPresenter.onEvent(R.id.btn_termsConditions);
        verify(onBoardingTourFragmentMock).addFragment(any(THSRegistrationFragment.class),eq(THSRegistrationFragment.TAG),(Bundle)isNull(),eq(false));
    }

    private void verifyOnBoardingLaunched() {
        verify(onBoardingTourFragmentMock).addFragment(any(OnBoardingFragment.class),eq(OnBoardingFragment.TAG),bundleCaptor.capture(),anyBoolean());

        assertThat(bundleCaptor.getValue().getBoolean(ARG_PAGER_FLOW)).isEqualTo(false);
    }

}