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
import com.philips.cdp.uikit.customviews.InlineForms;
import com.philips.cdp.uikit.customviews.PuiSwitch;

public class BillingAddressFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, InlineForms.Validator, TextWatcher {

    private Context mContext;

    private LinearLayout mSameAsBillingAddress;
    private TextView mTvTitle;
    private PuiSwitch mSwitchBillingAddress;

    private EditText mEtFirstName;
    private EditText mEtLastName;
    private EditText mEtAddress;
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

        mSameAsBillingAddress = (LinearLayout) rootView.findViewById(R.id.same_as_shipping_ll);
        mTvTitle = (TextView) rootView.findViewById(R.id.tv_title);
        mSwitchBillingAddress = (PuiSwitch) rootView.findViewById(R.id.switch_billing_address);
        mInlineFormsParent = (InlineForms) rootView.findViewById(R.id.InlineForms);

        mEtFirstName = (EditText) rootView.findViewById(R.id.et_first_name);
        mEtLastName = (EditText) rootView.findViewById(R.id.et_last_name);
        mEtAddress = (EditText) rootView.findViewById(R.id.et_address);
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

        if (null != bundle.getSerializable("addressField")) {
            mAddressFields = (AddressFields) bundle.getSerializable("addressField");
        }

        mEtFirstName.addTextChangedListener(this);
        mEtLastName.addTextChangedListener(this);
        mEtAddress.addTextChangedListener(this);
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
                } else {
                    clearAllFields();
                }
            }
        });


        mSwitchBillingAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    disableAllFields();
                    prePopulateShippingAddress();
                } else {
                    clearAllFields();
                }
            }
        });

        return rootView;
    }

    private void prePopulateShippingAddress() {
        if(mAddressFields != null) {
            mEtFirstName.setText(mAddressFields.getFirstName());
            mEtLastName.setText(mAddressFields.getLastName());
            mEtAddress.setText(mAddressFields.getLine1());
            mEtTown.setText(mAddressFields.getTown());
            mEtPostalCode.setText(mAddressFields.getPostalCode());
            mEtCountry.setText(mAddressFields.getCountryIsocode());
            mEtEmail.setText(mAddressFields.getEmail());
            mEtPhoneNumber.setText(mAddressFields.getPhoneNumber());
        }
    }

    public void checkFields() {

        if (mValidator.isValidFirstName(mEtFirstName) && mValidator.isValidLastName(mEtLastName)
                && mValidator.isValidAddress(mEtAddress) && mValidator.isValidPostalCode(mEtPostalCode)
                && mValidator.isValidEmail(mEtEmail) && mValidator.isValidPhoneNumber(mEtPhoneNumber)
                && mValidator.isValidTown(mEtTown) && mValidator.isValidCountry(mEtCountry)) {

            mAddressFields.setFirstName(mEtFirstName.getText().toString().trim());
            mAddressFields.setLastName(mEtLastName.getText().toString().trim());
            mAddressFields.setTitleCode("mr");
            mAddressFields.setCountryIsocode(mEtCountry.getText().toString().trim());
            mAddressFields.setLine1(mEtAddress.getText().toString());
            mAddressFields.setLine2("testline");
            mAddressFields.setPostalCode(mEtPostalCode.getText().toString());
            mAddressFields.setTown(mEtTown.getText().toString());
            mAddressFields.setPhoneNumber(mEtPhoneNumber.getText().toString().trim());
            mAddressFields.setEmail(mEtEmail.getText().toString());

            mBtnContinue.setEnabled(true);
        } else {
            mBtnContinue.setEnabled(false);
        }
    }

    private void clearAllFields() {
        mEtFirstName.setText("");
        mEtLastName.setText("");
        mEtAddress.setText("");
        mEtTown.setText("");
        mEtPostalCode.setText("");
        mEtCountry.setText("");
        mEtEmail.setText("");
        mEtPhoneNumber.setText("");
        removeErrorInAllFields();
        enableAllFields();
    }

    private void disableAllFields() {
        removeErrorInAllFields();
        mEtFirstName.setEnabled(false);
        mEtLastName.setEnabled(false);
        mEtAddress.setEnabled(false);
        mEtTown.setEnabled(false);
        mEtPostalCode.setEnabled(false);
        mEtCountry.setEnabled(false);
        mEtEmail.setEnabled(false);
        mEtPhoneNumber.setEnabled(false);
    }

    private void enableAllFields() {
        mEtFirstName.setEnabled(true);
        mEtLastName.setEnabled(true);
        mEtAddress.setEnabled(true);
        mEtTown.setEnabled(true);
        mEtPostalCode.setEnabled(true);
        mEtCountry.setEnabled(true);
        mEtEmail.setEnabled(true);
        mEtPhoneNumber.setEnabled(true);
    }

    private void removeErrorInAllFields() {
        mInlineFormsParent.removeError(mEtFirstName);
        mInlineFormsParent.removeError(mEtLastName);
        mInlineFormsParent.removeError(mEtAddress);
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
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
    }

    @Override
    protected void updateTitle() {

    }

    @Override
    public void onClick(View v) {
        if (v == mBtnContinue) {
            getMainActivity().addFragmentAndRemoveUnderneath(
                    OrderSummaryFragment.createInstance(new Bundle(), AnimationType.NONE), false);
        } else if (v == mBtnCancel) {
            getMainActivity().addFragmentAndRemoveUnderneath
                    (ShippingAddressFragment.createInstance(new Bundle(), AnimationType.NONE), false);
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

        if (editText.getId() == R.id.et_first_name && hasFocus == false) {
            result = mValidator.isValidFirstName(editText);
            errorMessage = getResources().getString(R.string.iap_first_name_error);
        }
        if (editText.getId() == R.id.et_last_name && hasFocus == false) {
            result = mValidator.isValidLastName(editText);
            errorMessage = getResources().getString(R.string.iap_last_name_error);
        }
        if (editText.getId() == R.id.et_town && hasFocus == false) {
            result = mValidator.isValidTown(editText);
            errorMessage = getResources().getString(R.string.iap_town_error);
        }
        if (editText.getId() == R.id.et_phone_number && hasFocus == false) {
            result = mValidator.isValidPhoneNumber(editText);
            errorMessage = getResources().getString(R.string.iap_phone_error);
        }
        if (editText.getId() == R.id.et_country && hasFocus == false) {
            result = mValidator.isValidCountry(editText);
            errorMessage = getResources().getString(R.string.iap_country_error);
        }
        if (editText.getId() == R.id.et_postal_code && hasFocus == false) {
            result = mValidator.isValidPostalCode(editText);
            errorMessage = getResources().getString(R.string.iap_postal_code_error);
        }
        if (editText.getId() == R.id.et_email && hasFocus == false) {
            result = mValidator.isValidEmail(editText);
            errorMessage = getResources().getString(R.string.iap_email_error);
        }
        if (editText.getId() == R.id.et_address && hasFocus == false) {
            result = mValidator.isValidAddress(editText);
            errorMessage = getResources().getString(R.string.iap_address_error);
        }

        if (!result) {
            mInlineFormsParent.showError((EditText) editText);

            int index = mInlineFormsParent.indexOfChild((ViewGroup) editText.getParent());
            TextView errorView = (TextView) ((ViewGroup) mInlineFormsParent.getChildAt(index + 1)).getChildAt(1);
            errorView.setText(errorMessage);
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
