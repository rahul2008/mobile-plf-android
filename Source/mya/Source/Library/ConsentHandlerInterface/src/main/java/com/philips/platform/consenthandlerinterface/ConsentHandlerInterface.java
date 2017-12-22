package com.philips.platform.consenthandlerinterface;

import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

public interface ConsentHandlerInterface {

    void checkConsents(final CheckConsentsCallback callback);

    void post(final ConsentDefinition definition, boolean status, PostConsentCallback callback);
}
