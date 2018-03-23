package com.philips.platform.mya.catk.device;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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

    public DeviceStoredConsentHandler(final AppInfraInterface appInfra) {
        this.appInfra = appInfra;
    }

    private void logError(SecureStorageInterface.SecureStorageError storageError, String type) {
        if (storageError.getErrorCode() != null) {
            appInfra.getLogging().log(LoggingInterface.LogLevel.ERROR, type, storageError.getErrorCode().toString());
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
        ConsentStatus consentStatus;

        SecureStorageInterface.SecureStorageError storageError = getSecureStorageError();
        String consentInfo = appInfra.getSecureStorage().fetchValueForKey(getStoredKey(consentType), storageError);

        if (consentInfo == null || storageError.getErrorCode() != null || consentInfo.toUpperCase().startsWith("FALSE")) {
            logError(storageError, consentType);
            consentStatus = new ConsentStatus(ConsentStates.inactive, 0);
        }else {
            consentStatus = new ConsentStatus(ConsentStates.active, Integer.valueOf(split(consentInfo, DEVICESTORE_VALUE_DELIMITER).get(LIST_POS_VERSION)));
        }

        callback.onGetConsentsSuccess(consentStatus);
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

        if (!storeStatus) {
            logError(storageError, consentType);
            callback.onPostConsentFailed(new ConsentError(DEVICESTORE_ERROR_UPDATE + storageError.getErrorCode().toString(), -1));
            return;
        }

        callback.onPostConsentSuccess();
    }
}
