package com.philips.platform.appinfra.consentmanager.consenthandler;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

public class DeviceStoredConsentHandler implements ConsentHandlerInterface {

    @VisibleForTesting
    static final String DEVICESTORE_VALUE_DELIMITER = "@#$^";
    private static final int LIST_POS_STATUS = 0;
    private static final int LIST_POS_VERSION = 1;
    private static final int LIST_POS_LOCALE = 2;
    private static final int LIST_POS_TIMESTAMP = 3;
    private static final String DEVICESTORE_TLA = "CAL";
    private static final String DEVICESTORE_ERROR_UPDATE = "Error updating device stored consent";
    private final AppInfraInterface appInfra;
    private HashMap<String, ConsentStatus> consentStatusMemoryCache = new HashMap<>();
    public DeviceStoredConsentHandler(final AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    private void logError(SecureStorageInterface.SecureStorageError storageError, String type) {
        if (storageError.getErrorCode() != null) {
            if(appInfra instanceof AppInfra){
                if (((AppInfra) appInfra).getAppInfraLogInstance() != null) {
                    ((AppInfra) appInfra).getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, type, storageError.getErrorCode().toString());
                }
            }
        }
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

    @VisibleForTesting
    long getUTCTime() {
        return appInfra.getTime().getUTCTime().getTime();
    }

    private String join(List<String> stringList, String delimiter) {
        StringBuilder sb = new StringBuilder();
        for (String i : stringList) {
            sb.append(i).append(delimiter);
        }
        return sb.toString();
    }

    private List<String> split(String string, String delimiter) {
        Pattern pattern = Pattern.compile(Pattern.quote(delimiter));
        return Arrays.asList(pattern.split(string));
    }

    @NonNull
    private String getStoredKey(String type) {
        return DEVICESTORE_TLA + "_" + type;
    }

    @Override
    public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
        if (consentStatusMemoryCache.containsKey(consentType)) {
            callback.onGetConsentsSuccess(consentStatusMemoryCache.get(consentType));
        } else {
            ConsentStatus consentStatus;

            SecureStorageInterface.SecureStorageError storageError = getSecureStorageError();
            String consentInfo = appInfra.getSecureStorage().fetchValueForKey(getStoredKey(consentType), storageError);
            Date timestamp = getTimestamp(String.valueOf(split(consentInfo, DEVICESTORE_VALUE_DELIMITER).get(LIST_POS_TIMESTAMP)));

            if (consentInfo == null || storageError.getErrorCode() != null || consentInfo.toUpperCase().startsWith("FALSE")) {
                logError(storageError, consentType);
                consentStatus = new ConsentStatus(ConsentStates.inactive, 0, timestamp);
            } else {
                consentStatus = new ConsentStatus(ConsentStates.active,
                        Integer.valueOf(split(consentInfo, DEVICESTORE_VALUE_DELIMITER).get(LIST_POS_VERSION)), timestamp);
            }
            consentStatusMemoryCache.put(consentType, consentStatus);
            callback.onGetConsentsSuccess(consentStatus);
        }
    }

    @Override
    public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
        List<String> storeValues = new ArrayList<>();
        storeValues.add(LIST_POS_STATUS, String.valueOf(status));
        storeValues.add(LIST_POS_VERSION, String.valueOf(version));
        storeValues.add(LIST_POS_LOCALE, appInfra.getInternationalization().getBCP47UILocale());
        storeValues.add(LIST_POS_TIMESTAMP, String.valueOf(getUTCTime()));

        String storedValue = join(storeValues, DEVICESTORE_VALUE_DELIMITER);
        SecureStorageInterface.SecureStorageError storageError = getSecureStorageError();
        boolean storeStatus = appInfra.getSecureStorage().storeValueForKey(getStoredKey(consentType), storedValue, storageError);
        Date lastModifiedTimeStamp = appInfra.getTime().getUTCTime();

        if (!storeStatus) {
            logError(storageError, consentType);
            callback.onPostConsentFailed(new ConsentError(DEVICESTORE_ERROR_UPDATE + storageError.getErrorCode().toString(), -1));
            return;
        }
        if (status) {
            consentStatusMemoryCache.put(consentType, new ConsentStatus(ConsentStates.active, version,lastModifiedTimeStamp ));
        } else {
            consentStatusMemoryCache.put(consentType, new ConsentStatus(ConsentStates.rejected, version, lastModifiedTimeStamp));
        }
        callback.onPostConsentSuccess();
    }

    private Date getTimestamp(String timestamp) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
        try {
            return dateFormat.parse(timestamp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
