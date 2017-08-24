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

    protected LinearLayout mLlFirstName;
    protected LinearLayout mLlLastName;
    protected LinearLayout mLlSalutation;
    protected LinearLayout mLlAddressLineOne;
    protected LinearLayout mLlAddressLineTwo;
    protected LinearLayout mLlTown;
    protected LinearLayout mLlPostalCode;
    protected LinearLayout mLlCountry;
    protected LinearLayout mlLState;
    protected LinearLayout mLlEmail;
    protected LinearLayout mLlPhone1;
    protected LinearLayout mLlPhone2;

    protected TextView mTvTitle;

    protected TextView mTvSalutation;
    protected TextView mTvFirstName;
    protected TextView mTvLastName;
    protected TextView mTvAddressLineOne;
    protected TextView mTvAddressLineTwo;
    protected TextView mTvTown;
    protected TextView mTvPostalCode;
    protected TextView mTvCountry;
    protected TextView mTvState;
    protected TextView mTvEmail;
    protected TextView mTvPhone1;


    protected EditText mEtFirstName;
    protected EditText mEtLastName;
    protected EditText mEtSalutation;
    protected EditText mEtAddressLineOne;
    protected EditText mEtAddressLineTwo;
    protected EditText mEtTown;
    protected EditText mEtPostalCode;
    protected EditText mEtCountry;
    protected EditText mEtState;
    protected EditText mEtEmail;
    protected EditText mEtPhone1;
    protected EditText mEtPhone2;
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
    private LinearLayout mLlFirstNameBilling;
    private InputValidator inputValidatorFirstNameBilling;
    private LinearLayout mLlLastNameBilling;
    private InputValidator inputValidatorLastNameBilling;
    private LinearLayout mLlSalutationBilling;
    private InputValidator inputValidatorSalutationBilling;
    private LinearLayout mLlAddressLineOneBilling;
    private InputValidator inputValidatorAddressLineOneBilling;
    private LinearLayout mLlAddressLineTwoBilling;
    private InputValidator inputValidatorAddressLineTwoBilling;
    private LinearLayout mLlTownBilling;
    private InputValidator inputValidatorTownBilling;
    private LinearLayout mLlPostalCodeBilling;
    private InputValidator inputValidatorPostalCodeBilling;
    private LinearLayout mLlCountryBilling;
    private InputValidator inputValidatorCountryBilling;
    private LinearLayout mlLStateBilling;
    private InputValidator inputValidatorStateBilling;
    private LinearLayout mLlEmailBilling;
    private InputValidator inputValidatorEmailBilling;
    private LinearLayout mLlPhone1Billing;
    private InputValidator inputValidatorPhoneBilling;
    private EditText mEtFirstNameBilling;
    private EditText mEtLastNameBilling;
    private EditText mEtSalutationBilling;
    private EditText mEtAddressLineOneBilling;
    private EditText mEtAddressLineTwoBilling;
    private EditText mEtTownBilling;
    private EditText mEtPostalCodeBilling;
    private EditText mEtCountryBilling;
    private EditText mEtStateBilling;
    private EditText mEtEmailBilling;
    private EditText mEtPhone1Billing;
    private SalutationDropDown mSalutationDropDownBilling;
    private StateDropDown mStateDropDownBilling;

    //Billing Address
    private AddressFields mBillingAddressFields;

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (!(this instanceof BillingAddressFragment))
//            CartModelContainer.getInstance().setAddressId(null);
//    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_shipping_billing_address_layout, container, false);

        phoneNumberUtil = PhoneNumberUtil.getInstance();

        mUseThisAddressCheckBox = (CheckBox) rootView.findViewById(R.id.use_this_address_checkbox);
        mSameAsShippingAddress = (LinearLayout) rootView.findViewById(R.id.iap_same_as_shipping_address);


        mLlFirstName = (LinearLayout) rootView.findViewById(R.id.ll_first_name);
        inputValidatorFirstName = getValidator(Validator.NAME_PATTERN);
        ((InputValidationLayout) mLlFirstName).setValidator(inputValidatorFirstName);

        mLlFirstNameBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_first_name);
        inputValidatorFirstNameBilling = getValidator(Validator.NAME_PATTERN);
        ((InputValidationLayout) mLlFirstNameBilling).setValidator(inputValidatorFirstNameBilling);


        mLlLastName = (LinearLayout) rootView.findViewById(R.id.ll_last_name);
        inputValidatorLastName = getValidator(Validator.NAME_PATTERN);
        ((InputValidationLayout) mLlLastName).setValidator(inputValidatorLastName);


        mLlLastNameBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_last_name);
        inputValidatorLastNameBilling = getValidator(Validator.NAME_PATTERN);
        ((InputValidationLayout) mLlLastNameBilling).setValidator(inputValidatorLastNameBilling);


        mLlSalutation = (LinearLayout) rootView.findViewById(R.id.ll_salutation);
        inputValidatorSalutation = getValidator(Validator.NAME_PATTERN);
        ((InputValidationLayout) mLlSalutation).setValidator(inputValidatorSalutation);

        mLlSalutationBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_salutation);
        inputValidatorSalutationBilling = getValidator(Validator.NAME_PATTERN);
        ((InputValidationLayout) mLlSalutationBilling).setValidator(inputValidatorSalutationBilling);

        mLlAddressLineOne = (LinearLayout) rootView.findViewById(R.id.ll_address_line_one);
        inputValidatorAddressLineOne = getValidator(Validator.ADDRESS_PATTERN);
        ((InputValidationLayout) mLlAddressLineOne).setValidator(inputValidatorAddressLineOne);

        mLlAddressLineOneBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_address_line_one);
        inputValidatorAddressLineOneBilling = getValidator(Validator.ADDRESS_PATTERN);
        ((InputValidationLayout) mLlAddressLineOneBilling).setValidator(inputValidatorAddressLineOneBilling);


        mLlAddressLineTwo = (LinearLayout) rootView.findViewById(R.id.ll_address_line_two);
        inputValidatorAddressLineTwo = getValidator(Validator.ADDRESS_PATTERN);
        ((InputValidationLayout) mLlAddressLineTwo).setValidator(inputValidatorAddressLineTwo);

        mLlAddressLineTwoBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_address_line_two);
        inputValidatorAddressLineTwoBilling = getValidator(Validator.ADDRESS_PATTERN);
        ((InputValidationLayout) mLlAddressLineTwoBilling).setValidator(inputValidatorAddressLineTwoBilling);

        mLlTown = (LinearLayout) rootView.findViewById(R.id.ll_town);
        inputValidatorTown = getValidator(Validator.TOWN_PATTERN);
        ((InputValidationLayout) mLlTown).setValidator(inputValidatorTown);

        mLlTownBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_town);
        inputValidatorTownBilling = getValidator(Validator.TOWN_PATTERN);
        ((InputValidationLayout) mLlTownBilling).setValidator(inputValidatorTownBilling);


        mLlPostalCode = (LinearLayout) rootView.findViewById(R.id.ll_postal_code);
        inputValidatorPostalCode = getValidator(Validator.POSTAL_CODE_PATTERN);
        ((InputValidationLayout) mLlPostalCode).setValidator(inputValidatorPostalCode);

        mLlPostalCodeBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_postal_code);
        inputValidatorPostalCodeBilling = getValidator(Validator.POSTAL_CODE_PATTERN);
        ((InputValidationLayout) mLlPostalCodeBilling).setValidator(inputValidatorPostalCodeBilling);


        mLlCountry = (LinearLayout) rootView.findViewById(R.id.ll_country);
        inputValidatorCountry = getValidator(Validator.COUNTRY_PATTERN);
        ((InputValidationLayout) mLlCountry).setValidator(inputValidatorCountry);

        mLlCountryBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_country);
        inputValidatorCountryBilling = getValidator(Validator.COUNTRY_PATTERN);
        ((InputValidationLayout) mLlCountryBilling).setValidator(inputValidatorCountryBilling);


        mlLState = (LinearLayout) rootView.findViewById(R.id.ll_state);
        inputValidatorState = getValidator(Validator.NAME_PATTERN);
        ((InputValidationLayout) mlLState).setValidator(inputValidatorState);

        mlLStateBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_state);
        inputValidatorStateBilling = getValidator(Validator.NAME_PATTERN);
        ((InputValidationLayout) mlLStateBilling).setValidator(inputValidatorStateBilling);


        mLlEmail = (LinearLayout) rootView.findViewById(R.id.ll_email);
        inputValidatorEmail = getValidator(Validator.EMAIL_PATTERN);
        ((InputValidationLayout) mLlEmail).setValidator(inputValidatorEmail);

        mLlEmailBilling = (LinearLayout) rootView.findViewById(R.id.ll_billing_email);
        inputValidatorEmailBilling = getValidator(Validator.EMAIL_PATTERN);
        ((InputValidationLayout) mLlEmailBilling).setValidator(inputValidatorEmailBilling);

        mLlPhone1 = (LinearLayout) rootView.findViewById(R.id.ll_phone1);
        inputValidatorPhone = getValidator(Validator.PHONE_NUMBER_PATTERN);
        ((InputValidationLayout) mLlPhone1).setValidator(inputValidatorPhone);

        mLlPhone1Billing = (LinearLayout) rootView.findViewById(R.id.ll_billing_phone1);
        inputValidatorPhoneBilling = getValidator(Validator.PHONE_NUMBER_PATTERN);
        ((InputValidationLayout) mLlPhone1Billing).setValidator(inputValidatorPhoneBilling);

        mEtFirstName = (EditText) rootView.findViewById(R.id.et_first_name);
        mEtLastName = (EditText) rootView.findViewById(R.id.et_last_name);
        mEtSalutation = (EditText) rootView.findViewById(R.id.et_salutation);
        mEtAddressLineOne = (EditText) rootView.findViewById(R.id.et_address_line_one);
        mEtAddressLineTwo = (EditText) rootView.findViewById(R.id.et_address_line_two);
        mEtTown = (EditText) rootView.findViewById(R.id.et_town);
        mEtPostalCode = (EditText) rootView.findViewById(R.id.et_postal_code);
        mEtCountry = (EditText) rootView.findViewById(R.id.et_country);
        mEtState = (EditText) rootView.findViewById(R.id.et_state);
        mEtEmail = (EditText) rootView.findViewById(R.id.et_email);
        mEtPhone1 = (EditText) rootView.findViewById(R.id.et_phone1);

        //For Billing address

        mEtFirstNameBilling = (EditText) rootView.findViewById(R.id.et_billing_first_name);
        mEtLastNameBilling = (EditText) rootView.findViewById(R.id.et_billing_last_name);
        mEtSalutationBilling = (EditText) rootView.findViewById(R.id.et_billing_salutation);
        mEtAddressLineOneBilling = (EditText) rootView.findViewById(R.id.et_billing_address_line_one);
        mEtAddressLineTwoBilling = (EditText) rootView.findViewById(R.id.et_billing_address_line_two);
        mEtTownBilling = (EditText) rootView.findViewById(R.id.et_billing_town);
        mEtPostalCodeBilling = (EditText) rootView.findViewById(R.id.et_billing_postal_code);
        mEtCountryBilling = (EditText) rootView.findViewById(R.id.et_billing_country);
        mEtStateBilling = (EditText) rootView.findViewById(R.id.et_billing_state);
        mEtEmailBilling = (EditText) rootView.findViewById(R.id.et_billing_email);
        mEtPhone1Billing = (EditText) rootView.findViewById(R.id.et_billing_phone1);

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
        mEtEmailBilling.setEnabled(false);
        mEtCountry.setText(HybrisDelegate.getInstance(mContext).getStore().getCountry());
        showUSRegions();
        mEtCountry.setEnabled(false);
        mEtCountryBilling.setEnabled(false);

        //should it be done for billing address -- Indrajit


//        mAddressController = new AddressController(mContext, this);
//        mPaymentController = new PaymentController(mContext, this);
//        mShippingAddressFields = new AddressFields();
//        mEtEmail.setText(HybrisDelegate.getInstance(mContext).getStore().getJanRainEmail());
//        mEtEmail.setEnabled(false);
//        mEtCountry.setText(HybrisDelegate.getInstance(mContext).getStore().getCountry());
//        showUSRegions();
//        mEtCountry.setEnabled(false);


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
        mEtState.setCompoundDrawables(null, null, imageArrow, null);
        mStateDropDown = new StateDropDown(mContext, mEtState, this);

        mEtSalutation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSalutationDropDown.show();
                return false;
            }
        });

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
        mEtPhone1Billing.addTextChangedListener(new IAPTextWatcher(mEtPhone1Billing));
        mEtStateBilling.addTextChangedListener(new IAPTextWatcher(mEtStateBilling));
        mEtSalutationBilling.addTextChangedListener(new IAPTextWatcher(mEtSalutationBilling));

        setImageArrow();
        mEtSalutationBilling.setCompoundDrawables(null, null, imageArrow, null);
        mSalutationDropDownBilling = new SalutationDropDown(mContext, mEtSalutationBilling, this);
        mEtStateBilling.setCompoundDrawables(null, null, imageArrow, null);
        mStateDropDownBilling = new StateDropDown(mContext, mEtStateBilling, this);

        mEtSalutationBilling.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSalutationDropDownBilling.show();
                return false;
            }
        });

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
                    //mSameAsShippingAddress.setVisibility(View.VISIBLE);
                    //disableAllFields();
                    mBillingAddressFields = mShippingAddressFields;
                    prePopulateBillingAddress();
                    mBtnContinue.setEnabled(true);
                } else {
                    // mSameAsShippingAddress.setVisibility(View.GONE);
                    clearAllBillingFields();
                    mBtnContinue.setEnabled(false);
                }
            }
        });


        return rootView;
    }

    private void clearAllBillingFields() {
        mIgnoreTextChangeListener = true;
        mEtFirstNameBilling.setText("");
        mEtLastNameBilling.setText("");
        mEtSalutationBilling.setText("");
        mEtAddressLineOneBilling.setText("");
        mEtAddressLineTwoBilling.setText("");
        mEtEmailBilling.setText("");
        mEtTownBilling.setText("");
        mEtPostalCodeBilling.setText("");
        mEtPhone1Billing.setText("");
//        mEtPhone2.setText("");
        mEtStateBilling.setText("");
        if (HybrisDelegate.getInstance().getStore().getCountry().equalsIgnoreCase("US")) {
            mlLStateBilling.setVisibility(View.VISIBLE);
        } else {
            mlLStateBilling.setVisibility(View.GONE);
        }
        mIgnoreTextChangeListener = false;
        // enableAllFields();
        // enableFocus();
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
        mEtFirstName.setEnabled(enable);
        mEtLastName.setEnabled(enable);
        mEtSalutation.setEnabled(enable);
        mEtAddressLineOne.setEnabled(enable);
        mEtAddressLineTwo.setEnabled(enable);
        mEtTown.setEnabled(enable);
        mEtPostalCode.setEnabled(enable);
        if (mlLState.getVisibility() == View.VISIBLE) {
            mEtState.setEnabled(enable);
        }
        mEtPhone1.setEnabled(enable);
        //mEtPhone2.setEnabled(enable);
    }

    private void setFieldsFocusable(boolean focusable) {
        mEtFirstName.setFocusable(focusable);
        mEtLastName.setFocusable(focusable);
        mEtSalutation.setFocusable(focusable);
        mEtAddressLineOne.setFocusable(focusable);
        mEtAddressLineTwo.setFocusable(focusable);
        mEtTown.setFocusable(focusable);
        mEtPostalCode.setFocusable(focusable);
        if (mlLState.getVisibility() == View.VISIBLE) {
            mEtState.setFocusable(focusable);
        }
        mEtPhone1.setFocusable(focusable);
        //  mEtPhone2.setFocusable(focusable);

        if (focusable) {
            mEtFirstName.setFocusableInTouchMode(true);
            mEtLastName.setFocusableInTouchMode(true);
            mEtSalutation.setFocusableInTouchMode(true);
            mEtAddressLineOne.setFocusableInTouchMode(true);
            mEtAddressLineTwo.setFocusableInTouchMode(true);
            mEtTown.setFocusableInTouchMode(true);
            mEtPostalCode.setFocusableInTouchMode(true);
            if (mlLState.getVisibility() == View.VISIBLE) {
                mEtState.setFocusableInTouchMode(true);
            }
            mEtPhone1.setFocusableInTouchMode(true);
            // mEtPhone2.setFocusableInTouchMode(true);
        }

        mEtCountry.setFocusable(false);
        mEtCountry.setFocusableInTouchMode(false);
        mEtEmail.setFocusableInTouchMode(false);
        mEtEmail.setFocusable(false);
        mEtEmail.setEnabled(false);
        mEtCountry.setEnabled(false);
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
        //setTextColor();
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
            //Edit and save address
//            if (mBtnContinue.getText().toString().equalsIgnoreCase(getString(R.string.iap_save))) {
//                if (!isProgressDialogShowing()) {
//                    showProgressDialog(mContext, getString(R.string.iap_please_wait));
//                    HashMap<String, String> addressHashMap = addressPayload();
//                    mAddressController.updateAddress(addressHashMap);
//                }
//            } else {//Add new address
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
//            }
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
                String errorMessage;
                if (error.getSubject().equalsIgnoreCase(ModelConstants.COUNTRY_ISOCODE)) {
                    //errorMessage = getResources().getString(R.string.iap_country_error);
                    ((InputValidationLayout) mLlCountryBilling).setValidator(inputValidatorCountryBilling);
                    // mInlineFormsParent.setErrorMessage(errorMessage);
                    //mInlineFormsParent.showError(mEtCountry);
                } else if (error.getSubject().equalsIgnoreCase(ModelConstants.POSTAL_CODE)) {
                    ((InputValidationLayout) mLlPostalCode).setValidator(inputValidatorPostalCode);
//                    errorMessage = getResources().getString(R.string.iap_postal_code_error);
//                    mInlineFormsParent.setErrorMessage(errorMessage);
//                    mInlineFormsParent.showError(mEtPostalCode);
                } else if (error.getSubject().equalsIgnoreCase(ModelConstants.PHONE_1)) {
                    ((InputValidationLayout) mLlPhone1).setValidator(inputValidatorPhone);
//                    errorMessage = getResources().getString(R.string.iap_phone_error);
//                    mInlineFormsParent.setErrorMessage(errorMessage);
//                    mInlineFormsParent.showError(mEtPhone1);
                } else if (error.getSubject().equalsIgnoreCase(ModelConstants.LINE_1)) {
                    ((InputValidationLayout) mLlAddressLineOne).setValidator(inputValidatorAddressLineOne);
//                    errorMessage = getResources().getString(R.string.iap_address_error);
//                    mInlineFormsParent.setErrorMessage(errorMessage);
//                    mInlineFormsParent.showError(mEtAddressLineOne);
                } else if (error.getSubject().equalsIgnoreCase(ModelConstants.LINE_2)) {
                    ((InputValidationLayout) mLlAddressLineTwo).setValidator(inputValidatorAddressLineTwo);
//                    errorMessage = getResources().getString(R.string.iap_address_error);
//                    mInlineFormsParent.setErrorMessage(errorMessage);
//                    mInlineFormsParent.showError(mEtAddressLineTwo);
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

        if (inputValidatorFirstName.validate(firstName) && inputValidatorLastName.validate(lastName)
                && inputValidatorAddressLineOne.validate(addressLineOne) && (addressLineTwo.trim().equals("") || inputValidatorAddressLineTwo.validate(addressLineTwo))
                && inputValidatorPostalCode.validate(postalCode)
                && inputValidatorEmail.validate(email) && inputValidatorPhone.validate(phone1)
                && inputValidatorTown.validate(town) && inputValidatorCountry.validate(country)
                && (!mEtSalutation.getText().toString().trim().equalsIgnoreCase(""))
                && (mlLState.getVisibility() == View.GONE || (mlLState.getVisibility() == View.VISIBLE && !mEtState.getText().toString().trim().equalsIgnoreCase("")))) {

            mShippingAddressFields = setAddressFields(mShippingAddressFields);

            mBtnContinue.setEnabled(true);
        } else {
            mBtnContinue.setEnabled(false);
        }
    }


    public void validate(View editText, boolean hasFocus) {
        boolean result = true;
        String errorMessage = null;

        if (editText.getId() == R.id.et_first_name && !hasFocus) {
            // result = mValidator.isValidName(mEtFirstName.getText().toString());
            errorMessage = getResources().getString(R.string.iap_first_name_error);
            result = inputValidatorFirstName.validate(((EditText) editText).getText().toString());
        }
        if (editText.getId() == R.id.et_last_name && !hasFocus) {
            //result = mValidator.isValidName(mEtLastName.getText().toString());
            errorMessage = getResources().getString(R.string.iap_last_name_error);
            result = inputValidatorLastName.validate(mEtLastName.getText().toString());
        }
        if (editText.getId() == R.id.et_town && !hasFocus) {
            //result = mValidator.isValidTown(mEtTown.getText().toString());
            errorMessage = getResources().getString(R.string.iap_town_error);
            result = inputValidatorTown.validate(mEtTown.getText().toString());
        }
        if (editText.getId() == R.id.et_phone1 && !hasFocus) {
            //result = validatePhoneNumber(mEtPhone1, HybrisDelegate.getInstance().getStore().getCountry()
            //      , mEtPhone1.getText().toString());
            errorMessage = getResources().getString(R.string.iap_phone_error);
            result = inputValidatorPhone.validate(mEtPhone1.getText().toString());
        }
        if (editText.getId() == R.id.et_country && !hasFocus) {
            //result = mValidator.isValidCountry(mEtCountry.getText().toString());
            errorMessage = getResources().getString(R.string.iap_country_error);
            showUSRegions();
            result = inputValidatorCountry.validate(mEtCountry.getText().toString());

        }
        if (editText.getId() == R.id.et_postal_code && !hasFocus) {
            //result = mValidator.isValidPostalCode(mEtPostalCode.getText().toString());
            errorMessage = getResources().getString(R.string.iap_postal_code_error);
            result = inputValidatorPostalCode.validate(mEtPostalCode.getText().toString());
        }
        if (editText.getId() == R.id.et_email && !hasFocus) {
            //result = mValidator.isValidEmail(mEtEmail.getText().toString());
            errorMessage = getResources().getString(R.string.iap_email_error);
            result = inputValidatorEmail.validate(mEtEmail.getText().toString());
        }
        if (editText.getId() == R.id.et_address_line_one && !hasFocus) {
            //result = mValidator.isValidAddress(mEtAddressLineOne.getText().toString());
            errorMessage = getResources().getString(R.string.iap_address_error);
            result = inputValidatorAddressLineOne.validate(mEtAddressLineOne.getText().toString());
        }
        if (editText.getId() == R.id.et_address_line_two && !hasFocus) {
            if (mEtAddressLineTwo.getText().toString().trim().equals("")) {
                result = true;
            } else {
                errorMessage = getResources().getString(R.string.iap_address_error);
                result = inputValidatorAddressLineTwo.validate(mEtAddressLineTwo.getText().toString());
            }

        }
        if ((editText.getId() == R.id.et_salutation || editText.getId() == R.id.et_state) && !hasFocus) {
            checkFields();
        }

        //================For billing Address

        if (editText.getId() == R.id.et_billing_first_name && !hasFocus) {
            result = inputValidatorFirstNameBilling.validate(((EditText) editText).getText().toString());
        }
        if (editText.getId() == R.id.et_billing_last_name && !hasFocus) {
            result = inputValidatorLastNameBilling.validate(((EditText) editText).getText().toString());
        }
        if (editText.getId() == R.id.et_billing_town && !hasFocus) {
            result = inputValidatorTownBilling.validate(((EditText) editText).getText().toString());
        }
        if (editText.getId() == R.id.et_billing_phone1 && !hasFocus) {
            result = inputValidatorPhoneBilling.validate(((EditText) editText).getText().toString());
        }
        if (editText.getId() == R.id.et_billing_country && !hasFocus) {
            showUSRegions();
            result = inputValidatorCountryBilling.validate(((EditText) editText).getText().toString());

        }
        if (editText.getId() == R.id.et_billing_postal_code && !hasFocus) {
            result = inputValidatorPostalCodeBilling.validate(((EditText) editText).getText().toString());
        }
        if (editText.getId() == R.id.et_billing_email && !hasFocus) {
            result = inputValidatorEmailBilling.validate(((EditText) editText).getText().toString());
        }
        if (editText.getId() == R.id.et_billing_address_line_one && !hasFocus) {
            result = inputValidatorAddressLineOneBilling.validate(((EditText) editText).getText().toString());
        }
        if (editText.getId() == R.id.et_billing_address_line_two && !hasFocus) {
            if (mEtAddressLineTwoBilling.getText().toString().trim().equals("")) {
                result = true;
            } else {
                result = inputValidatorAddressLineTwoBilling.validate(((EditText) editText).getText().toString());
            }

        }
        if ((editText.getId() == R.id.et_billing_salutation || editText.getId() == R.id.et_billing_state) && !hasFocus) {
            checkFields();
        }

        //===================

        if (!result) {
            mBtnContinue.setEnabled(false);
        } else {
            mBtnContinue.setEnabled(true);
        }
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
            // disableAllFields();
            mBtnContinue.setEnabled(true);
        }

        //if (!(this instanceof BillingAddressFragment)) {
        if (getArguments() != null &&
                getArguments().containsKey(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY)) {
            IAPAnalytics.trackPage(IAPAnalyticsConstant.SHIPPING_ADDRESS_EDIT_PAGE_NAME);
        } else {
            IAPAnalytics.trackPage(IAPAnalyticsConstant.SHIPPING_ADDRESS_PAGE_NAME);
        }
        if (CartModelContainer.getInstance().getRegionIsoCode() != null)
            mShippingAddressFields.setRegionIsoCode(CartModelContainer.getInstance().getRegionIsoCode());
//        }
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
    public void onSalutationSelect(String salutation) {
        mEtSalutation.setText(salutation);
    }

    @Override
    public void onStateSelect(String state) {
        mEtState.setText(state);
    }

    @Override
    public void stateRegionCode(String regionCode) {
        mRegionIsoCode = regionCode;
        mShippingAddressFields.setRegionIsoCode(regionCode);
        if (addressHashMap != null) {
            addressHashMap.put(ModelConstants.REGION_ISOCODE, regionCode);
        }

//        if (!(this instanceof BillingAddressFragment)) {
//            CartModelContainer.getInstance().setRegionIsoCode(regionCode);
//        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    private class IAPTextWatcher implements TextWatcher {
        private EditText mEditText;

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

        private boolean isInAfterTextChanged;

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


//        if (this instanceof BillingAddressFragment) {
//            if (mlLState.getVisibility() == View.VISIBLE) {
//                addressFields.setRegionIsoCode(mRegionIsoCode);
//                addressFields.setRegionName(mEtState.getText().toString());
//            } else {
//                addressFields.setRegionIsoCode(null);
//                addressFields.setRegionName(null);
//            }
//        }
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


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
