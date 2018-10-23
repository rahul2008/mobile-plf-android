package com.philips.platform.ths.intake;

import android.support.v4.app.FragmentTransaction;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.pharmacy.THSConsumerShippingAddressCallback;
import com.philips.platform.ths.pharmacy.THSPreferredPharmacyCallback;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;


class THSCheckPharmacyConditionsPresenter implements THSBasePresenter, THSPreferredPharmacyCallback, THSConsumerShippingAddressCallback {

    FragmentTransaction fragmentTransaction;
    private THSCheckPharmacyConditonsView thsCheckPharmacyConditonsView;
    private Pharmacy pharmacy;
    //private static final String CONSENT_FRAGMENT_TAG = "consentFragmentTAG";
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
            thsCheckPharmacyConditonsView.displayPharmacy();
        }
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
