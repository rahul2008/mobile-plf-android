/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import android.os.Bundle;
import android.view.View;

import com.americanwell.sdk.entity.SDKErrorReason;
import com.americanwell.sdk.entity.billing.CreatePaymentRequest;
import com.americanwell.sdk.entity.billing.PaymentMethod;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.HashMap;
import java.util.Map;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_PAYMENT;
import static com.philips.platform.ths.utility.THSConstants.CVV_HELP_TEXT;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_DATE_VALIDATION;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_CREDIT_CARD_VALIDATION;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_CVV_EXPLAINATION;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_RESPONSE_OK;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_CREDIT_CARD_NUMBER;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_CVV;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_EXPIRY_DATE;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD_INVALID_NAME_ON_CARD;


public class THSCreditCardDetailPresenter implements THSBasePresenter, THSPaymentCallback.THSgetPaymentMethodCallBack<THSPaymentMethod, THSSDKError> {

    private THSCreditCardDetailFragment mTHSCreditCardDetailFragment;
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
            THSTagUtils.tagInAppNotification("cvvHelp",mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_matchmaking_ok_button));
        }

    }


    void getPaymentMethod() {
        try {
            THSManager.getInstance().getPaymentMethod(mTHSCreditCardDetailFragment.getFragmentActivity(), this);
        } catch (Exception e) {

        }
    }


    boolean validateCreditCardDetails(String cardNumber) {
        boolean validationresult = false;
        try {
            validationresult = THSManager.getInstance().isCreditCardNumberValid(mTHSCreditCardDetailFragment.getFragmentActivity(), cardNumber);
        } catch (AWSDKInstantiationException e) {

        }
        return validationresult;
    }

    void saveCreditCardDetail() {


        String cardHolderName = mTHSCreditCardDetailFragment.mCardHolderNameEditText.getText().toString().trim();
        if (null == cardHolderName || cardHolderName.isEmpty()) {
            mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION,mTHSCreditCardDetailFragment.getString(R.string.ths_error_all_fields_mandatory),false);
            return;
        }

        String cardNumber = mTHSCreditCardDetailFragment.mCardNumberEditText.getText().toString().trim();
        if (null == cardNumber || cardNumber.isEmpty()) {
            mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION,mTHSCreditCardDetailFragment.getString(R.string.ths_error_all_fields_mandatory),false);
            return;
        }
        boolean isCreditcardValid = validateCreditCardDetails(cardNumber);
        if (!isCreditcardValid) {
            mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_not_valid_credit_card_number));
            mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_CREDIT_CARD_VALIDATION,mTHSCreditCardDetailFragment.getString(R.string.ths_not_valid_credit_card_number),false);
            return;
        }
        String expirationMonth = mTHSCreditCardDetailFragment.mCardExpiryMonthEditText.getText().toString().trim();
        if (null == expirationMonth || expirationMonth.isEmpty()) {
            mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION,mTHSCreditCardDetailFragment.getString(R.string.ths_error_all_fields_mandatory),false);
            return;
        }
        String expirationYear = mTHSCreditCardDetailFragment.mCardExpiryYearEditText.getText().toString().trim();
        if (null == expirationYear || expirationYear.isEmpty()) {
            mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION,mTHSCreditCardDetailFragment.getString(R.string.ths_error_all_fields_mandatory),false);
            return;
        }

        String CVVcode = mTHSCreditCardDetailFragment.mCVCcodeEditText.getText().toString().trim();
        if (null == CVVcode || CVVcode.isEmpty()) {
            mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_all_fields_mandatory));
            mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_PAYMENT_INFORMATION_VALIDATION,mTHSCreditCardDetailFragment.getString(R.string.ths_error_all_fields_mandatory),false);
            return;
        }
        boolean isCVVvalid = true;// validateCVVnumber(cardNumber, CVVcode); //todo validateCVVnumber always returns false
        if(CVVcode.length()<3 ){
            isCVVvalid=false;
        }
        if (!isCVVvalid) {
            mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getString(R.string.ths_not_valid_CVV_number));
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
                mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_cc_expiry_date_detail_not_valid));
                mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_DATE_VALIDATION,mTHSCreditCardDetailFragment.getString(R.string.ths_error_cc_expiry_date_detail_not_valid),false);
                return;
            }
            if (month > 12 || month <=0 ) {
                mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_cc_expiry_date_detail_not_valid));
                mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_DATE_VALIDATION,mTHSCreditCardDetailFragment.getString(R.string.ths_error_cc_expiry_date_detail_not_valid),false);
                return;
            }
            Map<String, String> errors = new HashMap<>();
            try {
                mTHSCreatePaymentRequest = THSManager.getInstance().getNewCreatePaymentRequest(mTHSCreditCardDetailFragment.getFragmentActivity());
                CreatePaymentRequest createPaymentRequest = mTHSCreatePaymentRequest.getCreatePaymentRequest();
                createPaymentRequest.setNameOnCard(cardHolderName);
                createPaymentRequest.setCreditCardNumber(cardNumber);
                createPaymentRequest.setCreditCardMonth(month);
                createPaymentRequest.setCreditCardYear(year);
                createPaymentRequest.setCreditCardSecCode(CVVcode);
                THSManager.getInstance().validateCreatePaymentRequest(mTHSCreditCardDetailFragment.getFragmentActivity(), mTHSCreatePaymentRequest, errors);
            } catch (AWSDKInstantiationException e) {

            }
            if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_NAME_ON_CARD)) {
                mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_not_valid_card_name));
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            }else if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_CREDIT_CARD_NUMBER)) {
                mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_not_valid_credit_card_number));
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            }else if (errors.containsKey(THS_PAYMENT_METHOD_INVALID_EXPIRY_DATE)) {
                mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_error_cc_expiry_date_detail_not_valid));
                mTHSCreditCardDetailFragment.doTagging(THS_ANALYTICS_DATE_VALIDATION,mTHSCreditCardDetailFragment.getString(R.string.ths_error_cc_expiry_date_detail_not_valid),false);
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            } else if(errors.containsKey(THS_PAYMENT_METHOD_INVALID_CVV)) {
                mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_not_valid_CVV_number));
                mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getString(R.string.ths_not_valid_CVV_number));
                AmwellLog.i("updateCard", "validateSubscriptionUpdateRequest error " + errors.toString());
            } else { // still errors will have ZIP and address error code, so ignoring them as they will be added in next screen
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
            } else if (tHSSDKError.getSdkError() != null && tHSSDKError.getSdkError().getSDKErrorReason()!= SDKErrorReason.CREDIT_CARD_MISSING) {
                THSSDKErrorFactory.getErrorType(mTHSCreditCardDetailFragment.getContext(), ANALYTICS_FETCH_PAYMENT, tHSSDKError.getSdkError());
            }

        }
    }

    @Override
    public void onGetPaymentFailure(Throwable throwable) {
        if (null != mTHSCreditCardDetailFragment && mTHSCreditCardDetailFragment.isFragmentAttached()) {
            mTHSCreditCardDetailFragment.showError(mTHSCreditCardDetailFragment.getString(R.string.ths_se_server_error_toast_message));
            mTHSCreditCardDetailFragment.hideProgressBar();
        }
    }
    /////////end of getPaymentMethod callback ////////////


    void showCVVdetail(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon) {
        View.OnClickListener alertDialogFragmentCVVListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTHSCreditCardDetailFragment.alertDialogFragment.dismiss();
                THSTagUtils.tagInAppNotification(THS_ANALYTICS_CVV_EXPLAINATION,THS_ANALYTICS_RESPONSE_OK);
            }
        };

        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mTHSCreditCardDetailFragment.getFragmentActivity())
                .setMessage(showLargeContent ? mTHSCreditCardDetailFragment.getFragmentActivity().getResources().getString(R.string.ths_cvv_explanation) : mTHSCreditCardDetailFragment.getFragmentActivity().getResources().getString(R.string.ths_cvv_explanation)).
                        setPositiveButton(mTHSCreditCardDetailFragment.getResources().getString(R.string.ths_matchmaking_ok_button), mTHSCreditCardDetailFragment);

        if (isWithTitle) {
            builder.setTitle(mTHSCreditCardDetailFragment.getFragmentActivity().getResources().getString(R.string.ths_credit_card_details_whats_this_text));

        }
        mTHSCreditCardDetailFragment.alertDialogFragment = builder.setCancelable(false).create();
        mTHSCreditCardDetailFragment.alertDialogFragment.setPositiveButtonListener(alertDialogFragmentCVVListener);
        mTHSCreditCardDetailFragment.alertDialogFragment.show(mTHSCreditCardDetailFragment.getFragmentManager(), CVV_HELP_TEXT);

    }
}
