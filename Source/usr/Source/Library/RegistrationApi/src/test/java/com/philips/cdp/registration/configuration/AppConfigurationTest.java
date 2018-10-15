package com.philips.cdp.registration.configuration;

import com.philips.cdp.registration.app.infra.AppInfraWrapper;
import com.philips.cdp.registration.injection.RegistrationComponent;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.philips.cdp.registration.configuration.URConfigurationConstants.DEFAULT;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.FALLBACK_HOME_COUNTRY;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.FLOW_EMAIL_VERIFICATION_REQUIRED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.FLOW_MINIMUM_AGE_LIMIT;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.FLOW_TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.PIL_CONFIGURATION_CAMPAIGN_ID;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.SHOW_COUNTRY_SELECTION;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.SIGNIN_PROVIDERS;
import static com.philips.cdp.registration.configuration.URConfigurationConstants.SUPPORTED_HOME_COUNTRIES;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AppConfigurationTest extends TestCase {

    private static final String REGISTRATION_ENVIRONMENT = "appidentity.appState";
    private static final String WE_CHAT_APP_ID_KEY = "weChatAppId";
    private static final String WE_CHAT_APP_SECRET_KEY = "weChatAppSecret";
    private static final String MICROSITE_ID_KEY = "appidentity.micrositeId";
    private static final String CLIENT_ID_KEY = "JanRainConfiguration.RegistrationClientID.";
    private static final String SD_COUNTRYMAPPING_ID_KEY = "servicediscovery.countryMapping";

    private MockAppIdentityInterface mockAppIdentityInterface;

    @Mock
   private AppInfraWrapper mockAppInfraWrapper;
    AppInfra mockAppInfra;

    @Mock
   private RegistrationComponent mockComponent;

    private AppConfiguration appConfiguration;

    private Map<String, String> testMap;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        RegistrationConfiguration.getInstance().setComponent(mockComponent);
        appConfiguration = new AppConfiguration();
        setConfigTestMap();
        appConfiguration.setAppInfraWrapper(mockAppInfraWrapper);
        mockAppIdentityInterface = new MockAppIdentityInterface();
    }

    private void setConfigTestMap() {
        testMap = new HashMap<>();
        testMap.put("US", "test_us");
        testMap.put("IN", "test_in");
        testMap.put("CN", "test_cn");
        testMap.put("default", "test_default");
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
        testMap = null;
        appConfiguration = null;
        mockAppInfraWrapper = null;
        mockComponent = null;
    }

    @Test
    public void testGetWeChatAppId_StringValue() {
        when(mockAppInfraWrapper.getURProperty(WE_CHAT_APP_ID_KEY)).thenReturn("we_chat_app_id");
        String weChatAppId = appConfiguration.getWeChatAppId();
        assertEquals("we_chat_app_id", weChatAppId);
    }

    @Test
    public void testGetWeChatAppId_ValueNotSet() {
        when(mockAppInfraWrapper.getURProperty(WE_CHAT_APP_ID_KEY)).thenReturn(null);
        String weChatAppId = appConfiguration.getWeChatAppId();
        assertNull(weChatAppId);
    }

    @Test
    public void testGetWeChatAppId_MapValue_ValidCountry() {
        RegistrationHelper.getInstance().setCountryCode("IN");
        when(mockAppInfraWrapper.getURProperty(WE_CHAT_APP_ID_KEY)).thenReturn(testMap);
        String weChatAppId = appConfiguration.getWeChatAppId();
        assertEquals("test_in", weChatAppId);
    }

    @Test
    public void testGetWeChatAppId_MapValue_InvalidCountry() {
        RegistrationHelper.getInstance().setCountryCode("CA");
        when(mockAppInfraWrapper.getURProperty(WE_CHAT_APP_ID_KEY)).thenReturn(testMap);
        String weChatAppId = appConfiguration.getWeChatAppId();
        assertEquals("test_default", weChatAppId);
    }

    @Test
    public void testGetWeChatAppId_MapValue_NullCountry() {
        RegistrationHelper.getInstance().setCountryCode(null);
        when(mockAppInfraWrapper.getURProperty(WE_CHAT_APP_ID_KEY)).thenReturn(testMap);
        String weChatAppId = appConfiguration.getWeChatAppId();
        assertEquals("test_default", weChatAppId);
    }

    @Test
    public void testGetWeChatAppId_IncorrectObjectTypeReturned() {
        when(mockAppInfraWrapper.getURProperty(WE_CHAT_APP_ID_KEY)).thenReturn(new ArrayList<>());
        String weChatAppId = appConfiguration.getWeChatAppId();
        assertNull(weChatAppId);
    }

    @Test
    public void testGetWeChatAppSecret() {
        when(mockAppInfraWrapper.getURProperty(WE_CHAT_APP_SECRET_KEY)).thenReturn("we_chat_secret");
        String weChatAppSecret = appConfiguration.getWeChatAppSecret();
        assertEquals(weChatAppSecret, "we_chat_secret");
    }

    @Test
    public void testGetRegistrationEnvironment_Development() {
        when(mockAppInfraWrapper.getAppState()).thenReturn(AppIdentityInterface.AppState.DEVELOPMENT);
        String registrationEnvironment = appConfiguration.getRegistrationEnvironment();
        assertEquals(registrationEnvironment, "DEVELOPMENT");
    }

    @Test
    public void testGetRegistrationEnvironment_ThrowException() {
        when(mockAppInfraWrapper.getAppState()).thenThrow(new IllegalArgumentException("App state not configured"));
        try {
            String registrationEnvironment = appConfiguration.getRegistrationEnvironment();
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "App state not configured");
        }
    }

    @Test
    public void testGetClientId() {
        when(mockAppInfraWrapper.getURProperty(CLIENT_ID_KEY + "test")).thenReturn("client_id_test");
        String clientId = appConfiguration.getClientId("test");
        assertEquals(clientId, "client_id_test");
    }

    @Test
    public void testGetCampaignId() {
        when(mockAppInfraWrapper.getURProperty(PIL_CONFIGURATION_CAMPAIGN_ID)).thenReturn("campaign_id");
        String campaignId = appConfiguration.getCampaignId();
        assertEquals(campaignId, "campaign_id");
    }

    @Test
    public void testGetEmailVerificationRequired() {
        when(mockAppInfraWrapper.getURProperty(FLOW_EMAIL_VERIFICATION_REQUIRED)).thenReturn("true");
        Object emailVerificationRequired = appConfiguration.getEmailVerificationRequired();
        assertNotNull(emailVerificationRequired);
    }

    @Test
    public void testGetTermsAndConditionsAcceptanceRequired() {
        when(mockAppInfraWrapper.getURProperty(FLOW_TERMS_AND_CONDITIONS_ACCEPTANCE_REQUIRED)).thenReturn("true");
        Object termsAndConditionsAcceptanceRequired = appConfiguration.getTermsAndConditionsAcceptanceRequired();
        assertNotNull(termsAndConditionsAcceptanceRequired);
    }

    @Test
    public void testGetMinimunAgeObject() {
        when(mockAppInfraWrapper.getURProperty(FLOW_MINIMUM_AGE_LIMIT)).thenReturn("true");
        Object minimunAgeObject = appConfiguration.getMinimunAgeObject();
        assertNotNull(minimunAgeObject);
    }

    @Test
    public void testGetProvidersForCountry_ValidCountry() {
        List<String> providers= new ArrayList<>();
        providers.add("FB");
        when(mockAppInfraWrapper.getURProperty(SIGNIN_PROVIDERS + "IN")).thenReturn(providers);
        List<String> providersForCountry = appConfiguration.getProvidersForCountry("IN");
        assertEquals(providersForCountry, providers);
    }

    @Test
    public void testGetProvidersForCountry_DefaultCountry() {
        List<String> providers= new ArrayList<>();
        providers.add("FB");
        when(mockAppInfraWrapper.getURProperty(SIGNIN_PROVIDERS + "IN")).thenReturn(null);
        when(mockAppInfraWrapper.getURProperty(SIGNIN_PROVIDERS + DEFAULT)).thenReturn(providers);
        List<String> providersForCountry = appConfiguration.getProvidersForCountry("IN");
        assertEquals(providersForCountry, providers);
    }

    @Test
    public void testGetShowCountrySelection() {
        when(mockAppInfraWrapper.getURProperty(SHOW_COUNTRY_SELECTION)).thenReturn("true");
        String showCountrySelection = appConfiguration.getShowCountrySelection();
        assertEquals(showCountrySelection, "true");
    }

    @Test
    public void testGetMicrositeId_WhenItIsSet(){
        when(mockAppInfraWrapper.getAppIdentity()).thenReturn(mockAppIdentityInterface);
        String micrositeId = appConfiguration.getMicrositeId();
        assertEquals(micrositeId, "qhq9jvkx35q8duef2fh6wwzceujjs9gs");
    }
    @Test(expected = NullPointerException.class)
    public void testGetMicrositeId_WhenItIsSetNull(){
        when(mockAppInfraWrapper.getAppIdentity()).thenReturn(null);
        String micrositeId = appConfiguration.getMicrositeId();
        assertNull(micrositeId);
    }

    @Test
    public void testGetServiceDiscoveryCountryMappingIfNull(){
        when(mockAppInfraWrapper.getAppInfraProperty(SD_COUNTRYMAPPING_ID_KEY)).thenReturn(null);
        assertNull(appConfiguration.getServiceDiscoveryCountryMapping());
    }

    @Test
    public void testGetServiceDiscoveryCountryMappingIfHashMap(){
        when(mockAppInfraWrapper.getAppInfraProperty(SD_COUNTRYMAPPING_ID_KEY)).thenReturn(new HashMap<String,String>());
        assertNotNull(appConfiguration.getServiceDiscoveryCountryMapping());
    }

    @Test
    public void testGetSupportedHomeCountriesIfNull(){
        when(mockAppInfraWrapper.getAppInfraProperty(SUPPORTED_HOME_COUNTRIES)).thenReturn(null);
        assertNull(appConfiguration.getSupportedHomeCountries());
    }

    @Test
    public void testGetSupportedHomeCountriesWithCountriesList(){
        when(mockAppInfraWrapper.getAppInfraProperty(SUPPORTED_HOME_COUNTRIES)).thenReturn(new ArrayList<String>());
        assertNull(appConfiguration.getSupportedHomeCountries());
    }

    @Test
    public void testGetFallBackHomeCountryIfNull(){
        when(mockAppInfraWrapper.getAppInfraProperty(FALLBACK_HOME_COUNTRY)).thenReturn(null);
        assertNull(appConfiguration.getFallBackHomeCountry());
    }

    @Test
    public void testGetFallBackHomeCountryCountry(){
        when(mockAppInfraWrapper.getAppInfraProperty(FALLBACK_HOME_COUNTRY)).thenReturn("US");
        assertNull(appConfiguration.getFallBackHomeCountry());
    }


}