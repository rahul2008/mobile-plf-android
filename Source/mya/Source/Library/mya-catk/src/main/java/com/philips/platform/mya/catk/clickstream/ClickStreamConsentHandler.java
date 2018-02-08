package com.philips.platform.mya.catk.clickstream;


import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.mya.catk.utils.CatkHelper;
import com.philips.platform.mya.chi.CheckConsentsCallback;
import com.philips.platform.mya.chi.ConsentError;
import com.philips.platform.mya.chi.ConsentHandlerInterface;
import com.philips.platform.mya.chi.PostConsentCallback;
import com.philips.platform.mya.chi.datamodel.Consent;
import com.philips.platform.mya.chi.datamodel.ConsentDefinition;
import com.philips.platform.mya.chi.datamodel.ConsentStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClickStreamConsentHandler implements ConsentHandlerInterface {

    public static final String CLICKSTREAM_CONSENT_TYPE = "AIL_ClickStream";
    public static final String CLICKSTREAM_CONSENT_ERROR = "Could not find ClickStream type in Consent definition";
    private final AppInfra appInfra;

    public ClickStreamConsentHandler(final AppInfra appInfra) {
        this.appInfra = appInfra;
    }

    @Override
    public void fetchConsentState(ConsentDefinition definition, CheckConsentsCallback callback) {
        callback.onGetConsentsSuccess(CatkHelper.getSuccessConsentForStatus(definition, toConsentStatus(processClickStreamConsentStatus(definition))));
    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {
        List<Consent> consents = new ArrayList<>(consentDefinitions.size());
        for (ConsentDefinition definition : consentDefinitions) {
            consents.add(CatkHelper.createConsentFromDefinition(definition, toConsentStatus(processClickStreamConsentStatus(definition))));
        }
        callback.onGetConsentsSuccess(consents);
    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        for (String type : definition.getTypes()) {
            if (Objects.equals(type, CLICKSTREAM_CONSENT_TYPE)) {
                appInfra.getTagging().setPrivacyConsent(toPrivacyStatus(status));
                appInfra.getSecureStorage().storeValueForKey(CLICKSTREAM_CONSENT_TYPE, String.valueOf(definition.getVersion()), getSecureStorageError());
                callback.onPostConsentSuccess(CatkHelper.createConsentFromDefinition(definition, CatkHelper.toStatus(status)));
                return;
            }
        }
        callback.onPostConsentFailed(definition, getClickStreamError());
    }

    private AppTaggingInterface.PrivacyStatus processClickStreamConsentStatus(ConsentDefinition definition) {
        AppTaggingInterface.PrivacyStatus privacyStatus = AppTaggingInterface.PrivacyStatus.UNKNOWN;
        for (String type : definition.getTypes()) {
            if (Objects.equals(type, CLICKSTREAM_CONSENT_TYPE)) {
                privacyStatus = isVersionMismatch(definition) ? AppTaggingInterface.PrivacyStatus.UNKNOWN : appInfra.getTagging().getPrivacyConsent();
                break;
            }
        }
        return privacyStatus;
    }

    private boolean isVersionMismatch(ConsentDefinition definition) {
        return definition.getVersion() > Integer.valueOf(appInfra.getSecureStorage().fetchValueForKey(CLICKSTREAM_CONSENT_TYPE, getSecureStorageError()));
    }

    @NonNull
    private ConsentError getClickStreamError() {
        return new ConsentError(CLICKSTREAM_CONSENT_ERROR, ConsentError.CONSENT_ERROR);
    }

    private AppTaggingInterface.PrivacyStatus toPrivacyStatus(boolean status) {
        return status ? AppTaggingInterface.PrivacyStatus.OPTIN : AppTaggingInterface.PrivacyStatus.OPTOUT;
    }

    private ConsentStatus toConsentStatus(AppTaggingInterface.PrivacyStatus privacyStatus) {
        if (privacyStatus == AppTaggingInterface.PrivacyStatus.OPTIN) {
            return ConsentStatus.active;
        } else if (privacyStatus == AppTaggingInterface.PrivacyStatus.OPTOUT) {
            return ConsentStatus.rejected;
        } else {
            return ConsentStatus.inactive;
        }
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }
}
