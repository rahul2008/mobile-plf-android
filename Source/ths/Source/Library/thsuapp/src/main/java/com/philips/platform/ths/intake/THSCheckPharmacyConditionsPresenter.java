package com.philips.platform.ths.intake;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.appinfra.consentmanager.ConsentManagerInterface;
import com.philips.platform.appinfra.consentmanager.FetchConsentCallback;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment;
import com.philips.platform.mya.csw.justintime.JustInTimeTextResources;
import com.philips.platform.mya.csw.justintime.JustInTimeWidgetHandler;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentDefinitionStatus;
import com.philips.platform.pif.chi.datamodel.ConsentStates;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.consent.THSJustInTimeConsentFragment;
import com.philips.platform.ths.consent.THSLocationConsentProvider;
import com.philips.platform.ths.pharmacy.THSConsumerShippingAddressCallback;
import com.philips.platform.ths.pharmacy.THSPreferredPharmacyCallback;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;

import static com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies.consentDefinition;

class THSCheckPharmacyConditionsPresenter implements THSBasePresenter, THSPreferredPharmacyCallback, THSConsumerShippingAddressCallback {

    FragmentTransaction fragmentTransaction;
    private THSCheckPharmacyConditonsView thsCheckPharmacyConditonsView;
    private Pharmacy pharmacy;
    private static final String CONSENT_FRAGMENT_TAG = "consentFragmentTAG";
    private ActionBarListener actionBarListener;

    THSCheckPharmacyConditionsPresenter(THSCheckPharmacyConditonsView thsCheckPharmacyConditonsView, ActionBarListener actionBarListener) {
        this.thsCheckPharmacyConditonsView = thsCheckPharmacyConditonsView;
        this.actionBarListener = actionBarListener;
    }

    @Override
    public void onEvent(int componentID) {

    }

    void fetchConsumerPreferredPharmacy() {
        try {
            THSManager.getInstance().getConsumerPreferredPharmacy(thsCheckPharmacyConditonsView.getFragmentActivity(), this);
        } catch (AWSDKInstantiationException e) {

        }
    }

    void getConsumerShippingAddress() {
        try {
            THSManager.getInstance().getConsumerShippingAddress(thsCheckPharmacyConditonsView.getFragmentActivity(), this);
        } catch (AWSDKInstantiationException e) {

        }
    }

    @Override
    public void onPharmacyReceived(Pharmacy pharmacy, SDKError sdkError) {

        if (null != pharmacy) {
            this.pharmacy = pharmacy;
            getConsumerShippingAddress();
        } else {
            checkForConsent();

        }
    }

    protected void checkForConsent() {
        final ConsentDefinition thsConsentDefinition = THSManager.getInstance().getConsentDefinition() != null ? THSManager.getInstance().getConsentDefinition() : THSLocationConsentProvider.getTHSConsentDefinition(thsCheckPharmacyConditonsView.getFragmentActivity());
        ConsentManagerInterface consentManager = THSManager.getInstance().getAppInfra().getConsentManager();
        consentManager.fetchConsentState(thsConsentDefinition, new FetchConsentCallback() {
            @Override
            public void onGetConsentsSuccess(ConsentDefinitionStatus consentDefinitionStatus) {
                if (consentDefinitionStatus.getConsentState() != ConsentStates.active) {
                    ((THSCheckPharmacyConditionsFragment) thsCheckPharmacyConditonsView).hideProgressBar();
                    JustInTimeTextResources justInTimeTextResources = new JustInTimeTextResources();
                    justInTimeTextResources.acceptTextRes = R.string.ths_location_consent_accept;
                    justInTimeTextResources.rejectTextRes = R.string.ths_location_consent_reject;
                    justInTimeTextResources.titleTextRes = R.string.ths_location_consent_fragment_title;
                    // justInTimeTextResources.userBenefitsTitleRes = R.string.ths_location_consent_title;
                    // justInTimeTextResources.userBenefitsDescriptionRes = R.string.ths_location_consent_help;

                    consentDefinition = thsConsentDefinition;
                    JustInTimeConsentDependencies.textResources = justInTimeTextResources;
                    JustInTimeConsentDependencies.completionListener = justInTimeWidgetHandler();
                    JustInTimeConsentFragment justInTimeConsentFragment = THSJustInTimeConsentFragment.newInstance(thsCheckPharmacyConditonsView.getContainerID());
                    justInTimeConsentFragment.setUpdateTitleListener(actionBarListener);
                    fragmentTransaction = thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.add(thsCheckPharmacyConditonsView.getContainerID(), justInTimeConsentFragment, "consentTAG");
                    //fragmentTransaction.addToBackStack(CONSENT_FRAGMENT_TAG);
                    fragmentTransaction.commitAllowingStateLoss();
                } else {
                    thsCheckPharmacyConditonsView.displayPharmacy();
                }
            }

            @Override
            public void onGetConsentsFailed(ConsentError error) {
                thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().popBackStack(CONSENT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                thsCheckPharmacyConditonsView.showPharmacySearch();
            }
        });
    }

    private JustInTimeWidgetHandler justInTimeWidgetHandler() {
        return new JustInTimeWidgetHandler() {
            @Override
            public void onConsentGiven() {
                thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().popBackStack(CONSENT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                thsCheckPharmacyConditonsView.displayPharmacy();
            }

            @Override
            public void onConsentRejected() {
                AmwellLog.v("FAIL", "onConsentRejected");
                thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().popBackStack(CONSENT_FRAGMENT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                thsCheckPharmacyConditonsView.showPharmacySearch();
            }
        };
    }

    @Override
    public void onSuccessfulFetch(Address address, SDKError sdkError) {

        thsCheckPharmacyConditonsView.displayPharmacyAndShippingPreferenceFragment(pharmacy, address);

    }

    @Override
    public void onFailure(Throwable throwable) {
        if (null != thsCheckPharmacyConditonsView && null != thsCheckPharmacyConditonsView.getFragmentActivity()) {
            thsCheckPharmacyConditonsView.showError(thsCheckPharmacyConditonsView.getFragmentActivity().getResources().getString(R.string.ths_se_server_error_toast_message));
        }
    }
}
