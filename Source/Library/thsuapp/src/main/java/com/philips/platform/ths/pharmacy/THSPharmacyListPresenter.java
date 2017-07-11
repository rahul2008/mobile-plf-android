package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;
import java.util.Map;


public class THSPharmacyListPresenter implements THSGetPharmaciesCallback {

    private THSPharmacyListViewListener thsPharmacyListViewListener;

    public THSPharmacyListPresenter(THSPharmacyListViewListener thsPharmacyListViewListener){
        this.thsPharmacyListViewListener = thsPharmacyListViewListener;
    }

    public void fetchPharmacyList(THSConsumer thsConsumer, String city, State state, String zipCode){
        try {
            THSManager.getInstance().getPharmacies(thsPharmacyListViewListener.getFragmentActivity(),thsConsumer,city,state,zipCode,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> map) {

    }

    @Override
    public void onPharmacyListReceived(List<Pharmacy> pharmacies, SDKError sdkError) {
        thsPharmacyListViewListener.updatePharmacyListView(pharmacies);
    }

    @Override
    public void onFailure(Throwable throwable) {

    }
}
