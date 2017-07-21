package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.State;
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

    private String mCardHolderName;
    private String mCardNumber;
    int mExpirationMonth;
    int mExpirationYear;
    private String mCVVcode;

    private String mZipCode;
    private String mAddress1;
    private String mAddress2;
    private String mCity;
    private State mState;


    public THSCreditCardDetailPresenter(THSCreditCardDetailFragment thsCreditCardDetailFragment) {
        mTHSCreditCardDetailFragment = thsCreditCardDetailFragment;
    }

    @Override
    public void onEvent(int componentID) {
        saveCreditCardDetail();

    }


    void getPaymentMethod() {
        mTHSCreditCardDetailFragment.showProgressBar();
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


    public void addCreditCard() throws AWSDKInstantiationException {

        mCreatePaymentRequest.setNameOnCard(mCardHolderName);
        mCreatePaymentRequest.setCreditCardNumber(mCardNumber);
        mCreatePaymentRequest.setCreditCardMonth(mExpirationMonth);
        mCreatePaymentRequest.setCreditCardYear(mExpirationYear);
        mCreatePaymentRequest.setCreditCardSecCode(mCVVcode);
        mCreatePaymentRequest.setCreditCardZip(mZipCode);
        final Address address = THSManager.getInstance().getAwsdk(mTHSCreditCardDetailFragment.getFragmentActivity()).getNewAddress();

        address.setAddress1(mAddress1);
        address.setAddress2(mAddress2);
        address.setCity(mCity);
        address.setState(mState);
        address.setZipCode(mZipCode);

        mCreatePaymentRequest.setAddress(address);

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

        mCardHolderName = mTHSCreditCardDetailFragment.mCardHolderNameEditText.getText().toString().trim();
        mCardNumber = mTHSCreditCardDetailFragment.mCardNumberEditText.getText().toString().trim();
        boolean isCreditcardValid = validateCreditCardDetails(mCardNumber);
        if (!isCreditcardValid) {
            mTHSCreditCardDetailFragment.showToast("Invalid Credit card number");
            return;
        }

        mExpirationMonth = Integer.parseInt(mTHSCreditCardDetailFragment.mCardExpiryMonthEditText.getText().toString().trim());
      //todo month validation
        mExpirationYear = Integer.parseInt(mTHSCreditCardDetailFragment.mCardExpiryYearEditText.getText().toString().trim());
        //todo year validation
        mCVVcode = mTHSCreditCardDetailFragment.mCVCcodeEditText.getText().toString().trim();
         boolean isCVVvalid = validateCVVnumber(mCardNumber,mCVVcode);
        if (!isCVVvalid) {
            mTHSCreditCardDetailFragment.showToast("Invalid CVV number");
            return;
        }else{
            // go to address fragment
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
