/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.*;
import org.mockito.*;

import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.rest.RestInterface;
import com.philips.platform.catk.injection.CatkComponent;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.mock.CatkComponentMock;
import com.philips.platform.catk.model.CreateConsentModelRequest;
import com.philips.platform.catk.model.GetConsentsModelRequest;
import com.philips.platform.catk.network.*;
import com.philips.platform.catk.response.ConsentStatus;

public class ConsentAccessToolKitTest {

    private static final String COUNTRY_CODE = "IN";

    private String appName = "OneBackend";

    private String propName = "OneBackendProp";

    private CatkComponentMock catkComponent;

    private ConsentAccessToolKit consentAccessToolKit;

    @Mock
    RestInterface mockRestInterface;

    @Mock
    private ConsentResponseListener listnerMock;

    @Mock
    private NetworkHelper mockNetworkHelper;

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
        when(user.getCountryCode()).thenReturn(COUNTRY_CODE);
        consentAccessToolKit.setCatkComponent(catkComponent);
        NetworkHelperManipulator.setInstance(mockNetworkHelper);
    }

    @After
    public void tearDown() throws Exception {
        mockNetworkHelper = null;
        listnerMock = null;
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenGetConsentDetailsMethodISCalled() {
        consentAccessToolKit.getConsentDetails(listnerMock);
        verify(mockNetworkHelper).sendRequest(captorNetworkAbstractModel.capture());
        assertTrue(captorNetworkAbstractModel.getValue() instanceof GetConsentsModelRequest);
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenCreateConsentDetailsMethodISCalled() {
        givenLocale("nl-NL");
        consentAccessToolKit.createConsent(ConsentStatus.active, mockCreateConsentListener);
        verify(mockNetworkHelper).sendRequest(captorNetworkAbstractModel.capture());
        assertTrue(captorNetworkAbstractModel.getValue() instanceof CreateConsentModelRequest);
    }

    @Test
    public void buildPolicyRule() {
        assertEquals("urn:com.philips.consent:consentType1" + "/" + COUNTRY_CODE + "/1/" + propName + "/" + appName,
                consentAccessToolKit.buildPolicyRule("consentType1", 1, COUNTRY_CODE, propName, appName));
    }

    private void givenLocale(String locale) {
        catkComponent.getServiceDiscoveryInterface_return.getServiceLocaleWithCountryPreference_return = locale;
    }

}
