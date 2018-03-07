package com.philips.platform.pif.chi;

import com.philips.platform.pif.chi.datamodel.ConsentState;

public interface FetchConsentTypeStateCallback {
    void onGetConsentsSuccess(ConsentState consentState);

    void onGetConsentsFailed(ConsentError error);
}
