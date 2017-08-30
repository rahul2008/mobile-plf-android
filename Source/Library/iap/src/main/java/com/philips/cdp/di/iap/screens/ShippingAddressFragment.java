/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.error.Error;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.InputValidator;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.SalutationDropDown;
import com.philips.cdp.di.iap.view.StateDropDown;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.platform.uid.view.widget.CheckBox;
import com.philips.platform.uid.view.widget.InputValidationLayout;
import com.philips.platform.uid.view.widget.ValidationEditText;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public class ShippingAddressFragment extends InAppBaseFragment
        implements View.OnClickListener, AddressController.AddressListener,
        PaymentController.PaymentListener,
        AdapterView.OnItemSelectedListener, SalutationDropDown.SalutationListener,
        StateDropDown.StateListener, View.OnFocusChangeListener {

    private Context mContext;
    public static final String TAG = ShippingAddressFragment.class.getName();

    protected InputValidationLayout mLlFirstName;
    protected InputValidationLayout mLlLastName;
    protected InputValidationLayout mLlSalutation;
    protected InputValidationLayout mLlAddressLineOne;
    protected InputValidationLayout mLlAddressLineTwo;
    protected InputValidationLayout mLlTown;
    protected InputValidationLayout mLlPostalCode;
    protected InputValidationLayout mLlCountry;
    protected InputValidationLayout mlLState;
    protected InputValidationLayout mLlEmail;
    protected InputValidationLayout mLlPhone1;

    protected CheckBox mUseThisAddressCheckBox;
    LinearLayout mSameAsShippingAddress;

    protected Button mBtnContinue;
    protected Button mBtnCancel;

    private PaymentController mPaymentController;
    private AddressController mAddressController;
    private AddressFields mShippingAddressFields;

    //'protected InlineForms mInlineFormsParent;


    private SalutationDropDown mSalutationDropDown;
    private StateDropDown mStateDropDown;

    private HashMap<String, String> mAddressFieldsHashmap = null;
    private HashMap<String, String> addressHashMap = new HashMap<>();
    private Drawable imageArrow;
    protected boolean mIgnoreTextChangeListener = false;

    private String mRegionIsoCode = null;

    PhoneNumberUtil phoneNumberUtil;
    Phonenumber.PhoneNumber phoneNumber = null;
    private InputValidator inputValidatorFirstName;
    private InputValidator inputValidatorLastName;
    private InputValidator inputValidatorPhone;
    private InputValidator inputValidatorEmail;
    private InputValidator inputValidatorState;
    private InputValidator inputValidatorCountry;
    private InputValidator inputValidatorPostalCode;
    private InputValidator inputValidatorTown;
    private InputValidator inputValidatorAddressLineOne;
    private InputValidator inputValidatorSalutation;
    private InputValidator inputValidatorAddressLineTwo;

    private InputValidationLayout mLlFirstNameBilling;
    private InputValidator inputValidatorFirstNameBilling;
    private InputValidationLayout mLlLastNameBilling;
    private InputValidator inputValidatorLastNameBilling;
    private InputValidationLayout mLlSalutationBilling;
    private InputValidator inputValidatorSalutationBilling;
    private InputValidationLayout mLlAddressLineOneBilling;
    private InputValidator inputValidatorAddressLineOneBilling;
    private InputValidationLayout mLlAddressLineTwoBilling;
    private InputValidator inputValidatorAddressLineTwoBilling;
    private InputValidationLayout mLlTownBilling;
    private InputValidator inputValidatorTownBilling;
    private InputValidationLayout mLlPostalCodeBilling;
    private InputValidator inputValidatorPostalCodeBilling;
    private InputValidationLayout mLlCountryBilling;
    private InputValidator inputValidatorCountryBilling;
    private InputValidationLayout mlLStateBilling;
    private InputValidator inputValidatorStateBilling;
    private InputValidationLayout mLlEmailBilling;
    private InputValidator inputValidatorEmailBilling;
    private InputValidationLayout mLlPhone1Billing;
    private InputValidator inputValidatorPhoneBilling;

    protected ValidationEditText mEtFirstName;
    protected ValidationEditText mEtLastName;
    protected ValidationEditText mEtSalutation;
    protected ValidationEditText mEtAddressLineOne;
    protected ValidationEditText mEtAddressLineTwo;
    protected ValidationEditText mEtTown;
    protected ValidationEditText mEtPostalCode;
    protected ValidationEditText mEtCountry;
    protected ValidationEditText mEtState;
    protected ValidationEditText mEtEmail;
    protected ValidationEditText mEtPhone1;

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

    //Billing Address
    private AddressFields mBillingAddressFields;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_shipping_billing_address_layout, container, false);


        TextView tv_checkOutSteps = (TextView) rootView.findViewById(R.id.tv_checkOutSteps);
        tv_checkOutSteps.setText(String.format(mContext.getString(R.string.iap_checkout_steps), "2"));

        phoneNumberUtil = PhoneNumberUtil.getInstance();

        mUseThisAddressCheckBox = (CheckBox) rootView.findViewById(R.id.use_this_address_checkbox);
        mSameAsShippingAddress = (LinearLayout) rootView.findViewById(R.id.iap_same_as_shipping_address);
        mUseThisAddressCheckBox.setChecked(true);


        mLlFirstName = (InputValidationLayout) rootView.findViewById(R.id.ll_first_name);
        inputValidatorFirstName = getValidator(Validator.NAME_PATTERN);
        mLlFirstName.setValidator(inputValidatorFirstName);

        mLlFirstNameBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_first_name);
        inputValidatorFirstNameBilling = getValidator(Validator.NAME_PATTERN);
        mLlFirstNameBilling.setValidator(inputValidatorFirstNameBilling);


        mLlLastName = (InputValidationLayout) rootView.findViewById(R.id.ll_last_name);
        inputValidatorLastName = getValidator(Validator.NAME_PATTERN);
        mLlLastName.setValidator(inputValidatorLastName);


        mLlLastNameBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_last_name);
        inputValidatorLastNameBilling = getValidator(Validator.NAME_PATTERN);
        mLlLastNameBilling.setValidator(inputValidatorLastNameBilling);


        mLlSalutation = (InputValidationLayout) rootView.findViewById(R.id.ll_salutation);
        inputValidatorSalutation = getValidator(Validator.ADDRESS_PATTERN);
        mLlSalutation.setValidator(inputValidatorSalutation);

        mLlSalutationBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_salutation);
        inputValidatorSalutationBilling = getValidator(Validator.ADDRESS_PATTERN);
        mLlSalutationBilling.setValidator(inputValidatorSalutationBilling);

        mLlAddressLineOne = (InputValidationLayout) rootView.findViewById(R.id.ll_address_line_one);
        inputValidatorAddressLineOne = getValidator(Validator.ADDRESS_PATTERN);
        mLlAddressLineOne.setValidator(inputValidatorAddressLineOne);

        mLlAddressLineOneBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_address_line_one);
        inputValidatorAddressLineOneBilling = getValidator(Validator.ADDRESS_PATTERN);
        mLlAddressLineOneBilling.setValidator(inputValidatorAddressLineOneBilling);


        mLlAddressLineTwo = (InputValidationLayout) rootView.findViewById(R.id.ll_address_line_two);
        inputValidatorAddressLineTwo = getValidator(Validator.ADDRESS_PATTERN);
        mLlAddressLineTwo.setValidator(inputValidatorAddressLineTwo);

        mLlAddressLineTwoBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_address_line_two);
        inputValidatorAddressLineTwoBilling = getValidator(Validator.ADDRESS_PATTERN);
        mLlAddressLineTwoBilling.setValidator(inputValidatorAddressLineTwoBilling);

        mLlTown = (InputValidationLayout) rootView.findViewById(R.id.ll_town);
        inputValidatorTown = getValidator(Validator.TOWN_PATTERN);
        mLlTown.setValidator(inputValidatorTown);

        mLlTownBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_town);
        inputValidatorTownBilling = getValidator(Validator.TOWN_PATTERN);
        mLlTownBilling.setValidator(inputValidatorTownBilling);


        mLlPostalCode = (InputValidationLayout) rootView.findViewById(R.id.ll_postal_code);
        inputValidatorPostalCode = getValidator(Validator.POSTAL_CODE_PATTERN);
        mLlPostalCode.setValidator(inputValidatorPostalCode);

        mLlPostalCodeBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_postal_code);
        inputValidatorPostalCodeBilling = getValidator(Validator.POSTAL_CODE_PATTERN);
        mLlPostalCodeBilling.setValidator(inputValidatorPostalCodeBilling);


        mLlCountry = (InputValidationLayout) rootView.findViewById(R.id.ll_country);
        inputValidatorCountry = getValidator(Validator.COUNTRY_PATTERN);
        mLlCountry.setValidator(inputValidatorCountry);

        mLlCountryBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_country);
        inputValidatorCountryBilling = getValidator(Validator.COUNTRY_PATTERN);
        mLlCountryBilling.setValidator(inputValidatorCountryBilling);


        mlLState = (InputValidationLayout) rootView.findViewById(R.id.ll_state);
        inputValidatorState = getValidator(Validator.NAME_PATTERN);
        mlLState.setValidator(inputValidatorState);

        mlLStateBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_state);
        inputValidatorStateBilling = getValidator(Validator.NAME_PATTERN);
        mlLStateBilling.setValidator(inputValidatorStateBilling);


        mLlEmail = (InputValidationLayout) rootView.findViewById(R.id.ll_email);
        inputValidatorEmail = getValidator(Validator.EMAIL_PATTERN);
        mLlEmail.setValidator(inputValidatorEmail);

        mLlEmailBilling = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_email);
        inputValidatorEmailBilling = getValidator(Validator.EMAIL_PATTERN);
        mLlEmailBilling.setValidator(inputValidatorEmailBilling);

        mLlPhone1 = (InputValidationLayout) rootView.findViewById(R.id.ll_phone1);
        inputValidatorPhone = getValidator(Validator.PHONE_NUMBER_PATTERN);
        mLlPhone1.setValidator(inputValidatorPhone);

        mLlPhone1Billing = (InputValidationLayout) rootView.findViewById(R.id.ll_billing_phone1);
        inputValidatorPhoneBilling = getValidator(Validator.PHONE_NUMBER_PATTERN);
        mLlPhone1Billing.setValidator(inputValidatorPhoneBilling);

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

        //For Billing address

        mEtFirstNameBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_first_name);
        mEtLastNameBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_last_name);
        mEtSalutationBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_salutation);
        mEtAddressLineOneBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_address_line_one);
        mEtAddressLineTwoBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_address_line_two);
        mEtTownBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_town);
        mEtPostalCodeBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_postal_code);
        mEtCountryBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_country);
        mEtStateBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_state);
        mEtEmailBilling = (ValidationEditText) rootView.findViewById(R.id.et_billing_email);
        mEtPhone1Billing = (ValidationEditText) rootView.findViewById(R.id.et_billing_phone1);

        mEtPostalCode.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mEtSalutation.setKeyListener(null);
        mEtState.setKeyListener(null);


        mEtCountryBilling.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        mEtSalutationBilling.setKeyListener(null);
        mEtStateBilling.setKeyListener(null);

        mBtnContinue = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);

        mBtnContinue.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mAddressController = new AddressController(mContext, this);
        mPaymentController = new PaymentController(mContext, this);
        mShippingAddressFields = new AddressFields();

        mEtEmail.setText(HybrisDelegate.getInstance(mContext).getStore().getJanRainEmail());
        mEtEmail.setEnabled(false);
        mEtEmailBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getJanRainEmail());
        mEtEmailBilling.setEnabled(false);
        mEtCountry.setText(HybrisDelegate.getInstance(mContext).getStore().getCountry());
        mEtCountryBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getCountry());
        showUSRegions();
        mEtCountry.setEnabled(false);
        mEtCountryBilling.setEnabled(false);

        mValidator = new Validator();
        mEtFirstName.addTextChangedListener(new IAPTextWatcher(mEtFirstName));
        mEtLastName.addTextChangedListener(new IAPTextWatcher(mEtLastName));
        mEtAddressLineOne.addTextChangedListener(new IAPTextWatcher(mEtAddressLineOne));
        mEtAddressLineTwo.addTextChangedListener(new IAPTextWatcher(mEtAddressLineTwo));
        mEtTown.addTextChangedListener(new IAPTextWatcher(mEtTown));
        mEtPostalCode.addTextChangedListener(new IAPTextWatcher(mEtPostalCode));
        mEtCountry.addTextChangedListener(new IAPTextWatcher(mEtCountry));
        mEtEmail.addTextChangedListener(new IAPTextWatcher(mEtEmail));
        mEtPhone1.addTextChangedListener(new IAPTextWatcher(mEtPhone1));
        mEtState.addTextChangedListener(new IAPTextWatcher(mEtState));
        mEtSalutation.addTextChangedListener(new IAPTextWatcher(mEtSalutation));

        Bundle bundle = getArguments();
        if (null != bundle && bundle.containsKey(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY)) {
            updateFields();
        }

        setImageArrow();
        mEtSalutation.setCompoundDrawables(null, null, imageArrow, null);
        mSalutationDropDown = new SalutationDropDown(mContext, mEtSalutation, this);

        mEtSalutation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSalutationDropDown.show();
                return false;
            }
        });


        mEtState.setCompoundDrawables(null, null, imageArrow, null);
        mStateDropDown = new StateDropDown(mContext, mEtState, this);

        mEtState.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideKeypad(mContext);
                mStateDropDown.show();
                return false;
            }
        });


        //For billing address fields
        mEtFirstNameBilling.addTextChangedListener(new IAPTextWatcher(mEtFirstNameBilling));
        mEtLastNameBilling.addTextChangedListener(new IAPTextWatcher(mEtLastNameBilling));
        mEtAddressLineOneBilling.addTextChangedListener(new IAPTextWatcher(mEtAddressLineOneBilling));
        mEtAddressLineTwoBilling.addTextChangedListener(new IAPTextWatcher(mEtAddressLineTwoBilling));
        mEtTownBilling.addTextChangedListener(new IAPTextWatcher(mEtTownBilling));
        mEtPostalCodeBilling.addTextChangedListener(new IAPTextWatcher(mEtPostalCodeBilling));
        mEtCountryBilling.addTextChangedListener(new IAPTextWatcher(mEtCountryBilling));
        mEtEmailBilling.addTextChangedListener(new IAPTextWatcher(mEtEmailBilling));
        mEtPhone1Billing.addTextChangedListener(new IAPTextWatcherPhoneBilling(mEtPhone1Billing));
        mEtStateBilling.addTextChangedListener(new IAPTextWatcher(mEtStateBilling));
        mEtSalutationBilling.addTextChangedListener(new IAPTextWatcher(mEtSalutationBilling));


        mEtSalutationBilling.setCompoundDrawables(null, null, imageArrow, null);
        mSalutationDropDownBilling = new SalutationDropDown(mContext, mEtSalutationBilling, this);
        mEtSalutationBilling.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSalutationDropDownBilling.show();
                return false;
            }
        });

        mEtStateBilling.setCompoundDrawables(null, null, imageArrow, null);
        mStateDropDownBilling = new StateDropDown(mContext, mEtStateBilling, this);
        mEtStateBilling.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Utility.hideKeypad(mContext);
                mStateDropDownBilling.show();
                return false;
            }
        });


        mUseThisAddressCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    disableAllFields();
                    prePopulateBillingAddress();
                    mBtnContinue.setEnabled(true);
                    // mSameAsShippingAddress.setVisibility(View.GONE);

                } else {
                    clearAllBillingFields();
                    mBtnContinue.setEnabled(false);
                    //  mSameAsShippingAddress.setVisibility(View.VISIBLE);
                }
            }
        });

        if (bundle != null && bundle.getBoolean(IAPConstant.EMPTY_BILLING_ADDRESS)) {
            mUseThisAddressCheckBox.setChecked(false);
        }
        if (!mUseThisAddressCheckBox.isChecked() && mBillingAddressFields == null) {
            mBtnContinue.setEnabled(false);
        }

        if (bundle != null && bundle.getBoolean(IAPConstant.ADD_BILLING_ADDRESS)) {
            mBillingAddressFields = CartModelContainer.getInstance().getShippingAddressFields();
            prePopulateBillingAddress();
            mSameAsShippingAddress.setVisibility(View.VISIBLE);
            mBtnContinue.setText(mContext.getString(R.string.iap_continue));
            rootView.findViewById(R.id.layout_iap_shipping_address).setVisibility(View.GONE);
            rootView.findViewById(R.id.tv_shipping_address).setVisibility(View.GONE);
        }

        return rootView;
    }

    private void clearAllBillingFields() {
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
        enableAllFields();
        enableFocus();
        //removeErrorInAllFields();
    }

    private void enableAllFields() {
        setFieldsEnabled(true);
    }

    private void disableAllFields() {
        // removeErrorInAllFields();
        setFieldsEnabled(false);
        disableFocus();
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
//        mEtEmailBilling.setFocusableInTouchMode(false);
//        mEtEmailBilling.setFocusable(false);
//        mEtEmailBilling.setEnabled(false);
        mEtCountryBilling.setEnabled(false);
    }

    private void prePopulateBillingAddress() {
        mIgnoreTextChangeListener = true;

        if (mBillingAddressFields != null) {
            mEtFirstNameBilling.setText(mBillingAddressFields.getFirstName());
            mEtLastNameBilling.setText(mBillingAddressFields.getLastName());
            mEtSalutationBilling.setText(mBillingAddressFields.getTitleCode());
            mEtAddressLineOneBilling.setText(mBillingAddressFields.getLine1());
            mEtAddressLineTwoBilling.setText(mBillingAddressFields.getLine2());
            mEtTownBilling.setText(mBillingAddressFields.getTown());
            mEtPostalCodeBilling.setText(mBillingAddressFields.getPostalCode());
            mEtCountryBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getCountry());
            mEtEmailBilling.setText(HybrisDelegate.getInstance(mContext).getStore().getJanRainEmail());

            if (HybrisDelegate.getInstance().getStore().getCountry().equalsIgnoreCase("US") &&
                    mBillingAddressFields.getRegionName() != null) {
                mEtStateBilling.setText(mBillingAddressFields.getRegionName());
                mlLStateBilling.setVisibility(View.VISIBLE);
            } else {
                mlLStateBilling.setVisibility(View.GONE);
            }
            mIgnoreTextChangeListener = false;
            mEtPhone1Billing.setText(mBillingAddressFields.getPhone1());
        }
    }

    private void setImageArrow() {
        imageArrow = VectorDrawable.create(mContext, R.drawable.iap_product_count_drop_down);
        int width = (int) getResources().getDimension(R.dimen.iap_count_drop_down_icon_width);
        int height = (int) getResources().getDimension(R.dimen.iap_count_drop_down_icon_height);
        imageArrow.setBounds(0, 0, width, height);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onGetPaymentDetails(Message msg) {
        dismissProgressDialog();
        if (mlLState.getVisibility() == View.VISIBLE) {
            mShippingAddressFields.setRegionIsoCode(CartModelContainer.getInstance().getRegionIsoCode());
            mShippingAddressFields.setRegionName(mEtState.getText().toString());
        }

        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
            //Track new address creation
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.NEW_SHIPPING_ADDRESS_ADDED);
            CartModelContainer.getInstance().setShippingAddressFields(mShippingAddressFields);

            //Prepopulate billing address with checked check box
            mBillingAddressFields = setAddressFields(mBillingAddressFields.clone());
            CartModelContainer.getInstance().setBillingAddress(mBillingAddressFields);
            prePopulateBillingAddress();
            mBtnContinue.setEnabled(true);
            mSameAsShippingAddress.setVisibility(View.GONE);
//            addFragment(
//                    BillingAddressFragment.createInstance(new Bundle(), AnimationType.NONE), BillingAddressFragment.TAG);
        } else if ((msg.obj instanceof IAPNetworkError)) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        } else if ((msg.obj instanceof PaymentMethods)) {
            //Track new address creation
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.NEW_SHIPPING_ADDRESS_ADDED);
            PaymentMethods mPaymentMethods = (PaymentMethods) msg.obj;
            List<PaymentMethod> mPaymentMethodsList = mPaymentMethods.getPayments();
            CartModelContainer.getInstance().setShippingAddressFields(mShippingAddressFields);
            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.PAYMENT_METHOD_LIST, (Serializable) mPaymentMethodsList);
            addFragment(
                    PaymentSelectionFragment.createInstance(bundle, AnimationType.NONE), PaymentSelectionFragment.TAG);
        }
    }

    @Override
    public void onClick(final View v) {
        Utility.hideKeypad(mContext);
        if (!isNetworkConnected()) return;
        if (v == mBtnContinue) {

            //  Edit and save address
            if (mBtnContinue.getText().toString().equalsIgnoreCase(getString(R.string.iap_save))) {
                if (!isProgressDialogShowing()) {
                    showProgressDialog(mContext, getString(R.string.iap_please_wait));
                    HashMap<String, String> addressHashMap = addressPayload();
                    mAddressController.updateAddress(addressHashMap);
                }
            } else {//Add new address
                if (!isProgressDialogShowing()) {
                    showProgressDialog(mContext, getString(R.string.iap_please_wait));
                    if (mlLState.getVisibility() == View.GONE)
                        mShippingAddressFields.setRegionIsoCode(null);
                    if (CartModelContainer.getInstance().getAddressId() != null) {
                        HashMap<String, String> updateAddressPayload = addressPayload();
                        if (mlLState.getVisibility() == View.VISIBLE && CartModelContainer.getInstance().getRegionIsoCode() != null)
                            updateAddressPayload.put(ModelConstants.REGION_ISOCODE, CartModelContainer.getInstance().getRegionIsoCode());
                        updateAddressPayload.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
                        mAddressController.updateAddress(updateAddressPayload);
                    } else {
                        mAddressController.createAddress(mShippingAddressFields);
                    }
                }
            }
        } else if (v == mBtnCancel) {
            Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
            if (fragment != null) {
                moveToVerticalAppByClearingStack();
            } else {
                getFragmentManager().popBackStackImmediate();
            }
        }
    }

    @Override
    public void onGetAddress(Message msg) {
        if (msg.what == RequestCode.UPDATE_ADDRESS) {
            if (msg.obj instanceof IAPNetworkError) {
                dismissProgressDialog();
                handleError(msg);
            } else {
                if (CartModelContainer.getInstance().getAddressId() == null) {
                    dismissProgressDialog();
                    getFragmentManager().popBackStackImmediate();
                } else {
                    mAddressController.setDeliveryAddress(CartModelContainer.getInstance().getAddressId());
                }
            }
        }
    }

    @Override
    public void onCreateAddress(Message msg) {
        if (msg.obj instanceof Addresses) {
            mBtnContinue.setEnabled(true);
            Addresses mAddresses = (Addresses) msg.obj;
            CartModelContainer.getInstance().setAddressId(mAddresses.getId());
            mAddressController.setDeliveryAddress(mAddresses.getId());
        } else if (msg.obj instanceof IAPNetworkError) {
            dismissProgressDialog();
            handleError(msg);
        }
    }

    private void showErrorFromServer(Error error) {

        if (error != null) {
            if (error.getSubject() != null) {
                if (error.getSubject().equalsIgnoreCase(ModelConstants.COUNTRY_ISOCODE)) {
                    mLlCountryBilling.setValidator(inputValidatorCountryBilling);
                    mLlCountryBilling.setErrorMessage(R.string.iap_country_error);
                    mLlCountryBilling.showError();
                } else if (error.getSubject().equalsIgnoreCase(ModelConstants.POSTAL_CODE)) {
                    mLlPostalCode.setValidator(inputValidatorPostalCode);
                    mLlPostalCode.setErrorMessage(R.string.iap_postal_code_error);
                    mLlPostalCode.showError();
                } else if (error.getSubject().equalsIgnoreCase(ModelConstants.PHONE_1)) {
                    mLlPhone1.setValidator(inputValidatorPhone);
                    mLlPhone1.setErrorMessage(R.string.iap_phone_error);
                    mLlPhone1.showError();
                } else if (error.getSubject().equalsIgnoreCase(ModelConstants.LINE_1)) {
                    mLlAddressLineOne.setValidator(inputValidatorAddressLineOne);
                    mLlAddressLineOne.setErrorMessage(R.string.iap_address_error);
                    mLlAddressLineOne.showError();
                } else if (error.getSubject().equalsIgnoreCase(ModelConstants.LINE_2)) {
                    mLlAddressLineTwo.setValidator(inputValidatorAddressLineTwo);
                    mLlAddressLineTwo.setErrorMessage(R.string.iap_address_error);
                    mLlAddressLineTwo.showError();
                }
                NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(),
                        getString(R.string.iap_ok), getString(R.string.iap_server_error),
                        error.getMessage());
                mBtnContinue.setEnabled(false);
            }
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

        if (mValidator.isValidName(firstName) && mValidator.isValidName(lastName)
                && mValidator.isValidAddress(addressLineOne) && (addressLineTwo.trim().equals("") || mValidator.isValidAddress(addressLineTwo))
                && mValidator.isValidPostalCode(postalCode)
                && mValidator.isValidEmail(email) && mValidator.isValidPhoneNumber(phone1)
                && mValidator.isValidTown(town) && mValidator.isValidCountry(country)
                && (!mEtSalutation.getText().toString().trim().equalsIgnoreCase(""))
                && (mlLState.getVisibility() == View.GONE || (mlLState.getVisibility() == View.VISIBLE && !mEtState.getText().toString().trim().equalsIgnoreCase("")))) {

            mShippingAddressFields = setAddressFields(mShippingAddressFields);

            if (mUseThisAddressCheckBox.isChecked()) {
                mBtnContinue.setEnabled(true);
            } else {
                mBtnContinue.setEnabled(false);
            }
        } else {
            mBtnContinue.setEnabled(false);
        }
    }

    public void checkBillingAddressFields() {
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
                && mValidator.isValidAddress(addressLineOne) && (addressLineTwo.trim().equals("") || mValidator.isValidAddress(addressLineTwo))
                && mValidator.isValidPostalCode(postalCode)
                && mValidator.isValidEmail(email) && mValidator.isValidPhoneNumber(phone1)
                && mValidator.isValidTown(town) && mValidator.isValidCountry(country)
                && (!mEtSalutationBilling.getText().toString().trim().equalsIgnoreCase(""))
                && (mlLStateBilling.getVisibility() == View.GONE || (mlLStateBilling.getVisibility() == View.VISIBLE && !mEtStateBilling.getText().toString().trim().equalsIgnoreCase("")))) {

            mBillingAddressFields = setAddressFields(mBillingAddressFields);

            mBtnContinue.setEnabled(true);
        } else {
            mBtnContinue.setEnabled(false);
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

        //================For billing Address
        if (!mUseThisAddressCheckBox.isChecked()) {
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
                result = validatePhoneNumber(mEtPhone1Billing, HybrisDelegate.getInstance().getStore().getCountry()
                        , mEtPhone1Billing.getText().toString());
                if (!result) {
                    mLlPhone1Billing.setErrorMessage(R.string.iap_phone_error);
                    mLlPhone1Billing.showError();
                }
            }
            if (editText.getId() == R.id.et_billing_country && !hasFocus) {
                showUSRegions();
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
            if (editText.getId() == R.id.et_billing_address_line_two) {
                result = inputValidatorAddressLineTwoBilling.isValidAddress(((EditText) editText).getText().toString());
                if (mEtAddressLineTwoBilling.getText().toString().trim().equals("")) {
                    result = true;
                } else {
                    if (!result) {
                        mLlAddressLineTwoBilling.setErrorMessage(R.string.iap_address_error);
                        mLlAddressLineTwoBilling.showError();
                    }
                }

            }
            if ((editText.getId() == R.id.et_billing_salutation || editText.getId() == R.id.et_billing_state) && !hasFocus) {
                checkBillingAddressFields();
            }
        }
        //===================

        if (!result) {
            mBtnContinue.setEnabled(false);
        }
//        else {
//            mBtnContinue.setEnabled(true);
//        }

    }

    @Override
    public void onStop() {
        super.onStop();
        Utility.hideKeypad(mContext);
    }

    private void showUSRegions() {
        if (mEtCountry.getText().toString().equals("US")) {
            mlLState.setVisibility(View.VISIBLE);
        } else {
            mlLState.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility(R.string.iap_address, true);

        if (mUseThisAddressCheckBox.isChecked()) {
            mBillingAddressFields = mShippingAddressFields;
            disableAllFields();
            mBtnContinue.setEnabled(true);
        }

        //if (!(this instanceof BillingAddressFragment)) {
        if (!mUseThisAddressCheckBox.isChecked()) {
            if (getArguments() != null &&
                    getArguments().containsKey(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY)) {
                IAPAnalytics.trackPage(IAPAnalyticsConstant.SHIPPING_ADDRESS_EDIT_PAGE_NAME);
            } else {
                IAPAnalytics.trackPage(IAPAnalyticsConstant.SHIPPING_ADDRESS_PAGE_NAME);
            }
            if (CartModelContainer.getInstance().getRegionIsoCode() != null)
                mShippingAddressFields.setRegionIsoCode(CartModelContainer.getInstance().getRegionIsoCode());
        }
    }


    public static ShippingAddressFragment createInstance(Bundle args, AnimationType animType) {
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSetDeliveryAddress(final Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            Bundle bundle = getArguments();
            DeliveryModes deliveryMode = bundle.getParcelable(IAPConstant.SET_DELIVERY_MODE);
            if (deliveryMode == null)
                mAddressController.getDeliveryModes();
            else
                mPaymentController.getPaymentDetails();
        } else {
            dismissProgressDialog();
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        }
    }

    @Override
    public void onSetDeliveryMode(final Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            mPaymentController.getPaymentDetails();
        } else {
            dismissProgressDialog();
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        }
    }

    @Override
    public void onGetRegions(Message msg) {

    }

    @Override
    public void onGetUser(Message msg) {

    }

    @Override
    public void onGetDeliveryModes(Message message) {
        handleDeliveryMode(message, mAddressController);
    }

    @Override
    public void onSalutationSelect(View editText, String salutation) {
        if (editText.getId() == R.id.et_salutation) {
            mEtSalutation.setText(salutation);
            mEtSalutation.setCompoundDrawables(null, null, imageArrow, null);
        } else {
            mEtSalutationBilling.setText(salutation);
            mEtSalutationBilling.setCompoundDrawables(null, null, imageArrow, null);
        }

    }

    @Override
    public void onStateSelect(View editText, String state) {
        if (editText.getId() == R.id.et_state) {
            mEtState.setText(state);
        } else {
            mEtStateBilling.setText(state);
        }

    }

    @Override
    public void stateRegionCode(String regionCode) {
        mRegionIsoCode = regionCode;
        mShippingAddressFields.setRegionIsoCode(regionCode);
        if (addressHashMap != null) {
            addressHashMap.put(ModelConstants.REGION_ISOCODE, regionCode);
        }

//        if (!(this instanceof BillingAddressFragment)) {
        if (!mUseThisAddressCheckBox.isChecked()) {
            CartModelContainer.getInstance().setRegionIsoCode(regionCode);
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

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

    private class IAPTextWatcherPhoneBilling implements TextWatcher {
        private EditText mEditText;
        private boolean isInAfterTextChanged;

        public IAPTextWatcherPhoneBilling(EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
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


    private boolean validatePhoneNumber(EditText editText, String country, String number) {
        try {
            phoneNumber = phoneNumberUtil.parse(number, country);
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

    private HashMap<String, String> addressPayload() {
        addressHashMap.put(ModelConstants.FIRST_NAME, mEtFirstName.getText().toString());
        addressHashMap.put(ModelConstants.LAST_NAME, mEtLastName.getText().toString());
        addressHashMap.put(ModelConstants.LINE_1, mEtAddressLineOne.getText().toString());
        addressHashMap.put(ModelConstants.LINE_2, mEtAddressLineTwo.getText().toString());
        addressHashMap.put(ModelConstants.TITLE_CODE, mEtSalutation.getText().toString().toLowerCase(Locale.getDefault()));
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, mEtCountry.getText().toString());
        addressHashMap.put(ModelConstants.POSTAL_CODE, mEtPostalCode.getText().toString().replaceAll(" ", ""));
        addressHashMap.put(ModelConstants.TOWN, mEtTown.getText().toString());
        if (mAddressFieldsHashmap != null)
            addressHashMap.put(ModelConstants.ADDRESS_ID, mAddressFieldsHashmap.get(ModelConstants.ADDRESS_ID));
        addressHashMap.put(ModelConstants.PHONE_1, mEtPhone1.getText().toString().replaceAll(" ", ""));
        addressHashMap.put(ModelConstants.PHONE_2, mEtPhone1.getText().toString().replaceAll(" ", ""));
        addressHashMap.put(ModelConstants.EMAIL_ADDRESS, mEtEmail.getText().toString());

        if (mlLState.getVisibility() == View.GONE) {
            addressHashMap.put(ModelConstants.REGION_ISOCODE, null);
        }

        return addressHashMap;
    }

    private void updateFields() {

        mUseThisAddressCheckBox.setVisibility(View.GONE);
        Bundle bundle = getArguments();
        mAddressFieldsHashmap = (HashMap<String, String>) bundle.getSerializable(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY);
        if (null == mAddressFieldsHashmap) {
            return;
        }
        mBtnContinue.setText(getString(R.string.iap_save));

        mEtFirstName.setText(mAddressFieldsHashmap.get(ModelConstants.FIRST_NAME));
        mEtLastName.setText(mAddressFieldsHashmap.get(ModelConstants.LAST_NAME));
        mEtSalutation.setText(mAddressFieldsHashmap.get(ModelConstants.TITLE_CODE));
        mEtAddressLineOne.setText(mAddressFieldsHashmap.get(ModelConstants.LINE_1));
        mEtAddressLineTwo.setText(mAddressFieldsHashmap.get(ModelConstants.LINE_2));
        mEtTown.setText(mAddressFieldsHashmap.get(ModelConstants.TOWN));
        mEtPostalCode.setText(mAddressFieldsHashmap.get(ModelConstants.POSTAL_CODE));
        mEtCountry.setText(mAddressFieldsHashmap.get(ModelConstants.COUNTRY_ISOCODE));
        mEtPhone1.setText(mAddressFieldsHashmap.get(ModelConstants.PHONE_1));
        mEtEmail.setText(mAddressFieldsHashmap.get(ModelConstants.EMAIL_ADDRESS));

        if (mAddressFieldsHashmap.containsKey(ModelConstants.REGION_CODE) &&
                mAddressFieldsHashmap.get(ModelConstants.REGION_CODE) != null) {
            String code = mAddressFieldsHashmap.get(ModelConstants.REGION_CODE);
            String stateCode = code.substring(code.length() - 2);
            mEtState.setText(stateCode);
            mlLState.setVisibility(View.VISIBLE);
        } else {
            mlLState.setVisibility(View.GONE);
        }

        if (mAddressFieldsHashmap.containsKey(ModelConstants.REGION_CODE) &&
                mAddressFieldsHashmap.get(ModelConstants.REGION_CODE) != null) {
            addressHashMap.put(ModelConstants.REGION_ISOCODE,
                    mAddressFieldsHashmap.get(ModelConstants.REGION_CODE));
        }
    }

    protected AddressFields setAddressFields(AddressFields addressFields) {
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

    private void handleError(Message msg) {
        IAPNetworkError iapNetworkError = (IAPNetworkError) msg.obj;
        if (null != iapNetworkError.getServerError()) {
            for (int i = 0; i < iapNetworkError.getServerError().getErrors().size(); i++) {
                Error error = iapNetworkError.getServerError().getErrors().get(i);
                showErrorFromServer(error);
            }
        }
    }

    @Override
    public void onSetPaymentDetails(Message msg) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public boolean handleBackEvent() {
        Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
        if (fragment != null) {
            moveToVerticalAppByClearingStack();
        }
        return super.handleBackEvent();
    }

    InputValidator getValidator(Pattern valid_regex_pattern) {
        return new InputValidator(valid_regex_pattern);
    }
}
