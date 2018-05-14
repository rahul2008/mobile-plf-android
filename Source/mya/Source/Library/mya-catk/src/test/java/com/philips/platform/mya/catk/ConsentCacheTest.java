package com.philips.platform.mya.catk;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.mya.catk.datamodel.CachedConsentStatus;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.notNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConsentCacheTest {

    @Mock
    AppInfraInterface appInfra;
    @Mock
    SecureStorageInterface storageInterface;
    @Mock
    private ConsentCacheInterface.FetchConsentCacheCallback fetchConsentCacheCallback;

    @Mock
    private PostConsentTypeCallback postConsentTypeCallback;
    @Mock
    SecureStorageInterface.SecureStorageError secureStorageError;

    @Mock
    private AppConfigurationInterface mockConfigInterface;

    private String CONSENT_CACHE_KEY = "CONSENT_CACHE";

    private DateTime NOW = new DateTime(DateTimeZone.UTC);

    private String CONSENT_TYPE_1 = "consentType1";
    private String CONSENT_TYPE_2 = "consentType2";
    private String CONSENT_TYPE_3 = "consentType3";
    private CachedConsentStatus consentTypeStatus1 = new CachedConsentStatus(ConsentStates.active, 1, NOW.plusMinutes(10));
    private String cacheMapWithConsentType3 = "{\"consentType3\":{\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\",\"consentState\":\"active\",\"version\":1}}";
    private String cacheMapTest = "{\"consentType1\":{\"consentState\":\"active\",\"version\":1,\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\"}}";
    private ConsentCacheInterface consentCache;
    private String consentType;

    @Before
    public void setUp() {
        DateTimeUtils.setCurrentMillisFixed(NOW.getMillis());
        consentCache = new ConsentCache(appInfra);
        when(appInfra.getSecureStorage()).thenReturn(storageInterface);
        when(storageInterface.fetchValueForKey(eq(CONSENT_CACHE_KEY), (SecureStorageInterface.SecureStorageError) notNull())).thenReturn(cacheMapTest);

    }

    @Test
    public void fetchConsentTypeState_VerifySecureStorage() {
        givenConsentType(CONSENT_TYPE_1);
        whenFetchConsentTypeStateIsCalled();
        thenSecureStorageIsCalled();
    }


    @Test
    public void fetchConsentTypeState_VerifyInMemoryCache() {
        givenConsentType(CONSENT_TYPE_1);
        whenFetchConsentTypeStateIsCalled();
        whenFetchConsentTypeStateIsCalled();
        thenSecureStorageIsCalledOnce();

    }

    @Test
    public void fetchConsentTypeState_ConsentGiven() {
        givenConsentType(CONSENT_TYPE_1);
        whenFetchConsentTypeStateIsCalled();
        thenActiveConsentStatusIsreturned();
    }

    @Test
    public void fetchConsentTypeState_ConsentNotGiven() {
        givenConsentType(CONSENT_TYPE_2);
        whenFetchConsentTypeStateIsCalled();
        thenNullConsentStatusIsreturned();
    }

    @Test
    public void fetchConsentTypeState_SecureStorageEmpty() {
        givenSecureStorageIsEmpty();
        givenConsentType(CONSENT_TYPE_1);
        whenFetchConsentTypeStateIsCalled();
        thenNullConsentStatusIsreturned();
    }


    @Test
    public void store_VerifyExpiryTime() {
        givenExpiryTimeInMins(1);
        whenStoreConsentTypeStateIsCalled(CONSENT_TYPE_3);
        thenConsentIsStoredInSecureStorage();
    }


    private void givenSecureStorageIsEmpty() {
        when(storageInterface.fetchValueForKey(eq(CONSENT_CACHE_KEY), (SecureStorageInterface.SecureStorageError) notNull())).thenReturn(null);
    }


    private void givenConsentType(String consentTypeParam) {
        consentType = consentTypeParam;
    }

    private void givenExpiryTimeInMins(int minutes) {
        when(appInfra.getConfigInterface()).thenReturn(mockConfigInterface);
        when(mockConfigInterface.getPropertyForKey(eq("ConsentCacheTTLInMinutes"), eq("css"),
                any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(minutes);
        cacheMapWithConsentType3 = "{\"consentType3\":{\"expires\":\"" + (NOW.plusMinutes(minutes)).toString() + "\",\"consentState\":\"active\",\"version\":1}}";
    }


    private void whenStoreConsentTypeStateIsCalled(String consentType) {
        consentCache.storeConsentTypeState(consentType, ConsentStates.active, 1, postConsentTypeCallback);
    }

    private void whenFetchConsentTypeStateIsCalled() {
        consentCache.fetchConsentTypeState(consentType, fetchConsentCacheCallback);

    }

    private void thenConsentIsStoredInSecureStorage() {
        verify(storageInterface, times(1)).storeValueForKey(eq(CONSENT_CACHE_KEY), eq(cacheMapWithConsentType3), (SecureStorageInterface.SecureStorageError) notNull());
    }

    private void thenSecureStorageIsCalledOnce() {
        verify(storageInterface, times(1)).fetchValueForKey(eq(CONSENT_CACHE_KEY), (SecureStorageInterface.SecureStorageError) notNull());
    }

    private void thenActiveConsentStatusIsreturned() {
        verify(fetchConsentCacheCallback).onGetConsentsSuccess(consentTypeStatus1);
    }

    private void thenSecureStorageIsCalled() {
        verify(storageInterface).fetchValueForKey(eq(CONSENT_CACHE_KEY), (SecureStorageInterface.SecureStorageError) notNull());
    }


    private void thenNullConsentStatusIsreturned() {
        verify(fetchConsentCacheCallback).onGetConsentsSuccess(null);
    }


}
