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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        instance.getConsentDetails(this);
    }

    @Override
    public void onResponseSuccessConsent(List<Consent> responseData) {
        if (responseData != null && !responseData.isEmpty()) {
            filterConsentsByDefinitions(responseData);
        } else {
            this.consentList.add(new ConsentError(CONSENT_NOT_FOUND));
            CswLogger.d(" Consent : ", CONSENT_NOT_FOUND);
        }
        if(listener != null){
            listener.onResponseSuccessConsent(consentList);
        }
    }

    private void filterConsentsByDefinitions(List<Consent> receivedConsents) {
        Map<String, Consent> consentsMap = toMap(receivedConsents);
        for (ConsentDefinition consentDefinition: consentDefinitionList) {
            Consent consent = consentsMap.get(consentDefinition.getType());
            if (consent != null) {
                this.consentList.add(consentsMap.get(consentDefinition.getType()));
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

    @Override
    public int onResponseFailureConsent(int consentError) {
        this.consentList.add(new ConsentError(REQUEST_ERROR));
        CswLogger.d(" Consent : ", "response failure:" + consentError);
        if(listener != null){
            listener.onResponseSuccessConsent(consentList);
        }
        return 0;
    }
}
