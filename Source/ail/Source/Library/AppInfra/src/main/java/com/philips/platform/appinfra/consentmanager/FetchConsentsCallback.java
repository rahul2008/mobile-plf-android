package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionState;

import java.util.List;

public interface FetchConsentsCallback {
    void onGetConsentsSuccess(List<ConsentDefinitionState> consentDefinitionStateList);

    void onGetConsentsFailed(ConsentError error);
}
