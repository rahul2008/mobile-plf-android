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
import android.widget.Button;
import android.widget.EditText;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.uikit.customviews.InlineForms;

public class ShippingAddressFragment extends BaseAnimationSupportFragment implements View.OnClickListener, AddressController.AddressListener {

    private Button mBtnCotinue;

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

        mBtnCotinue = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnCotinue.setOnClickListener(this);
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
    public void onFinish() {

    }

    @Override
    public void onClick(final View v) {
        if (v == mBtnCotinue) {
            IAPLog.d(IAPLog.SHIPPING_ADDRESS_FRAGMENT, "onClick ShippingAddressFragment");
            getMainActivity().addFragmentAndRemoveUnderneath(
                    OrderSummaryFragment.createInstance(AnimationType.NONE), false);
        }
    }

    public static ShippingAddressFragment createInstance(AnimationType animType) {
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
    }

}
