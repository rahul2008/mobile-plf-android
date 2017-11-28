/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.view.View;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.intake.THSVisitContext;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSPreOnBoardingTourFragmentTest {
    THSPreWelcomeFragment mTHSPreWelcomeFragment;

    @Mock
    THSPreWelcomePresenter thsPreWelcomePresenter;

    @Mock
    AppConfigurationInterface appConfigurationInterface;

    @Mock
    AppInfraInterface appInfraInterfaceMock;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;


    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);
        mTHSPreWelcomeFragment = new THSPreWelcomeFragmentTestMock();
        mTHSPreWelcomeFragment.setActionBarListener(actionBarListenerMock);
        SupportFragmentTestUtil.startFragment(mTHSPreWelcomeFragment);
        mTHSPreWelcomeFragment.mThsPreWelcomeScreenPresenter = thsPreWelcomePresenter;
    }

    @Test
    public void onClick() throws Exception {
        final View viewById = mTHSPreWelcomeFragment.getView().findViewById(R.id.ths_go_see_provider);
        viewById.performClick();
        verify(thsPreWelcomePresenter).onEvent(R.id.ths_go_see_provider);
    }

    @Test
    public void onClickths_video_consults() throws Exception {
        final View viewById = mTHSPreWelcomeFragment.getView().findViewById(R.id.ths_video_consults);
        viewById.performClick();
        verify(thsPreWelcomePresenter).onEvent(R.id.ths_video_consults);
    }

    @Test
    public void onClickths_ths_licence() throws Exception {
        final View viewById = mTHSPreWelcomeFragment.getView().findViewById(R.id.ths_licence);
        viewById.performClick();
        verify(thsPreWelcomePresenter).onEvent(R.id.ths_licence);
    }

    @Test
    public void onClickths_terms_and_conditions_cross() throws Exception {
        final View viewById = mTHSPreWelcomeFragment.getView().findViewById(R.id.ths_terms_and_conditions_cross);
        viewById.performClick();
        final THSVisitContext pthVisitContext = THSManager.getInstance().getPthVisitContext();
        assertNull(pthVisitContext);
    }

    @Test
    public void showTermsAndConditions(){
        mTHSPreWelcomeFragment.showTermsAndConditions("ssss");
    }

}