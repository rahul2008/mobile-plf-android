/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.gson.Gson;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.mya.catk.dto.CreateConsentDto;
import com.philips.platform.mya.catk.dto.GetConsentDto;
import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.injection.CatkComponent;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.listener.CreateConsentListener;
import com.philips.platform.mya.catk.mock.CatkComponentMock;
import com.philips.platform.mya.catk.mock.ServiceDiscoveryInterfaceMock;
import com.philips.platform.mya.catk.mock.ServiceInfoProviderMock;
import com.philips.platform.mya.catk.provider.AppInfraInfo;
import com.philips.platform.mya.catk.provider.ComponentProvider;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import android.content.Context;
import android.support.annotation.NonNull;

public class ConsentsClientTest {

    private static final String DUTCH_LOCALE = "nl-NL";
    private static final String ENGLISH_LOCALE = "en-GB";

    private CatkComponentMock catkComponent;

    private ConsentsClient consentsClient;

    private ServiceDiscoveryInterfaceMock serviceDiscoveryInterface;

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
    @Mock
    private AppInfraInterface mockAppInfra;
    @Mock
    private Context mockContext;
    @Mock
    private AppConfigurationInterface mockConfigInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        ConsentsClient.setInstance(new ConsentsClient());
        consentsClient = ConsentsClient.getInstance();
        catkComponent = new CatkComponentMock();
        serviceDiscoveryInterface = new ServiceDiscoveryInterfaceMock();
        catkComponent.getUser_return = user;
        catkComponent.getServiceDiscoveryInterface_return = serviceDiscoveryInterface;
        AppInfraInfo appInfraInfo = new AppInfraInfo("http://someurl.com");
        when(user.getHsdpUUID()).thenReturn(HSDP_UUID);
        consentsClient.setCatkComponent(catkComponent);
        serviceInfoProvider = new ServiceInfoProviderMock();
        serviceInfoProvider.retrievedInfo = appInfraInfo;
        consentsClient.setServiceInfoProvider(serviceInfoProvider);
        consentsClient.setNetworkController(mockNetworkController);
    }

    @After
    public void tearDown() throws Exception {
        ConsentsClient.setInstance(null);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenApplicationNameIsNull() throws Exception {
        givenAppNamePropName(null, "propName");
        consentsClient.init(validCatkInputs());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenApplicationNameIsEmpty() throws Exception {
        givenAppNamePropName("", "propName");
        consentsClient.init(validCatkInputs());
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenPropositionNameIsNull() throws Exception {
        givenAppNamePropName("appName", null);
        consentsClient.init(validCatkInputs());
    }


    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenPropositionNameIsEmpty() throws Exception {
        givenAppNamePropName("appName", "");
        consentsClient.init(validCatkInputs());
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInitIsNotCalledAndTryingToCreateConsent() throws Exception {
        givenAppNamePropName("appName", "");
        consentsClient.createConsent(null, null);
        verifyZeroInteractions(mockNetworkController);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInitIsNotCalledAndTryingToGetConsentDetails() throws Exception {
        givenAppNamePropName("appName", "");
        consentsClient.getConsentDetails(null);
        verifyZeroInteractions(mockNetworkController);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowExceptionWhenInitIsNotCalledAndTryingToGetConsentStatus() throws Exception {
        givenAppNamePropName("appName", "");
        consentsClient.getStatusForConsentType(null, -1, null);
        verifyZeroInteractions(mockNetworkController);
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenGetConsentDetailsMethodISCalled() {
        givenInitWasCalled("appName", "propName");
        consentsClient.getConsentDetails(listenerMock);
        verify(mockNetworkController).sendConsentRequest(captorNetworkAbstractModel.capture());
        assertTrue(captorNetworkAbstractModel.getValue() instanceof GetConsentsModelRequest);
        thenServiceInfoProviderWasCalled();
    }

    @Test
    public void shouldCallNetworkHelperSendRequestMethodWhenCreateConsentDetailsMethodISCalled() {
        givenInitWasCalled("myApplication", "myProposition");
        givenServiceDiscoveryReturnsHomeCountry("US");
        BackendConsent consent = new BackendConsent(DUTCH_LOCALE, ConsentStatus.active, "moment", 1);
        consentsClient.createConsent(Collections.singletonList(consent), mockCreateConsentListener);
        verify(mockNetworkController).sendConsentRequest(captorNetworkAbstractModel.capture());
        assertTrue(captorNetworkAbstractModel.getValue() instanceof CreateConsentModelRequest);
        thenServiceInfoProviderWasCalled();
        thenAssertConsentDto(HSDP_UUID, "moment", "active","nl-NL", "US","myApplication", "myProposition", 1);
    }

    @Test
    public void getStatusForConsentType_shouldFilterByType() {
        givenInitWasCalled("myApplication", "myProposition");
        givenConsentSuccessResponse(consentDtos);
        consentsClient.getStatusForConsentType("moment", 0, consentResponseListener);
        assertEquals(consents, consentResponseListener.responseData);
    }

    @Test
    public void getStatusForConsentType_returnsAccepted_whenNotInBackendAndStrictModeIsNotSet() {
        givenStrictConsentCheckIs(null);
        givenInitWasCalled("myApplication", "myProposition");
        givenConsentSuccessResponse(Collections.EMPTY_LIST);
        consentsClient.getStatusForConsentType("moment", 0, consentResponseListener);
        thenConsentStatusIs(ConsentStatus.active);
        thenConsentVersionIs(Integer.MAX_VALUE);
    }

    @Test
    public void getStatusForConsentType_returnsAccepted_whenNotInBackendAndStrictModeIsOff() {
        givenStrictConsentCheckIs(false);
        givenInitWasCalled("myApplication", "myProposition");
        givenConsentSuccessResponse(Collections.EMPTY_LIST);
        consentsClient.getStatusForConsentType("moment", 0, consentResponseListener);
        thenConsentStatusIs(ConsentStatus.active);
        thenConsentVersionIs(Integer.MAX_VALUE);
    }

    @Test
    public void getStatusForConsentType_returnsNull_whenNotInBackendAndStrictModeIsOn() {
        givenStrictConsentCheckIs(true);
        givenInitWasCalled("myApplication", "myProposition");
        givenConsentSuccessResponse(Collections.EMPTY_LIST);
        consentsClient.getStatusForConsentType("moment", 0, consentResponseListener);
        assertNull(consentResponseListener.responseData);
    }

    @Test
    public void init_setsConsentDefinition() {
        givenInitWasCalled("appName", "propName");
        assertEquals(consentDefinitions, consentsClient.getConsentDefinitions());
    }

    @Test
    public void givenCATKcreated_whenGetAppInfra_thenShouldReturnNonNull() {
        givenInitWasCalled("appName", "propName");

        assertNotNull(consentsClient.getAppInfra());
    }

    private void givenStrictConsentCheckIs(final Boolean strictConsentCheckEnabled) {
        when(mockConfigInterface.getPropertyForKey(anyString(), anyString(), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(strictConsentCheckEnabled);
    }

    private void givenConsentSuccessResponse(final List consentResponse) {
        networkControllerMock = new NetworkControllerMock();
        consentsClient.setNetworkController(networkControllerMock);
        networkControllerMock.sendConsentRequest_onSuccessResponse = consentResponse;
    }

    private void givenServiceDiscoveryReturnsHomeCountry(String homeCountry) {
        serviceDiscoveryInterface.getHomeCountry_return = homeCountry;
    }

    private void thenConsentStatusIs(final ConsentStatus expectedStatus) {
        assertEquals(expectedStatus, consentResponseListener.responseData.get(0).getStatus());
    }

    private void thenConsentVersionIs(final int expectedVersion) {
        assertEquals(expectedVersion, consentResponseListener.responseData.get(0).getVersion());
    }

    private void givenAppNamePropName(String appName, String propName) {
        when(mockAppInfra.getConfigInterface()).thenReturn(mockConfigInterface);
        when(mockConfigInterface.getPropertyForKey(eq("appName"), eq("hsdp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(appName);
        when(mockConfigInterface.getPropertyForKey(eq("propositionName"), eq("hsdp"), any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(propName);

        consentsClient.setComponentProvider(new ComponentProvider() {

            @Override
            public CatkComponent getComponent(CatkInputs catkInputs) {
                return catkComponent;
            }
        });
    }

    private void givenInitWasCalled(String appName, String propName) {
        givenAppNamePropName(appName, propName);
        consentsClient.init(validCatkInputs());
    }

    private void thenServiceInfoProviderWasCalled() {
        assertNotNull(serviceInfoProvider.responseListener);
        assertNotNull(serviceInfoProvider.serviceDiscovery);
    }

    private void thenAssertConsentDto(String subject, String type, String status, String locale, String country, String appName, String propName, int version) {
        CreateConsentDto createConsentDto = new Gson().fromJson(captorNetworkAbstractModel.getValue().requestBody(), CreateConsentDto.class);
        assertEquals(buildPolicyRule(type, version, country, propName, appName), createConsentDto.getPolicyRule());
        assertEquals(locale, createConsentDto.getLanguage());
        assertEquals(status, createConsentDto.getStatus());
        assertEquals(subject, createConsentDto.getSubject());
        assertEquals("Consent", createConsentDto.getResourceType());
    }

    @NonNull
    private CatkInputs validCatkInputs() {
        return new CatkInputs.Builder().setAppInfraInterface(mockAppInfra).setContext(mockContext).setConsentDefinitions(consentDefinitions).build();
    }

    private static String buildPolicyRule(String type, int version, String country, String propositionName, String applicationName) {
        return new StringBuilder("urn:com.philips.consent:").append(type).append("/").append(country).append("/").append(version).append("/").append(propositionName).append("/").append(applicationName).toString();
    }

    private ServiceInfoProviderMock serviceInfoProvider;
    private NetworkControllerMock networkControllerMock;
    private String momentConsentTimestamp = "2017-10-05T11:11:11.000Z";
    private String coachignConsentTimestamp = "2017-10-05T11:12:11.000Z";
    private String HSDP_UUID = "hsdp_user_id";
    private GetConsentDto momentConsentDto = new GetConsentDto(momentConsentTimestamp, ENGLISH_LOCALE, buildPolicyRule("moment", 0, "IN", "propName1", "appName1"), "BackendConsent", ConsentStatus.active, "Subject1");
    private GetConsentDto coachingConsentDto = new GetConsentDto(coachignConsentTimestamp, ENGLISH_LOCALE, buildPolicyRule("coaching", 0, "IN", "propName1", "appName1"), "BackendConsent", ConsentStatus.active, "Subject1");
    private List<GetConsentDto> consentDtos = Arrays.asList(momentConsentDto, coachingConsentDto);
    private BackendConsent momentConsent = new BackendConsent(ENGLISH_LOCALE, ConsentStatus.active, "moment", 0, new DateTime(momentConsentTimestamp));
    private List<BackendConsent> consents = Arrays.asList(momentConsent);
    private ConsentDefinition consentDefinitionWith1Type = new ConsentDefinition(0, 0, Collections.singletonList("type1"), 1);
    private ConsentDefinition consentDefinitionWith2Types = new ConsentDefinition(0, 0, Arrays.asList("type2", "type3"), 1);
    private List<ConsentDefinition> consentDefinitions = Arrays.asList(consentDefinitionWith1Type, consentDefinitionWith2Types);

    private static class ConsentResponseListenerImpl implements ConsentResponseListener {
        public List<BackendConsent> responseData;
        public ConsentNetworkError error;

        @Override
        public void onResponseSuccessConsent(List<BackendConsent> responseData) {
            this.responseData = responseData;
        }

        @Override
        public void onResponseFailureConsent(ConsentNetworkError error) {
            this.error = error;
        }
    }

    private ConsentResponseListenerImpl consentResponseListener = new ConsentResponseListenerImpl();
}
