package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionState;

public interface FetchConsentCallback {

    void onGetConsentsSuccess(ConsentDefinitionState consentDefinitionState);

    void onGetConsentsFailed(ConsentError error);
}
