package com.philips.cdp.digitalcare.homefragment;

import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

interface HomeFragmentContract {

    interface View {
        void onConsentProvided(boolean consentProvided);
        void askConsents();
    }

    interface HomeFragmentPresenter {
        void checkConsent(ConsentHandlerInterface handler, ConsentDefinition consentDefinition);
    }
}
