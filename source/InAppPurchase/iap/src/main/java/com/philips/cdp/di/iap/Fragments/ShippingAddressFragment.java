/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.address.Validator;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.error.Error;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.SalutationDropDown;
import com.philips.cdp.di.iap.view.StateDropDown;
import com.philips.cdp.uikit.customviews.InlineForms;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ShippingAddressFragment extends BaseAnimationSupportFragment
        implements View.OnClickListener, AddressController.AddressListener,
        PaymentController.PaymentListener, InlineForms.Validator,
        AdapterView.OnItemSelectedListener, SalutationDropDown.SalutationListener,
        StateDropDown.StateListener {
    private Context mContext;

    private LinearLayout mlLState;
    private EditText mEtFirstName;
    private EditText mEtLastName;
    private EditText mEtSalutation;
    private EditText mEtAddressLineOne;
    private EditText mEtAddressLineTwo;
    private EditText mEtTown;
    private EditText mEtPostalCode;
    private EditText mEtCountry;
    private EditText mEtState;
    private EditText mEtEmail;
    private EditText mEtPhoneNumber;

    private Button mBtnContinue;
    private Button mBtnCancel;

    private PaymentController mPaymentController;
    private AddressController mAddressController;
    private AddressFields mAddressFields;

    private InlineForms mInlineFormsParent;
    private Validator mValidator = null;

    private SalutationDropDown mSalutationDropDown;
    private StateDropDown mStateDropDown;

    private HashMap<String, String> mAddressFieldsHashmap = null;
    private Addresses mAddresses;
    private Drawable imageArrow;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.iap_shipping_address_layout, container, false);
        mInlineFormsParent = (InlineForms) rootView.findViewById(R.id.InlineForms);

        mlLState = (LinearLayout) rootView.findViewById(R.id.ll_state);
        mEtFirstName = (EditText) rootView.findViewById(R.id.et_first_name);
        mEtLastName = (EditText) rootView.findViewById(R.id.et_last_name);
        mEtSalutation = (EditText) rootView.findViewById(R.id.et_salutation);
        mEtAddressLineOne = (EditText) rootView.findViewById(R.id.et_address_line_one);
        mEtAddressLineTwo = (EditText) rootView.findViewById(R.id.et_address_line_two);
        mEtTown = (EditText) rootView.findViewById(R.id.et_town);
        mEtPostalCode = (EditText) rootView.findViewById(R.id.et_postal_code);
        mEtCountry = (EditText) rootView.findViewById(R.id.et_country);
        mEtState = (EditText) rootView.findViewById(R.id.et_state);
        mEtEmail = (EditText) mInlineFormsParent.findViewById(R.id.et_email);
        mEtPhoneNumber = (EditText) rootView.findViewById(R.id.et_phone_number);

        mBtnContinue = (Button) rootView.findViewById(R.id.btn_continue);
        mBtnCancel = (Button) rootView.findViewById(R.id.btn_cancel);

        mBtnContinue.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mValidator = new Validator();
        mInlineFormsParent.setValidator(this);

        mAddressController = new AddressController(mContext, this);
        mPaymentController = new PaymentController(mContext, this);
        mAddressFields = new AddressFields();

        mEtEmail.setText(HybrisDelegate.getInstance(getContext()).getStore().getJanRainEmail());

        mEtFirstName.addTextChangedListener(new IAPTextWatcher(mEtFirstName));
        mEtLastName.addTextChangedListener(new IAPTextWatcher(mEtLastName));
        mEtAddressLineOne.addTextChangedListener(new IAPTextWatcher(mEtAddressLineOne));
        mEtAddressLineTwo.addTextChangedListener(new IAPTextWatcher(mEtAddressLineTwo));
        mEtTown.addTextChangedListener(new IAPTextWatcher(mEtTown));
        mEtPostalCode.addTextChangedListener(new IAPTextWatcher(mEtPostalCode));
        mEtCountry.addTextChangedListener(new IAPTextWatcher(mEtCountry));
        mEtEmail.addTextChangedListener(new IAPTextWatcher(mEtEmail));
        mEtPhoneNumber.addTextChangedListener(new IAPTextWatcher(mEtPhoneNumber));

        Bundle bundle = getArguments();
        if (null != bundle) {
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
                mStateDropDown.show();
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
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onGetPaymentDetails(Message msg) {
        Utility.dismissProgressDialog();
        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
            CartModelContainer.getInstance().setShippingAddressFields(mAddressFields);
            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.SHIPPING_ADDRESS_FIELDS, mAddressFields);
            addFragment(
                    BillingAddressFragment.createInstance(bundle, AnimationType.NONE), null);
        } else if ((msg.obj instanceof IAPNetworkError)) {
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
        } else if ((msg.obj instanceof PaymentMethods)) {
            PaymentMethods mPaymentMethods = (PaymentMethods) msg.obj;
            List<PaymentMethod> mPaymentMethodsList = mPaymentMethods.getPayments();

            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.SHIPPING_ADDRESS_FIELDS, mAddressFields);
            bundle.putSerializable(IAPConstant.PAYMENT_METHOD_LIST, (Serializable) mPaymentMethodsList);
            addFragment(
                    PaymentSelectionFragment.createInstance(bundle, AnimationType.NONE), null);
        }
    }

    @Override
    public void onClick(final View v) {

        Utility.hideKeypad(mContext);

        if (v == mBtnContinue) {
            //Edit and save address
            if (mBtnContinue.getText().toString().equalsIgnoreCase(getString(R.string.iap_save))) {
                if (!Utility.isProgressDialogShowing()) {
                    if (Utility.isInternetConnected(mContext)) {
                        Utility.showProgressDialog(mContext, getString(R.string.iap_update_address));
                        HashMap<String, String> addressHashMap = addressPayload();
                        mAddressController.updateAddress(addressHashMap);
                    } else {
                        NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                                getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
                    }
                }
            } else {//Add new address
                if (!Utility.isProgressDialogShowing()) {
                    if (Utility.isInternetConnected(mContext)) {
                        Utility.showProgressDialog(mContext, getString(R.string.iap_please_wait));
                        mAddressController.createAddress(mAddressFields);
                    } else {
                        NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                                getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
                    }
                }
            }
        } else if (v == mBtnCancel) {
            if (getArguments().containsKey(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY)) {
                addFragment
                        (AddressSelectionFragment.createInstance(new Bundle(), AnimationType.NONE), null);
            } else {
                addFragment
                        (ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
            }
        }
    }

    @Override
    public void onGetAddress(Message msg) {
        Utility.dismissProgressDialog();
        if (msg.what == RequestCode.UPDATE_ADDRESS) {
            if (msg.obj instanceof IAPNetworkError) {
                IAPNetworkError iapNetworkError = (IAPNetworkError) msg.obj;
                if (null != iapNetworkError.getServerError()) {
                    for (int i = 0; i < iapNetworkError.getServerError().getErrors().size(); i++) {
                        Error error = iapNetworkError.getServerError().getErrors().get(i);
                        showErrorFromServer(error);
                    }
                } else {
                    NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                            getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
                }
            } else {
                addFragment(AddressSelectionFragment.createInstance(new Bundle(),
                        AnimationType.NONE), null);
            }
        }
    }

    @Override
    public void onCreateAddress(Message msg) {
        if (msg.obj instanceof Addresses) {
            mBtnContinue.setEnabled(true);
            mAddresses = (Addresses) msg.obj;
            mAddressController.setDeliveryAddress(mAddresses.getId());
        } else if (msg.obj instanceof IAPNetworkError) {
            Utility.dismissProgressDialog();

            IAPNetworkError iapNetworkError = (IAPNetworkError) msg.obj;
            if (null != iapNetworkError.getServerError()) {
                for (int i = 0; i < iapNetworkError.getServerError().getErrors().size(); i++) {
                    Error error = iapNetworkError.getServerError().getErrors().get(i);
                    showErrorFromServer(error);
                }
            } else {
                NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                        getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
            }
        }
    }

    private void showErrorFromServer(Error error) {
        if (error != null && (error.getSubject() != null) && error.getSubject().equalsIgnoreCase(ModelConstants.COUNTRY_ISOCODE)) {
            String errorMessage = getResources().getString(R.string.iap_country_error);
            mInlineFormsParent.setErrorMessage(errorMessage);
            mInlineFormsParent.showError(mEtCountry);
            mBtnContinue.setEnabled(false);
        }
    }

    public void checkFields() {
        String firstName = mEtFirstName.getText().toString();
        String lastName = mEtLastName.getText().toString();
        String addressLineOne = mEtAddressLineOne.getText().toString();
        String addressLineTwo = mEtAddressLineTwo.getText().toString();
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

            if (mlLState.getVisibility() == View.VISIBLE) {
                mAddressFields.setState(mEtState.getText().toString());
            }

            mBtnContinue.setEnabled(true);
        } else {
            mBtnContinue.setEnabled(false);
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
            showUSRegions();
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
            result = mValidator.isValidAddress(mEtAddressLineOne.getText().toString());
            errorMessage = getResources().getString(R.string.iap_address_error);
        }
        if (editText.getId() == R.id.et_address_line_two && !hasFocus) {
            if (mEtAddressLineTwo.getText().toString().trim().equals("")) {
                result = true;
            } else {
                result = mValidator.isValidAddress(mEtAddressLineTwo.getText().toString());
                errorMessage = getResources().getString(R.string.iap_address_error);
            }
        }

        if (!result) {
            mInlineFormsParent.setErrorMessage(errorMessage);
            mInlineFormsParent.showError((EditText) editText);
            mBtnContinue.setEnabled(false);
        } else {
            mInlineFormsParent.removeError(editText);
            checkFields();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_address);
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
            CartModelContainer.getInstance().setDeliveryAddress(mAddresses);
            mAddressController.setDeliveryMode();
        } else {
            Utility.dismissProgressDialog();
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
        }
    }

    @Override
    public void onSetDeliveryModes(final Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            mPaymentController.getPaymentDetails();
        } else {
            Utility.dismissProgressDialog();
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok),
                    getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
        }
    }

    @Override
    public void onSalutationSelect(String salutation) {
        mEtSalutation.setText(salutation);

    }

    @Override
    public void onStateSelect(String state) {
        mEtState.setText(state);
    }

    private void showUSRegions() {
        if (mEtCountry.getText().toString().equals("US")) {
            mlLState.setVisibility(View.VISIBLE);
        } else {
            mlLState.setVisibility(View.GONE);
        }
    }

    private class IAPTextWatcher implements TextWatcher {

        private EditText mEditText;

        public IAPTextWatcher(EditText editText) {
            mEditText = editText;
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            validate(mEditText, false);
        }

        public void afterTextChanged(Editable s) {
        }
    }

    private HashMap<String, String> addressPayload() {
        HashMap<String, String> addressHashMap = new HashMap<>();
        addressHashMap.put(ModelConstants.FIRST_NAME, mEtFirstName.getText().toString());
        addressHashMap.put(ModelConstants.LAST_NAME, mEtLastName.getText().toString());
        addressHashMap.put(ModelConstants.LINE_1, mEtAddressLineOne.getText().toString());
        addressHashMap.put(ModelConstants.LINE_2, mEtAddressLineTwo.getText().toString());
        addressHashMap.put(ModelConstants.TITLE_CODE, mEtSalutation.getText().toString().toLowerCase(Locale.getDefault()));
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, mEtCountry.getText().toString());
        addressHashMap.put(ModelConstants.POSTAL_CODE, mEtPostalCode.getText().toString());
        addressHashMap.put(ModelConstants.TOWN, mEtTown.getText().toString());
        addressHashMap.put(ModelConstants.ADDRESS_ID, mAddressFieldsHashmap.get(ModelConstants.ADDRESS_ID));
        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, mEtAddressLineOne.getText().toString());
        addressHashMap.put(ModelConstants.PHONE_NUMBER, mEtPhoneNumber.getText().toString());
        addressHashMap.put(ModelConstants.EMAIL_ADDRESS, mEtEmail.getText().toString());

        if (mlLState.getVisibility() == View.VISIBLE) {
            mAddressFields.setState(mEtState.getText().toString());
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
        mEtAddressLineOne.setText(mAddressFieldsHashmap.get(ModelConstants.DEFAULT_ADDRESS));
        mEtAddressLineTwo.setText(mAddressFieldsHashmap.get(ModelConstants.LINE_2));
        mEtTown.setText(mAddressFieldsHashmap.get(ModelConstants.TOWN));
        mEtPostalCode.setText(mAddressFieldsHashmap.get(ModelConstants.POSTAL_CODE));
        mEtCountry.setText(mAddressFieldsHashmap.get(ModelConstants.COUNTRY_ISOCODE));
        mEtPhoneNumber.setText(mAddressFieldsHashmap.get(ModelConstants.PHONE_NUMBER));
        mEtEmail.setText(mAddressFieldsHashmap.get(ModelConstants.EMAIL_ADDRESS));

        if (mAddressFieldsHashmap.containsKey(ModelConstants.STATE) &&
                mAddressFieldsHashmap.get(ModelConstants.STATE) != null) {
            mEtState.setText(mAddressFieldsHashmap.get(ModelConstants.STATE));
            mlLState.setVisibility(View.VISIBLE);
        } else {
            mlLState.setVisibility(View.GONE);
        }

        setRequestFocus();
    }

    private void setRequestFocus() {
        mEtFirstName.requestFocus();
        mEtLastName.requestFocus();
        mEtSalutation.requestFocus();
        mEtAddressLineOne.requestFocus();
        mEtAddressLineTwo.requestFocus();
        mEtTown.requestFocus();
        mEtPostalCode.requestFocus();
        mEtCountry.requestFocus();
        mEtEmail.requestFocus();
        mEtPhoneNumber.requestFocus();
        if (mlLState.getVisibility() == View.VISIBLE) {
            mEtState.requestFocus();
        }
    }

    @Override
    public void onGetDeliveryModes(final Message msg) {
        //NOP
    }

    @Override
    public void onSetPaymentDetails(Message msg) {

    }

    @Override
    public void onGetDeliveryAddress(final Message msg) {
        //NOP
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
