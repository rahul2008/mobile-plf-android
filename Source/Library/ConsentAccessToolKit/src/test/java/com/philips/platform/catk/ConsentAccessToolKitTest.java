/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.rest.RestInterface;
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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.util.Locale;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(RobolectricTestRunner.class)
public class ConsentAccessToolKitTest {

    private static final String COUNTRY_CODE = "IN";

    private CatkComponentMock catkComponent;

    private ConsentAccessToolKit consentAccessToolKit;

    @Mock
    RestInterface mockRestInterface;

    @Mock
    private ConsentResponseListener listenerMock;

    @Mock
    private NetworkController mockNetworkController;

    @Mock
    private CatkComponent mockCatkComponent;

    @Mock
    CreateConsentListener mockCreateConsentListener;

    @Mock
    User user;

    @Mock
    ConsentAccessToolKit mockConsentAccessToolKit;

    @Captor
    ArgumentCaptor<NetworkAbstractModel> captorNetworkAbstractModel;

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

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenGetConsentDetailsMethodISCalled() {
        consentAccessToolKit.getConsentDetails(listenerMock);
        verify(mockNetworkController).sendConsentRequest(captorNetworkAbstractModel.capture());
        assertTrue(captorNetworkAbstractModel.getValue() instanceof GetConsentsModelRequest);
        thenServiceInfoProviderWasCalled();
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenCreateConsentDetailsMethodISCalled() {
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

    private ServiceInfoProviderMock serviceInfoProvider;
}
