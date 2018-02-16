package com.philips.platform.mya.catk;


import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

public class ConsentAccessToolKitEmulator implements ConsentHandlerInterface {

    @Override
    public void checkConsents(CheckConsentsCallback callback) {

    }

    @Override
    public void post(ConsentDefinition definition, boolean status, PostConsentCallback callback) {

    }
}
