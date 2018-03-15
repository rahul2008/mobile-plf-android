package com.philips.cdp.digitalcare.homefragment;


import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;

class SupportHomePresenter implements HomeFragmentContract.HomeFragmentPresenter {

    private HomeFragmentContract.View viewContract;

    public SupportHomePresenter(HomeFragmentContract.View viewContract) {
        this.viewContract = viewContract;
    }

    @Override
    public void checkConsent(ConsentManagerInterface consentManager, ConsentDefinition consentDefinition) {
        consentManager.fetchConsentState(consentDefinition, new FetchConsentCallback() {
            @Override
            public void onGetConsentsSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
                viewContract.isConsentAccepted(consentDefinitionStatus.getConsentState() == ConsentStates.active);
            }

            @Override
            public void onGetConsentsFailed(ConsentError error) {
                viewContract.isConsentAccepted(false);
            }
        });
    }
}