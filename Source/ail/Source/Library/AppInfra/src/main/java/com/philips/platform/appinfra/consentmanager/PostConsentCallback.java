package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.pif.chi.ConsentError;

public interface PostConsentCallback {
    void onPostConsentFailed(ConsentError error);

    void onPostConsentSuccess();
}
