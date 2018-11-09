/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.catk;


import com.philips.cdp.registration.User;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.catk.datamodel.CachedConsentStatus;
import com.philips.platform.catk.mock.AppInfraInterfaceMock;
import com.philips.platform.catk.mock.CatkComponentMock;
import com.philips.platform.catk.mock.SecureStorageInterfaceMock;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ConsentCacheInteractorTest {

    private static final String CONSENT_CACHE_KEY = "CONSENT_CACHE";
    private static final DateTime NOW = new DateTime(DateTimeZone.UTC);
    private static final String CONSENT_TYPE_1 = "consentType1";
    private static final String CONSENT_TYPE_3 = "consentType3";
    private static final String CONSENT_STATUS_JSON_FOR_TWO_TYPES = "{\"userId\":{\"consentType1\":{\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\",\"consentState\":\"active\",\"version\":1,\"timestamp\":\"1998-12-31T13:00:00.000+0100\"},\"consentType3\":{\"expires\":\"" + (NOW.plusMinutes(10)).toString() + "\",\"consentState\":\"rejected\",\"version\":1,\"timestamp\":\"1998-12-31T13:00:00.000+0100\"}}}";
    private static final CachedConsentStatus CACHED_CONSENT_STATUS = new CachedConsentStatus(ConsentStates.active, 1, getDate(), NOW.plusMinutes(10));
    private ConsentCacheInteractor consentCacheInteractor;
    private CachedConsentStatus returnedCachedConsent;
    private AppInfraInterface appInfra;
    private SecureStorageInterfaceMock storageInterface;

    @Mock
    private User user;

    @Mock
    private AppConfigurationInterface configInterface;

    @Before
    public void setUp() {
        initMocks(this);

        storageInterface = new SecureStorageInterfaceMock();
        appInfra = new AppInfraInterfaceMock(storageInterface, configInterface);
        consentCacheInteractor = new ConsentCacheInteractor(appInfra, dateTimeZone -> NOW);
        when(user.getHsdpUUID()).thenReturn("userId");
        CatkComponentMock catkComponent = new CatkComponentMock();
        catkComponent.getUser_return = user;
        ConsentsClient.getInstance().setCatkComponent(catkComponent);
    }

    @Test
    public void fetchConsentTypeState_VerifySecureStorage() {
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentCacheIsLoadedFromSecureStorage();
    }

    @Test
    public void fetchConsentTypeState_VerifyInMemoryCache() {
        givenSecureStorageReturns(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_1, 10, "1998-12-31T13:00:00.000+0100"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentCacheIsLoadedFromSecureStorage();
    }

    @Test
    public void fetchConsentTypeState_VerifyInMemoryCache_returnsNull_WhenLegacyTimestampFound() {
        givenSecureStorageReturns(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_1, 10, "Dec 31, 1998 1:00:00 PM"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentStatusReturnedIs(null);
        thenConsentCacheIsCleared();
    }

    @Test
    public void fetchConsentTypeState_ConsentGiven() {
        givenSecureStorageReturns(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_1, 10, "1998-12-31T13:00:00.000+0100"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentStatusReturnedIs(CACHED_CONSENT_STATUS);
    }

    @Test
    public void fetchConsentTypeState_SecureStorageEmpty() {
        givenSecureStorageReturns(null);
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenNullConsentStatusIsReturned();
    }

    @Test(expected = NullPointerException.class)
    public void fetchConsentTypeState_WhenUserIdIsNull() {
        when(user.getHsdpUUID()).thenReturn(null);
        givenSecureStorageReturns(getSingleConsentStatusJson(null, "active", CONSENT_TYPE_1, 10, "1998-12-31T13:00:00.000+0100"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
    }

    @Test
    public void store_VerifyExpiryTime() {
        givenExpiryTimeConfiguredInAppConfigIs(1);
        whenStoreConsentStateIsCalled(CONSENT_TYPE_3, ConsentStates.active, 1);
        thenConsentIsStoredInSecureStorage(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_3, 1, "1998-12-31T13:00:00.000+0100"));
    }

    @Test
    public void fetchConsentTypeState_VerifyStoreIsNotOverwrittenAfterPost() {
        givenExpiryTimeConfiguredInAppConfigIs(10);
        givenSecureStorageReturns(getSingleConsentStatusJson("userId", "active", CONSENT_TYPE_1, 10, "1998-12-31T13:00:00.000+0100"));
        whenStoreConsentStateIsCalled(CONSENT_TYPE_3, ConsentStates.rejected, 1);
        thenConsentCacheIsLoadedFromSecureStorage();
        thenConsentIsStoredInSecureStorage(CONSENT_STATUS_JSON_FOR_TWO_TYPES);
    }

    @Test
    public void fetchConsent_ShouldNotReturnAnotherUserData() {
        givenSecureStorageReturns(getSingleConsentStatusJson("anotherUser", "active", CONSENT_TYPE_1, 10, "1998-12-31T13:00:00.000+0100"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentCacheIsLoadedFromSecureStorage();
        thenNullConsentStatusIsReturned();
    }

    @Test
    public void fetchConsent_ClearsCacheWhenAnotherUserIsLoggedIn() {
        givenSecureStorageReturns(getSingleConsentStatusJson("anotherUser", "active", CONSENT_TYPE_1, 1, "1998-12-31T13:00:00.000+0100"));
        whenFetchConsentStateIsCalled(CONSENT_TYPE_1);
        thenConsentCacheIsLoadedFromSecureStorage();
        thenConsentCacheIsCleared();
    }

    @Test
    public void storeConsent_IsStoredWithCurrentLoggedInUserId() {
        givenExpiryTimeConfiguredInAppConfigIs(1);
        when(user.getHsdpUUID()).thenReturn("someUserId");
        whenStoreConsentStateIsCalled(CONSENT_TYPE_3, ConsentStates.active, 1);
        thenConsentIsStoredInSecureStorage(getSingleConsentStatusJson("someUserId", "active", CONSENT_TYPE_3, 1, "1998-12-31T13:00:00.000+0100"));
    }

    @Test
    public void testClearCache() {
        givenSecureStorageReturns(CONSENT_STATUS_JSON_FOR_TWO_TYPES);
        whenClearingCacheFor("consentType1");
        thenConsentIsStoredInSecureStorage(getSingleConsentStatusJson("userId", "rejected", "consentType3", 10, "1998-12-31T13:00:00.000+0100"));
    }

    @Test
    public void testClearCache_WhenConsentStatusNotThere() {
        givenSecureStorageReturns(CONSENT_STATUS_JSON_FOR_TWO_TYPES);
        whenClearingCacheFor("nonExistingType");
        thenConsentIsStoredInSecureStorage(CONSENT_STATUS_JSON_FOR_TWO_TYPES);
    }

    private void givenSecureStorageReturns(String cacheMapTest) {
        storageInterface.valueToReturn = cacheMapTest;
    }

    private void givenExpiryTimeConfiguredInAppConfigIs(int minutes) {
        when(configInterface.getPropertyForKey(eq("ConsentCacheTTLInMinutes"), eq("css"),
                any(AppConfigurationInterface.AppConfigurationError.class))).thenReturn(minutes);
    }

    private void whenStoreConsentStateIsCalled(String consentType, ConsentStates active, int version) {
        try {
            Date date = new SimpleDateFormat("MMM d, yyyy hh:mm:ss a").parse("Dec 31, 1998 1:00:00 PM");
            consentCacheInteractor.storeConsentState(consentType, active, version, date);
        } catch (ParseException e) {
            fail("Error creating date");
        }
    }

    private void whenFetchConsentStateIsCalled(String consentType) {
        returnedCachedConsent = consentCacheInteractor.fetchConsentTypeState(consentType);
    }

    private void whenClearingCacheFor(String consentType) {
        consentCacheInteractor.clearCache(consentType);
    }

    private void thenConsentIsStoredInSecureStorage(String expectedConsentCacheJson) {
        assertEquals(CONSENT_CACHE_KEY, storageInterface.storeValueForKey_Key);
        assertEquals(expectedConsentCacheJson, storageInterface.storeValueForKey_Value);
    }

    private void thenConsentCacheIsLoadedFromSecureStorage() {
        assertEquals(CONSENT_CACHE_KEY, storageInterface.fetchValueForKey_Key);
    }

    private void thenConsentStatusReturnedIs(CachedConsentStatus expectedConsentStatus) {
        assertEquals(expectedConsentStatus, returnedCachedConsent);
    }

    private void thenNullConsentStatusIsReturned() {
        assertNull(returnedCachedConsent);
    }

    private void thenConsentCacheIsCleared() {
        assertEquals(CONSENT_CACHE_KEY, storageInterface.removeValueForKey_Key);
    }

    private static Date getDate() {
        try {
            return new SimpleDateFormat("dd/MM/yyyy").parse("10/07/2018");
        } catch (ParseException e) {
            fail("error creating date");
        }
        return null;
    }

    private String getSingleConsentStatusJson(final String userId, final String status, final String consentType, int expiryMinutes, final String timestamp) {
        return "{\"" + userId + "\":{\"" + consentType + "\":{\"expires\":\"" + (NOW.plusMinutes(expiryMinutes)).toString() + "\",\"consentState\":\"" + status + "\",\"version\":1,\"timestamp\":\"" + timestamp + "\"}}}";
    }

}
