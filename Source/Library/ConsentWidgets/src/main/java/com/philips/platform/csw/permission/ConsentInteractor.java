package com.philips.platform.csw.permission;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.philips.platform.catk.ConsentAccessToolKit;
import com.philips.platform.catk.listener.ConsentResponseListener;
import com.philips.platform.catk.model.Consent;
import com.philips.platform.catk.model.ConsentError;
import com.philips.platform.csw.ConsentDefinition;
import com.philips.platform.csw.utils.CswLogger;

import java.util.ArrayList;
import java.util.List;

public class ConsentInteractor implements ConsentResponseListener {

    private static final String CONSENT_NOT_FOUND = "no consent for type foond on server";
    private static final String REQUEST_ERROR = "no consent for type foond on server";

    @NonNull private final ConsentAccessToolKit instance;
    @NonNull private final List<ConsentDefinition> consentDefinitionList;
    @NonNull private final List<Consent> consentList;
    @Nullable private ConsentResponseListener listener;

    public ConsentInteractor(@NonNull final ConsentAccessToolKit instance, @NonNull final List<ConsentDefinition> consentDefinitionList) {
        this.instance = instance;
        this.consentDefinitionList = consentDefinitionList;
        this.consentList = new ArrayList<>();
    }

    void getConsents(@NonNull final ConsentResponseListener listener) {
        this.listener = listener;
        for(ConsentDefinition definition : consentDefinitionList) {
            instance.getStatusForConsentType(definition.getType(), definition.getVersion(), this);
        }
    }

    @Override
    public void onResponseSuccessConsent(List<Consent> responseData) {
        if (responseData != null && !responseData.isEmpty()) {
            this.consentList.add(responseData.get(0));
        } else {
            this.consentList.add(new ConsentError(CONSENT_NOT_FOUND));
            CswLogger.d(" Consent : ", CONSENT_NOT_FOUND);
        }

        reportResultIfAllConsentsWhereFetched();
    }

    @Override
    public int onResponseFailureConsent(int consentError) {
        this.consentList.add(new ConsentError(REQUEST_ERROR));
        CswLogger.d(" Consent : ", "response failure:" + consentError);
        reportResultIfAllConsentsWhereFetched();
        return 0;
    }

    private void reportResultIfAllConsentsWhereFetched() {
        if(listener != null && allConsentsFetched()){
            listener.onResponseSuccessConsent(consentList);
        }
    }

    private boolean allConsentsFetched() {
        return consentDefinitionList.size() == consentList.size();
    }
}
