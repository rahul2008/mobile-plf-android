package com.philips.cdp.digitalcare.homefragment;


import android.support.annotation.NonNull;

import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.ConsentHandlerInterface;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;

import java.util.List;

class SupportHomePresenter implements HomeFragmentContract.HomeFragmentPresenter {

    private HomeFragmentContract.View viewContract;

    public SupportHomePresenter(HomeFragmentContract.View viewContract) {
        this.viewContract = viewContract;
    }

    @Override
    public void checkConsent(ConsentHandlerInterface handler, ConsentDefinition consentDefinition) {
        handler.fetchConsentState(consentDefinition, new CheckConsentsCallback() {
            @Override
            public void onGetConsentsSuccess(List<Consent> consents) {
                if (consents != null) {
                    for (Consent consent : consents) {
                        if (DigitalCareConstants.CC_CONSENT_TYPE_LOCATION.equals(consent.getType()) && consent.isAccepted()) {
                            viewContract.isConsentAccepted(true);
                            return;
                        }
                    }
                }
                viewContract.isConsentAccepted(false);
            }

            @Override
            public void onGetConsentsFailed(ConsentError error) {
                viewContract.isConsentAccepted(false);
            }
        });
    }
}