package com.philips.platform.pim.migration;

import android.net.Uri;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscovery;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.pim.listeners.RefreshUSRTokenListener;
import com.philips.platform.pim.manager.PIMAuthManager;
import com.philips.platform.pim.manager.PIMConfigManager;
import com.philips.platform.pim.manager.PIMSettingManager;

import junit.framework.TestCase;

import org.json.JSONObject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@PrepareForTest({Uri.class, PIMSettingManager.class})
@RunWith(PowerMockRunner.class)
public class USRTokenManagerTest extends TestCase {
    private static final String JR_CAPTURE_REFRESH_SECRET = "jr_capture_refresh_secret";
    private static final String JR_CAPTURE_SIGNED_IN_USER = "jr_capture_signed_in_user";
    private static final String JR_CAPTURE_FLOW = "jr_capture_flow";
    private USRTokenManager usrTokenManager;
    @Mock
    private ServiceDiscoveryInterface mockServiceDiscoveryInterface;
    @Mock
    private ServiceDiscoveryInterface.OnGetServiceUrlMapListener mockOnGetServiceUrlMapListener;
    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlMapListener> captor;
    @Mock
    private AppInfraInterface mockAppInfraInterface;
    @Mock
    private LoggingInterface mockLoggingInterface;
    @Mock
    private PIMSettingManager mockPimSettingManager;
    @Mock
    private ServiceDiscoveryService mockServiceDiscoveryService;
    @Mock
    private RefreshUSRTokenListener mockRefreshUSRTokenListener;
    @Captor
    private ArgumentCaptor<ArrayList<String>> captorArrayList;
    @Mock
    private SecureStorageInterface mockSecureStorageInterface;
    @Mock
    private SecureStorageInterface.SecureStorageError mockSecureStorageError;
    @Mock
    private AppConfigurationInterface.AppConfigurationError mockAppConfigurationError;
    @Mock
    private AppConfigurationInterface mockAppConfigurationInterface;
    @Mock
    private TimeInterface mockTimeInterface;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        MockitoAnnotations.initMocks(this);
        mockStatic(PIMSettingManager.class);
        when(PIMSettingManager.getInstance()).thenReturn(mockPimSettingManager);
        when(mockPimSettingManager.getLoggingInterface()).thenReturn(mockLoggingInterface);
        when(mockAppInfraInterface.getSecureStorage()).thenReturn(mockSecureStorageInterface);
        when(mockAppInfraInterface.getServiceDiscovery()).thenReturn(mockServiceDiscoveryInterface);
        when(mockAppInfraInterface.getConfigInterface()).thenReturn(mockAppConfigurationInterface);
        whenNew(SecureStorageInterface.SecureStorageError.class).withNoArguments().thenReturn(mockSecureStorageError);
        when(mockAppInfraInterface.getSecureStorage().fetchValueForKey(JR_CAPTURE_SIGNED_IN_USER, mockSecureStorageError)).thenReturn(signedUserData());
        when(mockAppInfraInterface.getTime()).thenReturn(mockTimeInterface);
        when(mockTimeInterface.getUTCTime()).thenReturn(new Date());
        usrTokenManager = new USRTokenManager(mockAppInfraInterface);
        //Whitebox.invokeMethod(usrTokenManagerz, "fetchDataFromSecureStorage", signedUserData());

    }

    @Test
    public void test_GetServicesWithCountryPreference_OnSuccess() {
        usrTokenManager.fetchRefreshedAccessToken(mockRefreshUSRTokenListener);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();
        Map<String, ServiceDiscoveryService> mockMap = mock(Map.class);
        when(mockMap.get(any())).thenReturn(mockServiceDiscoveryService);
        when(mockServiceDiscoveryService.getConfigUrls()).thenReturn(new String());
        when(mockServiceDiscoveryService.getLocale()).thenReturn("en_US");
        mockOnGetServiceUrlMapListener.onSuccess(mockMap);
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "downloadUserUrlFromSD onSuccess. Refresh Url : " + "" + " Locale : " + "en_US");
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "downloadUserUrlFromSD onSuccess");
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "Migration Failed!! " + "Signed_in_user not found");
    }


    @Test
    public void test_GetServicesWithCountryPreference_OnError() {
        usrTokenManager.fetchRefreshedAccessToken(mockRefreshUSRTokenListener);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();
        ServiceDiscovery.Error sdError = new ServiceDiscovery.Error(ServiceDiscoveryInterface
                .OnErrorListener.ERRORVALUES.CONNECTION_TIMEOUT, "Connection Timeout");
        mockOnGetServiceUrlMapListener.onError(sdError.getErrorvalue(), sdError.getMessage());
        assertNotNull(sdError.getErrorvalue());
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "Migration Failed!! " + " Error in downloadUserUrlFromSD : " + "Connection Timeout");
    }

    @Test
    public void test_GetServicesWithCountryPreference_OnSuccess_When_URLMap_IsNotNull() {
        Map<String, ServiceDiscoveryService> mockMap = null;
        usrTokenManager.fetchRefreshedAccessToken(mockRefreshUSRTokenListener);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();
        mockOnGetServiceUrlMapListener.onSuccess(mockMap);
        verify(mockLoggingInterface).log(DEBUG, PIMMigrationManager.class.getSimpleName(), "Migration Failed!! " + " Error in downloadUserUrlFromSD : " + "Not able to fetch config url");
    }

    @Test
    public void test_GetServicesWithCountryPreference_OnSuccess_When_UserIsSignedIn() throws Exception {
        Map<String, ServiceDiscoveryService> mockMap = mock(Map.class);
        when(mockMap.get(any())).thenReturn(mockServiceDiscoveryService);
        when(mockServiceDiscoveryService.getConfigUrls()).thenReturn("https://stg.accounts.philips.com/c2a48310-9715-3beb-895e-000000000000/login");
        when(mockServiceDiscoveryService.getLocale()).thenReturn("en_US");
        Whitebox.setInternalState(usrTokenManager, "signedInUser", signedUserData());
        when(mockAppInfraInterface.getConfigInterface().getPropertyForKey("JanRainConfiguration.RegistrationClientID", "PIM", mockAppConfigurationError)).thenReturn(usrClientIds());
        usrTokenManager.fetchRefreshedAccessToken(mockRefreshUSRTokenListener);
        verify(mockServiceDiscoveryInterface).getServicesWithCountryPreference(captorArrayList.capture(), captor.capture(), eq(null));
        mockOnGetServiceUrlMapListener = captor.getValue();
        mockOnGetServiceUrlMapListener.onSuccess(mockMap);
    }


    private String signedUserData() {
        return "{r\"original\":{\"lastModifiedDate\":null,\"providerMergedLast\":null,\"preferredLanguage\":\"en\",\"cloudsearch\":{\"syncAttempts\":null,\"syncUpdated\":null},\"uuidHash\":null,\"personalDataUsageAcceptance\":null,\"optIn\":{\"status\":null,\"updated\":null},\"post_login_confirmation\":[],\"streamiumServicesTCAgreed\":null,\"birthday\":\"2019-01-26\",\"consumerPoints\":null,\"batchId\":null,\"familyName\":\"\",\"friends\":[],\"weddingDate\":null,\"interestStreamiumSurveys\":null,\"interestAvent\":null,\"lastModifiedSource\":null,\"medicalProfessionalRoleSpecified\":null,\"lastLoginMethod\":null,\"catalogLocaleItem\":null,\"mobileNumberSmsRequestedAt\":null,\"mobileNumber\":null,\"receiveMarketingEmail\":true,\"familyId\":null,\"children\":[],\"campaignID\":null,\"mobileNumberSmsEnvironment\":null,\"consumerInterests\":[],\"locale\":null,\"mobileNumberNeedVerification\":true,\"profiles\":[],\"sitecatalystIDs\":[],\"marketingOptIn\":{\"locale\":\"en_GB\",\"timestamp\":\"2019-03-18 12:02:14 +0000\"},\"legacyID\":null,\"lastUsedDevice\":{\"tokenType\":null,\"deviceId\":null,\"deviceToken\":null,\"deviceType\":null},\"controlField\":null,\"id\":1533260744,\"deactivatedAccount\":null,\"consentVerifiedAt\":null,\"avmTCAgreed\":null,\"mobileNumberVerified\":null,\"interestCommunications\":null,\"campaigns\":[],\"deviceIdentification\":[],\"badgeVillePlayerIDs\":[],\"nettvTCAgreed\":null,\"clients\":[{\"clientId\":\"yfvh9udgu9zkuse8jr2nah7uk6et6yas\",\"id\":1534874626,\"name\":null,\"firstLogin\":\"2019-07-08 11:54:15 +0000\",\"lastLogin\":\"2019-07-08 11:54:15 +0000\"},{\"clientId\":\"45fhu7hhxrhqx9sm78mgzx4afm9payau\",\"id\":1534924677,\"name\":null,\"firstLogin\":\"2019-07-03 09:04:05 +0000\",\"lastLogin\":\"2019-07-03 09:04:05 +0000\"}],\"visitedMicroSites\":[{\"microSiteID\":\"77000\",\"id\":1533260746,\"timestamp\":\"2019-03-12 11:07:00 +0000\"},{\"microSiteID\":\"81783\",\"id\":1533261026,\"timestamp\":\"2019-03-12 11:07:05 +0000\"}],\"middleName\":null,\"familyRole\":null,\"emailVerified\":\"2018-11-15 06:28:44.13826 +0000\",\"primaryAddress\":{\"company\":null,\"address2\":null,\"stateAbbreviation\":null,\"zipPlus4\":null,\"city\":null,\"state\":null,\"address1\":null,\"houseNumber\":null,\"phone\":null,\"dayTimePhoneNumber\":null,\"zip\":null,\"mobile\":null,\"country\":\"US\",\"address3\":null},\"gender\":\"Male\",\"identifierInformation\":{\"questionsThree\":null,\"questionOne\":null,\"questionTwo\":null,\"answerTwo\":null,\"answerOne\":null,\"questionThree\":null},\"lastUpdated\":\"2019-07-11 04:33:55.71651 +0000\",\"termsAndConditionsAcceptance\":null,\"roles\":[{\"id\":1533260745,\"role\":\"consumer\",\"expiryDate\":null,\"role_assigned\":\"2019-03-12 11:07:05 +0000\",\"verifier\":null}],\"requiresVerification\":null,\"wishList\":null,\"photos\":[],\"email\":\"shashi.ranjan@philips.com\",\"coppaCommunicationSentAt\":null,\"givenName\":\"Shashi Ranjan\",\"retentionConsentGivenAt\":null,\"nettvTermsAgreedDate\":null,\"currentLocation\":null,\"migration\":{\"source\":null,\"migratedAt\":null},\"deactivateAccount\":null,\"janrain\":{\"cloudsearch\":{\"syncAttempts\":null,\"syncUpdated\":null},\"controlFields\":{\"one\":\"false\",\"two\":\"false\",\"three\":\"false\"},\"controlField\":\"ControlFieldReset\",\"properties\":{\"managedBy\":[]}},\"lastLogin\":\"2019-07-11 04:33:55 +0000\",\"interestPromotions\":null,\"ssn\":null,\"NRIC\":null,\"interestCampaigns\":null,\"interestSurveys\":null,\"avmTermsAgreedDate\":null,\"interestWULsounds\":null,\"consentVerified\":null,\"interestCategories\":null,\"externalId\":null,\"created\":\"2018-11-15 06:27:57.120617 +0000\",\"interestStreamiumUpgrades\":null,\"nickName\":null,\"CPF\":null,\"displayName\":null,\"uuid\":\"5f920990-0e8d-49a0-8888-1e8cdb722555\",\"olderThanAgeLimit\":true,\"secondaryPassword\":null,\"aboutMe\":null,\"consents\":[],\"personalDataMarketingProfiling\":null,\"lastNamePronunciation\":null,\"personalDataTransferAcceptance\":null,\"salutation\":null,\"display\":null,\"statuses\":[],\"maritalStatus\":null,\"firstNamePronunciation\":null},\"accessToken\":\"ct5funwca5twwtra\",\"this\":{\"CPF\":null,\"NRIC\":null,\"aboutMe\":null,\"avmTCAgreed\":null,\"avmTermsAgreedDate\":null,\"badgeVillePlayerIDs\":[],\"batchId\":null,\"birthday\":\"2019-01-26\",\"campaignID\":null,\"campaigns\":[],\"catalogLocaleItem\":null,\"children\":[],\"clients\":[{\"clientId\":\"yfvh9udgu9zkuse8jr2nah7uk6et6yas\",\"id\":1534874626,\"name\":null,\"firstLogin\":\"2019-07-08 11:54:15 +0000\",\"lastLogin\":\"2019-07-08 11:54:15 +0000\"},{\"clientId\":\"45fhu7hhxrhqx9sm78mgzx4afm9payau\",\"id\":1534924677,\"name\":null,\"firstLogin\":\"2019-07-03 09:04:05 +0000\",\"lastLogin\":\"2019-07-03 09:04:05 +0000\"}],\"cloudsearch\":{\"syncAttempts\":null,\"syncUpdated\":null},\"consentVerified\":null,\"consentVerifiedAt\":null,\"consents\":[],\"consumerInterests\":[],\"consumerPoints\":null,\"controlField\":null,\"coppaCommunicationSentAt\":null,\"created\":\"2018-11-15 06:27:57.120617 +0000\",\"currentLocation\":null,\"deactivateAccount\":null,\"deactivatedAccount\":null,\"deviceIdentification\":[],\"display\":null,\"displayName\":null,\"email\":\"shashi.ranjan@philips.com\",\"emailVerified\":\"2018-11-15 06:28:44.13826 +0000\",\"externalId\":null,\"familyId\":null,\"familyName\":\"\",\"familyRole\":null,\"firstNamePronunciation\":null,\"friends\":[],\"gender\":\"Male\",\"givenName\":\"Shashi Ranjan\",\"id\":1533260744,\"identifierInformation\":{\"questionsThree\":null,\"questionOne\":null,\"questionTwo\":null,\"answerTwo\":null,\"answerOne\":null,\"questionThree\":null},\"interestAvent\":null,\"interestCampaigns\":null,\"interestCategories\":null,\"interestCommunications\":null,\"interestPromotions\":null,\"interestStreamiumSurveys\":null,\"interestStreamiumUpgrades\":null,\"interestSurveys\":null,\"interestWULsounds\":null,\"janrain\":{\"cloudsearch\":{\"syncAttempts\":null,\"syncUpdated\":null},\"controlFields\":{\"one\":\"false\",\"two\":\"false\",\"three\":\"false\"},\"controlField\":\"ControlFieldReset\",\"properties\":{\"managedBy\":[]}},\"lastLogin\":\"2019-07-11 04:33:55 +0000\",\"lastLoginMethod\":null,\"lastModifiedDate\":null,\"lastModifiedSource\":null,\"lastNamePronunciation\":null,\"lastUpdated\":\"2019-07-11 04:33:55.71651 +0000\",\"lastUsedDevice\":{\"tokenType\":null,\"deviceId\":null,\"deviceToken\":null,\"deviceType\":null},\"legacyID\":null,\"locale\":null,\"maritalStatus\":null,\"marketingOptIn\":{\"locale\":\"en_GB\",\"timestamp\":\"2019-03-18 12:02:14 +0000\"},\"medicalProfessionalRoleSpecified\":null,\"middleName\":null,\"migration\":{\"source\":null,\"migratedAt\":null},\"mobileNumber\":null,\"mobileNumberNeedVerification\":true,\"mobileNumberSmsEnvironment\":null,\"mobileNumberSmsRequestedAt\":null,\"mobileNumberVerified\":null,\"nettvTCAgreed\":null,\"nettvTermsAgreedDate\":null,\"nickName\":null,\"olderThanAgeLimit\":true,\"optIn\":{\"status\":null,\"updated\":null},\"personalDataMarketingProfiling\":null,\"personalDataTransferAcceptance\":null,\"personalDataUsageAcceptance\":null,\"photos\":[],\"post_login_confirmation\":[],\"preferredLanguage\":\"en\",\"primaryAddress\":{\"company\":null,\"address2\":null,\"stateAbbreviation\":null,\"zipPlus4\":null,\"city\":null,\"state\":null,\"address1\":null,\"houseNumber\":null,\"phone\":null,\"dayTimePhoneNumber\":null,\"zip\":null,\"mobile\":null,\"country\":\"US\",\"address3\":null},\"profiles\":[],\"providerMergedLast\":null,\"receiveMarketingEmail\":true,\"requiresVerification\":null,\"retentionConsentGivenAt\":null,\"roles\":[{\"id\":1533260745,\"role\":\"consumer\",\"expiryDate\":null,\"role_assigned\":\"2019-03-12 11:07:05 +0000\",\"verifier\":null}],\"salutation\":null,\"secondaryPassword\":null,\"sitecatalystIDs\":[],\"ssn\":null,\"statuses\":[],\"streamiumServicesTCAgreed\":null,\"termsAndConditionsAcceptance\":null,\"uuid\":\"5f920990-0e8d-49a0-8888-1e8cdb722555\",\"uuidHash\":null,\"visitedMicroSites\":[{\"microSiteID\":\"77000\",\"id\":1533260746,\"timestamp\":\"2019-03-12 11:07:00 +0000\"},{\"microSiteID\":\"81783\",\"id\":1533261026,\"timestamp\":\"2019-03-12 11:07:05 +0000\"}],\"weddingDate\":null,\"wishList\":null}}";
    }

    private Object usrClientIds() {
        return "\"JanRainConfiguration.RegistrationClientID\": {\n" +
                "      \"CN\": \"4rdpm7afu7bny6xnacw32etmt7htfraa\",\n" +
                "      \"default\": \"f2stykcygm7enbwfw2u9fbg6h6syb8yd\"\n" +
                "    }";
    }

    @After
    public void tearDown() throws Exception {
        super.tearDown();
    }

}