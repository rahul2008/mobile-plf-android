package com.philips.platform.ths.pharmacy;

import android.widget.Toast;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.base.THSBaseView;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;

import java.util.Map;

public class THSShippingAddressPresenter implements THSUpdateShippingAddressCallback {

    private THSBaseView thsBaseView;

    public THSShippingAddressPresenter(THSBaseView thsBaseView){
        this.thsBaseView = thsBaseView;
    }

    public void updateShippingAddress(THSConsumer thsConsumer, Address address){
        try {
            THSManager.getInstance().updatePreferredShippingAddress(thsBaseView.getFragmentActivity(),thsConsumer,address,this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAddressValidationFailure(Map<String, ValidationReason> map) {
        Toast.makeText(thsBaseView.getFragmentActivity(),"Shipping Address validation failure",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateSuccess(Address address, SDKError sdkErro) {
        ((THSShippingAddressFragment) thsBaseView).updateShippingAddressView(address);
        Toast.makeText(thsBaseView.getFragmentActivity(),"Update Shipping address success",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdateFailure(Throwable throwable) {
        Toast.makeText(thsBaseView.getFragmentActivity(),"Update Shipping address Failed",Toast.LENGTH_SHORT).show();
    }
}
