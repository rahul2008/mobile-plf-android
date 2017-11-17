package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.csw.utils.CswLogger;

import java.util.List;


class GetConsentInteractor {

    interface Callback {
        void onConsentRetrieved(@NonNull final ConsentView consent);

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

    void getConsents(@NonNull final Callback callback, PermissionInterface permissionInterface) {
        for (ConsentView consentView : consentDefinitionList) {
            instance.getStatusForConsentType(consentView.getType(), consentView.getVersion(), new ConsentViewResponseListener(consentView, callback));
        }
        permissionInterface.hideProgressDialog();
    }

    class ConsentViewResponseListener implements ConsentResponseListener {

        private ConsentView consentView;
        private Callback listener;

        ConsentViewResponseListener(ConsentView consentView, Callback listener) {
            this.consentView = consentView;
            this.listener = listener;
        }

        @Override
        public void onResponseSuccessConsent(List<Consent> responseData) {
            if (responseData != null && !responseData.isEmpty()) {
                this.listener.onConsentRetrieved(consentView.storeConsent(responseData.get(0)));
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
    }
}
