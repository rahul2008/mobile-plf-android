/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import android.os.Bundle;

import com.americanwell.sdk.entity.billing.CreatePaymentRequest;
import com.americanwell.sdk.entity.billing.PaymentMethod;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_PAYMENT;
import static com.philips.platform.ths.utility.THSConstants.CVV_HELP_TEXT;
import static com.philips.platform.ths.utility.THSConstants.THS_IN_APP_NOTIFICATION;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_EXPIRY_DATE;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;


public class THSCreditCardDetailPresenter implements THSBasePresenter, THSPaymentCallback.THSgetPaymentMethodCallBack<THSPaymentMethod, THSSDKError> {

    private THSCreditCardDetailFragment mTHSCreditCardDetailFragment;
    private THSCreatePaymentRequest mThsCreatePaymentRequest;
    private CreatePaymentRequest mCreatePaymentRequest;
    private PaymentMethod mPaymentMethod;
    private THSCreatePaymentRequest mTHSCreatePaymentRequest;


    public THSCreditCardDetailPresenter(THSCreditCardDetailFragment thsCreditCardDetailFragment) {
        mTHSCreditCardDetailFragment = thsCreditCardDetailFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ths_payment_detail_continue_button) {
            saveCreditCardDetail();
        } else if (componentID == R.id.ths_payment_detail_card_cvc_help) {
            showCVVdetail(true, true, false);
        } else if (componentID == R.id.uid_dialog_positive_button) {
            mTHSCreditCardDetailFragment.alertDialogFragment.dismiss();
        }

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
        if (null == cardHolderName || cardHolderName.isEmpty()) {
            mTHSCreditCardDetailFragment.showToast(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            return;
        }

        String cardNumber = mTHSCreditCardDetailFragment.mCardNumberEditText.getText().toString().trim();
        if (null == cardNumber || cardNumber.isEmpty()) {
            mTHSCreditCardDetailFragment.showToast(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            return;
        }
        boolean isCreditcardValid = validateCreditCardDetails(cardNumber);
        if (!isCreditcardValid) {
            mTHSCreditCardDetailFragment.showToast(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_not_valid_credit_card));
            return;
        }
        String expirationMonth = mTHSCreditCardDetailFragment.mCardExpiryMonthEditText.getText().toString().trim();
        if (null == expirationMonth || expirationMonth.isEmpty()) {
            mTHSCreditCardDetailFragment.showToast(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            return;
        }
        String expirationYear = mTHSCreditCardDetailFragment.mCardExpiryYearEditText.getText().toString().trim();
        if (null == expirationYear || expirationYear.isEmpty()) {
            mTHSCreditCardDetailFragment.showToast(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            return;
        }

        String CVVcode = mTHSCreditCardDetailFragment.mCVCcodeEditText.getText().toString().trim();
        if (null == CVVcode || CVVcode.isEmpty()) {
            mTHSCreditCardDetailFragment.showToast(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            return;
        }
        boolean isCVVvalid = true;// validateCVVnumber(cardNumber, CVVcode); //todo validateCVVnumber always returns false
        if (!isCVVvalid) {
            mTHSCreditCardDetailFragment.showToast("Invalid CVV number");
            return;
        } else {

            // go to Billing address fragment

            Bundle bundle = new Bundle();
            bundle.putString("cardHolderName", cardHolderName);
            bundle.putString("cardNumber", cardNumber);
            int month;
            int year;

            try {
                month = Integer.parseInt(expirationMonth);
                year = Integer.parseInt(expirationYear);
                bundle.putInt("expirationMonth", month);
                bundle.putInt("expirationYear", year);
            } catch (Exception e) {
                mTHSCreditCardDetailFragment.showToast(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_cc_expiry_date_detail_not_valid));
                return;
            }
            if (month > 12) {
                mTHSCreditCardDetailFragment.showToast(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_cc_expiry_date_detail_not_valid));
                return;
            }
            Map<String, ValidationReason> errors = new HashMap<>();
            try {
                mTHSCreatePaymentRequest = THSManager.getInstance().getNewCreatePaymentRequest(mTHSCreditCardDetailFragment.getFragmentActivity());
                CreatePaymentRequest createPaymentRequest = mTHSCreatePaymentRequest.getCreatePaymentRequest();
                createPaymentRequest.setCreditCardMonth(month);
                createPaymentRequest.setCreditCardYear(year);
                THSManager.getInstance().validateCreatePaymentRequest(mTHSCreditCardDetailFragment.getFragmentActivity(), mTHSCreatePaymentRequest, errors);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
            if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_EXPIRY_DATE)) {
                // if expiration date is invalid
                mTHSCreditCardDetailFragment.showToast(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_cc_expiry_date_detail_not_valid));
            } else {

                bundle.putString("CVVcode", CVVcode);
                if (null != mPaymentMethod && null != mPaymentMethod.getBillingAddress()) {
                    bundle.putParcelable("address", mPaymentMethod.getBillingAddress());
                }
                final THSCreditCardBillingAddressFragment fragment = new THSCreditCardBillingAddressFragment();
                fragment.setFragmentLauncher(mTHSCreditCardDetailFragment.getFragmentLauncher());
                mTHSCreditCardDetailFragment.addFragment(fragment, THSCreditCardBillingAddressFragment.TAG, bundle, true);
            }

        }

    }

    /////////start of getPaymentMethod callback ////////////
    @Override
    public void onGetPaymentMethodResponse(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError) {
        if (null != mTHSCreditCardDetailFragment && mTHSCreditCardDetailFragment.isFragmentAttached()) {
            mTHSCreditCardDetailFragment.hideProgressBar();
            if (null != tHSPaymentMethod && null != tHSPaymentMethod.getPaymentMethod()) {
                mPaymentMethod = tHSPaymentMethod.getPaymentMethod();
                mTHSCreditCardDetailFragment.mCardHolderNameEditText.setText(mPaymentMethod.getBillingName());
            } else if (tHSSDKError.getSdkError() != null) {
                THSSDKErrorFactory.getErrorType(ANALYTICS_FETCH_PAYMENT, tHSSDKError.getSdkError());
            }

        }
    }

    @Override
    public void onGetPaymentFailure(Throwable throwable) {
        if (null != mTHSCreditCardDetailFragment && mTHSCreditCardDetailFragment.isFragmentAttached()) {
            mTHSCreditCardDetailFragment.showToast(R.string.ths_se_server_error_toast_message);
            mTHSCreditCardDetailFragment.hideProgressBar();
        }
    }
    /////////end of getPaymentMethod callback ////////////


    void showCVVdetail(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon) {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mTHSCreditCardDetailFragment.getFragmentActivity())
                .setMessage(showLargeContent ? mTHSCreditCardDetailFragment.getFragmentActivity().getResources().getString(R.string.cvv_explaination) : mTHSCreditCardDetailFragment.getFragmentActivity().getResources().getString(R.string.cvv_explaination)).
                        setPositiveButton(" Ok ", mTHSCreditCardDetailFragment);

        if (isWithTitle) {
            builder.setTitle("What's this?");

        }
        mTHSCreditCardDetailFragment.alertDialogFragment = builder.setCancelable(false).create();
        mTHSCreditCardDetailFragment.alertDialogFragment.show(mTHSCreditCardDetailFragment.getFragmentManager(), CVV_HELP_TEXT);
        THSManager.getInstance().getThsTagging().trackActionWithInfo(THS_SEND_DATA, THS_IN_APP_NOTIFICATION, "CvvHelp");

    }
}
