package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.billing.CreatePaymentRequest;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.visit.THSWaitingRoomFragment;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by philips on 7/24/17.
 */

public class THSCreditCardBillingAddressPresenter implements THSBasePresenter, THSPaymentCallback.THSSDKValidatedCallback<THSPaymentMethod, THSSDKError> {

    THSCreditCardBillingAddressFragment mTHSBillingAddressFragment;
    THSCreatePaymentRequest mTHSCreatePaymentRequest;


    public THSCreditCardBillingAddressPresenter(THSCreditCardBillingAddressFragment thsBillingAddressFragment) {
        mTHSBillingAddressFragment = thsBillingAddressFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.update_shipping_address) {
            updatePaymentMethod();
        }

    }

    void updatePaymentMethod() {
        try {
            mTHSCreatePaymentRequest = THSManager.getInstance().getNewCreatePaymentRequest(mTHSBillingAddressFragment.getFragmentActivity());

            CreatePaymentRequest createPaymentRequest = mTHSCreatePaymentRequest.getCreatePaymentRequest();

            createPaymentRequest.setNameOnCard(mTHSBillingAddressFragment.mBundle.getString("cardHolderName"));
            createPaymentRequest.setCreditCardNumber(mTHSBillingAddressFragment.mBundle.getString("cardNumber"));
            createPaymentRequest.setCreditCardMonth(mTHSBillingAddressFragment.mBundle.getInt("expirationMonth"));
            createPaymentRequest.setCreditCardYear(mTHSBillingAddressFragment.mBundle.getInt("expirationYear"));
            createPaymentRequest.setCreditCardSecCode(mTHSBillingAddressFragment.mBundle.getString("CVVcode"));

            createPaymentRequest.setCreditCardZip(mTHSBillingAddressFragment.mZipcodeEditText.getText().toString().trim());

            THSAddress thsAddress = THSManager.getInstance().getAddress(mTHSBillingAddressFragment.getFragmentActivity());
            final Address address = thsAddress.getAddress();
            address.setAddress1(mTHSBillingAddressFragment.mAddressOneEditText.getText().toString().trim());
            address.setAddress2(mTHSBillingAddressFragment.mAddressTwoEditText.getText().toString().trim());
            address.setCity(mTHSBillingAddressFragment.mCityEditText.getText().toString().trim());
            address.setState(mTHSBillingAddressFragment.stateList.get(mTHSBillingAddressFragment.stateSpinner.getSelectedItemPosition()));
            address.setZipCode(mTHSBillingAddressFragment.mZipcodeEditText.getText().toString().trim());
            createPaymentRequest.setAddress(address);

            Map<String, ValidationReason> errors = new HashMap<>();
            THSManager.getInstance().validateCreatePaymentRequest(mTHSBillingAddressFragment.getFragmentActivity(), mTHSCreatePaymentRequest, errors);
            if (errors.isEmpty()) {
                THSManager.getInstance().updatePaymentMethod(mTHSBillingAddressFragment.getFragmentActivity(), mTHSCreatePaymentRequest, this);
            } else {

                AmwellLog.i("updateInsurance", "validateSubscriptionUpdateRequest error " + errors.toString());
            }


        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


    //// start os update payment callback
    @Override
    public void onResponse(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError) {
        if (null == tHSSDKError.getSdkError()) {
            AmwellLog.i("updatePayment", "success");
            mTHSBillingAddressFragment.addFragment(new THSWaitingRoomFragment(),THSWaitingRoomFragment.TAG,null);
        } else {
            AmwellLog.i("updatePayment", "failed");
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        AmwellLog.i("updatePayment", "failed");
    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> map) {
        AmwellLog.i("updatePayment", "failed");
    }
    //// end  os update payment callback
}
