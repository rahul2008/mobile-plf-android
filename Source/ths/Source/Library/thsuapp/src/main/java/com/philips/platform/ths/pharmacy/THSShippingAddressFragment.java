/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.americanwell.sdk.AWSDK;
import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Country;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.americanwell.sdk.manager.ConsumerManager;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.UIPicker;

import java.util.List;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_UPDATE_SHIPPING_ADDRESS;
import static com.philips.platform.ths.utility.THSConstants.THS_SHIPPING_ADDRESS;

public class THSShippingAddressFragment extends THSBaseFragment implements View.OnClickListener {

    public static String TAG = THSShippingAddressFragment.class.getSimpleName();
    private THSConsumerWrapper thsConsumerWrapper;
    private Address address;

    private EditText addressLineOne, addressLineTwo, postalCode, town;
    protected Button updateAddressButton;
    protected THSShippingAddressPresenter thsShippingAddressPresenter;
    static final long serialVersionUID = 81L;


    protected UIPicker uiPicker;
    private Label anchorUIPicker;
    protected EditText placeHolderUIPicker;
    protected State mCurrentSelectedState;
    private THSSpinnerAdapter spinnerAdapter;
    private List<State> stateList = null;
    private ActionBarListener actionBarListener;
    private InputValidationLayout postCodeValidationLayout, addressValidationLayout, cityValidationLayout;
    List<Country> supportedCountries;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_shipping_address_fragment, container, false);
        actionBarListener = getActionBarListener();

        if (null != actionBarListener) {
            actionBarListener.updateActionBar(R.string.ths_shipping_address_fragment_name, true);
        }

        anchorUIPicker = (Label) view.findViewById(R.id.sa_state_text);
        placeHolderUIPicker = (EditText) view.findViewById(R.id.sa_state_text_place_holder);
        placeHolderUIPicker.setOnClickListener(this);
        try {
            supportedCountries = getSupportedCountries();
            stateList = getValidShippingStates(supportedCountries);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

        spinnerAdapter = getThsSpinnerAdapter();
        uiPicker = getUiPicker();
        if(null!=uiPicker) {
            uiPicker.setAdapter(spinnerAdapter);
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
        }
        addressLineOne = (EditText) view.findViewById(R.id.sa_shipping_address_line_one);
        addressLineTwo = (EditText) view.findViewById(R.id.sa_shipping_address_line_two);
        postalCode = (EditText) view.findViewById(R.id.sa_postal_code_edittext);
        town = (EditText) view.findViewById(R.id.sa_town);
        postCodeValidationLayout = (InputValidationLayout) view.findViewById(R.id.sa_postal_code);
        cityValidationLayout = (InputValidationLayout) view.findViewById(R.id.sa_town_validation_layout);
        addressValidationLayout = (InputValidationLayout) view.findViewById(R.id.sa_shipping_address_line_one_validation_layout);
        addressValidationLayout.setValidator(new InputValidationLayout.Validator() {
            @Override
            public boolean validate(CharSequence charSequence) {
                boolean validateString = validateString(addressLineOne.getText().toString());
                updateContinueBtnState();
                if (!validateString) {
                    addressValidationLayout.showError();
                    doTagging(ANALYTICS_UPDATE_SHIPPING_ADDRESS, addressValidationLayout.getErrorLabelView().getText().toString(), false);
                    return false;
                }
                return true;
            }
        });
        cityValidationLayout.setValidator(new InputValidationLayout.Validator() {
            @Override
            public boolean validate(CharSequence charSequence) {
                updateContinueBtnState();
                boolean validateString = validateString(town.getText().toString());
                if (!validateString) {
                    cityValidationLayout.showError();
                    doTagging(ANALYTICS_UPDATE_SHIPPING_ADDRESS, cityValidationLayout.getErrorLabelView().getText().toString(), false);
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
                boolean validateString = thsShippingAddressPresenter.validateZip(postalCode.getText().toString());
                if (!validateString) {
                    postCodeValidationLayout.showError();
                    doTagging(ANALYTICS_UPDATE_SHIPPING_ADDRESS, postCodeValidationLayout.getErrorLabelView().getText().toString(), false);
                    return false;
                }
                return true;
            }
        });
        updateAddressButton = (Button) view.findViewById(R.id.update_shipping_address);
        updateAddressButton.setOnClickListener(this);
        updateAddressButton.setEnabled(false);
        thsShippingAddressPresenter = new THSShippingAddressPresenter(this);

        final Bundle arguments = getArguments();
        Address addressBundle = null;
        if(arguments!=null) {
            addressBundle = arguments.getParcelable(THS_SHIPPING_ADDRESS);
        }
        if(addressBundle!=null){
            updateAddressFields(addressBundle);
        }

        return view;
    }

    @NonNull
    public THSSpinnerAdapter getThsSpinnerAdapter() {
        return new THSSpinnerAdapter(getActivity(), R.layout.ths_pharmacy_spinner_layout, stateList);
    }

    private void updateAddressFields(Address addressBundle){
        if(addressBundle.getAddress1()!=null) {
            addressLineOne.setText(addressBundle.getAddress1());
        }
        if(addressBundle.getAddress2()!=null){
            addressLineTwo.setText(addressBundle.getAddress2());
        }
        if(addressBundle.getCity()!=null) {
            town.setText(addressBundle.getCity());
        }
        if(addressBundle.getZipCode()!=null) {
            postalCode.setText(addressBundle.getZipCode());
        }
        if(addressBundle.getState()!=null) {
            mCurrentSelectedState = addressBundle.getState();
            placeHolderUIPicker.setText(mCurrentSelectedState.getName());
        }
        updateContinueBtnState();
    }

    protected void updateContinueBtnState() {
        boolean enableContinueBtn = false;
        enableContinueBtn = thsShippingAddressPresenter.validateZip(postalCode.getText().toString()) && validateString(town.getText().toString()) &&
                validateString(addressLineOne.getText().toString()) && validateSelectedState();
        updateAddressButton.setEnabled(enableContinueBtn);
    }

    private boolean validateSelectedState() {
        return null != mCurrentSelectedState;
    }

    protected boolean validateString(String s) {
        if (null == s || s.isEmpty()) {
            addressValidationLayout.setErrorMessage(R.string.ths_address_validation_empty_string);
            cityValidationLayout.setErrorMessage(R.string.ths_address_validation_city_empty_string);
            return false;
        } else if (s.length() < 2) {
            addressValidationLayout.setErrorMessage(R.string.ths_address_validation_length_string);
            cityValidationLayout.setErrorMessage(R.string.ths_address_validation_city_length_string);
            return false;
        }else if (s.length() > 25) {
            addressValidationLayout.setErrorMessage(R.string.ths_address_validation_length__max_string);
            cityValidationLayout.setErrorMessage(R.string.ths_address_validation_city_length_max_string);
            return false;
        } else {
            return true;
        }
    }

    @NonNull
    public List<Country> getSupportedCountries() throws AWSDKInstantiationException {
        return THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getSupportedCountries();
    }

    public List<State> getValidShippingStates(List<Country> supportedCountries) throws AWSDKInstantiationException {

        Country country = supportedCountries.get(0);
        AWSDK awsdk = THSManager.getInstance().getAwsdk(getContext());
        ConsumerManager consumerManager = awsdk.getConsumerManager();
        List<State> validShippingStates = consumerManager.getValidShippingStates(country);

        return validShippingStates;
    }

    public void setConsumerAndAddress(THSConsumerWrapper thsConsumerWrapper, Address address) {
        this.thsConsumerWrapper = thsConsumerWrapper;
        this.address = address;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update_shipping_address) {

            try {
                Address address1 = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getNewAddress();
                address1.setAddress1(addressLineOne.getText().toString());
                address1.setAddress2(addressLineTwo.getText().toString());
                address1.setCity(town.getText().toString());
                address1.setZipCode(postalCode.getText().toString());
                address1.setState(mCurrentSelectedState);
                thsShippingAddressPresenter.updateShippingAddress(address1);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }
        } else if (v.getId() == R.id.sa_state_text_place_holder) {
            if(null!=uiPicker) {
                uiPicker.show();
                updateUiPickerSelection();
            }
        }
    }

    private void updateUiPickerSelection() {
        if (null != mCurrentSelectedState) {
            int currentStateindex = stateList.indexOf(mCurrentSelectedState);
            if (currentStateindex > -1) {
                uiPicker.setSelection(currentStateindex);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        THSTagUtils.doTrackPageWithInfo(THS_SHIPPING_ADDRESS, null, null);
    }

     UIPicker getUiPicker() {
        Context popupThemedContext = UIDHelper.getPopupThemedContext(getContext());
        UIPicker uiPicker = new UIPicker(popupThemedContext);
        return uiPicker;
    }
}
