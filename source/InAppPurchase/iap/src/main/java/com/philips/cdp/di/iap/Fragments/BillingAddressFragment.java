package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.customviews.InlineForms;
import com.philips.cdp.uikit.customviews.PuiSwitch;

public class BillingAddressFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, InlineForms.Validator, TextWatcher {

    private Context mContext;
    private PuiSwitch mSwitchBillingAddress;
    private EditText mEtFirstName;
    private EditText mEtLastName;
    private EditText mEtAddresslineOne;
    private EditText mEtAddresslineTwo;
    private EditText mEtTown;
    private EditText mEtPostalCode;
    private EditText mEtCountry;
    private EditText mEtEmail;
    private EditText mEtPhoneNumber;

    private Button mBtnContinue;
    private Button mBtnCancel;

    private AddressFields mAddressFields;

    private InlineForms mInlineFormsParent;
    private Validator mValidator = null;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shipping_address_layout, container, false);

        LinearLayout mSameAsBillingAddress = (LinearLayout) rootView.findViewById(R.id.same_as_shipping_ll);
        TextView mTvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        mSwitchBillingAddress = (PuiSwitch) rootView.findViewById(R.id.switch_billing_address);
        mInlineFormsParent = (InlineForms) rootView.findViewById(R.id.InlineForms);

        mEtFirstName = (EditText) rootView.findViewById(R.id.et_first_name);
        mEtLastName = (EditText) rootView.findViewById(R.id.et_last_name);
        mEtAddresslineOne = (EditText) rootView.findViewById(R.id.et_address_line_one);
        mEtAddresslineTwo = (EditText) rootView.findViewById(R.id.et_address_line_two);
        mEtTown = (EditText) rootView.findViewById(R.id.et_town);
        mEtPostalCode = (EditText) rootView.findViewById(R.id.et_postal_code);
        mEtCountry = (EditText) rootView.findViewById(R.id.et_country);
        mEtEmail = (EditText) mInlineFormsParent.findViewById(R.id.et_email);
        mEtPhoneNumber = (EditText) rootView.findViewById(R.id.et_phone_number);

        mBtnContinue = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);

        mTvTitle.setText(getResources().getString(R.string.iap_billing_address));
        mSameAsBillingAddress.setVisibility(View.VISIBLE);

        mBtnContinue.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mValidator = new Validator();
        mInlineFormsParent.setValidator(this);

        Bundle bundle = getArguments();

        if (getArguments().containsKey(IAPConstant.SHIPPING_ADDRESS_FIELDS)) {
            mAddressFields = (AddressFields) bundle.getSerializable(IAPConstant.SHIPPING_ADDRESS_FIELDS);
            disableAllFields();
            prePopulateShippingAddress();
            mBtnContinue.setEnabled(true);
        }

        mEtFirstName.addTextChangedListener(this);
        mEtLastName.addTextChangedListener(this);
        mEtAddresslineOne.addTextChangedListener(this);
        mEtAddresslineTwo.addTextChangedListener(this);
        mEtTown.addTextChangedListener(this);
        mEtPostalCode.addTextChangedListener(this);
        mEtCountry.addTextChangedListener(this);
        mEtEmail.addTextChangedListener(this);
        mEtPhoneNumber.addTextChangedListener(this);

        mSwitchBillingAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    disableAllFields();
                    prePopulateShippingAddress();
                    mBtnContinue.setEnabled(true);
                } else {
                    clearAllFields();
                    mBtnContinue.setEnabled(false);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_address);
        if (mSwitchBillingAddress.isChecked()) {
            disableAllFields();
            mBtnContinue.setEnabled(true);
        }
    }

    private void prePopulateShippingAddress() {
        if (mAddressFields != null) {
            mEtFirstName.setText(mAddressFields.getFirstName());
            mEtLastName.setText(mAddressFields.getLastName());
            mEtAddresslineOne.setText(mAddressFields.getLine1());
            mEtAddresslineTwo.setText(mAddressFields.getLine2());
            mEtTown.setText(mAddressFields.getTown());
            mEtPostalCode.setText(mAddressFields.getPostalCode());
            mEtCountry.setText(mAddressFields.getCountryIsocode());
            mEtEmail.setText(mAddressFields.getEmail());
            mEtPhoneNumber.setText(mAddressFields.getPhoneNumber());
        }
    }

    public void checkFields() {

        String firstName = mEtFirstName.getText().toString();
        String lastName = mEtLastName.getText().toString();
        String addressLineOne = mEtAddresslineOne.getText().toString();
        String addressLineTwo = mEtAddresslineTwo.getText().toString();
        String postalCode = mEtPostalCode.getText().toString();
        String phoneNumber = mEtPhoneNumber.getText().toString();
        String town = mEtTown.getText().toString();
        String country = mEtCountry.getText().toString();
        String email = mEtEmail.getText().toString();

        if (mValidator.isValidFirstName(firstName) && mValidator.isValidLastName(lastName)
                && mValidator.isValidAddress(addressLineOne) && mValidator.isValidAddress(addressLineTwo)
                && mValidator.isValidPostalCode(postalCode)
                && mValidator.isValidEmail(email) && mValidator.isValidPhoneNumber(phoneNumber)
                && mValidator.isValidTown(town) && mValidator.isValidCountry(country)) {

            mAddressFields.setFirstName(firstName);
            mAddressFields.setLastName(lastName);
            mAddressFields.setTitleCode("mr");
            mAddressFields.setCountryIsocode(country);
            mAddressFields.setLine1(addressLineOne);
            mAddressFields.setLine2(addressLineTwo);
            mAddressFields.setPostalCode(postalCode);
            mAddressFields.setTown(town);
            mAddressFields.setPhoneNumber(phoneNumber);
            mAddressFields.setEmail(email);

            mBtnContinue.setEnabled(true);
        } else {
            mBtnContinue.setEnabled(false);
        }
    }

    private void clearAllFields() {
        mEtFirstName.setText("");
        mEtLastName.setText("");
        mEtAddresslineOne.setText("");
        mEtAddresslineTwo.setText("");
        mEtTown.setText("");
        mEtPostalCode.setText("");
        mEtCountry.setText("");
        mEtEmail.setText("");
        mEtPhoneNumber.setText("");
        enableAllFields();
        removeErrorInAllFields();
    }

    private void disableAllFields() {
        removeErrorInAllFields();
        mEtFirstName.setEnabled(false);
        mEtLastName.setEnabled(false);
        mEtAddresslineOne.setEnabled(false);
        mEtAddresslineTwo.setEnabled(false);
        mEtTown.setEnabled(false);
        mEtPostalCode.setEnabled(false);
        mEtCountry.setEnabled(false);
        mEtEmail.setEnabled(false);
        mEtPhoneNumber.setEnabled(false);
    }

    private void enableAllFields() {
        mEtFirstName.setEnabled(true);
        mEtLastName.setEnabled(true);
        mEtAddresslineOne.setEnabled(true);
        mEtAddresslineTwo.setEnabled(true);
        mEtTown.setEnabled(true);
        mEtPostalCode.setEnabled(true);
        mEtCountry.setEnabled(true);
        mEtEmail.setEnabled(true);
        mEtPhoneNumber.setEnabled(true);
    }

    private void removeErrorInAllFields() {
        mInlineFormsParent.removeError(mEtFirstName);
        mInlineFormsParent.removeError(mEtLastName);
        mInlineFormsParent.removeError(mEtAddresslineOne);
        mInlineFormsParent.removeError(mEtAddresslineTwo);
        mInlineFormsParent.removeError(mEtTown);
        mInlineFormsParent.removeError(mEtPostalCode);
        mInlineFormsParent.removeError(mEtCountry);
        mInlineFormsParent.removeError(mEtEmail);
        mInlineFormsParent.removeError(mEtPhoneNumber);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onClick(View v) {

        Utility.hideKeypad(mContext);

        if (v == mBtnContinue) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.BILLING_ADDRESS_FIELDS, mAddressFields);
            addFragment(
                    OrderSummaryFragment.createInstance(bundle, AnimationType.NONE), null);
        } else if (v == mBtnCancel) {
            addFragment
                    (ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        checkFields();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void validate(View editText, boolean hasFocus) {
        boolean result = true;
        String errorMessage = null;

        if (editText.getId() == R.id.et_first_name && !hasFocus) {
            result = mValidator.isValidFirstName(mEtFirstName.getText().toString());
            errorMessage = getResources().getString(R.string.iap_first_name_error);
        }
        if (editText.getId() == R.id.et_last_name && !hasFocus) {
            result = mValidator.isValidLastName(mEtLastName.getText().toString());
            errorMessage = getResources().getString(R.string.iap_last_name_error);
        }
        if (editText.getId() == R.id.et_town && !hasFocus) {
            result = mValidator.isValidTown(mEtTown.getText().toString());
            errorMessage = getResources().getString(R.string.iap_town_error);
        }
        if (editText.getId() == R.id.et_phone_number && !hasFocus) {
            result = mValidator.isValidPhoneNumber(mEtPhoneNumber.getText().toString());
            errorMessage = getResources().getString(R.string.iap_phone_error);
        }
        if (editText.getId() == R.id.et_country && !hasFocus) {
            result = mValidator.isValidCountry(mEtCountry.getText().toString());
            errorMessage = getResources().getString(R.string.iap_country_error);
        }
        if (editText.getId() == R.id.et_postal_code && !hasFocus) {
            result = mValidator.isValidPostalCode(mEtPostalCode.getText().toString());
            errorMessage = getResources().getString(R.string.iap_postal_code_error);
        }
        if (editText.getId() == R.id.et_email && !hasFocus) {
            result = mValidator.isValidEmail(mEtEmail.getText().toString());
            errorMessage = getResources().getString(R.string.iap_email_error);
        }
        if (editText.getId() == R.id.et_address_line_one && !hasFocus) {
            result = mValidator.isValidAddress(mEtAddresslineOne.getText().toString());
            errorMessage = getResources().getString(R.string.iap_address_error);
        }
        if (editText.getId() == R.id.et_address_line_two && !hasFocus) {
            result = mValidator.isValidAddress(mEtAddresslineTwo.getText().toString());
            errorMessage = getResources().getString(R.string.iap_address_error);
        }

        if (!result) {
            mInlineFormsParent.setErrorMessage(errorMessage);
            mInlineFormsParent.showError((EditText) editText);
        } else {
            mInlineFormsParent.removeError(editText);
        }
    }

    public static BillingAddressFragment createInstance(Bundle args, AnimationType animType) {
        BillingAddressFragment fragment = new BillingAddressFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }
}
