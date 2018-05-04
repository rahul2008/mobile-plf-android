package com.philips.platform.mya.catk;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.mya.catk.datamodel.CachedConsentStatus;
import com.philips.platform.pif.chi.PostConsentTypeCallback;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Consent cache implementation.
 *
 * @since 18.2.0s
 */

public class ConsentCache implements ConsentCacheInterface {

    private String CONSENT_CACHE_KEY = "CONSENT_CACHE";
    private AppInfraInterface appInfra;
    private Gson objGson = new GsonBuilder().create();
    private Map<String, CachedConsentStatus> inMemoryCache;

    public ConsentCache(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    @Override
    public void fetchConsentTypeState(String consentType, FetchConsentCacheCallback callback) {
        if (inMemoryCache != null) {
            callback.onGetConsentsSuccess(inMemoryCache.get(consentType));
            return;
        }
        String serializedCache = appInfra.getSecureStorage().fetchValueForKey(CONSENT_CACHE_KEY, getSecureStorageError());
        Type listType = new TypeToken<Map<String, CachedConsentStatus>>() {
        }.getType();
        inMemoryCache = objGson.fromJson(serializedCache, listType);
        if (inMemoryCache == null) {
            callback.onGetConsentsSuccess(null);
        } else {
            callback.onGetConsentsSuccess(inMemoryCache.get(consentType));
        }

    }

    @Override
    public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {

    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }
}
