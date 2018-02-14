package com.philips.platform.mya.catk.device;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.mya.catk.utils.CatkHelper;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

public class DeviceStoredConsentHandler implements ConsentHandlerInterface {

    private final AppInfra appInfra;
    private static final int LIST_POS_STATUS = 0;
    private static final int LIST_POS_VERSION = 1;
    private static final int LIST_POS_TIMESTAMP = 2;

    @VisibleForTesting
    static final String DEVICESTORE_VALUE_DELIMITER = "@#$^";
    private static final String DEVICESTORE_TLA = "CAL";
    private static final String DEVICESTORE_ERROR_UPDATE = "Error updating device stored consent";

    public DeviceStoredConsentHandler(final AppInfra appInfra) {
        this.appInfra = appInfra;
    }

    @Override
    public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {
        callback.onGetConsentsSuccess(getSuccessConsentForStatus(consentDefinition, processDefinition(consentDefinition)));
    }

    private ConsentStatus processDefinition(ConsentDefinition consentDefinition) {
        SecureStorageInterface.SecureStorageError storageError = getSecureStorageError();
        String consentInfo = appInfra.getSecureStorage().fetchValueForKey(getConsentKey(consentDefinition.getTypes().get(0)), storageError);
        if (consentInfo == null || storageError.getErrorCode() != null) {
            logError(storageError, consentDefinition.getTypes().get(0));
            return ConsentStatus.inactive;
        }

        List<String> consentInfos = split(consentInfo, DEVICESTORE_VALUE_DELIMITER);
        if (isVersionMismatch(consentDefinition, consentInfos)) {
            return ConsentStatus.inactive;
        }

        for (String type : consentDefinition.getTypes()) {
            String status = appInfra.getSecureStorage().fetchValueForKey(getConsentKey(type), storageError);
            if (status == null || status.toUpperCase().startsWith("FALSE")) {
                logError(storageError, type);
                return ConsentStatus.inactive;
            }
        }
        return ConsentStatus.active;
    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
        String consentLanguage = appInfra.getInternationalization().getBCP47UILocale();

        List<Consent> consents = new ArrayList<>(consentDefinitions.size());
        for (ConsentDefinition definition : consentDefinitions) {
            ConsentStatus consentStatus = processDefinition(definition);
            consents.add(CatkHelper.createConsentFromDefinition(definition, consentStatus, consentLanguage));
        }
        callback.onGetConsentsSuccess(consents);
    }

    private void logError(SecureStorageInterface.SecureStorageError storageError, String type) {
        if (storageError.getErrorCode() != null) {
            appInfra.getLogging().log(LoggingInterface.LogLevel.ERROR, type, storageError.getErrorCode().toString());
        }
    }

    private List<Consent> getSuccessConsentForStatus(ConsentDefinition consentDefinition, ConsentStatus status) {
        String consentLanguage = appInfra.getInternationalization().getBCP47UILocale();
        return Collections.singletonList(CatkHelper.createConsentFromDefinition(consentDefinition, status, consentLanguage));
    }

    private boolean isVersionMismatch(ConsentDefinition consentDefinition, List<String> definitionValues) {
        return consentDefinition.getVersion() > Integer.valueOf(definitionValues.get(LIST_POS_VERSION));
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        String storedValue = join(extractStringsFromDefinition(definition, status), DEVICESTORE_VALUE_DELIMITER);
        SecureStorageInterface.SecureStorageError storageError = getSecureStorageError();
        for (String type : definition.getTypes()) {
            boolean storeStatus = appInfra.getSecureStorage().storeValueForKey(getConsentKey(type), storedValue, storageError);
            if (!storeStatus) {
                logError(storageError, type);
                callback.onPostConsentFailed(definition, new ConsentError(DEVICESTORE_ERROR_UPDATE + storageError.getErrorCode().toString(), -1));
                return;
            }
        }

        String consentLanguage = appInfra.getInternationalization().getBCP47UILocale();
        callback.onPostConsentSuccess(CatkHelper.createConsentFromDefinition(definition, CatkHelper.toStatus(status), consentLanguage));
    }

    @NonNull
    private List<String> extractStringsFromDefinition(ConsentDefinition definition, boolean status) {
        List<String> definitionString = new ArrayList<>();
        definitionString.add(LIST_POS_STATUS, String.valueOf(status));
        definitionString.add(LIST_POS_VERSION, String.valueOf(definition.getVersion()));
        definitionString.add(LIST_POS_TIMESTAMP, String.valueOf(getUTCTime()));
        return definitionString;
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
    private String getConsentKey(String type) {
        return DEVICESTORE_TLA + "_" + type;
    }

}
