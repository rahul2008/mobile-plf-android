package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.Consent;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

public interface PostConsentCallback {
    void onPostConsentFailed(ConsentDefinition definition, ConsentError error);

    void onPostConsentSuccess(Consent consent);
}
