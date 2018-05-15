package com.philips.platform.mya.catk;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.mya.catk.datamodel.CachedConsentStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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
    AppInfraInterface appInfra;
    @Mock
    SecureStorageInterface storageInterface;

    @Mock
    SecureStorageInterface.SecureStorageError secureStorageError;

    @Mock
    private AppConfigurationInterface mockConfigInterface;

    private String CONSENT_CACHE_KEY = "CONSENT_CACHE";

    private DateTime NOW = new DateTime(DateTimeZone.UTC);

    private String CONSENT_TYPE_1 = "consentType1";
    private String CONSENT_TYPE_3 = "consentType3";
    private CachedConsentStatus consentTypeStatus1 = new CachedConsentStatus(ConsentStates.active, 1, NOW.plusMinutes(10));
    private String cacheMapWithConsentType3 = "{\"consentType3\":{\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\",\"consentState\":\"active\",\"version\":1}}";
    private String activeConsentJsonForType1 = "{\"consentType1\":{\"consentState\":\"active\",\"version\":1,\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\"}}";
    private String consentStatusJsonForTwoTypes = "{\"consentType1\":{\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\",\"consentState\":\"active\",\"version\":1},\"consentType3\":{\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\",\"consentState\":\"rejected\",\"version\":1}}";
    private ConsentCacheInterface consentCacheInteractor;
    private CachedConsentStatus returnedCachedConsent;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(NOW.getMillis());
        consentCacheInteractor = new ConsentCacheInteractor(appInfra);
        when(appInfra.getSecureStorage()).thenReturn(storageInterface);
    }

    @Test
    public void fetchConsentTypeState_VerifySecureStorage() {
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentCacheIsFetchedFromSecureStorage();
    }

    @Test
    public void fetchConsentTypeState_VerifyInMemoryCache() {
        givenSecureStorageReturns(activeConsentJsonForType1);
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenSecureStorageFetchIsCalledOnce();
    }

    @Test
    public void fetchConsentTypeState_ConsentGiven() {
        givenSecureStorageReturns(activeConsentJsonForType1);
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentStatusReturnedIs(consentTypeStatus1);
    }

    @Test
    public void fetchConsentTypeState_SecureStorageEmpty() {
        givenSecureStorageReturns(null);
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenNullConsentStatusIsreturned();
    }

    @Test
    public void store_VerifyExpiryTime() {
        givenExpiryTimeInMins(1);
        whenStoreConsentStateIsCalled(CONSENT_TYPE_3, ConsentStates.active, 1);
        thenConsentIsStoredInSecureStorage(cacheMapWithConsentType3);
    }

    @Test
    public void fetchConsentTypeState_VerifyStoreIsNotOverwrittenAfterPost() {
        givenExpiryTimeInMins(10);
        givenSecureStorageReturns(activeConsentJsonForType1);
        whenStoreConsentStateIsCalled(CONSENT_TYPE_3, ConsentStates.rejected, 1);
        thenConsentCacheIsFetchedFromSecureStorage();
        thenConsentIsStoredInSecureStorage(consentStatusJsonForTwoTypes);
    }

    private void givenSecureStorageReturns(String cacheMapTest) {
        when(storageInterface.fetchValueForKey(eq(CONSENT_CACHE_KEY), (SecureStorageInterface.SecureStorageError) notNull())).thenReturn(cacheMapTest);
    }

    private void givenExpiryTimeInMins(int minutes) {
        when(appInfra.getConfigInterface()).thenReturn(mockConfigInterface);
        when(mockConfigInterface.getPropertyForKey(eq("ConsentCacheTTLInMinutes"), eq("css"),
                any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(minutes);
        cacheMapWithConsentType3 = "{\"consentType3\":{\"expires\":\"" + (NOW.plusMinutes(minutes)).toString() + "\",\"consentState\":\"active\",\"version\":1}}";
    }

    private void whenStoreConsentStateIsCalled(String consentType, ConsentStates active, int version) {
        consentCacheInteractor.storeConsentTypeState(consentType, active, version);
    }

    private void whenFetchConsentStateIsCalled(String consentType) {
        returnedCachedConsent = consentCacheInteractor.fetchConsentTypeState(consentType);

    }

    private void thenConsentIsStoredInSecureStorage(String expectedConsentCacheJson) {
        verify(storageInterface, times(1)).storeValueForKey(eq(CONSENT_CACHE_KEY), eq(expectedConsentCacheJson), (SecureStorageInterface.SecureStorageError) notNull());
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


    private void thenNullConsentStatusIsreturned() {
        assertNull(returnedCachedConsent);
    }


}
