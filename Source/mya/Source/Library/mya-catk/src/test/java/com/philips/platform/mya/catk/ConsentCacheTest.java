package com.philips.platform.mya.catk;


import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.mya.catk.datamodel.CachedConsentStatus;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

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

    private String CONSENT_CACHE_KEY = "CONSENT_CACHE";

    private DateTime NOW = new DateTime("2015-11-30", DateTimeZone.UTC);

    private String CONSENT_TYPE_1 = "consentType1";
    private String CONSENT_TYPE_2 = "consentType2";
    private CachedConsentStatus consentTypeStatus1 = new CachedConsentStatus(ConsentStates.active, 1, NOW.plusMinutes(10).toDate());
    private CachedConsentStatus consentTypeStatus2 = new CachedConsentStatus(ConsentStates.inactive, 1, NOW.plusMinutes(10).toDate());
    private String cacheMapTest = "{\"consentType1\":{\"consentState\":\"active\",\"version\":\"1\",\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\"}}";
    private ConsentCacheInterface consentCache;
    private String consentType;

    @Before
    public void setUp() {
        consentCache = new ConsentCache(appInfra);
        when(appInfra.getSecureStorage()).thenReturn(storageInterface);
//        when(storageInterface.storeValueForKey(CONSENT_TYPE_1, "1", secureStorageError)).thenReturn(true);
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

    private void givenSecureStorageIsEmpty() {
        when(storageInterface.fetchValueForKey(eq(CONSENT_CACHE_KEY), (SecureStorageInterface.SecureStorageError) notNull())).thenReturn(null);
    }


    private void givenConsentType(String consentTypeParam) {
        consentType = consentTypeParam;
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

    private void whenFetchConsentTypeStateIsCalled() {
        consentCache.fetchConsentTypeState(consentType, fetchConsentCacheCallback);

    }

    private void thenNullConsentStatusIsreturned() {
        verify(fetchConsentCacheCallback).onGetConsentsSuccess(null);
    }


}
