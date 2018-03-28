package com.philips.cdp.digitalcare.homefragment;

import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

interface HomeFragmentContract {

    interface View {
        void onConsentProvided(boolean consentProvided);
        void isConsentAccepted(boolean consentAccepted);
    }

    interface HomeFragmentPresenter {
        void checkConsent(ConsentManagerInterface consentManagerInterface, ConsentDefinition consentDefinition);
    }
}
