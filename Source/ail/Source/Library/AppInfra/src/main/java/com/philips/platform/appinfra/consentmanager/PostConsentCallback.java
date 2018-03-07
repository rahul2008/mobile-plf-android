package com.philips.platform.appinfra.consentmanager;

import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentState;

public interface PostConsentCallback {
    void onPostConsentFailed(ConsentError error);

    void onPostConsentSuccess(ConsentState consentState);
}
