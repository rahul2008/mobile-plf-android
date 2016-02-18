/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.uikit.customviews.InlineForms;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShippingAddressFragment extends BaseNoAnimationFragment implements AddressController.AddressListener{

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
        mValidator = new Validator();
        layout = (InlineForms) rootView.findViewById(R.id.InlineForms);

        firstName = (EditText) layout.findViewById(R.id.et_first_name);
        lastName = (EditText) layout.findViewById(R.id.et_last_name);
        firstAddressLine = (EditText) layout.findViewById(R.id.et_address);
        town = (EditText) layout.findViewById(R.id.et_town);
        postalCode = (EditText) layout.findViewById(R.id.et_postal_code);
        country = (EditText) layout.findViewById(R.id.et_country);
        email = (EditText) layout.findViewById(R.id.et_email);
        phoneNumber = (EditText) layout.findViewById(R.id.et_phone_number);

        validateEmail();

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
                boolean result = mValidator.validateEmail(email, email.hasFocus());
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
    public void onFinish() {

    }



}
