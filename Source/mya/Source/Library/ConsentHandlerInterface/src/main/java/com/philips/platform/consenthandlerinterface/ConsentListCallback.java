package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.Consent;

import java.util.List;

/**
 * Created by Entreco on 15/12/2017.
 */
public interface ConsentListCallback {
    void onGetConsentRetrieved(final List<Consent> consents);

    void onGetConsentFailed(ConsentError error);
}
