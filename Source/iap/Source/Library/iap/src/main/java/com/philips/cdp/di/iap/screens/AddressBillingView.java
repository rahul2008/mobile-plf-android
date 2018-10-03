package com.philips.cdp.di.iap.screens;

import android.app.Activity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.InputValidator;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.SalutationDropDown;
import com.philips.cdp.di.iap.view.StateDropDown;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.ValidationEditText;

import java.util.Map;

/**
 * Created by philips on 8/31/17.
 */

public class AddressBillingView
        implements SalutationDropDown.SalutationListener,
        StateDropDown.StateListener {


    private final AddressPresenter addressPresenter;
    private final Activity mContext;
    private final View view;

    private InputValidationLayout mLlFirstNameBilling;
    private InputValidator inputValidatorFirstNameBilling;
    private InputValidationLayout mLlLastNameBilling;
    private InputValidator inputValidatorLastNameBilling;
    private InputValidationLayout mLlAddressLineOneBilling;
    private InputValidator inputValidatorAddressLineOneBilling;
    private InputValidationLayout mLlAddressLineTwoBilling;
    private InputValidator inputValidatorAddressLineTwoBilling;
    private InputValidationLayout mLlTownBilling;

    private AddressBillingPresenter addressBillingPresenter;

    AddressContractor addressContractor;



    public AddressBillingView(AddressPresenter addressPresenter) {
        this.addressPresenter = addressPresenter;
        addressContractor = addressPresenter.getAddressContractor();
        this.mContext = this.addressPresenter.getAddressContractor().getActivityContext();
        this.view = this.addressPresenter.getAddressContractor().getBillingAddressView();
        addressBillingPresenter = new AddressBillingPresenter();
        initializeViews(view);
    }

    private InputValidator inputValidatorTownBilling;
    private InputValidationLayout mLlPostalCodeBilling;
    private InputValidator inputValidatorPostalCodeBilling;
    private InputValidationLayout mLlCountryBilling;
    private InputValidator inputValidatorCountryBilling;
    private InputValidationLayout mlLStateBilling;
    private InputValidationLayout mLlEmailBilling;
    private InputValidator inputValidatorEmailBilling;
    private InputValidationLayout mLlPhone1Billing;
    private ValidationEditText mEtFirstNameBilling;
    private ValidationEditText mEtLastNameBilling;
    private ValidationEditText mEtSalutationBilling;
    private ValidationEditText mEtAddressLineOneBilling;
    private ValidationEditText mEtAddressLineTwoBilling;
    private ValidationEditText mEtTownBilling;
    private ValidationEditText mEtPostalCodeBilling;
    private ValidationEditText mEtCountryBilling;
    private ValidationEditText mEtStateBilling;
    private ValidationEditText mEtEmailBilling;
    private ValidationEditText mEtPhone1Billing;
    private SalutationDropDown mSalutationDropDownBilling;
    private StateDropDown mStateDropDownBilling;
    private Validator mValidator;
    AddressFields billingAddressFields;
    private String mRegionIsoCode;



    private void initializeViews(View rootView) {
        billingAddressFields = new AddressFields();

        mLlFirstNameBilling = rootView.findViewById(R.id.ll_billing_first_name);
        inputValidatorFirstNameBilling = new InputValidator(Validator.NAME_PATTERN);
        mLlFirstNameBilling.setValidator(inputValidatorFirstNameBilling);

        mLlLastNameBilling = rootView.findViewById(R.id.ll_billing_last_name);
        inputValidatorLastNameBilling = new InputValidator(Validator.NAME_PATTERN);
        mLlLastNameBilling.setValidator(inputValidatorLastNameBilling);

        InputValidationLayout mLlSalutationBilling = rootView.findViewById(R.id.ll_billing_salutation);
        InputValidator inputValidatorSalutationBilling = new InputValidator(Validator.ADDRESS_PATTERN);
        mLlSalutationBilling.setValidator(inputValidatorSalutationBilling);

        mLlAddressLineOneBilling = rootView.findViewById(R.id.ll_billing_address_line_one);
        inputValidatorAddressLineOneBilling = new InputValidator(Validator.ADDRESS_PATTERN);
        mLlAddressLineOneBilling.setValidator(inputValidatorAddressLineOneBilling);

        mLlAddressLineTwoBilling = rootView.findViewById(R.id.ll_billing_address_line_two);
        inputValidatorAddressLineTwoBilling = new InputValidator(Validator.ADDRESS_PATTERN);
       // mLlAddressLineTwoBilling.setValidator(inputValidatorAddressLineTwoBilling);

        mLlTownBilling = rootView.findViewById(R.id.ll_billing_town);
        inputValidatorTownBilling = new InputValidator(Validator.TOWN_PATTERN);
        mLlTownBilling.setValidator(inputValidatorTownBilling);

        mLlPostalCodeBilling = rootView.findViewById(R.id.ll_billing_postal_code);
        inputValidatorPostalCodeBilling = new InputValidator(Validator.POSTAL_CODE_PATTERN);
        mLlPostalCodeBilling.setValidator(inputValidatorPostalCodeBilling);


        mLlCountryBilling = rootView.findViewById(R.id.ll_billing_country);
        inputValidatorCountryBilling = new InputValidator(Validator.COUNTRY_PATTERN);
        mLlCountryBilling.setValidator(inputValidatorCountryBilling);

        mlLStateBilling = rootView.findViewById(R.id.ll_billing_state);
        InputValidator inputValidatorStateBilling = new InputValidator(Validator.NAME_PATTERN);
        mlLStateBilling.setValidator(inputValidatorStateBilling);

        mLlEmailBilling = rootView.findViewById(R.id.ll_billing_email);
        inputValidatorEmailBilling = new InputValidator(Validator.EMAIL_PATTERN);
        mLlEmailBilling.setValidator(inputValidatorEmailBilling);

        mLlPhone1Billing = rootView.findViewById(R.id.ll_billing_phone1);
        InputValidator inputValidatorPhoneBilling = new InputValidator(Validator.PHONE_NUMBER_PATTERN);
        mLlPhone1Billing.setValidator(inputValidatorPhoneBilling);


        //For Billing address

        mEtFirstNameBilling = rootView.findViewById(R.id.et_billing_first_name);
        mEtLastNameBilling = rootView.findViewById(R.id.et_billing_last_name);
        mEtSalutationBilling = rootView.findViewById(R.id.et_billing_salutation);
        mEtAddressLineOneBilling = rootView.findViewById(R.id.et_billing_address_line_one);
        mEtAddressLineTwoBilling = rootView.findViewById(R.id.et_billing_address_line_two);
        mEtTownBilling = rootView.findViewById(R.id.et_billing_town);
        mEtPostalCodeBilling = rootView.findViewById(R.id.et_billing_postal_code);
        mEtCountryBilling = rootView.findViewById(R.id.et_billing_country);
        mEtStateBilling = rootView.findViewById(R.id.et_billing_state);
        mEtEmailBilling = rootView.findViewById(R.id.et_billing_email);
        mEtPhone1Billing = rootView.findViewById(R.id.et_billing_phone1);


        mEtCountryBilling.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mEtSalutationBilling.setKeyListener(null);
        mEtStateBilling.setKeyListener(null);

        mEtFirstNameBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getGivenName());
        if(HybrisDelegate.getInstance(mContext).getStore().getFamilyName()!=null){
            mEtLastNameBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getFamilyName());
        }else {
            mEtLastNameBilling.setText("");
        }

        mEtEmailBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getJanRainEmail());
        mEtEmailBilling.setEnabled(false);

        mEtCountryBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getCountry());
        mEtCountryBilling.setEnabled(false);
        addressBillingPresenter.showUSRegions(mEtCountryBilling,mlLStateBilling);

        //For billing address fields
        mValidator = new Validator();
        mEtFirstNameBilling.addTextChangedListener(new IAPTextWatcher(mEtFirstNameBilling));
        mEtLastNameBilling.addTextChangedListener(new IAPTextWatcher(mEtLastNameBilling));
        mEtAddressLineOneBilling.addTextChangedListener(new IAPTextWatcher(mEtAddressLineOneBilling));
        mEtAddressLineTwoBilling.addTextChangedListener(new IAPTextWatcherAddress(mEtAddressLineTwoBilling));
        mEtTownBilling.addTextChangedListener(new IAPTextWatcher(mEtTownBilling));
        mEtPostalCodeBilling.addTextChangedListener(new IAPTextWatcher(mEtPostalCodeBilling));
        mEtCountryBilling.addTextChangedListener(new IAPTextWatcher(mEtCountryBilling));
        mEtEmailBilling.addTextChangedListener(new IAPTextWatcher(mEtEmailBilling));
        mEtPhone1Billing.addTextChangedListener(new IAPTextWatcherPhoneBilling(mEtPhone1Billing));
        mEtStateBilling.addTextChangedListener(new IAPTextWatcher(mEtStateBilling));
        mEtSalutationBilling.addTextChangedListener(new IAPTextWatcher(mEtSalutationBilling));


        mEtSalutationBilling.setCompoundDrawables(null, null, Utility.getImageArrow(mContext), null);
        mSalutationDropDownBilling = new SalutationDropDown(mContext, mEtSalutationBilling, this);
        mEtSalutationBilling.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSalutationDropDownBilling.show();
                return false;
            }
        });

        mEtStateBilling.setCompoundDrawables(null, null, Utility.getImageArrow(mContext), null);
        mStateDropDownBilling = new StateDropDown(this);
        mStateDropDownBilling.createPopUp(mEtStateBilling,mContext);
        mEtStateBilling.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideKeypad(mContext);
                mStateDropDownBilling.show();
                return false;
            }
        });
    }

    @Override
    public void onSalutationSelect(View view, String salutation) {
        mEtSalutationBilling.setText(salutation);
        mEtSalutationBilling.setCompoundDrawables(null, null, Utility.getImageArrow(mContext), null);
    }

    @Override
    public void onStateSelect(View view, String state) {
        mEtStateBilling.setText(state);
    }

    @Override
    public void stateRegionCode(String regionCode) {
        mRegionIsoCode = regionCode;
        billingAddressFields.setRegionIsoCode(regionCode);
    }

    public void updateFields(Map<String, String> mAddressFieldsHashmap) {
        if (mAddressFieldsHashmap == null) return;
        addressPresenter.setContinueButtonState(true);
        CartModelContainer.getInstance().setAddressId(mAddressFieldsHashmap.get(ModelConstants.ADDRESS_ID));

        mEtFirstNameBilling.setText(mAddressFieldsHashmap.get(ModelConstants.FIRST_NAME));
        mEtLastNameBilling.setText(mAddressFieldsHashmap.get(ModelConstants.LAST_NAME));
        mEtSalutationBilling.setText(mAddressFieldsHashmap.get(ModelConstants.TITLE_CODE));
        mEtAddressLineOneBilling.setText(addressWithNewLineIfNull(mAddressFieldsHashmap.get(ModelConstants.LINE_1)));
        mEtAddressLineTwoBilling.setText(addressWithNewLineIfNull(mAddressFieldsHashmap.get(ModelConstants.LINE_2)));
        mEtTownBilling.setText(mAddressFieldsHashmap.get(ModelConstants.TOWN));
        mEtPostalCodeBilling.setText(mAddressFieldsHashmap.get(ModelConstants.POSTAL_CODE));
        mEtCountryBilling.setText(mAddressFieldsHashmap.get(ModelConstants.COUNTRY_ISOCODE));
        mEtPhone1Billing.setText(mAddressFieldsHashmap.get(ModelConstants.PHONE_1));
        mEtEmailBilling.setText(mAddressFieldsHashmap.get(ModelConstants.EMAIL_ADDRESS));

        if (mAddressFieldsHashmap.containsKey(ModelConstants.REGION_CODE) &&
                mAddressFieldsHashmap.get(ModelConstants.REGION_CODE) != null) {
            String code = mAddressFieldsHashmap.get(ModelConstants.REGION_CODE);
            String stateCode = code.substring(code.length() - 2);
            mEtStateBilling.setText(stateCode);
            mlLStateBilling.setVisibility(View.VISIBLE);
        } else {
            mlLStateBilling.setVisibility(View.GONE);
        }

        if (mAddressFieldsHashmap.containsKey(ModelConstants.REGION_CODE) &&
                mAddressFieldsHashmap.get(ModelConstants.REGION_CODE) != null) {
            mAddressFieldsHashmap.put(ModelConstants.REGION_ISOCODE,
                    mAddressFieldsHashmap.get(ModelConstants.REGION_CODE));
        }
    }

    private class IAPTextWatcher implements TextWatcher {
        private EditText mEditText;

        IAPTextWatcher(EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //  Do nothing
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText != mEtPhone1Billing) {
                validate(mEditText, false);
            }
        }

        public synchronized void afterTextChanged(Editable text) {
            if (mEditText == mEtPhone1Billing) {
                validate(mEditText, false);
            }
        }
    }

    protected boolean mIgnoreTextChangeListener = false;

    private class IAPTextWatcherPhoneBilling implements TextWatcher {
        private EditText mEditText;
        private boolean isInAfterTextChanged;

        IAPTextWatcherPhoneBilling(EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do Nothing
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText != mEtPhone1Billing && !mIgnoreTextChangeListener) {
                validate(mEditText, false);
            }
        }

        public synchronized void afterTextChanged(Editable text) {
            if (mEditText == mEtPhone1Billing && !isInAfterTextChanged && !mIgnoreTextChangeListener) {
                isInAfterTextChanged = true;
                validate(mEditText, false);
                isInAfterTextChanged = false;
            }
        }
    }

    private class IAPTextWatcherAddress implements TextWatcher {
        private EditText mEditText;

        public IAPTextWatcherAddress(EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // Do Nothing
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (mEditText .getText().toString().trim().length() !=0) {
                mLlAddressLineTwoBilling.setValidator(inputValidatorAddressLineTwoBilling);
                validate(mEditText, false);
            }
            else {
                mLlAddressLineTwoBilling.hideError();
            }

        }

        public synchronized void afterTextChanged(Editable text) {
            if (mEditText .getText().toString().trim().length() ==0) {
                mLlAddressLineTwoBilling.setValidator(null);
            }
        }

    }

    public void validate(View editText, boolean hasFocus) {

        boolean result = true;
        if (editText.getId() == R.id.et_billing_first_name && !hasFocus) {
            result = inputValidatorFirstNameBilling.isValidName(((EditText) editText).getText().toString());
            if (!result) {
                mLlFirstNameBilling.setErrorMessage(R.string.iap_first_name_error);
                mLlFirstNameBilling.showError();
            }
        }
        if (editText.getId() == R.id.et_billing_last_name && !hasFocus) {
            result = inputValidatorLastNameBilling.isValidName(((EditText) editText).getText().toString());
            if (!result) {
                mLlLastNameBilling.setErrorMessage(R.string.iap_last_name_error);
                mLlLastNameBilling.showError();
            }
        }
        if (editText.getId() == R.id.et_billing_town && !hasFocus) {
            result = inputValidatorTownBilling.isValidTown(((EditText) editText).getText().toString());
            if (!result) {
                mLlTownBilling.setErrorMessage(R.string.iap_town_error);
                mLlTownBilling.showError();
            }
        }
        if (editText.getId() == R.id.et_billing_phone1 && !hasFocus) {
            result = addressPresenter.validatePhoneNumber(mEtPhone1Billing, HybrisDelegate.getInstance().getStore().getCountry()
                    , mEtPhone1Billing.getText().toString());
            if (!result) {
                mLlPhone1Billing.setErrorMessage(R.string.iap_phone_error);
                mLlPhone1Billing.showError();
            }
        }
        if (editText.getId() == R.id.et_billing_country && !hasFocus) {
            addressBillingPresenter.showUSRegions(mEtCountryBilling,mlLStateBilling);
            result = inputValidatorCountryBilling.isValidCountry(((EditText) editText).getText().toString());
            if (!result) {
                mLlCountryBilling.setErrorMessage(R.string.iap_country_error);
                mLlCountryBilling.showError();
            }
        }
        if (editText.getId() == R.id.et_billing_postal_code && !hasFocus) {
            result = inputValidatorPostalCodeBilling.isValidPostalCode(((EditText) editText).getText().toString());
            if (!result) {
                mLlPostalCodeBilling.setErrorMessage(R.string.iap_postal_code_error);
                mLlPostalCodeBilling.showError();
            }
        }
        if (editText.getId() == R.id.et_billing_email && !hasFocus) {
            result = inputValidatorEmailBilling.isValidEmail(((EditText) editText).getText().toString());
            if (!result) {
                mLlEmailBilling.setErrorMessage(R.string.iap_email_error);
                mLlEmailBilling.showError();
            }
        }
        if (editText.getId() == R.id.et_billing_address_line_one && !hasFocus) {
            result = inputValidatorAddressLineOneBilling.isValidAddress(((EditText) editText).getText().toString());
            if (!result) {
                mLlAddressLineOneBilling.setErrorMessage(R.string.iap_address_error);
                mLlAddressLineOneBilling.showError();
            }
        }
        if (editText.getId() == R.id.et_billing_address_line_two && !hasFocus) {
            result = inputValidatorAddressLineTwoBilling.isValidAddress(mEtAddressLineTwoBilling.getText().toString());
            if (!result) {
                mLlAddressLineTwoBilling.setErrorMessage(R.string.iap_address_error);
                mLlAddressLineTwoBilling.showError();
            }
        }
        if ((editText.getId() == R.id.et_billing_salutation || editText.getId() == R.id.et_billing_state) && !hasFocus) {
            checkBillingAddressFields();
        }
        if (!result) {
            addressPresenter.setContinueButtonState(false);
        } else {
            checkBillingAddressFields();
        }


    }

    public boolean checkBillingAddressFields() {
        String firstName = mEtFirstNameBilling.getText().toString();
        String lastName = mEtLastNameBilling.getText().toString();
        String addressLineOne = mEtAddressLineOneBilling.getText().toString();
        String addressLineTwo = mEtAddressLineTwoBilling.getText().toString();
        String postalCode = mEtPostalCodeBilling.getText().toString().replaceAll(" ", "");
        String phone1 = mEtPhone1Billing.getText().toString().replaceAll(" ", "");
        String town = mEtTownBilling.getText().toString();
        String country = mEtCountryBilling.getText().toString();
        String email = mEtEmailBilling.getText().toString();


        if (mValidator.isValidName(firstName) && mValidator.isValidName(lastName)
                && mValidator.isValidAddress(addressLineOne)
                && mValidator.isValidPostalCode(postalCode)
                && mValidator.isValidEmail(email) && mValidator.isValidPhoneNumber(phone1)
                && mValidator.isValidTown(town) && mValidator.isValidCountry(country)
                && (!mEtSalutationBilling.getText().toString().trim().equalsIgnoreCase(""))
                && (mlLStateBilling.getVisibility() == View.GONE || (mlLStateBilling.getVisibility() == View.VISIBLE && !mEtStateBilling.getText().toString().trim().equalsIgnoreCase("")))) {

            setBillingAddressFields(billingAddressFields);
            IAPLog.d(IAPLog.LOG, billingAddressFields.toString());
            if (billingAddressFields != null) {
                addressPresenter.setBillingAddressFields(billingAddressFields);
                addressContractor.setBillingAddressFilledStatus(true);
                if(addressContractor.isShippingAddressFilled() || addressContractor.isAddressFilledFromDeliveryAddress()) {
                    addressPresenter.setContinueButtonState(true);
                }
                else
                {
                    addressPresenter.setContinueButtonState(false);
                }
                return true;
            }else {
                addressContractor.setBillingAddressFilledStatus(false);
                addressPresenter.setContinueButtonState(false);
            }
        } else {
            addressContractor.setBillingAddressFilledStatus(false);
            addressPresenter.setContinueButtonState(false);
        }
        return false;
    }

    protected AddressFields setBillingAddressFields(AddressFields billingAddressFields) {
        billingAddressFields.setFirstName(mEtFirstNameBilling.getText().toString().trim());
        billingAddressFields.setLastName(mEtLastNameBilling.getText().toString().trim());
        billingAddressFields.setTitleCode(mEtSalutationBilling.getText().toString().trim());
        billingAddressFields.setCountryIsocode(mEtCountryBilling.getText().toString().trim());
        billingAddressFields.setLine1(mEtAddressLineOneBilling.getText().toString().trim());
        billingAddressFields.setLine2(mEtAddressLineTwoBilling.getText().toString().trim());
        billingAddressFields.setPostalCode(mEtPostalCodeBilling.getText().toString().replaceAll(" ", ""));
        billingAddressFields.setTown(mEtTownBilling.getText().toString().trim());
        billingAddressFields.setPhone1(mEtPhone1Billing.getText().toString().replaceAll(" ", ""));
        billingAddressFields.setPhone2(mEtPhone1Billing.getText().toString().replaceAll(" ", ""));
        billingAddressFields.setEmail(mEtEmailBilling.getText().toString().trim());


        if (mlLStateBilling.getVisibility() == View.VISIBLE) {
            this.billingAddressFields.setRegionIsoCode(mRegionIsoCode);
            this.billingAddressFields.setRegionName(mEtStateBilling.getText().toString());
        } else {
            this.billingAddressFields.setRegionIsoCode(null);
            this.billingAddressFields.setRegionName(null);
        }
        return this.billingAddressFields;
    }

    void clearAllFields() {
        mIgnoreTextChangeListener = true;
        mEtFirstNameBilling.setText("");
        mEtLastNameBilling.setText("");
        mEtSalutationBilling.setText("");
        mEtAddressLineOneBilling.setText("");
        mEtAddressLineTwoBilling.setText("");
        mEtTownBilling.setText("");
        mEtPostalCodeBilling.setText("");
        mEtPhone1Billing.setText("");
        mEtStateBilling.setText("");
        if (HybrisDelegate.getInstance().getStore().getCountry().equalsIgnoreCase("US")) {
            mlLStateBilling.setVisibility(View.VISIBLE);
        } else {
            mlLStateBilling.setVisibility(View.GONE);
        }
        mIgnoreTextChangeListener = false;
    }

    void disableAllFields() {
        setFieldsEnabled(false);
        disableFocus();
    }

    void enableAllFields(){
        setFieldsEnabled(true);
        enableFocus();
    }

    private void enableFocus() {
        setFieldsFocusable(true);
    }

    private void disableFocus() {
        setFieldsFocusable(false);
    }

    private void setFieldsEnabled(boolean enable) {
        mEtFirstNameBilling.setEnabled(enable);
        mEtLastNameBilling.setEnabled(enable);
        mEtSalutationBilling.setEnabled(enable);
        mEtAddressLineOneBilling.setEnabled(enable);
        mEtAddressLineTwoBilling.setEnabled(enable);
        mEtTownBilling.setEnabled(enable);
        mEtPostalCodeBilling.setEnabled(enable);
        if (mlLStateBilling.getVisibility() == View.VISIBLE) {
            mEtStateBilling.setEnabled(enable);
        }
        mEtPhone1Billing.setEnabled(enable);
    }

    private void setFieldsFocusable(boolean focusable) {
        mEtFirstNameBilling.setFocusable(focusable);
        mEtLastNameBilling.setFocusable(focusable);
        mEtSalutationBilling.setFocusable(focusable);
        mEtAddressLineOneBilling.setFocusable(focusable);
        mEtAddressLineTwoBilling.setFocusable(focusable);
        mEtTownBilling.setFocusable(focusable);
        mEtPostalCodeBilling.setFocusable(focusable);
        if (mlLStateBilling.getVisibility() == View.VISIBLE) {
            mEtStateBilling.setFocusable(focusable);
        }
        mEtPhone1Billing.setFocusable(focusable);

        if (focusable) {
            mEtFirstNameBilling.setFocusableInTouchMode(true);
            mEtLastNameBilling.setFocusableInTouchMode(true);
            mEtSalutationBilling.setFocusableInTouchMode(true);
            mEtAddressLineOneBilling.setFocusableInTouchMode(true);
            mEtAddressLineTwoBilling.setFocusableInTouchMode(true);
            mEtTownBilling.setFocusableInTouchMode(true);
            mEtPostalCodeBilling.setFocusableInTouchMode(true);
            if (mlLStateBilling.getVisibility() == View.VISIBLE) {
                mEtStateBilling.setFocusableInTouchMode(true);
            }
            mEtPhone1Billing.setFocusableInTouchMode(true);
        }

        mEtCountryBilling.setFocusable(false);
        mEtCountryBilling.setFocusableInTouchMode(false);
        mEtEmailBilling.setFocusableInTouchMode(false);
        mEtEmailBilling.setFocusable(false);
        mEtEmailBilling.setEnabled(false);
        mEtCountryBilling.setEnabled(false);
    }

    void prePopulateShippingAddress() {
        mIgnoreTextChangeListener = true;

        if (billingAddressFields != null) {
            mEtFirstNameBilling.setText(billingAddressFields.getFirstName());
            mEtLastNameBilling.setText(billingAddressFields.getLastName());
            mEtSalutationBilling.setText(billingAddressFields.getTitleCode());
            mEtAddressLineOneBilling.setText(addressWithNewLineIfNull(billingAddressFields.getLine1()));
            mEtAddressLineTwoBilling.setText(addressWithNewLineIfNull(billingAddressFields.getLine2()));
            mEtTownBilling.setText(billingAddressFields.getTown());
            mEtPostalCodeBilling.setText(billingAddressFields.getPostalCode());
            mEtCountryBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getCountry());
            mEtEmailBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getJanRainEmail());

            if (HybrisDelegate.getInstance().getStore().getCountry().equalsIgnoreCase("US") &&
                    billingAddressFields.getRegionName() != null) {
                mEtStateBilling.setText(billingAddressFields.getRegionName());
                mlLStateBilling.setVisibility(View.VISIBLE);
            } else {
                mlLStateBilling.setVisibility(View.GONE);
            }
            mIgnoreTextChangeListener = false;
            mEtPhone1Billing.setText(billingAddressFields.getPhone1());
        }
    }

    private String addressWithNewLineIfNull( String code) {
        if (!TextUtils.isEmpty(code)) {
                return code.replaceAll("[,null]", " ");
        }
        return null;
    }
    
}
