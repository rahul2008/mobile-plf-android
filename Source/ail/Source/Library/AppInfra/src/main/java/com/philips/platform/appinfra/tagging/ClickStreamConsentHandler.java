package com.philips.platform.appinfra.tagging;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.philips.platform.appinfra.tagging.AppTagging.CLICKSTREAM_CONSENT_TYPE;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;

class ClickStreamConsentHandler implements ConsentHandlerInterface {

    @VisibleForTesting
    static final String CLICKSTREAM_CONSENT_VERSION = CLICKSTREAM_CONSENT_TYPE + "_Version";
    AppInfraInterface appInfraInterface;

    ClickStreamConsentHandler(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }

    @Override
    public void fetchConsentState(ConsentDefinition definition, CheckConsentsCallback callback) {
        callback.onGetConsentsSuccess(getSuccessConsentForStatus(definition, getConsentStatus(processClickStreamConsentStatus(definition))));
    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
        String consentLanguage = appInfraInterface.getInternationalization().getBCP47UILocale();
        List<Consent> consents = new ArrayList<>();
        for (ConsentDefinition definition : consentDefinitions) {
            if(getClickStreamType(definition) != null){
                consents.add(createConsentFromDefinition(definition, getConsentStatus(processClickStreamConsentStatus(definition)), consentLanguage));
            }
        }
        assertFalse(consents.isEmpty());
        callback.onGetConsentsSuccess(consents);
    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        assertNotNull(getClickStreamType(definition));
        String consentLanguage = appInfraInterface.getInternationalization().getBCP47UILocale();
        appInfraInterface.getTagging().setPrivacyConsent(getPrivacyStatus(status));
        appInfraInterface.getSecureStorage().storeValueForKey(CLICKSTREAM_CONSENT_VERSION, String.valueOf(definition.getVersion()), getSecureStorageError());
        callback.onPostConsentSuccess(createConsentFromDefinition(definition, toStatus(status), consentLanguage));
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

    private AppTaggingInterface.PrivacyStatus getPrivacyStatus(boolean status) {
        return status ? AppTaggingInterface.PrivacyStatus.OPTIN : AppTaggingInterface.PrivacyStatus.OPTOUT;
    }

    private ConsentStatus getConsentStatus(AppTaggingInterface.PrivacyStatus privacyStatus) {
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

    public static Consent createConsentFromDefinition(ConsentDefinition definition, ConsentStatus consentStatus, String consentLanguage) {
        final BackendConsent backendConsent = new BackendConsent(consentLanguage, consentStatus, definition.getTypes().get(0), definition.getVersion());
        return new Consent(backendConsent, definition);
    }

    private ConsentStatus toStatus(boolean status) {
        return status ? ConsentStatus.active : ConsentStatus.rejected;
    }

    private List<Consent> getSuccessConsentForStatus(ConsentDefinition consentDefinition, ConsentStatus status) {
        String consentLanguage = appInfraInterface.getInternationalization().getBCP47UILocale();
        return Collections.singletonList(createConsentFromDefinition(consentDefinition, status, consentLanguage));
    }
}