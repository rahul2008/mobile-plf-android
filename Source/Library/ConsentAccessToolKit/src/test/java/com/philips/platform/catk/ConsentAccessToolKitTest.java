/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.catk.dto.CreateConsentModelRequest;
import com.philips.platform.catk.dto.GetConsentsModelRequest;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mock.CatkComponentMock;
import com.philips.platform.catk.mock.ServiceInfoProviderMock;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentStatus;
import com.philips.platform.catk.network.NetworkAbstractModel;
import com.philips.platform.catk.network.NetworkController;
import com.philips.platform.catk.provider.AppInfraInfo;
import com.philips.platform.catk.provider.ComponentProvider;
import com.philips.platform.mya.consentaccesstoolkit.BuildConfig;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 25)
public class ConsentAccessToolKitTest {

    private static final String COUNTRY_CODE = "IN";

    private CatkComponentMock catkComponent;

    private ConsentAccessToolKit consentAccessToolKit;

    @Mock
    private ConsentResponseListener listenerMock;

    @Mock
    private NetworkController mockNetworkController;

    @Mock
    CreateConsentListener mockCreateConsentListener;

    @Mock
    User user;

    @Captor
    ArgumentCaptor<NetworkAbstractModel> captorNetworkAbstractModel;
    @Mock private AppInfraInterface mockAppInfra;
    @Mock private Context mockContext;
    @Mock private AppConfigurationInterface mockConfigInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        consentAccessToolKit = ConsentAccessToolKit.getInstance();
        catkComponent = new CatkComponentMock();
        catkComponent.getUser_return = user;
        AppInfraInfo appInfraInfo = new AppInfraInfo("http://someurl.com");
        when(user.getCountryCode()).thenReturn(COUNTRY_CODE);
        consentAccessToolKit.setCatkComponent(catkComponent);
        serviceInfoProvider = new ServiceInfoProviderMock();
        serviceInfoProvider.retrievedInfo = appInfraInfo;
        consentAccessToolKit.setServiceInfoProvider(serviceInfoProvider);
        consentAccessToolKit.setNetworkController(mockNetworkController);
    }

    @After
    public void tearDown() throws Exception {
        consentAccessToolKit.setCatkComponent(null);
        consentAccessToolKit.setNetworkController(null);
        listenerMock = null;
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenApplicationNameIsNull() throws Exception {
        givenAppNamePropName(null, "propName");
        consentAccessToolKit.init(validCatkInputs());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenApplicationNameIsEmpty() throws Exception {
        givenAppNamePropName("", "propName");
        consentAccessToolKit.init(validCatkInputs());
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenPropositionNameIsNull() throws Exception {
        givenAppNamePropName("appName", null);
        consentAccessToolKit.init(validCatkInputs());
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenPropositionNameIsEmpty() throws Exception {
        givenAppNamePropName("appName", "");
        consentAccessToolKit.init(validCatkInputs());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInitIsNotCalledAndTryingToCreateConsent() throws Exception {
        givenAppNamePropName("appName", "");
        consentAccessToolKit.createConsent(null, null);
        verifyZeroInteractions(mockNetworkController);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInitIsNotCalledAndTryingToGetConsentDetails() throws Exception {
        givenAppNamePropName("appName", "");
        consentAccessToolKit.getConsentDetails(null);
        verifyZeroInteractions(mockNetworkController);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInitIsNotCalledAndTryingToGetConsentStatus() throws Exception {
        givenAppNamePropName("appName", "");
        consentAccessToolKit.getStatusForConsentType(null, -1, null);
        verifyZeroInteractions(mockNetworkController);
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenGetConsentDetailsMethodISCalled() {
        givenInitWasCalled("appName", "propName");
        consentAccessToolKit.getConsentDetails(listenerMock);
        verify(mockNetworkController).sendConsentRequest(captorNetworkAbstractModel.capture());
        assertTrue(captorNetworkAbstractModel.getValue() instanceof GetConsentsModelRequest);
        thenServiceInfoProviderWasCalled();
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenCreateConsentDetailsMethodISCalled() {
        givenInitWasCalled("myApplication", "myProposition");
        Consent consent = new Consent(new Locale("nl", "NL"), ConsentStatus.active, "moment", 1);
        consentAccessToolKit.createConsent(consent, mockCreateConsentListener);
        verify(mockNetworkController).sendConsentRequest(captorNetworkAbstractModel.capture());
        assertTrue(captorNetworkAbstractModel.getValue() instanceof CreateConsentModelRequest);
        thenServiceInfoProviderWasCalled();
    }

    private void thenServiceInfoProviderWasCalled() {
        assertNotNull(serviceInfoProvider.responseListener);
        assertNotNull(serviceInfoProvider.serviceDiscovery);
    }

    private void givenAppNamePropName(String appName, String propName) {
        when(mockAppInfra.getConfigInterface()).thenReturn(mockConfigInterface);
        when(mockConfigInterface.getPropertyForKey(eq("appName"), eq("hsdp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(appName);
        when(mockConfigInterface.getPropertyForKey(eq("propositionName"), eq("hsdp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(propName);

        consentAccessToolKit.setComponentProvider(new ComponentProvider(){

            @Override
            public CatkComponent getComponent(CatkInputs catkInputs) {
                return catkComponent;
            }
        });
    }

    private void givenInitWasCalled(String appName, String propName) {
        givenAppNamePropName(appName, propName);
        consentAccessToolKit.init(validCatkInputs());
    }

    @NonNull
    private CatkInputs validCatkInputs() {
        CatkInputs catkInputs = new CatkInputs();
        catkInputs.setAppInfra(mockAppInfra);
        catkInputs.setContext(mockContext);
        return catkInputs;
    }

    private ServiceInfoProviderMock serviceInfoProvider;
}
