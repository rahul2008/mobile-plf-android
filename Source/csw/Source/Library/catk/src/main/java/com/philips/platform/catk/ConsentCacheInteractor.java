package com.philips.platform.catk;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.catk.datamodel.CachedConsentStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Consent cache interactor implementation.
 *
 * @since 18.2.0s
 */

public class ConsentCacheInteractor implements ConsentCacheInterface {

    private String CONSENT_CACHE_KEY = "CONSENT_CACHE";

    private String CONSENT_EXPIRY_KEY = "ConsentCacheTTLInMinutes";

    private AppInfraInterface appInfra;

    private Gson objGson = new GsonBuilder().registerTypeAdapter(DateTime.class, new DateTimeSerializer())
            .registerTypeAdapter(DateTime.class, new DateTimeDeSerializer()).create();

    private Map<String, Map<String, CachedConsentStatus>> inMemoryCache = new HashMap<>();

    public ConsentCacheInteractor(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    @Override
    public CachedConsentStatus fetchConsentTypeState(String consentType) {
        if (inMemoryCache.get(getCurrentLoggedInUserId()) == null || inMemoryCache.get(getCurrentLoggedInUserId()).get(consentType) == null) {
            inMemoryCache = getMapFromSecureStorage();
        }
        return inMemoryCache.get(getCurrentLoggedInUserId()).get(consentType);
    }

    @Override
    public void storeConsentState(String consentType, ConsentStates status, int version, Date lastModifiedTimeStamp) {
        inMemoryCache = getMapFromSecureStorage();
        inMemoryCache.get(getCurrentLoggedInUserId()).put(consentType, new CachedConsentStatus(status, version, (new DateTime(DateTimeZone.UTC)).plusMinutes(getConfiguredExpiryTime()), lastModifiedTimeStamp));
        writeMapToSecureStorage(inMemoryCache);
    }

    private int getConfiguredExpiryTime() {
        AppConfigurationInterface appConfigInterface = appInfra.getConfigInterface();
        AppConfigurationInterface.AppConfigurationError error = new AppConfigurationInterface.AppConfigurationError();
        return (int) appConfigInterface.getPropertyForKey(CONSENT_EXPIRY_KEY, "css", error);
    }

    private Map<String, Map<String, CachedConsentStatus>> getMapFromSecureStorage() {
        String serializedCache = appInfra.getSecureStorage().fetchValueForKey(CONSENT_CACHE_KEY, getSecureStorageError());
        Type listType = new TypeToken<Map<String, Map<String, CachedConsentStatus>>>() { }.getType();
        Map<String, Map<String, CachedConsentStatus>> temp = objGson.fromJson(serializedCache, listType);
        temp = temp == null ? new HashMap<String, Map<String, CachedConsentStatus>>() : temp;
        Map<String, CachedConsentStatus> consentCachedForUser = temp.get(getCurrentLoggedInUserId());
        if (consentCachedForUser == null) {
            appInfra.getSecureStorage().removeValueForKey(CONSENT_CACHE_KEY);
            throwRuntimeExceptionIfUserIdIsNull();
            temp.put(getCurrentLoggedInUserId(),  new HashMap<String, CachedConsentStatus>());
            return temp;
        }
        return temp;
    }

    private void throwRuntimeExceptionIfUserIdIsNull() {
        if(getCurrentLoggedInUserId() == null){
            throw new RuntimeException("user is not logged in");
        }
    }

    private synchronized void writeMapToSecureStorage(Map<String, Map<String, CachedConsentStatus>> cacheMap) {
        appInfra.getSecureStorage().storeValueForKey(CONSENT_CACHE_KEY, objGson.toJson(cacheMap), getSecureStorageError());
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

    @Override
    public void clearCache(String consentType) {
        inMemoryCache = getMapFromSecureStorage();
        inMemoryCache.get(getCurrentLoggedInUserId()).remove(consentType);
        writeMapToSecureStorage(inMemoryCache);
    }

    class DateTimeSerializer implements JsonSerializer {
        @Override
        public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    class DateTimeDeSerializer implements JsonDeserializer {

        @Override
        public Object deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new DateTime(json.getAsJsonPrimitive().getAsString(), DateTimeZone.UTC);
        }
    }

    private String getCurrentLoggedInUserId() {
        return ConsentsClient.getInstance().getCatkComponent().getUser().getHsdpUUID();
    }
}
