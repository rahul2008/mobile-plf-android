package com.philips.platform.appinfra.tagging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

class ClickStreamConsentHandler implements ConsentHandlerInterface {

    public static final String CLICKSTREAM_CONSENT_TYPE = "AIL_ClickStream";
    @VisibleForTesting
    static final String CLICKSTREAM_CONSENT_VERSION = CLICKSTREAM_CONSENT_TYPE + "_Version";
    AppInfraInterface appInfraInterface;

    @Override
    public void fetchConsentState(ConsentDefinition definition, CheckConsentsCallback callback) {
        callback.onGetConsentsSuccess(getSuccessConsentForStatus(definition, toConsentStatus(processClickStreamConsentStatus(definition))));
    }

    ClickStreamConsentHandler(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
        List<Consent> consents = new ArrayList<>();
        for (ConsentDefinition definition : consentDefinitions) {
            if(getClickStreamType(definition) != null){
                consents.add(createConsentFromDefinition(definition, toConsentStatus(processClickStreamConsentStatus(definition))));
            }
        }
        assertFalse(consents.isEmpty());
        callback.onGetConsentsSuccess(consents);
    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        assertNotNull(getClickStreamType(definition));
        appInfraInterface.getTagging().setPrivacyConsent(toPrivacyStatus(status));
        appInfraInterface.getSecureStorage().storeValueForKey(CLICKSTREAM_CONSENT_VERSION, String.valueOf(definition.getVersion()), getSecureStorageError());
        callback.onPostConsentSuccess(createConsentFromDefinition(definition, toStatus(status)));
    }

    private AppTaggingInterface.PrivacyStatus processClickStreamConsentStatus(ConsentDefinition definition) {
        AppTaggingInterface.PrivacyStatus privacyStatus = AppTaggingInterface.PrivacyStatus.UNKNOWN;
        assertNotNull(getClickStreamType(definition));

        if (!isVersionMismatch(definition)) {
            privacyStatus = appInfraInterface.getTagging().getPrivacyConsent();
        }

        if (privacyStatus.equals(AppTaggingInterface.PrivacyStatus.UNKNOWN)) {
            appInfraInterface.getTagging().setPrivacyConsent(privacyStatus);
        }
        return privacyStatus;
    }

    @Nullable
    private String getClickStreamType(ConsentDefinition definition) {
        String clickStreamType = null;
        for (String type : definition.getTypes()) {
            if (type.equals(CLICKSTREAM_CONSENT_TYPE)){
                clickStreamType = type;
            }
        }
        return clickStreamType;
    }

    private boolean isVersionMismatch(ConsentDefinition definition) {
        return definition.getVersion() > Integer.valueOf(appInfraInterface.getSecureStorage().fetchValueForKey(CLICKSTREAM_CONSENT_VERSION, getSecureStorageError()));
    }

    private AppTaggingInterface.PrivacyStatus toPrivacyStatus(boolean status) {
        return status ? AppTaggingInterface.PrivacyStatus.OPTIN : AppTaggingInterface.PrivacyStatus.OPTOUT;
    }

    private ConsentStatus toConsentStatus(AppTaggingInterface.PrivacyStatus privacyStatus) {
        ConsentStatus status = ConsentStatus.inactive;
        if (privacyStatus.equals(AppTaggingInterface.PrivacyStatus.OPTIN))
            status = ConsentStatus.active;
        else if (privacyStatus.equals(AppTaggingInterface.PrivacyStatus.OPTOUT))
            status = ConsentStatus.rejected;
        return status;
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

    private Consent createConsentFromDefinition(ConsentDefinition definition, ConsentStatus consentStatus) {
        final BackendConsent backendConsent = new BackendConsent(new Locale(definition.getLocale()), consentStatus, definition.getTypes().get(0), definition.getVersion());
        return new Consent(backendConsent, definition);
    }

    private ConsentStatus toStatus(boolean status) {
        return status ? ConsentStatus.active : ConsentStatus.rejected;
    }

    private List<Consent> getSuccessConsentForStatus(ConsentDefinition consentDefinition, ConsentStatus status) {
        return Collections.singletonList(createConsentFromDefinition(consentDefinition, status));
    }
}