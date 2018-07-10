package com.philips.platform.appinfra.tagging;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static com.philips.platform.appinfra.tagging.AppTagging.CLICKSTREAM_CONSENT_TYPE;
import static junit.framework.Assert.assertEquals;

class ClickStreamConsentHandler implements ConsentHandlerInterface {

    @VisibleForTesting
    static final String CLICKSTREAM_CONSENT_VERSION = CLICKSTREAM_CONSENT_TYPE + "_Version";
    static final String CLICKSTREAM_CONSENT_TIMESTAMP = CLICKSTREAM_CONSENT_TYPE + "_Timestamp";
    AppInfraInterface appInfraInterface;

    ClickStreamConsentHandler(AppInfraInterface appInfraInterface) {
        this.appInfraInterface = appInfraInterface;
    }

    private AppTaggingInterface.PrivacyStatus getPrivacyStatus(boolean status) {
        return status ? AppTaggingInterface.PrivacyStatus.OPTIN : AppTaggingInterface.PrivacyStatus.OPTOUT;
    }

    private ConsentStatus getConsentStatus(AppTaggingInterface.PrivacyStatus privacyStatus, int version, Date lastModifiedTimeStamp) {
        ConsentStates status = ConsentStates.inactive;
        if (privacyStatus.equals(AppTaggingInterface.PrivacyStatus.OPTIN))
            status = ConsentStates.active;
        else if (privacyStatus.equals(AppTaggingInterface.PrivacyStatus.OPTOUT))
            status = ConsentStates.rejected;
        return new ConsentStatus(status, version,lastModifiedTimeStamp);
    }

    @VisibleForTesting
    @NonNull
    SecureStorageInterface.SecureStorageError getSecureStorageError() {
        return new SecureStorageInterface.SecureStorageError();
    }

    @Override
    public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
        assertEquals(consentType, CLICKSTREAM_CONSENT_TYPE);

        String valueForKey = appInfraInterface.getSecureStorage().fetchValueForKey(CLICKSTREAM_CONSENT_VERSION, getSecureStorageError());
        int version = valueForKey == null ? 0 : Integer.valueOf(valueForKey);

        String valueForTimestamp = appInfraInterface.getSecureStorage().fetchValueForKey(CLICKSTREAM_CONSENT_TIMESTAMP, getSecureStorageError());
        AppTaggingInterface.PrivacyStatus privacyStatus = appInfraInterface.getTagging().getPrivacyConsent();

        callback.onGetConsentsSuccess(getConsentStatus(privacyStatus, version, getUTCTimestamp(valueForTimestamp)));
    }

    @Override
    public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
        assertEquals(consentType, CLICKSTREAM_CONSENT_TYPE);

        appInfraInterface.getTagging().setPrivacyConsent(getPrivacyStatus(status));
        appInfraInterface.getSecureStorage().storeValueForKey(CLICKSTREAM_CONSENT_VERSION, String.valueOf(version), getSecureStorageError());
        appInfraInterface.getSecureStorage().storeValueForKey(CLICKSTREAM_CONSENT_TIMESTAMP, String.valueOf(appInfraInterface.getTime().getUTCTime()), getSecureStorageError());
        callback.onPostConsentSuccess();
    }

    private Date getUTCTimestamp(String utcTimeStamp) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z", Locale.ENGLISH);
        dateFormat.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
        try {
            return dateFormat.parse(utcTimeStamp);
        } catch (ParseException e) {
           e.printStackTrace();
        }

        return null;
    }

}