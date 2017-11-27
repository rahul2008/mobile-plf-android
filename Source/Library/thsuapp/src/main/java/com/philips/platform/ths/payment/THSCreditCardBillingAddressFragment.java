/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Country;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.pharmacy.THSSpinnerAdapter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;

import java.util.List;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_PAYMENT;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_VITALS;
import static com.philips.platform.ths.utility.THSConstants.THS_BILLING_ADDRESS;



public class THSCreditCardBillingAddressFragment extends THSBaseFragment implements View.OnClickListener {

    public static final String TAG = THSCreditCardBillingAddressFragment.class.getSimpleName();
    private ActionBarListener actionBarListener;
    protected THSCreditCardBillingAddressPresenter mTHSCreditCardBillingAddressPresenter;
    Bundle mBundle;

    Label mBillingAddresslabel;
    private RelativeLayout mProgressbarContainer;

    AppCompatSpinner stateSpinner;
    private THSSpinnerAdapter stateSpinnerAdapter;
    List<State> stateList = null;

    EditText mAddressOneEditText;
    EditText mAddressTwoEditText;
    EditText mCityEditText;
    EditText mZipcodeEditText;
    Button mContinueButton;
    private InputValidationLayout postCodeValidationLayout, addressValidationLayout, cityValidationLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_shipping_address_fragment, container, false);
        mBundle = getArguments();
        mBillingAddresslabel = (Label) view.findViewById(R.id.shipping_address_text_label);
        mBillingAddresslabel.setText(R.string.ths_address_shipping_address_string);
        mAddressOneEditText = (EditText) view.findViewById(R.id.sa_shipping_address_line_one);
        mAddressTwoEditText = (EditText) view.findViewById(R.id.sa_shipping_address_line_two);
        mCityEditText = (EditText) view.findViewById(R.id.sa_town);
        mZipcodeEditText = (EditText) view.findViewById(R.id.sa_postal_code_edittext);
        mContinueButton = (Button) view.findViewById(R.id.update_shipping_address);
        postCodeValidationLayout = (InputValidationLayout) view.findViewById(R.id.sa_postal_code);
        cityValidationLayout = (InputValidationLayout) view.findViewById(R.id.sa_town_validation_layout);
        addressValidationLayout = (InputValidationLayout) view.findViewById(R.id.sa_shipping_address_line_one_validation_layout);
        mContinueButton.setOnClickListener(this);
        mContinueButton.setEnabled(false);
        addressValidationLayout.setValidator(new InputValidationLayout.Validator() {
            @Override
            public boolean validate(CharSequence charSequence) {
                boolean validateString = validateString(mAddressOneEditText.getText().toString());
                updateContinueBtnState();
                if (!validateString) {
                    addressValidationLayout.showError();
                    doTagging(ANALYTICS_UPDATE_PAYMENT,addressValidationLayout.getErrorLabelView().getText().toString(),false);
                    return false;
                }
                return true;
            }
        });
        cityValidationLayout.setValidator(new InputValidationLayout.Validator() {
            @Override
            public boolean validate(CharSequence charSequence) {
                updateContinueBtnState();
                boolean validateString = validateString(mCityEditText.getText().toString());
                if(!validateString){
                    cityValidationLayout.showError();
                    doTagging(ANALYTICS_UPDATE_PAYMENT,addressValidationLayout.getErrorLabelView().getText().toString(),false);
                    return false;
                }
                return true;
            }
        });
        postCodeValidationLayout.setErrorMessage(R.string.ths_pharmacy_search_error);
        postCodeValidationLayout.setValidator(new InputValidationLayout.Validator() {
            @Override
            public boolean validate(CharSequence charSequence) {
                updateContinueBtnState();
                boolean validateString = mTHSCreditCardBillingAddressPresenter.validateZip(mZipcodeEditText.getText().toString());
                if(!validateString){
                    postCodeValidationLayout.showError();
                    doTagging(ANALYTICS_UPDATE_PAYMENT,getString(R.string.ths_pharmacy_search_error),false);
                    return false;
                }
                return true;
            }
        });
        mTHSCreditCardBillingAddressPresenter = new THSCreditCardBillingAddressPresenter(this);
        stateSpinner = (AppCompatSpinner) view.findViewById(R.id.sa_state_spinner);

        try {
            final List<Country> supportedCountries = THSManager.getInstance().getAwsdk(getContext()).getSupportedCountries();
            stateList = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getConsumerManager().getValidPaymentMethodStates(supportedCountries.get(0));
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

        stateSpinnerAdapter = new THSSpinnerAdapter(getActivity(), R.layout.ths_pharmacy_spinner_layout, stateList);
        stateSpinner.setAdapter(stateSpinnerAdapter);
        stateSpinner.setSelection(0);
        mProgressbarContainer = (RelativeLayout) view.findViewById(R.id.shipping_address_container);
        return view;
    }

    private void updateContinueBtnState() {
        boolean enableContinueBtn = false;
        enableContinueBtn = mTHSCreditCardBillingAddressPresenter.validateZip(mZipcodeEditText.getText().toString()) && validateString(mCityEditText.getText().toString()) &&
                validateString(mAddressOneEditText.getText().toString());
        mContinueButton.setEnabled(enableContinueBtn);
    }

    private boolean validateString(String s) {
        if (null == s || s.isEmpty()) {
            addressValidationLayout.setErrorMessage(R.string.ths_address_validation_empty_string);
            cityValidationLayout.setErrorMessage(R.string.ths_address_validation_city_empty_string);
            doTagging(ANALYTICS_UPDATE_PAYMENT,getString(R.string.ths_address_validation_empty_string),false);
            doTagging(ANALYTICS_UPDATE_PAYMENT,getString(R.string.ths_address_validation_city_empty_string),false);
            return false;
        } else if (s.length() < 2 || s.length() > 25) {
            addressValidationLayout.setErrorMessage(R.string.ths_address_validation_length_string);
            cityValidationLayout.setErrorMessage(R.string.ths_address_validation_city_length_string);
            doTagging(ANALYTICS_UPDATE_PAYMENT,getString(R.string.ths_address_validation_length_string),false);
            doTagging(ANALYTICS_UPDATE_PAYMENT,getString(R.string.ths_address_validation_city_length_string),false);
            return false;
        } else {
            return true;
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        Address address = mBundle.getParcelable("address");
        mTHSCreditCardBillingAddressPresenter.updateAddresIfAvailable(address);
        //createCustomProgressBar(mProgressbarContainer, MEDIUM);
    }


    @Override
    public void onResume() {
        super.onResume();
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_BILLING_ADDRESS,null,null);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar("Billing address", true);
        }

    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
            mTHSCreditCardBillingAddressPresenter.onEvent(v.getId());

    }
}
