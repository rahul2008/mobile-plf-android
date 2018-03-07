package com.philips.platform.pif.chi;

public interface PostConsentTypeCallback {
    void onPostConsentFailed(ConsentError error);

    void onPostConsentSuccess(ConsentTypeState consentTypeState);
}
