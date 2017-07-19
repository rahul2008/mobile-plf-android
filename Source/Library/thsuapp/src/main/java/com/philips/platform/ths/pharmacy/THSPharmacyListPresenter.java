package com.philips.platform.ths.pharmacy;

import android.widget.Toast;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;

import java.util.List;
import java.util.Map;


public class THSPharmacyListPresenter implements THSGetPharmaciesCallback, THSUpdatePharmacyCallback, THSBasePresenter {

    private THSPharmacyListViewListener thsPharmacyListViewListener;

    public THSPharmacyListPresenter(THSPharmacyListViewListener thsPharmacyListViewListener) {
        this.thsPharmacyListViewListener = thsPharmacyListViewListener;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.switch_view_layout) {
            thsPharmacyListViewListener.switchView();
        }
        if (componentID == R.id.segment_control_view_one) {
            thsPharmacyListViewListener.showRetailView();
        }
        if (componentID == R.id.segment_control_view_two) {
            thsPharmacyListViewListener.showMailOrderView();

        }
        if (componentID == R.id.choose_pharmacy_button) {
            thsPharmacyListViewListener.setPreferredPharmacy();
        }
    }

    public void fetchPharmacyList(THSConsumer thsConsumer, String city, State state, String zipCode) {
        try {
            THSManager.getInstance().getPharmacies(thsPharmacyListViewListener.getFragmentActivity(), thsConsumer, city, state, zipCode, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    public void fetchPharmacyList(THSConsumer thsConsumer,float latitude,float longitude,int radius){
        try {
            THSManager.getInstance().getPharmacies(thsPharmacyListViewListener.getFragmentActivity(),thsConsumer,latitude,longitude,radius,this);
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

    public void updateConsumerPreferredPharmacy(THSConsumer thsConsumer, Pharmacy pharmacy) {
        try {
            THSManager.getInstance().updateConsumerPreferredPharmacy(thsPharmacyListViewListener.getFragmentActivity(), thsConsumer, pharmacy, this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateSuccess(SDKError sdkError) {
        thsPharmacyListViewListener.validateForMailOrder();
        Toast.makeText(thsPharmacyListViewListener.getFragmentActivity(), "Update success", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateFailure(Throwable throwable) {

    }
}
