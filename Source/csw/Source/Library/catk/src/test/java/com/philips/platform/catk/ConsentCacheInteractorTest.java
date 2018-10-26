package com.philips.platform.catk;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.catk.datamodel.CachedConsentStatus;
import com.philips.platform.catk.mock.CatkComponentMock;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsentCacheInteractorTest {

    @Mock
    private AppInfraInterface appInfra;
    @Mock
    private SecureStorageInterface storageInterface;

    @Mock
    private User userMock;

    @Mock
    private AppConfigurationInterface mockConfigInterface;

    private String CONSENT_CACHE_KEY = "CONSENT_CACHE";

    private DateTime NOW = new DateTime(DateTimeZone.UTC);

    private String CONSENT_TYPE_1 = "consentType1";
    private String CONSENT_TYPE_3 = "consentType3";
    private CachedConsentStatus consentTypeStatus1 = new CachedConsentStatus(ConsentStates.active, 1, getDate(), NOW.plusMinutes(10));
    private String consentStatusJsonForTwoTypes = "{\"userId\":{\"consentType1\":{\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\",\"consentState\":\"active\",\"version\":1,\"timestamp\":\"Dec 31, 1998 1:00:00 PM\"},\"consentType3\":{\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\",\"consentState\":\"rejected\",\"version\":1,\"timestamp\":\"Dec 31, 1998 1:00:00 PM\"}}}";
    private ConsentCacheInteractor consentCacheInteractor;
    private CachedConsentStatus returnedCachedConsent;

    private static Date getDate(){
        String sDate1="10/07/2018";
        Date date1 = null;
        try {
            date1=new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date1;
    }

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(NOW.getMillis());
        consentCacheInteractor = new ConsentCacheInteractor(appInfra);
        when(appInfra.getSecureStorage()).thenReturn(storageInterface);
        when(userMock.getHsdpUUID()).thenReturn("userId");
        CatkComponentMock catkComponentMock = new CatkComponentMock();
        catkComponentMock.getUser_return = userMock;
        ConsentsClient.getInstance().setCatkComponent(catkComponentMock);
    }

    @Test
    public void fetchConsentTypeState_VerifySecureStorage() {
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentCacheIsFetchedFromSecureStorage();
    }

    @Test
    public void fetchConsentTypeState_VerifyInMemoryCache() {
        givenSecureStorageReturns(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_1, 10, "Dec 31, 1998 12:00:00 AM"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenSecureStorageFetchIsCalledOnce();
    }

    @Test
    public void fetchConsentTypeState_VerifyInMemoryCache_DifferentDateFormat() {
        givenSecureStorageReturns(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_1, 10, "Dec 31, 1998 12:00:00"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
    }

    @Test
    public void fetchConsentTypeState_ConsentGiven() {
        givenSecureStorageReturns(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_1, 10, "Dec 31, 1998 12:00:00 AM"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentStatusReturnedIs(consentTypeStatus1);
    }

    @Test
    public void fetchConsentTypeState_SecureStorageEmpty() {
        givenSecureStorageReturns(null);
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenNullConsentStatusIsReturned();
    }

    @Test(expected=NullPointerException.class)
    public void fetchConsentTypeState_WhenUserIdIsNull() {
        when(userMock.getHsdpUUID()).thenReturn(null);
        givenSecureStorageReturns(getSingleConsentStatusJson(null, "active", CONSENT_TYPE_1, 10, "Dec 31, 1998 12:00:00 AM"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
    }

    @Test
    public void store_VerifyExpiryTime() {
        givenExpiryTimeConfiguredInAppConfigIs(1);
        whenStoreConsentStateIsCalled(CONSENT_TYPE_3, ConsentStates.active, 1);
        thenConsentIsStoredInSecureStorage(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_3, 1, "Dec 31, 1998 1:00:00 PM"));
    }

    @Test
    public void fetchConsentTypeState_VerifyStoreIsNotOverwrittenAfterPost() {
        givenExpiryTimeConfiguredInAppConfigIs(10);
        givenSecureStorageReturns(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_1, 10, "Dec 31, 1998 1:00:00 PM"));
        whenStoreConsentStateIsCalled(CONSENT_TYPE_3, ConsentStates.rejected, 1);
        thenConsentCacheIsFetchedFromSecureStorage();
        thenConsentIsStoredInSecureStorage(consentStatusJsonForTwoTypes);
    }

    @Test
    public void fetchConsent_ShouldNotReturnAnotherUserData() {
        givenSecureStorageReturns(getSingleConsentStatusJson("anotherUser", "active", CONSENT_TYPE_1, 10, "Dec 31, 1998 12:00:00 AM"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentCacheIsFetchedFromSecureStorage();
        thenNullConsentStatusIsReturned();
    }

    @Test
    public void fetchConsent_ClearsCacheWhenAnotherUserIsLoggedIn() {
        givenSecureStorageReturns(getSingleConsentStatusJson("anotherUser", "active", CONSENT_TYPE_1, 1, "Dec 31, 1998 12:00:00 AM"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentCacheIsFetchedFromSecureStorage();
        thenConsentCacheIsCleared();
    }

    @Test
    public void storeConsent_IsStoredWithCurrentLoggedInUserId() {
        givenExpiryTimeConfiguredInAppConfigIs(1);
        when(userMock.getHsdpUUID()).thenReturn("someUserId");
        whenStoreConsentStateIsCalled(CONSENT_TYPE_3, ConsentStates.active, 1);
        thenConsentIsStoredInSecureStorage(getSingleConsentStatusJson("someUserId", "active", CONSENT_TYPE_3, 1, "Dec 31, 1998 1:00:00 PM"));
    }

    @Test
    public void testClearCache() {
        givenSecureStorageReturns(consentStatusJsonForTwoTypes);
        whenClearingCacheFor("consentType1");
        thenConsentIsStoredInSecureStorage(getSingleConsentStatusJson("userId", "rejected", "consentType3", 10, "Dec 31, 1998 1:00:00 PM"));
    }

    @Test
    public void testClearCache_WhenConsentStatusNotThere() {
        givenSecureStorageReturns(consentStatusJsonForTwoTypes);
        whenClearingCacheFor("nonExistingType");
        thenConsentIsStoredInSecureStorage(consentStatusJsonForTwoTypes);
    }

    private void givenSecureStorageReturns(String cacheMapTest) {
        when(storageInterface.fetchValueForKey(eq(CONSENT_CACHE_KEY), (SecureStorageInterface.SecureStorageError) notNull())).thenReturn(cacheMapTest);
    }

    private void givenExpiryTimeConfiguredInAppConfigIs(int minutes) {
        when(appInfra.getConfigInterface()).thenReturn(mockConfigInterface);
        when(mockConfigInterface.getPropertyForKey(eq("ConsentCacheTTLInMinutes"), eq("css"),
                any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(minutes);
    }

    private void whenStoreConsentStateIsCalled(String consentType, ConsentStates active, int version) {
        String sDate1="Dec 31, 1998 1:00:00 PM";
        Date date1 = null;
        try {
            date1=new SimpleDateFormat("MMM d, yyyy hh:mm:ss a").parse(sDate1);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        consentCacheInteractor.storeConsentState(consentType, active, version, date1);
    }
    private void whenFetchConsentStateIsCalled(String consentType) {
        returnedCachedConsent = consentCacheInteractor.fetchConsentTypeState(consentType);
    }


    private void whenClearingCacheFor(String consentType) {
        consentCacheInteractor.clearCache(consentType);
    }

    private void thenConsentIsStoredInSecureStorage(String expectedConsentCacheJson) {
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        verify(storageInterface, times(1)).storeValueForKey(eq(CONSENT_CACHE_KEY), argumentCaptor.capture(), (SecureStorageInterface.SecureStorageError) notNull());
        assertEquals(expectedConsentCacheJson, argumentCaptor.getValue());
    }

    private void assertEqualJson(String expectedConsentCacheJson, ArgumentCaptor<String> argumentCaptor) {
        JsonParser parser = new JsonParser();
        JsonElement expectedJson = parser.parse(expectedConsentCacheJson);
        JsonElement capturedJson = parser.parse(argumentCaptor.getValue());
        assertEquals(expectedJson, capturedJson);
    }

    private void thenSecureStorageFetchIsCalledOnce() {
        verify(storageInterface, times(1)).fetchValueForKey(eq(CONSENT_CACHE_KEY), (SecureStorageInterface.SecureStorageError) notNull());
    }

    private void thenConsentStatusReturnedIs(CachedConsentStatus expectedConsentStatus) {
        assertEquals(expectedConsentStatus, returnedCachedConsent);
    }

    private void thenConsentCacheIsFetchedFromSecureStorage() {
        verify(storageInterface).fetchValueForKey(eq(CONSENT_CACHE_KEY), (SecureStorageInterface.SecureStorageError) notNull());
    }

    private void thenNullConsentStatusIsReturned() {
        assertNull(returnedCachedConsent);
    }

    private void thenConsentCacheIsCleared() {
        verify(storageInterface).removeValueForKey(CONSENT_CACHE_KEY);
    }

    private String getSingleConsentStatusJson(final String userId, final String status, final String consentType, int expiryMinutes, final String timestamp){
       // return "{\"" + userId + "\":{\"" + consentType + "\":{\"consentState\":\"" + status + "\",\"version\":1,\"expires\":\"" + (NOW.plusMinutes(expiryMinutes)).toString() + "\"}}}";

        return "{\"" + userId+ "\":{\"" +consentType + "\":{\"expires\":\"" +(NOW.plusMinutes(expiryMinutes)).toString() +"\",\"consentState\":\"" +status+ "\",\"version\":1,\"timestamp\":\"" + timestamp + "\"}}}";
    }

}
