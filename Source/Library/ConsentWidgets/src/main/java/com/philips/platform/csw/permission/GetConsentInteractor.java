package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.csw.utils.CswLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


class GetConsentInteractor {

    interface Callback {
        void onConsentRetrieved(@NonNull final List<ConsentView> consent);

        void onConsentFailed(int error);
    }

    @NonNull
    private final ConsentAccessToolKit instance;
    @NonNull
    private final List<ConsentView> consentDefinitionList;

    GetConsentInteractor(@NonNull final ConsentAccessToolKit instance, @NonNull final List<ConsentView> consentDefinitionList) {
        this.instance = instance;
        this.consentDefinitionList = consentDefinitionList;
    }

    void getConsents(@NonNull final Callback callback) {
        instance.getConsentDetails(new ConsentViewResponseListener(consentDefinitionList, callback));
    }

    class ConsentViewResponseListener implements ConsentResponseListener {

        private List<ConsentView> consentViews;
        private Callback listener;

        ConsentViewResponseListener(@NonNull final List<ConsentView> consentViews, @NonNull final Callback listener) {
            this.consentViews = consentViews;
            this.listener = listener;
        }

        @Override
        public void onResponseSuccessConsent(List<Consent> responseData) {
            if (responseData != null && !responseData.isEmpty()) {
                filterConsentsByDefinitions(responseData);
                listener.onConsentRetrieved(consentViews);
            } else {
                this.listener.onConsentFailed(-1);
                CswLogger.d(" Consent : ", "no consent for type found on server");
            }
        }

        @Override
        public int onResponseFailureConsent(int consentError) {
            CswLogger.d(" Consent : ", "response failure:" + consentError);
            this.listener.onConsentFailed(consentError);
            return 0;
        }

        private void filterConsentsByDefinitions(List<Consent> receivedConsents) {
            Map<String, Consent> consentsMap = toMap(receivedConsents);
            for (ConsentView consentView: consentViews) {
                Consent consent = consentsMap.get(consentView.getType());
                if (consent != null) {
                    consentView.storeConsent(consent);
                } else {
                    consentView.setNotFound();
                }
            }
        }

        private Map<String,Consent> toMap(List<Consent> responseData) {
            Map<String, Consent> map = new HashMap<>();
            for (Consent consent: responseData) {
                map.put(consent.getType(), consent);
            }
            return map;
        }
    }
}
