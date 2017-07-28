package com.philips.platform.ths.payment;

import android.os.Bundle;

import com.americanwell.sdk.entity.billing.CreatePaymentRequest;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSManager;

/**
 * Created by philips on 7/21/17.
 */

public class THSCreditCardDetailPresenter implements THSBasePresenter, THSPaymentCallback.THSSDKCallBack<THSPaymentMethod, THSSDKError> {

    THSCreditCardDetailFragment mTHSCreditCardDetailFragment;
    THSCreatePaymentRequest mThsCreatePaymentRequest;
    CreatePaymentRequest mCreatePaymentRequest;


    public THSCreditCardDetailPresenter(THSCreditCardDetailFragment thsCreditCardDetailFragment) {
        mTHSCreditCardDetailFragment = thsCreditCardDetailFragment;
    }

    @Override
    public void onEvent(int componentID) {
        saveCreditCardDetail();

    }


    void getPaymentMethod() {
        try {
            THSManager.getInstance().getPaymentMethod(mTHSCreditCardDetailFragment.getFragmentActivity(), this);
        } catch (Exception e) {

        }
    }

    THSCreatePaymentRequest getNewCreatePaymentRequest() {
        THSCreatePaymentRequest thsCreatePaymentRequest = null;
        try {
            THSManager.getInstance().getNewCreatePaymentRequest(mTHSCreditCardDetailFragment.getFragmentActivity());
        } catch (Exception e) {
            // todo
        }
        return thsCreatePaymentRequest;
    }


    boolean validateCreditCardDetails(String cardNumber) {
        boolean validationresult = false;
        try {
            validationresult = THSManager.getInstance().isCreditCardNumberValid(mTHSCreditCardDetailFragment.getFragmentActivity(), cardNumber);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
        return validationresult;
    }

    boolean validateCVVnumber(String cardNumber, String cvv) {
        boolean isCVVvalid = false;
        try {
            THSManager.getInstance().isSecurityCodeValid(mTHSCreditCardDetailFragment.getFragmentActivity(), cardNumber, cvv);
        } catch (Exception e) {
            // todo
        }
        return isCVVvalid;
    }

    void saveCreditCardDetail() {

        String cardHolderName = mTHSCreditCardDetailFragment.mCardHolderNameEditText.getText().toString().trim();
        String cardNumber = mTHSCreditCardDetailFragment.mCardNumberEditText.getText().toString().trim();
        boolean isCreditcardValid = validateCreditCardDetails(cardNumber);
        if (!isCreditcardValid) {
            mTHSCreditCardDetailFragment.showToast("Invalid Credit card number");
            return;
        }
        String expirationMonth = mTHSCreditCardDetailFragment.mCardExpiryMonthEditText.getText().toString().trim();
        //todo month validation

        String expirationYear = mTHSCreditCardDetailFragment.mCardExpiryYearEditText.getText().toString().trim();
        //todo year validation

        String CVVcode = mTHSCreditCardDetailFragment.mCVCcodeEditText.getText().toString().trim();
        //todo CVV validation
        boolean isCVVvalid = true;//validateCVVnumber(cardNumber, CVVcode);
        if (!isCVVvalid) {
            mTHSCreditCardDetailFragment.showToast("Invalid CVV number");
            return;
        } else {
            // go to Billing address fragment

            Bundle bundle = new Bundle();
            bundle.putString("cardHolderName", cardHolderName);
            bundle.putString("cardNumber", cardNumber);
            bundle.putInt("expirationMonth", Integer.parseInt(expirationMonth));
            bundle.putInt("expirationYear", Integer.parseInt(expirationYear));
            bundle.putString("CVVcode", CVVcode);

            final THSCreditCardBillingAddressFragment fragment = new THSCreditCardBillingAddressFragment();
            fragment.setFragmentLauncher(mTHSCreditCardDetailFragment.getFragmentLauncher());
            mTHSCreditCardDetailFragment.addFragment(fragment, THSCreditCardBillingAddressFragment.TAG, bundle);

        }

    }

    /////////start of getPaymentMethod callback ////////////
    @Override
    public void onResponse(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError) {
        mTHSCreditCardDetailFragment.hideProgressBar();
        if (null != tHSPaymentMethod && null != tHSPaymentMethod.getPaymentMethod()) {

        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        mTHSCreditCardDetailFragment.hideProgressBar();
    }
    /////////end of getPaymentMethod callback ////////////
}
