package com.philips.platform.pif.chi;

import com.philips.platform.pif.chi.datamodel.ConsentStatus;

public interface FetchConsentTypeStateCallback {
    void onGetConsentsSuccess(ConsentStatus consentStatus);

    void onGetConsentsFailed(ConsentError error);
}
