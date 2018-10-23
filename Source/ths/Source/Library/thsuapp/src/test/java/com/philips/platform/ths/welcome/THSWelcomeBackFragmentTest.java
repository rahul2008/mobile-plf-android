/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.practice.PracticeInfo;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderType;
import com.americanwell.sdk.manager.PracticeProvidersManager;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.CustomRobolectricRunnerAmwel;
import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.shadows.ShadowLog;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.Date;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(CustomRobolectricRunnerAmwel.class)
public class THSWelcomeBackFragmentTest {

    @Mock
    AWSDK awsdkMock;

    @Mock
    PracticeInfo practiceInfoMock;

    THSWelcomeBackFragment mThsWelcomeBackFragment;

    @Mock
    THSWelcomeBackPresenter presenterMock;

    @Mock
    ActionBarListener actionBarListenerMock;

    @Mock
    AppInfraInterface appInfraInterface;

    @Mock
    AppTaggingInterface appTaggingInterface;

    @Mock
    LoggingInterface loggingInterface;

    @Mock
    ServiceDiscoveryInterface serviceDiscoveryMock;

    @Mock
    AppConfigurationInterface appConfigurationInterfaceMock;

    @Mock
    Provider providerMock;

    @Mock
    ProviderType type;
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ShadowLog.stream = System.out;
        THSManager.getInstance().setAwsdk(awsdkMock);

        mThsWelcomeBackFragment = new THSWelcomeBackFragment();
        mThsWelcomeBackFragment.setActionBarListener(actionBarListenerMock);
        mThsWelcomeBackFragment.mThsWelcomeBackPresenter = presenterMock;

        when(providerMock.getRating()).thenReturn(2);
        when(providerMock.getFullName()).thenReturn("Spoorti Hallur");
        when(providerMock.getSpecialty()).thenReturn(type);
        when(type.getName()).thenReturn("sss");
        when(practiceInfoMock.getName()).thenReturn("OPD");

        when(appInfraInterface.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterface.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterface.getConfigInterface()).thenReturn(appConfigurationInterfaceMock);
        when(appInfraInterface.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterface.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        when(appInfraInterface.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        THSManager.getInstance().setAppInfra(appInfraInterface);

        Bundle bundle = new Bundle();
        Long date = 1223455L;
        bundle.putSerializable(THSConstants.THS_DATE, date);
        bundle.putParcelable(THSConstants.THS_PRACTICE_INFO,practiceInfoMock);
        bundle.putParcelable(THSConstants.THS_PROVIDER,providerMock);

        mThsWelcomeBackFragment.setArguments(bundle);
    }



    @Test
    public void getPracticeInfo() throws Exception {
        SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
        final PracticeInfo practiceInfo = mThsWelcomeBackFragment.getPracticeInfo();
        assertNotNull(practiceInfo);
    }

    @Test
    public void getProvider() throws Exception {
        SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
        final Provider provider = mThsWelcomeBackFragment.getProvider();
        assertNotNull(provider);
    }

    @Test
    public void testWelcomeBackWithArguments() throws Exception {
        SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
        final Provider provider = mThsWelcomeBackFragment.getProvider();
        assertNotNull(provider);
    }
    @Test
    public void testOnClick(){
        SupportFragmentTestUtil.startFragment(mThsWelcomeBackFragment);
        final View viewById = mThsWelcomeBackFragment.getView().findViewById(R.id.ths_get_started);
        mThsWelcomeBackFragment.mThsWelcomeBackPresenter = presenterMock;
        viewById.performClick();
        verify(presenterMock).onEvent(R.id.ths_get_started);
    }

}