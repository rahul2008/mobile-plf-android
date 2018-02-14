package com.philips.platform.mya.catk;


import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.List;

public class ConsentAccessToolKitEmulator implements ConsentHandlerInterface {

    @Override
    public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {

    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {

    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {

    }
}
