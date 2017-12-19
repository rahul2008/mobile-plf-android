package com.philips.platform.catk;


import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.consenthandlerinterface.ConsentListCallback;
import com.philips.platform.consenthandlerinterface.CreateConsentCallback;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

public class ConsentAccessToolKitEmulator implements ConsentHandlerInterface {

    @Override
    public void checkConsents(ConsentListCallback callback) {

    }

    @Override
    public void post(ConsentDefinition definition, boolean status, CreateConsentCallback callback) {

    }
}
