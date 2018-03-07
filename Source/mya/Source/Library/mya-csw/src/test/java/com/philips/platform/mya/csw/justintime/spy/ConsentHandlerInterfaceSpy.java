package com.philips.platform.mya.csw.justintime.spy;

import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.List;

public class ConsentHandlerInterfaceSpy implements ConsentHandlerInterface {
    public ConsentDefinition definition_storeConsentState;
    public boolean status_storeConsentState;
    public PostConsentCallback callback_storeConsentState;
    private Consent consent;
    private ConsentDefinition definition;
    private ConsentError error;

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
        if (shouldSucceed()) {
            this.callback_storeConsentState.onPostConsentSuccess(consent);
        } else if (shouldFail()) {
            this.callback_storeConsentState.onPostConsentFailed(definition, error);
        }
    }

    public void callsCallback_onPostConsentFailed(ConsentDefinition definition, ConsentError error) {
        this.definition = definition;
        this.error = error;
    }

    public void callsCallback_onPostConsentSuccess(Consent consent) {
        this.consent = consent;
    }

    private boolean shouldSucceed() {
        return consent != null;
    }

    private boolean shouldFail() {
        return definition != null && error != null;
    }
}
