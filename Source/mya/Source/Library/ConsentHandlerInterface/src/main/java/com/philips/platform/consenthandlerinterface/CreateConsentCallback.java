package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.Consent;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

/**
 * Created by Entreco on 15/12/2017.
 */
public interface CreateConsentCallback {
    void onCreateConsentFailed(ConsentDefinition definition, ConsentError error);

    void onCreateConsentSuccess(Consent consent);
}
