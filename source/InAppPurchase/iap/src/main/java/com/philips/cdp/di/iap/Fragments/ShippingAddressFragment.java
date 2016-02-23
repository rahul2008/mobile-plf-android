/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.uikit.customviews.InlineForms;

import java.util.HashMap;

public class ShippingAddressFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, AddressController.AddressListener, InlineForms.Validator,
        TextWatcher {
    private Context mContext;

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

    private AddressController mAddressController;
    private AddressFields mAddressFields;

    private InlineForms mInlineFormsParent;
    private Validator mValidator = null;

    private HashMap<String, String> addressFeilds = null;

    @Override
    protected void updateTitle() {
        setTitle(R.string.iap_address);
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

        mValidator = new Validator();
        mInlineFormsParent.setValidator(this);

        mAddressController = new AddressController(mContext, this);
        mAddressFields = new AddressFields();

        mEtFirstName.addTextChangedListener(this);
        mEtLastName.addTextChangedListener(this);
        mEtAddress.addTextChangedListener(this);
        mEtTown.addTextChangedListener(this);
        mEtPostalCode.addTextChangedListener(this);
        mEtCountry.addTextChangedListener(this);
        mEtEmail.addTextChangedListener(this);
        mEtPhoneNumber.addTextChangedListener(this);

        Bundle bundle = getArguments();
        if (null != bundle) {
            updateFeilds();
        }

        return rootView;
    }

    private void updateFeilds() {
        Bundle bundle = getArguments();
        addressFeilds = (HashMap) bundle.getSerializable(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY);
        if (null == addressFeilds) {
            IAPLog.d(IAPLog.SHIPPING_ADDRESS_FRAGMENT, "addressfeild is null = " + addressFeilds);
            return;
        }
        mBtnContinue.setText(getString(R.string.iap_save));
        mBtnContinue.requestFocus();
        mEtFirstName.setText(addressFeilds.get(ModelConstants.FIRST_NAME));
        mEtFirstName.requestFocus();
        mEtLastName.setText(addressFeilds.get(ModelConstants.LAST_NAME));
        mEtLastName.requestFocus();
        mEtTown.setText(addressFeilds.get(ModelConstants.TOWN));
        mEtTown.requestFocus();
        mEtPostalCode.setText(addressFeilds.get(ModelConstants.POSTAL_CODE));
        mEtPostalCode.requestFocus();
        mEtCountry.setText(addressFeilds.get(ModelConstants.COUNTRY_ISOCODE));
        mEtCountry.requestFocus();
        mEtAddress.setText(addressFeilds.get(ModelConstants.DEFAULT_ADDRESS));
        mEtAddress.requestFocus();
        mEtPhoneNumber.setText(addressFeilds.get(ModelConstants.PHONE_NUMBER));
        mEtPhoneNumber.requestFocus();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onFetchAddressSuccess(Message msg) {
        int requestCode = msg.what;
        switch (requestCode) {
            case RequestCode.UPDATE_ADDRESS:
                getMainActivity().addFragmentAndRemoveUnderneath
                        (AddressSelectionFragment.createInstance(AnimationType.NONE), false);
                break;
        }
    }

    @Override
    public void onFetchAddressFailure(final Message msg) {
        int requestCode = msg.what;
        switch (requestCode) {
            case RequestCode.UPDATE_ADDRESS:
                Toast.makeText(mContext, msg.obj.toString(), Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onCreateAddress(boolean isSuccess) {
        Utility.dismissProgressDialog();
        if (isSuccess) {
            if (getArguments().containsKey(IAPConstant.IS_SECOND_USER) && getArguments().getBoolean(IAPConstant.IS_SECOND_USER)) {
                getMainActivity().onBackPressed();
            } else {
                Bundle bundle = new Bundle();
                bundle.putSerializable(IAPConstant.ADDRESS_FIELDS, mAddressFields);
                getMainActivity().addFragmentAndRemoveUnderneath(
                        BillingAddressFragment.createInstance(bundle, AnimationType.NONE), false);
            }
            Toast.makeText(mContext, "Address created successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(mContext, "Address not created successfully", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(final View v) {
        if (v == mBtnContinue) {
            if (mBtnContinue.getText().toString().equalsIgnoreCase(getString(R.string.iap_save))) {
                if (Utility.isInternetConnected(mContext)) {
                    if (!Utility.isProgressDialogShowing()) {
                        Utility.showProgressDialog(mContext, getString(R.string.iap_update_address));
                        HashMap<String, String> addressHashMap = updateToHybrisTheFeilds();
                        mAddressController.updateAddress(addressHashMap);
                    }
                } else {
                    NetworkUtility.getInstance().showNetworkError(mContext);
                }
            } else {
                if (!Utility.isProgressDialogShowing()) {
                    if (Utility.isInternetConnected(mContext)) {
                        Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
                        mAddressController.createAddress(mAddressFields);
                    } else {
                        NetworkUtility.getInstance().showNetworkError(mContext);
                    }
                }
            }
        } else if (v == mBtnCancel) {
            getMainActivity().addFragmentAndRemoveUnderneath
                    (ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), false);
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

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
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
    public void onResume() {
        super.onResume();
        updateTitle();
    }

    private HashMap updateToHybrisTheFeilds() {
        HashMap<String, String> addressHashMap = new HashMap<>();
        addressHashMap.put(ModelConstants.FIRST_NAME, mEtFirstName.getText().toString());
        addressHashMap.put(ModelConstants.LAST_NAME, mEtLastName.getText().toString());
        addressHashMap.put(ModelConstants.TITLE_CODE, "mr");
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, mEtCountry.getText().toString());
        addressHashMap.put(ModelConstants.LINE_1, mEtAddress.getText().toString());
        addressHashMap.put(ModelConstants.POSTAL_CODE, mEtPostalCode.getText().toString());
        addressHashMap.put(ModelConstants.TOWN, mEtTown.getText().toString());
        addressHashMap.put(ModelConstants.ADDRESS_ID, addressFeilds.get(ModelConstants.ADDRESS_ID));
        addressHashMap.put(ModelConstants.LINE_2, "");
        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, mEtAddress.getText().toString());
        addressHashMap.put(ModelConstants.PHONE_NUMBER, mEtPhoneNumber.getText().toString());
        return addressHashMap;
    }

    public static ShippingAddressFragment createInstance(Bundle args, AnimationType animType) {
        ShippingAddressFragment fragment = new ShippingAddressFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }
}
