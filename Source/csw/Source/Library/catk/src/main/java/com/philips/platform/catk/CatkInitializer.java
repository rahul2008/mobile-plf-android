package com.philips.platform.catk;


import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.catk.utils.CatkLogger;
import com.philips.platform.pif.chi.ConsentHandlerInterface;

import java.util.Arrays;

public class CatkInitializer implements CatkInterface {

    private ConsentManagerInterface consentManagerInterface;

    @Override
    public void initCatk(CatkInputs catkInputs) {
        this.consentManagerInterface = catkInputs.getAppInfra().getConsentManager();
        ConsentsClient.getInstance().init(catkInputs);
        registerBackendPlatformConsent();
    }

    public static ConsentHandlerInterface fetchCatkConsentHandler() {
        return new ConsentInteractor(ConsentsClient.getInstance());
    }

    private void registerBackendPlatformConsent() {
        try {
            consentManagerInterface.registerHandler(Arrays.asList(CatkConsentTypes.TYPE_MOMENT,
                    CatkConsentTypes.TYPE_COACHING, CatkConsentTypes.TYPE_BINARY,
                    CatkConsentTypes.TYPE_RESEARCH, CatkConsentTypes.TYPE_ANALYTICS, CatkConsentTypes.TYPE_DEVICE_TAGGING_CLICKSTREAM),
                    new ConsentInteractor(ConsentsClient.getInstance()));
        } catch (RuntimeException exception) {
            CatkLogger.d("RuntimeException", exception.getMessage());
        }
    }
}
