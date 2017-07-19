package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;

public class THSPharmacyAndShippingPresenter implements THSBasePresenter,THSPreferredPharmacyCallback, THSConsumerShippingAddressCallback {

    private THSPharmacyShippingViewInterface thsBaseView;
    private THSConsumer thsConsumer;
    private Pharmacy pharmacy;

    public THSPharmacyAndShippingPresenter(THSPharmacyShippingViewInterface thsBaseView){
        this.thsBaseView = thsBaseView;
    }

    @Override
    public void onEvent(int componentID) {

    }

    public void fetchConsumerPreferredPharmacy(THSConsumer thsConsumer){
        this.thsConsumer = thsConsumer;
        try {
            THSManager.getInstance().getConsumerPreferredPharmacy(thsBaseView.getFragmentActivity(),thsConsumer,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    public void getConsumerShippingAddress(THSConsumer thsConsumer){
        try {
            THSManager.getInstance().getConsumerShippingAddress(thsBaseView.getFragmentActivity(),thsConsumer,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPharmacyReceived(Pharmacy pharmacy, SDKError sdkError) {
        this.pharmacy = pharmacy;
        getConsumerShippingAddress(thsConsumer);

    }

    @Override
    public void onSuccessfulFetch(Address address, SDKError sdkError) {
        if(null != address) {
            thsBaseView.onSuccessUpdateFragmentView(pharmacy, address);
        }
        else {
            thsBaseView.onFailureCallSearchPharmacy(address);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
    }
}
