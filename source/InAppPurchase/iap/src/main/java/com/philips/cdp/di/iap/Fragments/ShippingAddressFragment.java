/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.uikit.customviews.InlineForms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShippingAddressFragment extends BaseAnimationSupportFragment implements View.OnClickListener, AddressController.AddressListener {

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

    EditText firstName;
    EditText lastName;
    EditText firstAddressLine;
    EditText town;
    EditText postalCode;
    EditText country;
    EditText email;
    EditText phoneNumber;
    InlineForms layout;
    Validator mValidator = null;

    @Override
    protected void updateTitle() {
        setTitle(R.string.iap_shipping_address);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shipping_address_layout, container, false);
        mEtFirstName = (EditText) rootView.findViewById(R.id.et_first_name);
        mEtLastName = (EditText) rootView.findViewById(R.id.et_last_name);
        mEtAddress = (EditText) rootView.findViewById(R.id.et_address);
        mEtTown = (EditText) rootView.findViewById(R.id.et_town);
        mEtPostalCode = (EditText) rootView.findViewById(R.id.et_postal_code);
        mEtCountry = (EditText) rootView.findViewById(R.id.et_country);
        mEtEmail = (EditText) rootView.findViewById(R.id.et_email);
        mEtPhoneNumber = (EditText) rootView.findViewById(R.id.et_phone_number);

        mBtnContinue = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);
        mBtnContinue.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        return rootView;
    }

    private void validateEmail() {
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                boolean result = mValidator.validateEmail(email);
                if (result) {
                    /**
                     * Error Layout should be removed after the entered text is verified as the right Email Address
                     */
                    layout.removeError(email);
                }
            }
        });

        layout.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.et_email && hasFocus == false) {
                    boolean result = mValidator.validateEmail(editText);
                    if (!result) {
                        layout.showError((EditText) editText);
                    }
                }
            }
        });
    }

    @Override
    public void onFetchAddressSuccess(GetShippingAddressData data) {

    }

    @Override
    public void onFetchAddressFailure(final Message msg) {

    }

    @Override
    public void onClick(final View v) {
        if (v == mBtnContinue) {
            IAPLog.d(IAPLog.SHIPPING_ADDRESS_FRAGMENT, "onClick ShippingAddressFragment");
            getMainActivity().addFragmentAndRemoveUnderneath(
                    OrderSummaryFragment.createInstance(AnimationType.NONE), false);
            //AddressSelectionFragment.createInstance(AnimationType.NONE), false);
        }
    }

    public static ShippingAddressFragment createInstance(AnimationType animType) {
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    public boolean isValidEmail(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public boolean isValidPostalCode(CharSequence postalCode) {
        if (TextUtils.isEmpty(postalCode)) {
            return false;
        } else {
            Pattern postalCodePattern = Pattern.compile("^[A-Z0-9]*$");
            Matcher match = postalCodePattern.matcher(postalCode);
            return match.matches();
        }
    }

    public boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            return false;
        } else {
            return PhoneNumberUtils.isGlobalPhoneNumber(phoneNumber.toString());
        }
    }

    public boolean isValidFirstName(CharSequence firstName) {
        if (TextUtils.isEmpty(firstName)) {
            return false;
        } else {
            Pattern firstNamePattern = Pattern.compile("^[\\p{IsAlphabetic}]+( [\\p{IsAlphabetic}]+)*$");
            Matcher match = firstNamePattern.matcher(firstName);
            return match.matches();
        }
    }

    public boolean isValidLastName(CharSequence lastName) {
        if (TextUtils.isEmpty(lastName)) {
            return false;
        }
        return true;
    }

    public boolean isValidAddress(CharSequence address) {
        if (TextUtils.isEmpty(address)) {
            return false;
        }
        return true;
    }

    public boolean isValidTown(CharSequence town){
        if (TextUtils.isEmpty(town)) {
            return false;
        }
        return true;
    }

    public boolean isValidCountry(CharSequence country){
        if (TextUtils.isEmpty(country)) {
            return false;
        }
        return true;
    }

    public void checkFields() {
        String firstName = mEtFirstName.getText().toString().trim();
        String lastName = mEtLastName.getText().toString().trim();
        String address = mEtAddress.getText().toString().trim();
        String town = mEtTown.getText().toString().trim();
        String postalCode = mEtPostalCode.getText().toString().trim();
        String country = mEtCountry.getText().toString().trim();
        String email = mEtEmail.getText().toString().trim();
        String phoneNumber = mEtPhoneNumber.getText().toString().trim();

        if (isValidFirstName(firstName) && isValidLastName(lastName)
                && isValidAddress(address) && isValidPostalCode(postalCode)
                && isValidEmail(email) && isValidPhoneNumber(phoneNumber)
                && isValidTown(town) && isValidCountry(country)) {
            mBtnContinue.setEnabled(true);
        } else {
            mBtnContinue.setEnabled(false);
        }
    }

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
    }

}
