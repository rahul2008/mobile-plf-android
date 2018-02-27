package com.philips.platform.mya.csw.justintime.spy;

import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.PostConsentCallback;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.List;

public class ConsentHandlerInterfaceSpy implements ConsentHandlerInterface {
    public ConsentDefinition definition_storeConsentState;
    public boolean status_storeConsentState;
    public PostConsentCallback callback_storeConsentState;

    @Override
    public void fetchConsentState(ConsentDefinition consentDefinition, CheckConsentsCallback callback) {

    }

    @Override
    public void fetchConsentStates(List<ConsentDefinition> consentDefinitions, CheckConsentsCallback callback) {

    }

    @Override
    public void storeConsentState(ConsentDefinition definition, boolean status, PostConsentCallback callback) {
        this.definition_storeConsentState = definition;
        this.status_storeConsentState = status;
        this.callback_storeConsentState = callback;
    }
}
