/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import android.content.Context;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.SDKErrorReason;
import com.americanwell.sdk.entity.billing.CreatePaymentRequest;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.pharmacy.THSConsumerShippingAddressCallback;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSDateUtils;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_PAYMENT;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_PAYMENT;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_BILLING_ADDRESS1;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_CREDIT_CARD_NUMBER;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_CVV;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_EXPIRY_DATE;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_MONTH;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_NAME_ON_CARD;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;


public class THSCreditCardDetailPresenter implements THSBasePresenter, THSPaymentCallback.THSGetPaymentMethodCallBack<THSPaymentMethod, THSSDKError>, THSPaymentCallback.THSUpdatePaymentMethodCallBack<THSPaymentMethod, THSSDKError>, THSPaymentCallback.THSUpdatePaymentMethodValidatedCallback<THSPaymentMethod, THSSDKError>, THSConsumerShippingAddressCallback {

    protected THSCreditCardDetailFragment mTHSCreditCardDetailFragment;
    private THSCreatePaymentRequest mTHSCreatePaymentRequest;
    private String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
    private Pattern pattern = Pattern.compile(regex);
    private THSCreditCardDetailViewInterface thsCreditCardDetailViewInterface;
    private Address shippingAddress;

    THSCreditCardDetailPresenter(THSCreditCardDetailFragment thsCreditCardDetailFragment, THSCreditCardDetailViewInterface thsCreditCardDetailViewInterface) {
        mTHSCreditCardDetailFragment = thsCreditCardDetailFragment;
        this.thsCreditCardDetailViewInterface = thsCreditCardDetailViewInterface;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ths_payment_detail_continue_button) {
            validateFormDetails();
        } else if (componentID == R.id.ths_payment_detail_card_cvc_help) {
            thsCreditCardDetailViewInterface.showCvvDetail(true, true, false);
        } else if (componentID == R.id.uid_dialog_positive_button) {
            mTHSCreditCardDetailFragment.alertDialogFragment.dismiss();
            THSTagUtils.tagInAppNotification("cvvHelp", mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_matchmaking_ok_button));
        } else if (componentID == R.id.ths_credit_card_details_checkbox) {
            updateAddressAsShippingAddress();
        }

    }

    private void updateAddressAsShippingAddress() {
        thsCreditCardDetailViewInterface.updateAddress(shippingAddress);
    }


    void getPaymentMethod() {
        try {
            THSManager.getInstance().getPaymentMethod(mTHSCreditCardDetailFragment.getFragmentActivity(), this);
        } catch (Exception e) {
            AmwellLog.i(THSCreditCardDetailFragment.TAG, " Credit card details exception" + e.getLocalizedMessage());
        }
    }


    protected boolean validateCreditCardDetails(String cardNumber) {
        boolean validationResponse = false;
        try {
            validationResponse = THSManager.getInstance().isCreditCardNumberValid(mTHSCreditCardDetailFragment.getFragmentActivity(), cardNumber);
        } catch (AWSDKInstantiationException e) {
            AmwellLog.i(THSCreditCardDetailFragment.TAG, " Credit card details exception" + e.getLocalizedMessage());
        }
        return validationResponse;
    }

    protected boolean isNameValid(String cardName){
        return !(cardName.isEmpty() || cardName.length() == 0);
    }

    protected boolean isExpirationMonthValid(String expiryMonth){
        if(expiryMonth.isEmpty()){
            return false;
        } else if(expiryMonth.length() <= 0){
            return false;
        } else if(Integer.parseInt(expiryMonth) < 0 || Integer.parseInt(expiryMonth) > 12) {
            return false;
        } else {
            return true;
        }
    }

    protected boolean isExpirationYearValid(String expiryYear){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date currentDate = null;
        try {
            currentDate = sdf.parse(expiryYear);
        } catch (ParseException e) {

        }
        if(expiryYear.isEmpty()){
            return false;
        } else if(expiryYear.length() <= 0){
            return false;
        } else if(expiryYear.length() > 4){
            return false;
        } else if(!THSDateUtils.isYearValid(currentDate)){
            return false;
        } else return true;

    }

    protected boolean isCVCValid(Context context,String cardNumber, String cvcValue){
        boolean isCVVvalid = false;
        try {
            isCVVvalid = THSManager.getInstance().isSecurityCodeValid(context, cardNumber, cvcValue);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
        return isCVVvalid;
    }

    protected void getShippingAddress(Context context) {
        try {
            THSManager.getInstance().getConsumerShippingAddress(context, this);
        } catch (AWSDKInstantiationException e) {
            AmwellLog.i(THSCreditCardDetailFragment.TAG, e.getLocalizedMessage());
        }

    }

    private void validateFormDetails() {

        String cardHolderName = mTHSCreditCardDetailFragment.mCardHolderNameEditText.getText().toString().trim();
        String cardNumber = mTHSCreditCardDetailFragment.mCardNumberEditText.getText().toString().trim();
        String expirationMonth = mTHSCreditCardDetailFragment.mCardExpiryMonthEditText.getText().toString().trim();
        String expirationYear = mTHSCreditCardDetailFragment.mCardExpiryYearEditText.getText().toString().trim();
        String cvvCode = mTHSCreditCardDetailFragment.mCVCcodeEditText.getText().toString().trim();



        int month = 0;
        int year = 0;

        try {
            month = Integer.parseInt(expirationMonth);
            year = Integer.parseInt(expirationYear);

        } catch (Exception e) {

        }
        try {
            mTHSCreatePaymentRequest = THSManager.getInstance().getNewCreatePaymentRequest(mTHSCreditCardDetailFragment.getFragmentActivity());

            CreatePaymentRequest createPaymentRequest = mTHSCreatePaymentRequest.getCreatePaymentRequest();
            createPaymentRequest.setNameOnCard(cardHolderName);
            createPaymentRequest.setCreditCardNumber(cardNumber);
            createPaymentRequest.setCreditCardMonth(month);
            createPaymentRequest.setCreditCardYear(year);
            createPaymentRequest.setCreditCardSecCode(cvvCode);
            createPaymentRequest.setCreditCardZip(mTHSCreditCardDetailFragment.mZipcodeEditText.getText().toString().trim());

            THSAddress thsAddress = THSManager.getInstance().getAddress(mTHSCreditCardDetailFragment.getFragmentActivity());
            final Address address = thsAddress.getAddress();
            address.setAddress1(mTHSCreditCardDetailFragment.mAddressOneEditText.getText().toString().trim());
            address.setAddress2(mTHSCreditCardDetailFragment.mAddressTwoEditText.getText().toString().trim());
            address.setCity(mTHSCreditCardDetailFragment.mCityEditText.getText().toString().trim());
            address.setState(mTHSCreditCardDetailFragment.mCurrentSelectedState);
            address.setZipCode(mTHSCreditCardDetailFragment.mZipcodeEditText.getText().toString().trim());
            createPaymentRequest.setAddress(address);

            //TODO : validation for cvv 000 not done.
            Map<String, String> errors = new HashMap<>();
            THSManager.getInstance().validateCreatePaymentRequest(mTHSCreditCardDetailFragment.getFragmentActivity(), mTHSCreatePaymentRequest, errors);
            if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_NAME_ON_CARD)) {
                thsCreditCardDetailViewInterface.showCCNameError();
                mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION, mTHSCreditCardDetailFragment.getString(R.string.ths_not_valid_card_name), false);
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            } else if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_CREDIT_CARD_NUMBER)) {
                thsCreditCardDetailViewInterface.showCCNumberError();
                mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION, mTHSCreditCardDetailFragment.getString(R.string.ths_not_valid_credit_card_number), false);
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            } else if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_EXPIRY_DATE)) {
                thsCreditCardDetailViewInterface.showCCDateError();
                mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION, mTHSCreditCardDetailFragment.getString(R.string.ths_error_cc_expiry_date_detail_not_valid), false);
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            }else if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_MONTH)){
                thsCreditCardDetailViewInterface.showCCDateError();
                mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION, mTHSCreditCardDetailFragment.getString(R.string.ths_error_cc_expiry_date_detail_not_valid), false);
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            } else if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_CVV)) {
                thsCreditCardDetailViewInterface.showCCCVVError();
                mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION, mTHSCreditCardDetailFragment.getString(R.string.ths_not_valid_CVV_number), false);
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            } else if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_BILLING_ADDRESS1)) {
                mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_not_valid_address1));
                mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION, mTHSCreditCardDetailFragment.getString(R.string.ths_not_valid_address1), false);
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            } else if (errors.isEmpty()) {
                THSManager.getInstance().updatePaymentMethod(mTHSCreditCardDetailFragment.getFragmentActivity(), mTHSCreatePaymentRequest, this);
            }

        } catch (AWSDKInstantiationException e) {
            AmwellLog.i(THSCreditCardDetailFragment.TAG, " Credit card details exception" + e.getLocalizedMessage());
        }

    }

    @Override
    public void onGetPaymentSuccess(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError) {
        if (null != mTHSCreditCardDetailFragment && mTHSCreditCardDetailFragment.isFragmentAttached()) {
            if (null != tHSPaymentMethod && null != tHSPaymentMethod.getPaymentMethod()) {
                thsCreditCardDetailViewInterface.updateCreditCardDetails(tHSPaymentMethod);
            } else if (tHSSDKError.getSdkError() != null && !tHSSDKError.getSdkError().getSDKErrorReason().equalsIgnoreCase(SDKErrorReason.CREDIT_CARD_MISSING)) {
                THSSDKErrorFactory.getErrorType(mTHSCreditCardDetailFragment.getContext(), ANALYTICS_FETCH_PAYMENT, tHSSDKError.getSdkError());
            }

        }
    }

    boolean validateZip(String zipCode) {
        return pattern.matcher(zipCode).matches();

    }

    @Override
    public void onGetPaymentFailure(Throwable throwable) {
        mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getString(R.string.ths_se_server_error_toast_message));
    }

    @Override
    public void onUpdatePaymentSuccess(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError) {
        if (null != mTHSCreditCardDetailFragment && mTHSCreditCardDetailFragment.isFragmentAttached()) {
            if (null == tHSSDKError.getSdkError()) {
                AmwellLog.i("updatePayment", "success");
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "paymentMethodsAdded");
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "billingAddressAdded");
                mTHSCreditCardDetailFragment.popSelfBeforeTransition();
                mTHSCreditCardDetailFragment.popSelfBeforeTransition();
            } else {
                AmwellLog.e("updatePayment", "failed");
                mTHSCreditCardDetailFragment.showError(THSSDKErrorFactory.getErrorType(mTHSCreditCardDetailFragment.getContext(), ANALYTICS_UPDATE_PAYMENT, tHSSDKError.getSdkError()));
            }
        }
    }

    @Override
    public void onUpdatePaymentFailure(Throwable throwable) {
        if (null != mTHSCreditCardDetailFragment && mTHSCreditCardDetailFragment.isFragmentAttached()) {
            AmwellLog.e("updatePayment", throwable.toString());
            mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getString(R.string.ths_se_server_error_toast_message));
        }
    }

    @Override
    public void onValidationFailure(Map<String, String> map) {
        mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getString(R.string.ths_se_server_error_toast_message));
    }

    @Override
    public void onSuccessfulFetch(Address address, SDKError sdkError) {
        mTHSCreditCardDetailFragment.hideProgressBar();
        shippingAddress = address;
        /**
         * If shipping address is null, then disable the option to select address same as shipping address
         */
        if (null == shippingAddress) {
            thsCreditCardDetailViewInterface.updateCheckBoxState(false);
        }
    }

    @Override
    public void onFailure(Throwable throwable) {
        mTHSCreditCardDetailFragment.hideProgressBar();

    }
}
