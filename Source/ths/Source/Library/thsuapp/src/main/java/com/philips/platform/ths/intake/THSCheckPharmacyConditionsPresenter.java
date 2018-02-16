package com.philips.platform.ths.intake;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.mya.catk.device.DeviceStoredConsentHandler;

import com.philips.platform.mya.csw.justintime.JustInTimeConsentDependencies;
import com.philips.platform.mya.csw.justintime.JustInTimeConsentFragment;
import com.philips.platform.mya.csw.justintime.JustInTimeTextResources;
import com.philips.platform.mya.csw.justintime.JustInTimeWidgetHandler;
import com.philips.platform.pif.chi.CheckConsentsCallback;
import com.philips.platform.pif.chi.ConsentError;
import com.philips.platform.pif.chi.datamodel.Consent;
import com.philips.platform.pif.chi.datamodel.ConsentDefinition;
import com.philips.platform.pif.chi.datamodel.ConsentStatus;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.consent.THSConsentProvider;
import com.philips.platform.ths.pharmacy.THSConsumerShippingAddressCallback;
import com.philips.platform.ths.pharmacy.THSPreferredPharmacyCallback;
import com.philips.platform.ths.pharmacy.THSSearchPharmacyFragment;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.welcome.THSWelcomeFragment;

import java.util.List;
import java.util.Locale;

class THSCheckPharmacyConditionsPresenter implements THSBasePresenter, THSPreferredPharmacyCallback, THSConsumerShippingAddressCallback {

    private THSCheckPharmacyConditonsView thsCheckPharmacyConditonsView;
    private Pharmacy pharmacy;

    THSCheckPharmacyConditionsPresenter(THSCheckPharmacyConditonsView thsCheckPharmacyConditonsView) {
        this.thsCheckPharmacyConditonsView = thsCheckPharmacyConditonsView;
    }

    @Override
    public void onEvent(int componentID) {

    }

    void fetchConsumerPreferredPharmacy() {
        try {
            THSManager.getInstance().getConsumerPreferredPharmacy(thsCheckPharmacyConditonsView.getFragmentActivity(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    void getConsumerShippingAddress() {
        try {
            THSManager.getInstance().getConsumerShippingAddress(thsCheckPharmacyConditonsView.getFragmentActivity(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPharmacyReceived(Pharmacy pharmacy, SDKError sdkError) {
        if (null != pharmacy) {
            this.pharmacy = pharmacy;
            getConsumerShippingAddress();
        } else {
            final DeviceStoredConsentHandler pDeviceStoredConsentHandler = new DeviceStoredConsentHandler(THSManager.getInstance().getAppInfra());
            Locale locale = THSConsentProvider.getCompleteLocale();
            final ConsentDefinition thsConsentDefinition = THSManager.getInstance().getConsentDefinition()!=null?THSManager.getInstance().getConsentDefinition():THSConsentProvider.getTHSConsentDefinition(thsCheckPharmacyConditonsView.getFragmentActivity(), locale);
            pDeviceStoredConsentHandler.fetchConsentState(thsConsentDefinition, new CheckConsentsCallback() {
                @Override
                public void onGetConsentsSuccess(List<Consent> consents) {
                    boolean hasLocationConsent = false;
                    for (Consent consent : consents) {
                        if (consent.getType() == THSConsentProvider.THS_LOCATION) {
                            if (consent.getStatus() != ConsentStatus.active) {
                                hasLocationConsent = false;
                            } else {
                                hasLocationConsent = true;
                            }
                        }
                    }

                    if (!hasLocationConsent) {
                        JustInTimeTextResources justInTimeTextResources = new JustInTimeTextResources();
                        justInTimeTextResources.acceptTextRes = R.string.ths_location_consent_accept;
                        justInTimeTextResources.rejectTextRes = R.string.ths_location_consent_reject;
                        justInTimeTextResources.titleTextRes = R.string.ths_location_consent_title;
                        JustInTimeConsentDependencies.appInfra= THSManager.getInstance().getAppInfra();
                        JustInTimeConsentDependencies.consentDefinition=thsConsentDefinition;
                        JustInTimeConsentDependencies.textResources=justInTimeTextResources;
                        JustInTimeConsentDependencies.completionListener=justInTimeWidgetHandler();
                        JustInTimeConsentDependencies.consentHandlerInterface=pDeviceStoredConsentHandler;


                        JustInTimeConsentFragment  justInTimeConsentFragment= JustInTimeConsentFragment.newInstance(android.R.layout.activity_list_item);
                       // thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().popBackStack();
                        FragmentTransaction fragmentTransaction = thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(thsCheckPharmacyConditonsView.getContainerID(), justInTimeConsentFragment, "consentTAG");
                        fragmentTransaction.addToBackStack("consentTAG");
                        fragmentTransaction.commit();

                        //fragment push
                    } else {
                        thsCheckPharmacyConditonsView.displayPharmacy();
                    }
                }

                @Override
                public void onGetConsentsFailed(ConsentError error) {

                }
            });

        }
    }

    private JustInTimeWidgetHandler justInTimeWidgetHandler(){
       return  new JustInTimeWidgetHandler() {
            @Override
            public void onConsentGiven() {
                Log.v("SUCC", "onConsentGiven");
                thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().popBackStack();
                //thsCheckPharmacyConditonsView.popFragmentByTag("consentTAG", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                // thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().popBackStack();
                thsCheckPharmacyConditonsView.displayPharmacy();
            }

            @Override
            public void onConsentRejected() {
                Log.v("FAIL", "onConsentRejected");
                thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().popBackStack();
                //thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().popBackStack();
                showPharmacySearch();
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

    private void showPharmacySearch() {

        if (null != thsCheckPharmacyConditonsView.getFragmentActivity() ) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    thsCheckPharmacyConditonsView.getFragmentActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            thsCheckPharmacyConditonsView.getFragmentActivity().getSupportFragmentManager().popBackStack();
                            THSSearchPharmacyFragment thsSearchPharmacyFragment = new THSSearchPharmacyFragment();
                            thsCheckPharmacyConditonsView.addFragment(thsSearchPharmacyFragment, THSSearchPharmacyFragment.TAG, null, true);
                        }
                    });
                }
            }).start();

        }
    }

}
