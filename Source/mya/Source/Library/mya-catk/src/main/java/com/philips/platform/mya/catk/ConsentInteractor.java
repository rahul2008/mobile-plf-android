/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.mya.catk;

import android.support.annotation.NonNull;

import com.philips.platform.mya.catk.error.ConsentNetworkError;
import com.philips.platform.mya.catk.listener.ConsentResponseListener;
import com.philips.platform.mya.catk.listener.CreateConsentListener;
import com.philips.platform.mya.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;
import com.philips.platform.pif.chi.datamodel.BackendConsent;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

import java.util.List;

import javax.inject.Inject;

public class ConsentInteractor implements ConsentHandlerInterface {

    @NonNull
    private final ConsentsClient consentsClient;

    @Inject
    public ConsentInteractor(@NonNull final ConsentsClient consentsClient) {
        this.consentsClient = consentsClient;
    }

    @Override
    public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {
        consentsClient.getStatusForConsentType(consentType, new GetConsentForTypeResponseListener(callback));
    }

   /* @Override
    public void fetchConsentTypeStates(List<String> consentTypes, FetchConsentTypesStateCallback callback) {
        consentsClient.getConsentDetails(new GetConsentsResponseListener(consentTypes, callback));
    }*/

    @Override
    public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {
        ConsentStates consentStates = status ? ConsentStates.active : ConsentStates.rejected;
        BackendConsent backendConsent = createConsents(consentType, consentStates, version);
        consentsClient.createConsent(backendConsent, new CreateConsentResponseListener(backendConsent, callback));
    }

    private BackendConsent createConsents(String consentType, ConsentStates status, int version) {
        String locale = consentsClient.getAppInfra().getInternationalization().getBCP47UILocale();
        return new BackendConsent(locale, status, consentType, version);
    }

    class GetConsentForTypeResponseListener implements ConsentResponseListener {
        private FetchConsentTypeStateCallback callback;

        GetConsentForTypeResponseListener(FetchConsentTypeStateCallback callback) {
            this.callback = callback;
        }

        @Override
        public void onResponseSuccessConsent(List<BackendConsent> responseData) {
            if (responseData != null && !responseData.isEmpty()) {
                BackendConsent backendConsent = responseData.get(0);
                callback.onGetConsentsSuccess(new ConsentStatus(backendConsent.getStatus(), backendConsent.getVersion()));
            } else {
                callback.onGetConsentsSuccess(null);
            }

        }

        @Override
        public void onResponseFailureConsent(ConsentNetworkError error) {
            callback.onGetConsentsFailed(new ConsentError(error.getMessage(), error.getCatkErrorCode()));
        }
    }

  /*  class GetConsentsResponseListener implements ConsentResponseListener {
        private List<String> consentTypes;
        private FetchConsentTypesStateCallback callback;

        GetConsentsResponseListener(List<String> consentTypes, FetchConsentTypesStateCallback callback) {
            this.consentTypes = consentTypes;
            this.callback = callback;
        }

        @Override
        public void onResponseSuccessConsent(List<BackendConsent> responseData) {
            //TODO strict consent check needs to be verified ?
            List<ConsentStatus> consentStates = new ArrayList<>();
            if (responseData != null && !responseData.isEmpty()) {
                for (BackendConsent backendConsent : responseData) {
                    if (consentTypes.contains(backendConsent.getType())) {
                        consentStates.add(new ConsentStatus(backendConsent.getStatus(), backendConsent.getVersion()));
                    }
                }
                callback.onGetConsentsSuccess(consentStates);
            } else {
                CatkLogger.d(" BackendConsent : ", "no consent for type found on server");
                callback.onGetConsentsSuccess(new ArrayList<ConsentStatus>());
            }
        }

        @Override
        public void onResponseFailureConsent(ConsentNetworkError error) {
            CatkLogger.d(" BackendConsent : ", "response failure:" + error);
            this.callback.onGetConsentsFailed(new ConsentError(error.getMessage(), error.getCatkErrorCode()));
        }
    }*/

    static class CreateConsentResponseListener implements CreateConsentListener {

        private final BackendConsent backendConsent;
        private final PostConsentTypeCallback callback;

        public CreateConsentResponseListener(BackendConsent backendConsent, PostConsentTypeCallback postConsentCallback) {
            this.backendConsent = backendConsent;
            this.callback = postConsentCallback;
        }

        @Override
        public void onSuccess() {
            CatkLogger.d(" Create BackendConsent: ", "Success");
            callback.onPostConsentSuccess();
        }

        @Override
        public void onFailure(ConsentNetworkError error) {
            CatkLogger.d(" Create BackendConsent: ", "Failed : " + error.getCatkErrorCode());
            callback.onPostConsentFailed(new ConsentError(error.getMessage(), error.getCatkErrorCode()));
        }
    }
}
