/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.catk;

import android.support.annotation.NonNull;

import com.philips.cdp.registration.UserLoginState;
import com.philips.platform.catk.datamodel.CachedConsentStatus;
import com.philips.platform.catk.datamodel.ConsentDTO;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.listener.CreateConsentListener;
import com.philips.platform.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;

public class ConsentInteractor implements ConsentHandlerInterface {

    @NonNull
    private final ConsentsClient consentsClient;
    @NonNull
    private ConsentCacheInteractor consentCacheInteractor;

    @Inject
    public ConsentInteractor(@NonNull final ConsentsClient consentsClient) {
        this.consentsClient = consentsClient;
        this.consentCacheInteractor = new ConsentCacheInteractor(consentsClient.getAppInfra());
    }

    ConsentInteractor(@NonNull final ConsentsClient consentsClient, @NonNull ConsentCacheInteractor consentCacheInteractor) {
        this(consentsClient);
        this.consentCacheInteractor = consentCacheInteractor;
    }

    @Override
    public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
        if (consentsClient.getCatkComponent().getUser().getUserLoginState() != UserLoginState.USER_LOGGED_IN) {
            callback.onGetConsentsFailed((new ConsentError("User not logged in", ConsentError.CONSENT_ERROR_USER_NOT_LOGGED_IN)));
            return;
        }
        CachedConsentStatus consentStatus = consentCacheInteractor.fetchConsentTypeState(consentType);
        if (consentStatus != null && (consentStatus.getExpires().isAfterNow() || !isInternetAvailable())) {
            callback.onGetConsentsSuccess(new ConsentStatus(consentStatus.getConsentState(), consentStatus.getVersion(), consentStatus.getTimestamp()));
        } else {
            fetchConsentFromBackendAndUpdateCache(consentType, callback);
        }
    }

    @Override
    public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
        if (isInternetAvailable()) {
            ConsentStates consentStates = status ? ConsentStates.active : ConsentStates.rejected;
            ConsentDTO consentDTO = createConsents(consentType, consentStates, version);
            consentsClient.createConsent(consentDTO, new CreateConsentResponseListener(consentDTO, consentCacheInteractor, callback));
        } else {
            callback.onPostConsentFailed(new ConsentError("Please check your internet connection", ConsentError.CONSENT_ERROR_NO_CONNECTION));
        }
    }

    private boolean isInternetAvailable() {
        return consentsClient.getAppInfra().getRestClient().isInternetReachable();
    }

    private ConsentDTO createConsents(String consentType, ConsentStates status, int version) {
        String locale = consentsClient.getAppInfra().getInternationalization().getBCP47UILocale();
        DateTime timestamp = new DateTime(consentsClient.getAppInfra().getTime().getUTCTime(), DateTimeZone.UTC);
        return new ConsentDTO(locale, status, consentType, version, timestamp);
    }

    private void fetchConsentFromBackendAndUpdateCache(String consentType, FetchConsentTypeStateCallback callback) {
        if (isInternetAvailable()) {
            consentsClient.getStatusForConsentType(consentType, new GetConsentForTypeResponseListener(callback));
        } else {
            callback.onGetConsentsFailed(new ConsentError("Please check your internet connection", ConsentError.CONSENT_ERROR_NO_CONNECTION));
        }
    }

    class GetConsentForTypeResponseListener implements ConsentResponseListener {
        private FetchConsentTypeStateCallback callback;

        GetConsentForTypeResponseListener(FetchConsentTypeStateCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponseSuccessConsent(List<ConsentDTO> responseData) {
            if (responseData != null && !responseData.isEmpty()) {
                ConsentDTO consentDTO = responseData.get(0);
                Date timestamp = consentDTO.getTimestamp().toDateTime(DateTimeZone.UTC).toDate();
                consentCacheInteractor.storeConsentState(consentDTO.getType(), consentDTO.getStatus(), consentDTO.getVersion(), timestamp);
                callback.onGetConsentsSuccess(new ConsentStatus(consentDTO.getStatus(), consentDTO.getVersion(), timestamp));
            } else {
                callback.onGetConsentsSuccess(new ConsentStatus(ConsentStates.inactive, 0, new Date(0)));
            }
        }

        @Override
        public void onResponseFailureConsent(ConsentNetworkError error) {
            callback.onGetConsentsFailed(new ConsentError(error.getMessage(), error.getCatkErrorCode()));
        }
    }

    static class CreateConsentResponseListener implements CreateConsentListener {
        static final int VERSION_MISMATCH_ERROR_FROM_BACKEND = 1252;
        private final PostConsentTypeCallback callback;
        private final ConsentCacheInteractor consentCacheInteractor;
        private final ConsentDTO consentDTO;

        CreateConsentResponseListener(ConsentDTO consentDTO, ConsentCacheInteractor consentCacheInteractor, PostConsentTypeCallback callback) {
            this.consentDTO = consentDTO;
            this.consentCacheInteractor = consentCacheInteractor;
            this.callback = callback;
        }

        @Override
        public void onSuccess() {
            CatkLogger.d(" Create ConsentDTO: ", "Success");
            consentCacheInteractor.storeConsentState(consentDTO.getType(), consentDTO.getStatus(), consentDTO.getVersion(), consentDTO.getTimestamp().toDate());
            callback.onPostConsentSuccess();
        }

        @Override
        public void onFailure(ConsentNetworkError error) {
            CatkLogger.d(" Create ConsentDTO: ", "Failed : " + error.getCatkErrorCode());
            if (error.getServerError() != null && error.getServerError().getErrorCode() == VERSION_MISMATCH_ERROR_FROM_BACKEND) {
                consentCacheInteractor.clearCache(consentDTO.getType());
            }
            callback.onPostConsentFailed(new ConsentError(error.getMessage(), error.getCatkErrorCode()));
        }
    }
}
