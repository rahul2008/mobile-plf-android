package com.philips.platform.mya.catk;


import com.philips.platform.consenthandlerinterface.ConsentHandlerInterface;
import com.philips.platform.consenthandlerinterface.CheckConsentsCallback;
import com.philips.platform.consenthandlerinterface.PostConsentCallback;
import com.philips.platform.consenthandlerinterface.datamodel.ConsentDefinition;

public class ConsentAccessToolKitEmulator implements ConsentHandlerInterface {

    @Override
    public void checkConsents(CheckConsentsCallback callback) {

    }

    @Override
    public void post(ConsentDefinition definition, boolean status, PostConsentCallback callback) {

    }
}
