package com.philips.platform.ths.visit;

import android.view.View;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.entity.provider.ProviderImageSize;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSDKCallback;
import com.philips.platform.ths.practice.THSPracticeFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;

/**
 * Created by philips on 8/4/17.
 */

public class THSVisitSummaryPresenter implements THSBasePresenter, THSVisitSummaryCallbacks.THSVisitSummaryCallback<THSVisitSummary, THSSDKError>, THSSDKCallback<Void, SDKError> {

    THSVisitSummaryFragment mTHSVisitSummaryFragment;

    public THSVisitSummaryPresenter(THSVisitSummaryFragment mTHSVisitSummaryFragment) {
        this.mTHSVisitSummaryFragment = mTHSVisitSummaryFragment;
    }

    @Override
    public void onEvent(int componentID) {
        // todo clear fragments till Practice (Show practice)
        mTHSVisitSummaryFragment.getFragmentManager().popBackStack(THSPracticeFragment.TAG,0);

    }

    void fetchVisitSummary() {
        try {
            THSManager.getInstance().getVisitSummary(mTHSVisitSummaryFragment.getFragmentActivity(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

    }

    void sendRatings(Float providerRating, Float visitRating) {

       try {
           int providerRatingInt = Math.round(providerRating);
           int visitRatingInt = Math.round(visitRating);
           THSManager.getInstance().sendRatings(mTHSVisitSummaryFragment.getFragmentActivity(), providerRatingInt, visitRatingInt, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


    public void updateShippingAddressView(Address address, String consumerName) {
        //ths_shipping_pharmacy_layout.setVisibility(View.VISIBLE);
        mTHSVisitSummaryFragment.consumerName.setText(consumerName);
        mTHSVisitSummaryFragment.consumerCity.setText(address.getCity());
        mTHSVisitSummaryFragment.consumerState.setText(address.getState().getCode());
        mTHSVisitSummaryFragment.consumerShippingAddress.setText(address.getAddress1());
        mTHSVisitSummaryFragment.consumerShippingZip.setText(address.getZipCode());
    }

    public void updatePharmacyDetailsView(Pharmacy pharmacy) {
        mTHSVisitSummaryFragment.pharmacyAddressLineOne.setText(pharmacy.getAddress().getAddress1());
        mTHSVisitSummaryFragment.pharmacyAddressLIneTwo.setText(pharmacy.getAddress().getAddress2());
        mTHSVisitSummaryFragment.pharmacyName.setText(pharmacy.getName());
        mTHSVisitSummaryFragment.pharmacyState.setText(pharmacy.getAddress().getState().getCode());
        mTHSVisitSummaryFragment.pharmacyZip.setText(pharmacy.getAddress().getZipCode());
    }

    //start of visit summary callbacks
    @Override
    public void onResponse(THSVisitSummary tHSVisitSummary, THSSDKError tHSSDKError) {

        updateView(tHSVisitSummary);

    }

    private void updateView(THSVisitSummary tHSVisitSummary){
        ProviderInfo providerInfo= tHSVisitSummary.getVisitSummary().getAssignedProviderInfo();
        mTHSVisitSummaryFragment.providerName.setText(providerInfo.getFullName());
        mTHSVisitSummaryFragment.providerPractice.setText(providerInfo.getPracticeInfo().getName());
        if(tHSVisitSummary.getVisitSummary().getVisitCost().isFree()){
            mTHSVisitSummaryFragment.visitCost.setText("$ 0");
        }else{
            //mTHSVisitSummaryFragment.visitCost.setText(tHSVisitSummary.getVisitSummary().getVisitCost().getDeferredBillingWrapUpText());
            double cost= tHSVisitSummary.getVisitSummary().getVisitCost().getExpectedConsumerCopayCost();

            mTHSVisitSummaryFragment.visitCost.setText("$"+Double.toString(cost));
        }
        if (providerInfo.hasImage()) {
            try {
                THSManager.getInstance().getAwsdk(mTHSVisitSummaryFragment.getFragmentActivity()).getPracticeProvidersManager().
                        newImageLoader(providerInfo, mTHSVisitSummaryFragment.mImageProviderImage,
                                ProviderImageSize.SMALL).placeholder(mTHSVisitSummaryFragment.mImageProviderImage.getResources().
                        getDrawable(R.drawable.doctor_placeholder, mTHSVisitSummaryFragment.getFragmentActivity().getTheme())).build().load();
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        }


        Address address = tHSVisitSummary.getVisitSummary().getShippingAddress();
        String consumerFullName = tHSVisitSummary.getVisitSummary().getConsumerInfo().getFullName();
        updateShippingAddressView(address, consumerFullName);
        Pharmacy pharmacy = tHSVisitSummary.getVisitSummary().getPharmacy();
        updatePharmacyDetailsView(pharmacy);
    }


    @Override
    public void onResponse(Void var1, SDKError var2) {
        AmwellLog.d("Send rating","success");
    }

    @Override
    public void onFailure(Throwable throwable) {
        AmwellLog.d("Send rating","failure");
    }


}
