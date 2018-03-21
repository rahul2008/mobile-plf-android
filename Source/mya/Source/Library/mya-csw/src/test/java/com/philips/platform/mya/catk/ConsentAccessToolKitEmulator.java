package com.philips.platform.mya.catk;


import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.FetchConsentTypeStateCallback;
import com.philips.platform.pif.chi.PostConsentTypeCallback;

public class ConsentAccessToolKitEmulator implements ConsentHandlerInterface {

    @Override
    public void fetchConsentTypeState(String consentType, FetchConsentTypeStateCallback callback) {

    }

    @Override
    public void storeConsentTypeState(String consentType, boolean status, int version, PostConsentTypeCallback callback) {

    }
}
