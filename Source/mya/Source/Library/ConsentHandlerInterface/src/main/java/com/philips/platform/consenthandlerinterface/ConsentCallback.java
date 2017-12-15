package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.Consent;

/**
 * Created by Entreco on 15/12/2017.
 */
public interface ConsentCallback {
    void onGetConsentRetrieved(final Consent consent);

    void onGetConsentFailed(ConsentError error);
}
