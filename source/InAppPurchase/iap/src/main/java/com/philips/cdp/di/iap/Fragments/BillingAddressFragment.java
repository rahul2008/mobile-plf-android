package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.philips.cdp.di.iap.view.SalutationDropDown;
import com.philips.cdp.uikit.customviews.InlineForms;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.philips.cdp.uikit.drawable.VectorDrawable;

public class BillingAddressFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, InlineForms.Validator,
        SalutationDropDown.SalutationListener {

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
    private EditText mEtSalutation;

    private Button mBtnContinue;
    private Button mBtnCancel;

    private AddressFields mAddressFields;

    private InlineForms mInlineFormsParent;
    private Validator mValidator = null;

    private Drawable imageArrow;

    private SalutationDropDown mSalutationDropDown;

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
        mEtSalutation = (EditText) rootView.findViewById(R.id.et_salutation);

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

        mEtFirstName.addTextChangedListener(new IAPTextWatcher(mEtFirstName));
        mEtLastName.addTextChangedListener(new IAPTextWatcher(mEtLastName));
        mEtAddresslineOne.addTextChangedListener(new IAPTextWatcher(mEtAddresslineOne));
        mEtAddresslineTwo.addTextChangedListener(new IAPTextWatcher(mEtAddresslineTwo));
        mEtTown.addTextChangedListener(new IAPTextWatcher(mEtTown));
        mEtPostalCode.addTextChangedListener(new IAPTextWatcher(mEtPostalCode));
        mEtCountry.addTextChangedListener(new IAPTextWatcher(mEtCountry));
        mEtEmail.addTextChangedListener(new IAPTextWatcher(mEtEmail));
        mEtPhoneNumber.addTextChangedListener(new IAPTextWatcher(mEtPhoneNumber));

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


        return rootView;
    }

    private void setImageArrow() {
        imageArrow = VectorDrawable.create(mContext, R.drawable.iap_product_count_drop_down);
        int width = (int) getResources().getDimension(R.dimen.iap_count_drop_down_icon_width);
        int height = (int) getResources().getDimension(R.dimen.iap_count_drop_down_icon_height);
        imageArrow.setBounds(0, 0, width, height);
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
            mEtSalutation.setText(mAddressFields.getTitleCode());
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
                && mValidator.isValidAddress(addressLineOne) && (addressLineTwo.trim().equals("") || mValidator.isValidAddress(addressLineTwo))
                && mValidator.isValidPostalCode(postalCode)
                && mValidator.isValidEmail(email) && mValidator.isValidPhoneNumber(phoneNumber)
                && mValidator.isValidTown(town) && mValidator.isValidCountry(country)) {

            mAddressFields.setFirstName(firstName);
            mAddressFields.setLastName(lastName);
            mAddressFields.setTitleCode(mEtSalutation.getText().toString());
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
        mEtSalutation.setText("");
        mEtAddresslineOne.setText("");
        mEtAddresslineTwo.setText("");
        mEtTown.setText("");
        mEtPostalCode.setText("");
        mEtCountry.setText("");
        mEtEmail.setText("");
        mEtPhoneNumber.setText("");
        enableAllFields();
        enableFocus();
        removeErrorInAllFields();
    }

    private void disableAllFields() {
        removeErrorInAllFields();
        disableFocus();
        mEtFirstName.setEnabled(false);
        mEtLastName.setEnabled(false);
        mEtSalutation.setEnabled(false);
        mEtAddresslineOne.setEnabled(false);
        mEtAddresslineTwo.setEnabled(false);
        mEtTown.setEnabled(false);
        mEtPostalCode.setEnabled(false);
        mEtCountry.setEnabled(false);
        mEtEmail.setEnabled(false);
        mEtPhoneNumber.setEnabled(false);
    }

    private void disableFocus() {
        mEtFirstName.setFocusable(false);
        mEtLastName.setFocusable(false);
        mEtSalutation.setFocusable(false);
        mEtAddresslineOne.setFocusable(false);
        mEtAddresslineTwo.setFocusable(false);
        mEtTown.setFocusable(false);
        mEtPostalCode.setFocusable(false);
        mEtCountry.setFocusable(false);
        mEtEmail.setFocusable(false);
        mEtPhoneNumber.setFocusable(false);
    }

    private void enableAllFields() {
        mEtFirstName.setEnabled(true);
        mEtLastName.setEnabled(true);
        mEtSalutation.setEnabled(true);
        mEtAddresslineOne.setEnabled(true);
        mEtAddresslineTwo.setEnabled(true);
        mEtTown.setEnabled(true);
        mEtPostalCode.setEnabled(true);
        mEtCountry.setEnabled(true);
        mEtEmail.setEnabled(true);
        mEtPhoneNumber.setEnabled(true);
    }

    private void enableFocus() {
        mEtFirstName.setFocusable(true);
        mEtFirstName.setFocusableInTouchMode(true);
        mEtLastName.setFocusable(true);
        mEtLastName.setFocusableInTouchMode(true);
        mEtSalutation.setFocusable(true);
        mEtSalutation.setFocusableInTouchMode(true);
        mEtAddresslineOne.setFocusable(true);
        mEtAddresslineOne.setFocusableInTouchMode(true);
        mEtAddresslineTwo.setFocusable(true);
        mEtAddresslineTwo.setFocusableInTouchMode(true);
        mEtTown.setFocusable(true);
        mEtTown.setFocusableInTouchMode(true);
        mEtPostalCode.setFocusable(true);
        mEtPostalCode.setFocusableInTouchMode(true);
        mEtCountry.setFocusable(true);
        mEtCountry.setFocusableInTouchMode(true);
        mEtEmail.setFocusable(true);
        mEtEmail.setFocusableInTouchMode(true);
        mEtPhoneNumber.setFocusable(true);
        mEtPhoneNumber.setFocusableInTouchMode(true);
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
            if (getArguments().containsKey(IAPConstant.FROM_PAYMENT_SELECTION) &&
                    getArguments().getBoolean(IAPConstant.FROM_PAYMENT_SELECTION)) {
                getFragmentManager().popBackStackImmediate();
            } else {
                addFragment
                        (ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
            }
        }
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
            if(mEtAddresslineTwo.getText().toString().trim().equals("")){
                result = true;
            }else {
                result = mValidator.isValidAddress(mEtAddresslineTwo.getText().toString());
                errorMessage = getResources().getString(R.string.iap_address_error);
            }
        }

        if (!result) {
            mInlineFormsParent.setErrorMessage(errorMessage);
            mInlineFormsParent.showError((EditText) editText);
        } else {
            mInlineFormsParent.removeError(editText);
            checkFields();
        }
    }

    public static BillingAddressFragment createInstance(Bundle args, AnimationType animType) {
        BillingAddressFragment fragment = new BillingAddressFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSalutationSelect(String salutation) {
        mEtSalutation.setText(salutation);
    }

    private class IAPTextWatcher implements TextWatcher {

        private EditText mEditText;

        public IAPTextWatcher(EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                validate(mEditText, false);
            }
        }

        public void afterTextChanged(Editable s) {
        }
    }
}
