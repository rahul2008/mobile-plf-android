package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.base.THSBaseFragment;
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
        ((THSBaseFragment)thsBaseView).showToast("Shipping Address validation failure");
    }

    @Override
    public void onUpdateSuccess(Address address, SDKError sdkErro) {
        //TODO: check this immediately
        //Toast.makeText(thsBaseView.getFragmentActivity(),"Update address success",Toast.LENGTH_SHORT).show();
        //thsBaseView.addFragment(new THSInsuranceConfirmationFragment(),THSInsuranceConfirmationFragment.TAG,null);
        ((THSShippingAddressFragment) thsBaseView).updateShippingAddressView(address);
        ((THSShippingAddressFragment) thsBaseView).showToast("Update Shipping address success");
    }

    @Override
    public void onUpdateFailure(Throwable throwable) {
        ((THSShippingAddressFragment) thsBaseView).showToast("Update Shipping address Failed");
    }
}
