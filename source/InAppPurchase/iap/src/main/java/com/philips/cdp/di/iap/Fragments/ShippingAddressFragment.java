/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.uikit.customviews.InlineForms;

public class ShippingAddressFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, AddressController.AddressListener {

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

    private InlineForms mInlineFormsParent;
    private Validator mValidator = null;

    @Override
    protected void updateTitle() {
        setTitle(R.string.iap_shipping_address);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.shipping_address_layout, container, false);
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

        mBtnContinue.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        validateFirstName();
        validateLastName();
        validateAddress();
        validateTown();
        validatePostalCode();
        validateCountry();
        validateEmail();
        validatePhone();

        mValidator = new Validator();

        return rootView;
    }


    private void validateEmail() {
        mInlineFormsParent.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.et_email && hasFocus == false) {
                    boolean result = mValidator.isValidEmail(editText);
                    if (!result) {
                        mInlineFormsParent.showError((EditText) editText);
                    }
                }
            }
        });
    }

    private void validateFirstName() {
        mInlineFormsParent.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.et_first_name && hasFocus == false) {
                    boolean result = mValidator.isValidFirstName(editText);
                    if (!result) {
                        mInlineFormsParent.showError((EditText) editText);
                    }
                }
            }
        });
    }

    private void validateLastName() {
        mInlineFormsParent.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.et_last_name && hasFocus == false) {
                    boolean result = mValidator.isValidLastName(editText);
                    if (!result) {
                        mInlineFormsParent.showError((EditText) editText);
                    }
                }
            }
        });
    }

    private void validateAddress() {
        mInlineFormsParent.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.et_address && hasFocus == false) {
                    boolean result = mValidator.isValidAddress(editText);
                    if (!result) {
                        mInlineFormsParent.showError((EditText) editText);
                    }
                }
            }
        });
    }

    private void validatePostalCode() {
        mInlineFormsParent.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.et_postal_code && hasFocus == false) {
                    boolean result = mValidator.isValidPostalCode(editText);
                    if (!result) {
                        mInlineFormsParent.showError((EditText) editText);
                    }
                }
            }
        });
    }

    private void validateCountry() {
        mInlineFormsParent.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.et_country && hasFocus == false) {
                    boolean result = mValidator.isValidCountry(editText);
                    if (!result) {
                        mInlineFormsParent.showError((EditText) editText);
                    }
                }
            }
        });
    }

    private void validatePhone() {
        mInlineFormsParent.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.et_phone_number && hasFocus == false) {
                    boolean result = mValidator.isValidPhoneNumber(editText);
                    if (!result) {
                        mInlineFormsParent.showError((EditText) editText);
                    }
                }
            }
        });
    }

    private void validateTown() {
        mInlineFormsParent.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.et_town && hasFocus == false) {
                    boolean result = mValidator.isValidTown(editText);
                    if (!result) {
                        mInlineFormsParent.showError((EditText) editText);
                    }
                }
            }
        });
    }

    @Override
    public void onFetchAddressSuccess(Message msg) {

    }

    @Override
    public void onFetchAddressFailure(final Message msg) {

    }

    @Override
    public void onCreateAddress(boolean isSuccess) {
        if (isSuccess) {
            //Navigate to billing Address
        } else {
            //Show error msg and stay on the page
        }
    }

    @Override
    public void onClick(final View v) {
        if (v == mBtnContinue) {
           /* IAPLog.d(IAPLog.SHIPPING_ADDRESS_FRAGMENT, "onClick ShippingAddressFragment");
            getMainActivity().addFragmentAndRemoveUnderneath(
                    OrderSummaryFragment.createInstance(AnimationType.NONE), false);
            AddressSelectionFragment.createInstance(AnimationType.NONE), false);*/
        } else if (v == mBtnCancel) {

        }
    }

    public static ShippingAddressFragment createInstance(AnimationType animType) {
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);

        return fragment;
    }


    public void checkFields() {
       /* String firstName = mEtFirstName.getText().toString().trim();
        String lastName = mEtLastName.getText().toString().trim();
        String address = mEtAddress.getText().toString().trim();
        String town = mEtTown.getText().toString().trim();
        String postalCode = mEtPostalCode.getText().toString().trim();
        String country = mEtCountry.getText().toString().trim();
        String email = mEtEmail.getText().toString().trim();
        String phoneNumber = mEtPhoneNumber.getText().toString().trim();*/

        if (mValidator.isValidFirstName(mEtFirstName) && mValidator.isValidLastName(mEtLastName)
                && mValidator.isValidAddress(mEtAddress) && mValidator.isValidPostalCode(mEtPostalCode)
                && mValidator.isValidEmail(mEtEmail) && mValidator.isValidPhoneNumber(mEtPhoneNumber)
                && mValidator.isValidTown(mEtTown) && mValidator.isValidCountry(mEtCountry)) {
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
