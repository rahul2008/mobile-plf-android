/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.payment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Country;
import com.americanwell.sdk.entity.State;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.pharmacy.THSSpinnerAdapter;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIPicker;

import java.util.List;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_STATES;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_PAYMENT;
import static com.philips.platform.ths.utility.THSConstants.CVV_HELP_TEXT;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_CVV_EXPLAINATION;
import static com.philips.platform.ths.utility.THSConstants.THS_ANALYTICS_RESPONSE_OK;
import static com.philips.platform.ths.utility.THSConstants.THS_PAYMENT_METHOD;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SERVER_ERROR;


@SuppressWarnings("serial")
public class THSCreditCardDetailFragment extends THSBaseFragment implements View.OnClickListener,THSCreditCardDetailViewInterface, CompoundButton.OnCheckedChangeListener {

    public static final String TAG = THSCreditCardDetailFragment.class.getSimpleName();
    THSCreditCardDetailPresenter mTHSCreditCardDetailPresenter;
    EditText mCardHolderNameEditText, mCardNumberEditText, mCardExpiryMonthEditText, mCardExpiryYearEditText, mCVCcodeEditText,
            mAddressOneEditText, mAddressTwoEditText, mCityEditText, placeHolderUIPicker, mZipcodeEditText;
    Label cvvDetail, mBillingAddresslabel, anchorUIPicker;
    private Button mPaymentDetailContinueButton;
    AlertDialogFragment alertDialogFragment;
    private ActionBarListener actionBarListener;
    private RelativeLayout mProgressbarContainer;
    protected UIPicker uiPicker;
    protected State mCurrentSelectedState;
    private THSSpinnerAdapter stateSpinnerAdapter;
    List<State> stateList = null;
    private InputValidationLayout postCodeValidationLayout, addressValidationLayout, cityValidationLayout;
    private CheckBox ths_credit_card_details_checkbox;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_payment_detail, container, false);
        mTHSCreditCardDetailPresenter = new THSCreditCardDetailPresenter(this,this);
        mCardHolderNameEditText = view.findViewById(R.id.ths_payment_detail_card_holder_name_edittext);
        mCardNumberEditText = view.findViewById(R.id.ths_payment_detail_card_number_edittext);
        mCardExpiryMonthEditText = view.findViewById(R.id.ths_payment_detail_card_expiration_month_edittext);
        mCardExpiryYearEditText = view.findViewById(R.id.ths_payment_detail_card_expiration_year_edittext);
        mCVCcodeEditText = view.findViewById(R.id.ths_payment_detail_card_cvc_edittext);
        mPaymentDetailContinueButton = view.findViewById(R.id.ths_payment_detail_continue_button);
        ths_credit_card_details_checkbox = view.findViewById(R.id.ths_credit_card_details_checkbox);
        ths_credit_card_details_checkbox.setOnCheckedChangeListener(this);
        mPaymentDetailContinueButton.setOnClickListener(this);
        mProgressbarContainer = view.findViewById(R.id.ths_payment_detail_container);
        cvvDetail = view.findViewById(R.id.ths_payment_detail_card_cvc_help);
        cvvDetail.setOnClickListener(this);
        mBillingAddresslabel = view.findViewById(R.id.shipping_address_text_label);
        mBillingAddresslabel.setText(R.string.ths_payment_billing_address_string);
        mAddressOneEditText = view.findViewById(R.id.sa_shipping_address_line_one);
        mAddressTwoEditText = view.findViewById(R.id.sa_shipping_address_line_two);
        mCityEditText = view.findViewById(R.id.sa_town);
        mZipcodeEditText = view.findViewById(R.id.sa_postal_code_edittext);
        postCodeValidationLayout = view.findViewById(R.id.sa_postal_code);
        cityValidationLayout = view.findViewById(R.id.sa_town_validation_layout);
        addressValidationLayout = view.findViewById(R.id.sa_shipping_address_line_one_validation_layout);
        anchorUIPicker = view.findViewById(R.id.sa_state_text);
        placeHolderUIPicker = view.findViewById(R.id.sa_state_text_place_holder);
        placeHolderUIPicker.setOnClickListener(this);
        addressValidationLayout.setValidator(new InputValidationLayout.Validator() {
            @Override
            public boolean validate(CharSequence charSequence) {
                boolean validateString = validateString(mAddressOneEditText.getText().toString());
                updateContinueBtnState();
                if (!validateString) {
                    addressValidationLayout.showError();
                    doTagging(ANALYTICS_UPDATE_PAYMENT, addressValidationLayout.getErrorLabelView().getText().toString(), false);
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
                if (!validateString) {
                    cityValidationLayout.showError();
                    doTagging(ANALYTICS_UPDATE_PAYMENT, addressValidationLayout.getErrorLabelView().getText().toString(), false);
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
                boolean validateString = mTHSCreditCardDetailPresenter.validateZip(mZipcodeEditText.getText().toString());
                if (!validateString) {
                    postCodeValidationLayout.showError();
                    doTagging(ANALYTICS_UPDATE_PAYMENT, getString(R.string.ths_pharmacy_search_error), false);
                    return false;
                }
                return true;
            }
        });

        try {
            final List<Country> supportedCountries = THSManager.getInstance().getAwsdk(getContext()).getSupportedCountries();
            stateList = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getConsumerManager().getValidPaymentMethodStates(supportedCountries.get(0));
        } catch (Exception e) {
            final String errorTag = THSTagUtils.createErrorTag(ANALYTICS_FETCH_STATES, e.getMessage());
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SERVER_ERROR, errorTag);
        }

        stateSpinnerAdapter = new THSSpinnerAdapter(getActivity(), R.layout.ths_pharmacy_spinner_layout, stateList);
        Context popupThemedContext = UIDHelper.getPopupThemedContext(getContext());
        uiPicker = new UIPicker(popupThemedContext);
        uiPicker.setAdapter(stateSpinnerAdapter);
        uiPicker.setAnchorView(anchorUIPicker);
        uiPicker.setModal(true);
        uiPicker.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mCurrentSelectedState = stateList.get(position);
                        placeHolderUIPicker.setText(mCurrentSelectedState.getName());
                        uiPicker.setSelection(position);
                        uiPicker.dismiss();
                        updateContinueBtnState();
                    }
                }
        );

        mProgressbarContainer = view.findViewById(R.id.shipping_address_container);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarListener = getActionBarListener();
        createCustomProgressBar(mProgressbarContainer, BIG);
        mTHSCreditCardDetailPresenter.getPaymentMethod();
        mTHSCreditCardDetailPresenter.getShippingAddress(getActivity());
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        alertDialogFragment = (AlertDialogFragment) getFragmentManager().findFragmentByTag(CVV_HELP_TEXT);
        if (alertDialogFragment != null) {
            alertDialogFragment.setPositiveButtonListener(this);

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_PAYMENT_METHOD, null, null);
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(getString(R.string.ths_credit_card_details_fragment_name), true);
        }

    }

    private void updateContinueBtnState() {
        boolean enableContinueBtn;
        enableContinueBtn = mTHSCreditCardDetailPresenter.validateZip(mZipcodeEditText.getText().toString()) && validateString(mCityEditText.getText().toString()) &&
                validateString(mAddressOneEditText.getText().toString()) && validateSelectedState();
        mPaymentDetailContinueButton.setEnabled(enableContinueBtn);
    }

    private boolean validateSelectedState() {
        return null != mCurrentSelectedState;
    }

    private boolean validateString(String s) {
        if (null == s || s.isEmpty()) {
            addressValidationLayout.setErrorMessage(R.string.ths_address_validation_empty_string);
            cityValidationLayout.setErrorMessage(R.string.ths_address_validation_city_empty_string);
            doTagging(ANALYTICS_UPDATE_PAYMENT, getString(R.string.ths_address_validation_empty_string), false);
            doTagging(ANALYTICS_UPDATE_PAYMENT, getString(R.string.ths_address_validation_city_empty_string), false);
            return false;
        } else if (s.length() < 2) {
            addressValidationLayout.setErrorMessage(R.string.ths_address_validation_length_string);
            cityValidationLayout.setErrorMessage(R.string.ths_address_validation_city_length_string);
            doTagging(ANALYTICS_UPDATE_PAYMENT, getString(R.string.ths_address_validation_length_string), false);
            doTagging(ANALYTICS_UPDATE_PAYMENT, getString(R.string.ths_address_validation_city_length_string), false);
            return false;
        } else if (s.length() > 25) {
            addressValidationLayout.setErrorMessage(R.string.ths_address_validation_length__max_string);
            cityValidationLayout.setErrorMessage(R.string.ths_address_validation_city_length_max_string);
            doTagging(ANALYTICS_UPDATE_PAYMENT, getString(R.string.ths_address_validation_length__max_string), false);
            doTagging(ANALYTICS_UPDATE_PAYMENT, getString(R.string.ths_address_validation_city_length_max_string), false);
            return false;
        } else {
            return true;
        }
    }

    private void updateUiPickerSelection() {
        if (null != mCurrentSelectedState) {
            int currentStateIndex = stateList.indexOf(mCurrentSelectedState);
            if (currentStateIndex > -1) {
                uiPicker.setSelection(currentStateIndex);
            }
        }
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.sa_state_text_place_holder) {
            uiPicker.show();
            updateUiPickerSelection();
        } else {
            mTHSCreditCardDetailPresenter.onEvent(v.getId());
        }
    }

    @Override
    public void updateCreditCardDetails(THSPaymentMethod thsPaymentMethod) {

        if (null != thsPaymentMethod.getPaymentMethod()) {
            Address address = thsPaymentMethod.getPaymentMethod().getBillingAddress();
            mCardHolderNameEditText.setText(thsPaymentMethod.getPaymentMethod().getBillingName());
            updateAddress(address);
            updateContinueBtnState();
        }
    }

    @Override
    public void updateAddress(Address address) {
        mAddressOneEditText.setText(address.getAddress1());
        mAddressTwoEditText.setText(address.getAddress2());
        mCityEditText.setText(address.getCity());
        mZipcodeEditText.setText(address.getZipCode());
        mCurrentSelectedState = null != address.getState() ? address.getState() : stateList.get(0);
        placeHolderUIPicker.setText(mCurrentSelectedState.getName());
    }

    @Override
    public void updateCheckBoxState(boolean isEnabled) {
        ths_credit_card_details_checkbox.setEnabled(isEnabled);
    }

    @Override
    public void showCvvDetail(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon) {
        View.OnClickListener alertDialogFragmentCVVListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogFragment.dismiss();
                THSTagUtils.tagInAppNotification(THS_ANALYTICS_CVV_EXPLAINATION, THS_ANALYTICS_RESPONSE_OK);
            }
        };

        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getFragmentActivity())
                .setMessage(showLargeContent ? getFragmentActivity().getResources().getString(R.string.ths_cvv_explanation) :
                        getFragmentActivity().getResources().getString(R.string.ths_cvv_explanation)).
                        setPositiveButton(getResources().getString(R.string.ths_matchmaking_ok_button), this);

        if (isWithTitle) {
            builder.setTitle(getFragmentActivity().getResources().getString(R.string.ths_credit_card_details_whats_this_text));

        }
        alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.setPositiveButtonListener(alertDialogFragmentCVVListener);
        alertDialogFragment.show(getFragmentManager(), CVV_HELP_TEXT);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isEnabled) {
        if(isEnabled){
            mTHSCreditCardDetailPresenter.onEvent(compoundButton.getId());
        }
    }
}
