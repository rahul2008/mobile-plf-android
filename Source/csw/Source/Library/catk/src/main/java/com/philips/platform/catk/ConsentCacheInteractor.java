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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Consent cache interactor implementation.
 *
 * @since 18.2.0s
 */

public class ConsentCacheInteractor implements ConsentCacheInterface {

    private static final String DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    private static final String CONSENT_CACHE_KEY = "CONSENT_CACHE";
    private static final String CONSENT_EXPIRY_KEY = "ConsentCacheTTLInMinutes";
    private Map<String, Map<String, CachedConsentStatus>> inMemoryCache = new HashMap<>();
    private AppInfraInterface appInfra;
    private DateTimeProvider dateTimeProvider;
    private Gson objGson = new GsonBuilder()
            .registerTypeAdapter(DateTime.class, new DateTimeSerializer())
            .registerTypeAdapter(DateTime.class, new DateTimeDeSerializer())
            .registerTypeAdapter(Date.class, new DateSerializer())
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    public ConsentCacheInteractor(AppInfraInterface appInfra) {
        this.appInfra = appInfra;
        dateTimeProvider = new DateTimeProvider() {
            @Override
            public DateTime now(final DateTimeZone dateTimeZone) {
                return DateTime.now(dateTimeZone);
            }
        };
    }

    ConsentCacheInteractor(final AppInfraInterface appInfra, final DateTimeProvider dateTimeProvider) {
        this.appInfra = appInfra;
        this.dateTimeProvider = dateTimeProvider;
    }

    @Override
    public CachedConsentStatus fetchConsentTypeState(String consentType) {
        if (inMemoryCache.get(getCurrentLoggedInUserId()) == null || inMemoryCache.get(getCurrentLoggedInUserId()).get(consentType) == null) {
            try {
                inMemoryCache = getMapFromSecureStorage();
            } catch (LegacyConsentStatusTimestampsException e) {
                inMemoryCache.clear();
                appInfra.getSecureStorage().removeValueForKey(CONSENT_CACHE_KEY);
                return null;
            }
        }
        return inMemoryCache.get(getCurrentLoggedInUserId()).get(consentType);
    }

    @Override
    public void storeConsentState(String consentType, ConsentStates status, int version, Date timestamp) {
        inMemoryCache = getMapFromSecureStorage();
        inMemoryCache.get(getCurrentLoggedInUserId()).put(consentType,
                new CachedConsentStatus(status, version, timestamp,
                        (dateTimeProvider.now(DateTimeZone.UTC)).plusMinutes(getConfiguredExpiryTime())));
        writeMapToSecureStorage(inMemoryCache);
    }

    private int getConfiguredExpiryTime() {
        AppConfigurationInterface appConfigInterface = appInfra.getConfigInterface();
        AppConfigurationInterface.AppConfigurationError error = new AppConfigurationInterface.AppConfigurationError();
        return (int) appConfigInterface.getPropertyForKey(CONSENT_EXPIRY_KEY, "css", error);
    }

    private Map<String, Map<String, CachedConsentStatus>> getMapFromSecureStorage() {
        String serializedCache = appInfra.getSecureStorage().fetchValueForKey(CONSENT_CACHE_KEY, getSecureStorageError());
        Type listType = new TypeToken<Map<String, Map<String, CachedConsentStatus>>>() {
        }.getType();
        Map<String, Map<String, CachedConsentStatus>> temp = objGson.fromJson(serializedCache, listType);
        temp = temp == null ? new HashMap<>() : temp;
        Map<String, CachedConsentStatus> consentCachedForUser = temp.get(getCurrentLoggedInUserId());
        if (consentCachedForUser == null) {
            appInfra.getSecureStorage().removeValueForKey(CONSENT_CACHE_KEY);
            temp.put(getCurrentLoggedInUserId(), new HashMap<>());
            return temp;
        }
        return temp;
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

    class DateTimeSerializer implements JsonSerializer<DateTime> {
        @Override
        public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
    }

    class DateSerializer implements JsonSerializer<Date> {

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(new SimpleDateFormat(DATE_PATTERN).format(src));
        }
    }

    class DateTimeDeSerializer implements JsonDeserializer<DateTime> {

        @Override
        public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new DateTime(json.getAsJsonPrimitive().getAsString(), DateTimeZone.UTC);
        }
    }

    class DateDeserializer implements JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return parseDate(json.getAsString(), DATE_PATTERN);
        }

        private Date parseDate(String date, String pattern) {
            try {
                return new SimpleDateFormat(pattern).parse(date);
            } catch (ParseException e) {
                throw new LegacyConsentStatusTimestampsException();
            }
        }
    }

    private String getCurrentLoggedInUserId() {
        return ConsentsClient.getInstance().getCatkComponent().getUser().getHsdpUUID();
    }

    public class LegacyConsentStatusTimestampsException extends RuntimeException {

    }
}
