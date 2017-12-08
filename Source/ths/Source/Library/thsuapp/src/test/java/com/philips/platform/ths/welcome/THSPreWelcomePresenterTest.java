/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.welcome;

import android.os.Bundle;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.ths.BuildConfig;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.utility.THSManager;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.net.MalformedURLException;
import java.net.URL;

import static com.philips.platform.ths.utility.THSConstants.THS_APPLICATION_ID;
import static com.philips.platform.ths.utility.THSConstants.THS_TERMS_AND_CONDITIONS;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class THSPreWelcomePresenterTest {

    THSPreWelcomePresenter mTHSPreWelcomePresenter;

    @Mock
    THSPreWelcomeFragment thsPreWelcomeFragment;

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

    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlListener> getServiceUrlListenerArgumentCaptor;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(appInfraInterfaceMock.getTagging()).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryMock);
        when(appInfraInterfaceMock.getTagging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(appTaggingInterface);
        when(appInfraInterfaceMock.getLogging()).thenReturn(loggingInterface);
        when(appInfraInterfaceMock.getLogging().createInstanceForComponent(THS_APPLICATION_ID, BuildConfig.VERSION_NAME)).thenReturn(loggingInterface);
        THSManager.getInstance().setAppInfra(appInfraInterfaceMock);
        mTHSPreWelcomePresenter = new THSPreWelcomePresenter(thsPreWelcomeFragment);
    }

    @Test
    public void onEvent() throws Exception {
        mTHSPreWelcomePresenter.onEvent(R.id.ths_go_see_provider);
        verify(thsPreWelcomeFragment).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class), anyBoolean());
    }

    @Test
    public void onEventIsReurtuningUser() throws Exception {
        THSManager.getInstance().setIsReturningUser(false);
        mTHSPreWelcomePresenter.onEvent(R.id.ths_go_see_provider);
        verify(thsPreWelcomeFragment).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class), anyBoolean());
    }

    @Test
    public void onEventths_video_consults() throws Exception {
        THSManager.getInstance().setIsReturningUser(false);
        mTHSPreWelcomePresenter.onEvent(R.id.ths_video_consults);
        verify(thsPreWelcomeFragment).addFragment(any(THSBaseFragment.class),anyString(),any(Bundle.class), anyBoolean());
    }

    @Test
    public void onEventths_ths_licence() throws Exception {
        THSManager.getInstance().setIsReturningUser(false);
        mTHSPreWelcomePresenter.onEvent(R.id.ths_licence);
        verify(serviceDiscoveryMock).getServiceUrlWithCountryPreference(anyString(), any(ServiceDiscoveryInterface.OnGetServiceUrlListener.class));
    }

    @Test
    public void getTermsAndConditions() throws MalformedURLException {
        mTHSPreWelcomePresenter.getTermsAndConditions();
        verify(serviceDiscoveryMock).getServiceUrlWithCountryPreference(anyString(), getServiceUrlListenerArgumentCaptor.capture());
        final ServiceDiscoveryInterface.OnGetServiceUrlListener value = getServiceUrlListenerArgumentCaptor.getValue();
        URL url_g = new URL("http://www.google.com/");
        value.onSuccess(url_g);
        verify(thsPreWelcomeFragment).showTermsAndConditions(anyString());
    }

    @Test
    public void getTermsAndConditionsError() throws MalformedURLException {
        mTHSPreWelcomePresenter.getTermsAndConditions();
        verify(serviceDiscoveryMock).getServiceUrlWithCountryPreference(anyString(), getServiceUrlListenerArgumentCaptor.capture());
        final ServiceDiscoveryInterface.OnGetServiceUrlListener value = getServiceUrlListenerArgumentCaptor.getValue();
        value.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, anyString());
        verify(thsPreWelcomeFragment).showError(anyString());
    }

}