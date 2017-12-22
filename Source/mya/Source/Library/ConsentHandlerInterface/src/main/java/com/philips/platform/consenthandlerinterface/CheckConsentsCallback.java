package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.Consent;

import java.util.List;

public interface CheckConsentsCallback {
    void onGetConsentsSuccess(final List<Consent> consents);

    void onGetConsentsFailed(ConsentError error);
}
