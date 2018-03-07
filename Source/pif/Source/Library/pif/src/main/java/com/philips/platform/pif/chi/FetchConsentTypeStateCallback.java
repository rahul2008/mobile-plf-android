package com.philips.platform.pif.chi;

public interface FetchConsentTypeStateCallback {
    void onGetConsentsSuccess(ConsentTypeState consentTypeState);

    void onGetConsentsFailed(ConsentError error);
}
