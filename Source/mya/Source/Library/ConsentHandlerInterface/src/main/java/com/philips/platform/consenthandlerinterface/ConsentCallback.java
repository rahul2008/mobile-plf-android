package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.Consent;

public interface ConsentCallback {
    void onGetConsentRetrieved(final Consent consent);

    void onGetConsentFailed(ConsentError error);
}
