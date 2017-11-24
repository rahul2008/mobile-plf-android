/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.entity.billing.CreatePaymentRequest;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ValidationReason;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.cost.THSCostSummaryFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.AmwellLog;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_PAYMENT;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;


public class THSCreditCardBillingAddressPresenter implements THSBasePresenter, THSPaymentCallback.THSgetPaymentMethodValidatedCallback<THSPaymentMethod, THSSDKError> {

    private THSCreditCardBillingAddressFragment mTHSBillingAddressFragment;
    protected THSCreatePaymentRequest mTHSCreatePaymentRequest;
    String regex = "^[0-9]{5}(?:-[0-9]{4})?$";
    Pattern pattern = Pattern.compile(regex);


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
                mTHSBillingAddressFragment.showError(errors.toString());
                AmwellLog.i("updateInsurance", "validateSubscriptionUpdateRequest error " + errors.toString());
            }


        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


    void updateAddresIfAvailable(Address address) {
        if (null != address) {
            mTHSBillingAddressFragment.mAddressOneEditText.setText(address.getAddress1());
            mTHSBillingAddressFragment.mAddressTwoEditText.setText(address.getAddress2());
            mTHSBillingAddressFragment.mCityEditText.setText(address.getCity());
            mTHSBillingAddressFragment.mZipcodeEditText.setText(address.getZipCode());
            State currentState = address.getState();
            if (null != currentState) {
                //  currentState.
                int currentStateindex = mTHSBillingAddressFragment.stateList.indexOf(currentState);
                if (currentStateindex > -1) {
                    mTHSBillingAddressFragment.stateSpinner.setSelection(currentStateindex);
                }
            }
        }

    }

    public boolean validateZip(String zipCode){
        return pattern.matcher(zipCode).matches();

    }

    //// start os update payment callback
    @Override
    public void onGetPaymentMethodResponse(THSPaymentMethod tHSPaymentMethod, THSSDKError tHSSDKError) {
        if (null != mTHSBillingAddressFragment && mTHSBillingAddressFragment.isFragmentAttached()) {
            if (null == tHSSDKError.getSdkError()) {
                AmwellLog.i("updatePayment", "success");
                //mTHSBillingAddressFragment.addFragment(new THSWaitingRoomFragment(), THSWaitingRoomFragment.TAG, null);
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "paymentMethodsAdded");
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "billingAddressAdded");
                mTHSBillingAddressFragment.getFragmentActivity().getSupportFragmentManager().popBackStack(THSCostSummaryFragment.TAG, 0);

            } else {
                AmwellLog.i("updatePayment", "failed");
                mTHSBillingAddressFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_UPDATE_PAYMENT, tHSSDKError.getSdkError()));
            }
        }
    }

    @Override
    public void onGetPaymentFailure(Throwable throwable) {
        if (null != mTHSBillingAddressFragment && mTHSBillingAddressFragment.isFragmentAttached()) {
            AmwellLog.i("updatePayment", "failed");
            mTHSBillingAddressFragment.showError(mTHSBillingAddressFragment.getString(R.string.ths_se_server_error_toast_message));
        }
    }

    @Override
    public void onValidationFailure(Map<String, ValidationReason> map) {
        if (null != mTHSBillingAddressFragment && mTHSBillingAddressFragment.isFragmentAttached()) {
            AmwellLog.i("updatePayment", "failed");
        }
    }
    //// end  os update payment callback
}
