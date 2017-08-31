package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.InputValidator;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.SalutationDropDown;
import com.philips.cdp.di.iap.view.StateDropDown;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.ValidationEditText;

import static com.philips.cdp.di.iap.utils.Utility.getValidator;

/**
 * Created by philips on 8/31/17.
 */

public class DLSShippingAddressFragment extends InAppBaseFragment
        implements View.OnClickListener, AddressController.AddressListener,
        PaymentController.PaymentListener,
        SalutationDropDown.SalutationListener,
        StateDropDown.StateListener, View.OnFocusChangeListener {

    private PhoneNumberUtil phoneNumberUtil;
    private Context mContext;
    private InputValidationLayout mLlFirstName;
    private InputValidator inputValidatorFirstName;
    private InputValidationLayout mLlLastName;
    private InputValidator inputValidatorLastName;
    private InputValidationLayout mLlSalutation;
    private InputValidator inputValidatorSalutation;
    private InputValidationLayout mLlAddressLineOne;
    private InputValidator inputValidatorAddressLineOne;
    private InputValidationLayout mLlAddressLineTwo;
    private InputValidator inputValidatorAddressLineTwo;
    private InputValidationLayout mLlTown;
    private InputValidator inputValidatorTown;
    private InputValidationLayout mLlPostalCode;
    private InputValidator inputValidatorPostalCode;
    private InputValidationLayout mLlCountry;
    private InputValidator inputValidatorCountry;
    private InputValidationLayout mlLState;
    private InputValidator inputValidatorState;
    private InputValidationLayout mLlEmail;
    private InputValidator inputValidatorEmail;
    private InputValidationLayout mLlPhone1;
    private InputValidator inputValidatorPhone;
    private ValidationEditText mEtFirstName;
    private ValidationEditText mEtLastName;
    private ValidationEditText mEtSalutation;
    private ValidationEditText mEtAddressLineOne;
    private ValidationEditText mEtAddressLineTwo;
    private ValidationEditText mEtTown;
    private ValidationEditText mEtPostalCode;
    private ValidationEditText mEtCountry;
    private ValidationEditText mEtState;
    private ValidationEditText mEtEmail;
    private ValidationEditText mEtPhone1;
    private Validator mValidator;
    private SalutationDropDown mSalutationDropDown;
    private StateDropDown mStateDropDown;
    private AddressFields addressFields;
    private String mRegionIsoCode;
    private DLSAddressFragment mParentFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dls_iap_address_shipping, container, false);
        initializeViews(view);
        return view;
    }

    private void initializeViews(View rootView) {

        mParentFragment = (DLSAddressFragment) DLSShippingAddressFragment.this.getParentFragment();


        phoneNumberUtil = PhoneNumberUtil.getInstance();

        mLlFirstName = (InputValidationLayout) rootView.findViewById(R.id.ll_first_name);
        inputValidatorFirstName = getValidator(Validator.NAME_PATTERN);
        mLlFirstName.setValidator(inputValidatorFirstName);


        mLlLastName = (InputValidationLayout) rootView.findViewById(R.id.ll_last_name);
        inputValidatorLastName = getValidator(Validator.NAME_PATTERN);
        mLlLastName.setValidator(inputValidatorLastName);


        mLlSalutation = (InputValidationLayout) rootView.findViewById(R.id.ll_salutation);
        inputValidatorSalutation = getValidator(Validator.ADDRESS_PATTERN);
        mLlSalutation.setValidator(inputValidatorSalutation);


        mLlAddressLineOne = (InputValidationLayout) rootView.findViewById(R.id.ll_address_line_one);
        inputValidatorAddressLineOne = getValidator(Validator.ADDRESS_PATTERN);
        mLlAddressLineOne.setValidator(inputValidatorAddressLineOne);


        mLlAddressLineTwo = (InputValidationLayout) rootView.findViewById(R.id.ll_address_line_two);
        inputValidatorAddressLineTwo = getValidator(Validator.ADDRESS_PATTERN);
        mLlAddressLineTwo.setValidator(inputValidatorAddressLineTwo);


        mLlTown = (InputValidationLayout) rootView.findViewById(R.id.ll_town);
        inputValidatorTown = getValidator(Validator.TOWN_PATTERN);
        mLlTown.setValidator(inputValidatorTown);


        mLlPostalCode = (InputValidationLayout) rootView.findViewById(R.id.ll_postal_code);
        inputValidatorPostalCode = getValidator(Validator.POSTAL_CODE_PATTERN);
        mLlPostalCode.setValidator(inputValidatorPostalCode);


        mLlCountry = (InputValidationLayout) rootView.findViewById(R.id.ll_country);
        inputValidatorCountry = getValidator(Validator.COUNTRY_PATTERN);
        mLlCountry.setValidator(inputValidatorCountry);


        mlLState = (InputValidationLayout) rootView.findViewById(R.id.ll_state);
        inputValidatorState = getValidator(Validator.NAME_PATTERN);
        mlLState.setValidator(inputValidatorState);


        mLlEmail = (InputValidationLayout) rootView.findViewById(R.id.ll_email);
        inputValidatorEmail = getValidator(Validator.EMAIL_PATTERN);
        mLlEmail.setValidator(inputValidatorEmail);


        mLlPhone1 = (InputValidationLayout) rootView.findViewById(R.id.ll_phone1);
        inputValidatorPhone = getValidator(Validator.PHONE_NUMBER_PATTERN);
        mLlPhone1.setValidator(inputValidatorPhone);


        mEtFirstName = (ValidationEditText) rootView.findViewById(R.id.et_first_name);
        mEtLastName = (ValidationEditText) rootView.findViewById(R.id.et_last_name);
        mEtSalutation = (ValidationEditText) rootView.findViewById(R.id.et_salutation);
        mEtAddressLineOne = (ValidationEditText) rootView.findViewById(R.id.et_address_line_one);
        mEtAddressLineTwo = (ValidationEditText) rootView.findViewById(R.id.et_address_line_two);
        mEtTown = (ValidationEditText) rootView.findViewById(R.id.et_town);
        mEtPostalCode = (ValidationEditText) rootView.findViewById(R.id.et_postal_code);
        mEtCountry = (ValidationEditText) rootView.findViewById(R.id.et_country);
        mEtState = (ValidationEditText) rootView.findViewById(R.id.et_state);
        mEtEmail = (ValidationEditText) rootView.findViewById(R.id.et_email);
        mEtPhone1 = (ValidationEditText) rootView.findViewById(R.id.et_phone1);


        mEtPostalCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mEtSalutation.setKeyListener(null);
        mEtState.setKeyListener(null);


/*
        mBtnContinue = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);

        mBtnContinue.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);*/


        mEtEmail.setText(HybrisDelegate.getInstance(mContext).getStore().getJanRainEmail());
        mEtEmail.setEnabled(false);


        mEtCountry.setText(HybrisDelegate.getInstance(mContext).getStore().getCountry());
        mEtCountry.setEnabled(false);
        showUSRegions();


        mValidator = new Validator();
        mEtFirstName.addTextChangedListener(new IAPTextWatcher(mEtFirstName));
        mEtLastName.addTextChangedListener(new IAPTextWatcher(mEtLastName));
        mEtAddressLineOne.addTextChangedListener(new IAPTextWatcher(mEtAddressLineOne));
        mEtAddressLineTwo.addTextChangedListener(new IAPTextWatcher(mEtAddressLineTwo));
        mEtTown.addTextChangedListener(new IAPTextWatcher(mEtTown));
        mEtPostalCode.addTextChangedListener(new IAPTextWatcher(mEtPostalCode));
        mEtCountry.addTextChangedListener(new IAPTextWatcher(mEtCountry));
        mEtEmail.addTextChangedListener(new IAPTextWatcher(mEtEmail));
        mEtPhone1.addTextChangedListener(new IAPTextWatcherPhone(mEtPhone1));
        mEtState.addTextChangedListener(new IAPTextWatcher(mEtState));
        mEtSalutation.addTextChangedListener(new IAPTextWatcher(mEtSalutation));


        mEtSalutation.setCompoundDrawables(null, null, Utility.getImageArrow(mContext), null);
        mSalutationDropDown = new SalutationDropDown(mContext, mEtSalutation, this);

        mEtSalutation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSalutationDropDown.show();
                return false;
            }
        });


        mEtState.setCompoundDrawables(null, null, Utility.getImageArrow(mContext), null);
        mStateDropDown = new StateDropDown(mContext, mEtState, this);

        mEtState.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideKeypad(mContext);
                mStateDropDown.show();
                return false;
            }
        });


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    @Override
    public void onSalutationSelect(View view, String salutation) {
        mEtSalutation.setText(salutation);
        mEtSalutation.setCompoundDrawables(null, null, Utility.getImageArrow(mContext), null);
    }

    @Override
    public void onStateSelect(View view, String state) {
        mEtState.setText(state);
    }

    @Override
    public void stateRegionCode(String regionCode) {

        mRegionIsoCode = regionCode;
        addressFields.setRegionIsoCode(regionCode);
       /* if (addressHashMap != null) {
            addressHashMap.put(ModelConstants.REGION_ISOCODE, regionCode);
        }*/

//        if (!(this instanceof BillingAddressFragment)) {
      /*  if (!mUseThisAddressCheckBox.isChecked()) {
            CartModelContainer.getInstance().setRegionIsoCode(regionCode);
        }*/
    }

    @Override
    public void onGetPaymentDetails(Message msg) {

    }

    @Override
    public void onSetPaymentDetails(Message msg) {

    }

    @Override
    public void onGetRegions(Message msg) {

    }

    @Override
    public void onGetUser(Message msg) {

    }

    @Override
    public void onCreateAddress(Message msg) {

    }

    @Override
    public void onGetAddress(Message msg) {

    }

    @Override
    public void onSetDeliveryAddress(Message msg) {

    }

    @Override
    public void onGetDeliveryModes(Message msg) {

    }

    @Override
    public void onSetDeliveryMode(Message msg) {

    }


    private class IAPTextWatcher implements TextWatcher {
        private EditText mEditText;
        private boolean isInAfterTextChanged;

        public IAPTextWatcher(EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText != mEtPhone1) {
                validate(mEditText, false);
            }
        }

        public synchronized void afterTextChanged(Editable text) {
            if (mEditText == mEtPhone1) {
                isInAfterTextChanged = true;
                validate(mEditText, false);
                isInAfterTextChanged = false;
            }
        }

    }

    private class IAPTextWatcherPhone implements TextWatcher {
        private EditText mEditText;
        private boolean isInAfterTextChanged;
        private boolean mIgnoreTextChangeListener = false;

        public IAPTextWatcherPhone(EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText != mEtPhone1 && !mIgnoreTextChangeListener) {
                validate(mEditText, false);
            }
        }

        public synchronized void afterTextChanged(Editable text) {
            if (mEditText == mEtPhone1 && !isInAfterTextChanged && !mIgnoreTextChangeListener) {
                isInAfterTextChanged = true;
                validate(mEditText, false);
                isInAfterTextChanged = false;
            }
        }

    }


    private boolean validatePhoneNumber(EditText editText, String country, String number) {
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneNumberUtil.parse(number, country);
            boolean isValid = phoneNumberUtil.isValidNumber(phoneNumber);
            String formattedPhoneNumber = phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
            editText.setText(formattedPhoneNumber);
            editText.setSelection(editText.getText().length());
            return isValid;
        } catch (Exception e) {
            IAPLog.d("ShippingAddressFragment", "NumberParseException");
        }
        return false;
    }


    private void showUSRegions() {
        if (mEtCountry.getText().toString().equals("US")) {
            mlLState.setVisibility(View.VISIBLE);
        } else {
            mlLState.setVisibility(View.GONE);
        }
    }


    public void validate(View editText, boolean hasFocus) {
        boolean result = true;
        if (editText.getId() == R.id.et_first_name && !hasFocus) {
            result = inputValidatorFirstName.isValidName(((EditText) editText).getText().toString());
            if (!result) {
                mLlFirstName.setErrorMessage(R.string.iap_first_name_error);
                mLlFirstName.showError();
            }
        }
        if (editText.getId() == R.id.et_last_name && !hasFocus) {
            result = inputValidatorLastName.isValidName(mEtLastName.getText().toString());
            if (!result) {
                mLlLastName.setErrorMessage(R.string.iap_last_name_error);
                mLlLastName.showError();
            }
        }
        if (editText.getId() == R.id.et_town && !hasFocus) {
            result = inputValidatorTown.isValidTown(mEtTown.getText().toString());
            if (!result) {
                mLlTown.setErrorMessage(R.string.iap_town_error);
                mLlTown.showError();
            }
        }
        if (editText.getId() == R.id.et_phone1 && !hasFocus) {
            if (mEtPhone1.getText() != null) {
                result = validatePhoneNumber(mEtPhone1, HybrisDelegate.getInstance().getStore().getCountry()
                        , mEtPhone1.getText().toString());
                if (!result) {
                    mLlPhone1.setErrorMessage(R.string.iap_phone_error);
                    mLlPhone1.showError();
                }
            }
        }
        if (editText.getId() == R.id.et_country && !hasFocus) {
            showUSRegions();
            result = inputValidatorCountry.isValidCountry(mEtCountry.getText().toString());
            if (!result) {
                mLlCountry.setErrorMessage(R.string.iap_country_error);
                mLlCountry.showError();
            }
        }
        if (editText.getId() == R.id.et_postal_code && !hasFocus) {
            result = inputValidatorPostalCode.isValidPostalCode(mEtPostalCode.getText().toString());
            if (!result) {
                mLlPostalCode.setErrorMessage(R.string.iap_postal_code_error);
                mLlPostalCode.showError();
            }
        }
        if (editText.getId() == R.id.et_email && !hasFocus) {
            result = inputValidatorEmail.isValidEmail(mEtEmail.getText().toString());
            if (!result) {
                mLlEmail.setErrorMessage(R.string.iap_email_error);
                mLlEmail.showError();
            }
        }
        if (editText.getId() == R.id.et_address_line_one && !hasFocus) {
            result = inputValidatorAddressLineOne.isValidAddress(mEtAddressLineOne.getText().toString());
            if (!result) {
                mLlAddressLineOne.setErrorMessage(R.string.iap_address_error);
                mLlAddressLineOne.showError();
            }
        }
        if (editText.getId() == R.id.et_address_line_two) {
            result = inputValidatorAddressLineTwo.isValidAddress(mEtAddressLineTwo.getText().toString());
            if (mEtAddressLineTwo.getText().toString().trim().equals("")) {
                result = true;
            } else {
                if (!result) {
                    mLlAddressLineTwo.setErrorMessage(R.string.iap_address_error);
                    mLlAddressLineTwo.showError();
                }
            }

        }
        if ((editText.getId() == R.id.et_salutation || editText.getId() == R.id.et_state) && !hasFocus) {
            checkFields();
        }

    }

    public void checkFields() {
        String firstName = mEtFirstName.getText().toString();
        String lastName = mEtLastName.getText().toString();
        String addressLineOne = mEtAddressLineOne.getText().toString();
        String addressLineTwo = mEtAddressLineTwo.getText().toString();
        String postalCode = mEtPostalCode.getText().toString().replaceAll(" ", "");
        String phone1 = mEtPhone1.getText().toString().replaceAll(" ", "");
        String town = mEtTown.getText().toString();
        String country = mEtCountry.getText().toString();
        String email = mEtEmail.getText().toString();

        addressFields = new AddressFields();

        if (mValidator.isValidName(firstName) && mValidator.isValidName(lastName)
                && mValidator.isValidAddress(addressLineOne) && (addressLineTwo.trim().equals("") || mValidator.isValidAddress(addressLineTwo))
                && mValidator.isValidPostalCode(postalCode)
                && mValidator.isValidEmail(email) && mValidator.isValidPhoneNumber(phone1)
                && mValidator.isValidTown(town) && mValidator.isValidCountry(country)
                && (!mEtSalutation.getText().toString().trim().equalsIgnoreCase(""))
                && (mlLState.getVisibility() == View.GONE || (mlLState.getVisibility() == View.VISIBLE && !mEtState.getText().toString().trim().equalsIgnoreCase("")))) {

            setAddressFields();

            if (mParentFragment.checkBox.isChecked()) {
                mParentFragment.mBtnContinue.setEnabled(true);
            } else {
                mParentFragment.mBtnContinue.setEnabled(false);
            }
        } else {
            mParentFragment.mBtnContinue.setEnabled(false);
        }
    }

    protected AddressFields setAddressFields() {
        if (addressFields == null) addressFields = new AddressFields();
        addressFields.setFirstName(mEtFirstName.getText().toString());
        addressFields.setLastName(mEtLastName.getText().toString());
        addressFields.setTitleCode(mEtSalutation.getText().toString());
        addressFields.setCountryIsocode(mEtCountry.getText().toString());
        addressFields.setLine1(mEtAddressLineOne.getText().toString());
        addressFields.setLine2(mEtAddressLineTwo.getText().toString());
        addressFields.setPostalCode(mEtPostalCode.getText().toString().replaceAll(" ", ""));
        addressFields.setTown(mEtTown.getText().toString());
        addressFields.setPhone1(mEtPhone1.getText().toString().replaceAll(" ", ""));
        addressFields.setPhone2(mEtPhone1.getText().toString().replaceAll(" ", ""));
        addressFields.setEmail(mEtEmail.getText().toString());


        //  if (this instanceof BillingAddressFragment) {
        if (mlLState.getVisibility() == View.VISIBLE) {
            addressFields.setRegionIsoCode(mRegionIsoCode);
            addressFields.setRegionName(mEtState.getText().toString());
        } else {
            addressFields.setRegionIsoCode(null);
            addressFields.setRegionName(null);
        }
        return addressFields;
    }


}
