package com.philips.cdp.di.iap.screens;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.error.Error;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.platform.uid.view.widget.CheckBox;

import java.util.HashMap;

public class AddressFragment extends InAppBaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, AddressContractor {

    public static final String TAG = AddressFragment.class.getName();
    Context mContext;
    protected CheckBox checkBox;
    protected Button mBtnContinue;
    protected Button mBtnCancel;
    private RelativeLayout mParentContainer;
    AddressFields shippingAddressFields;
    AddressFields billingAddressFields;
    private TextView tv_checkOutSteps;
    private AddressBillingView addressBillingView;
    private AddressShippingView addressShippingView;
    private View billingView;
    private View shoppingView;
    private AddressPresenter addressPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.iap_address, container, false);
        addressPresenter = new AddressPresenter(this);
        initializeViews(view,addressPresenter);
        return view;
    }

    void initializeViews(View rootView,AddressPresenter addressPresenter) {

        mParentContainer = rootView.findViewById(R.id.address_container);
        billingView = rootView.findViewById(R.id.dls_iap_address_billing);
        shoppingView = rootView.findViewById(R.id.dls_iap_address_shipping);

        addressBillingView = new AddressBillingView(addressPresenter);
        addressShippingView = new AddressShippingView(addressPresenter);

        tv_checkOutSteps = rootView.findViewById(R.id.tv_checkOutSteps);

        updateCheckoutStepNumber();

        mBtnContinue = rootView.findViewById(R.id.btn_continue);
        mBtnCancel = rootView.findViewById(R.id.btn_cancel);
        mBtnContinue.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);
        checkBox = rootView.findViewById(R.id.use_this_address_checkbox);

        disableView(billingView);

        upDateUi(true);

        checkBox.setOnCheckedChangeListener(this);
    }

    private void upDateUi(boolean isChecked) {
        Bundle bundle = getArguments();
        updateCheckoutStepNumber(); // for default
        if (null != bundle && bundle.containsKey(IAPConstant.FROM_PAYMENT_SELECTION)) {
            if (bundle.containsKey(IAPConstant.UPDATE_BILLING_ADDRESS_KEY)) {
                updateCheckoutStepNumber();
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(true);
                enableView(billingView);
                disableView(shoppingView);
                HashMap<String, String> mAddressFieldsHashmap = (HashMap<String, String>) bundle.getSerializable(IAPConstant.UPDATE_BILLING_ADDRESS_KEY);
                getDLSBillingAddress().updateFields(mAddressFieldsHashmap);
            }
        }

        if (null != bundle && bundle.containsKey(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY)) {
            updateCheckoutStepNumber();
            checkBox.setVisibility(View.GONE);
            HashMap<String, String> mAddressFieldsHashmap = (HashMap<String, String>) bundle.getSerializable(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY);
            addressShippingView.updateFields(mAddressFieldsHashmap);
        }

        if (null != bundle && bundle.containsKey(IAPConstant.ADD_BILLING_ADDRESS) && bundle.containsKey(IAPConstant.UPDATE_BILLING_ADDRESS_KEY)) {
            updateCheckoutStepNumber();
            checkBox.setVisibility(View.VISIBLE);
            if (!isChecked) {
                //((AddressBillingView) billingFragment).disableAllFields();
                getDLSBillingAddress().clearAllFields();
                mBtnContinue.setEnabled(false);
                Utility.isAddressFilledFromDeliveryAddress = true;
                getDLSBillingAddress().enableAllFields();

            } else {
                // ((AddressBillingView) billingFragment).enableAllFields();
                getDLSBillingAddress().disableAllFields();
                Utility.isAddressFilledFromDeliveryAddress = true;
                mBtnContinue.setEnabled(true);
                HashMap<String, String> mAddressFieldsHashmap = (HashMap<String, String>) bundle.getSerializable(IAPConstant.UPDATE_BILLING_ADDRESS_KEY);
                getDLSBillingAddress().updateFields(mAddressFieldsHashmap);

            }
            disableView(shoppingView);
            enableView(billingView);

        }
    }

    private void updateCheckoutStepNumber() {
        tv_checkOutSteps.setText(String.format(mContext.getString(R.string.iap_checkout_steps), "2"));
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility(R.string.iap_checkout, true);
        setCartIconVisibility(false);
    }

    public static AddressFragment createInstance(Bundle args, AnimationType animType) {
        AddressFragment fragment = new AddressFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onClick(View v) {

        if (!isNetworkConnected()) return;
        if (v == mBtnContinue) {
            createCustomProgressBar(mParentContainer, BIG);
            //Edit and save address
            if (mBtnContinue.getText().toString().equalsIgnoreCase(mContext.getString(R.string.iap_save))) {
                saveShippingAddressToBackend();
            } else {
                createNewAddressOrUpdateIfAddressIDPresent();
            }
            // removeStaticFragments();
        } else if (v == mBtnCancel) {
            Fragment fragment = getFragmentManager().findFragmentByTag(BuyDirectFragment.TAG);
            if (fragment != null) {
                moveToVerticalAppByClearingStack();
            } else {
                getFragmentManager().popBackStackImmediate();
            }
        }

        Utility.hideKeypad(getActivity());

    }


    private void createNewAddressOrUpdateIfAddressIDPresent() {
        // createCustomProgressBar(mParentContainer,BIG);
        if (shippingAddressFields != null) {
            CartModelContainer.getInstance().setShippingAddressFields(shippingAddressFields);
        }
        if (checkBox.isChecked()) {
            CartModelContainer.getInstance().setSwitchToBillingAddress(true);
            CartModelContainer.getInstance().setBillingAddress(shippingAddressFields);
        }
        if (!CartModelContainer.getInstance().isAddessStateVisible()) {
            shippingAddressFields.setRegionIsoCode(null);
        }

        HashMap<String, String> updateAddressPayload = new HashMap<>();
        if (mBtnContinue.getText().toString().equalsIgnoreCase(getString(R.string.iap_save)))
            updateAddressPayload = addressPresenter.addressPayload(shippingAddressFields);
        else {
            if (checkBox.isChecked() && billingAddressFields == null) {
                billingAddressFields = shippingAddressFields;
                if (billingAddressFields != null) {
                    updateAddressPayload = addressPresenter.addressPayload(billingAddressFields);
                }
            }
        }
        if (!getArguments().getBoolean(IAPConstant.FROM_PAYMENT_SELECTION)) {
            if (CartModelContainer.getInstance().getAddressId() != null && CartModelContainer.getInstance().getAddressFromDelivery() != null) {
                if (CartModelContainer.getInstance().isAddessStateVisible() && CartModelContainer.getInstance().getRegionIsoCode() != null) {
                    updateAddressPayload.put(ModelConstants.REGION_ISOCODE, CartModelContainer.getInstance().getRegionIsoCode());
                }

                if (billingView.getVisibility() == View.VISIBLE && billingAddressFields != null) {
                    CartModelContainer.getInstance().setBillingAddress(billingAddressFields);
                    hideProgressBar();
                    addFragment(OrderSummaryFragment.createInstance(new Bundle(), AnimationType.NONE), OrderSummaryFragment.TAG, true);
                    mBtnContinue.setEnabled(true);
                } else {
                    updateAddressPayload.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
                    addressPresenter.updateAddress(updateAddressPayload);
                    mBtnContinue.setEnabled(false);
                }
                CartModelContainer.getInstance().setAddressIdFromDelivery(null);
            } else {
                CartModelContainer.getInstance().setShippingAddressFields(shippingAddressFields);
                addressPresenter.createAddress(shippingAddressFields);
            }
        } else {
            addressPresenter.setBillingAddressAndOpenOrderSummary();
        }
    }

    private void saveShippingAddressToBackend() {
        createCustomProgressBar(mParentContainer, BIG);
        HashMap<String, String> addressHashMap = addressPresenter.addressPayload(shippingAddressFields);
        addressPresenter.updateAddress(addressHashMap);
    }




    private void showError(Message msg) {
        IAPNetworkError iapNetworkError = (IAPNetworkError) msg.obj;
        if (null != iapNetworkError.getServerError()) {
            for (int i = 0; i < iapNetworkError.getServerError().getErrors().size(); i++) {
                Error error = iapNetworkError.getServerError().getErrors().get(i);
                NetworkUtility.getInstance().showErrorDialog(mContext, getFragmentManager(),
                        getString(R.string.iap_ok), getString(R.string.iap_server_error),
                        error.getMessage());
                mBtnContinue.setEnabled(false);
            }
        }
        mBtnContinue.setEnabled(false);
    }


    @Override
    public void setShippingAddressFields(AddressFields shippingAddressFields) {
        this.shippingAddressFields = shippingAddressFields;
    }

    @Override
    public boolean getCheckBoxState() {
        return checkBox.isChecked();
    }

    @Override
    public void setContinueButtonState(boolean state) {
        mBtnContinue.setText(getString(R.string.iap_continue));
        mBtnContinue.setEnabled(state);
    }

    @Override
    public void setBillingAddressFields(AddressFields addressFields) {
        this.billingAddressFields = addressFields;
        CartModelContainer.getInstance().setBillingAddress(addressFields);
    }


    @Override
    public View getShippingAddressView() {
        return shoppingView;
    }

    @Override
    public View getBillingAddressView() {
        return billingView;
    }

    @Override
    public Activity getActivityContext() {
        return getActivity();
    }

    @Override
    public FragmentActivity getFragmentActivity() {
        return getActivity();
    }

    @Override
    public AddressBillingView getDLSBillingAddress() {
        return addressBillingView;
    }

    @Override
    public void enableView(View view) {
        view.setVisibility(View.VISIBLE);
    }

    @Override
    public void disableView(View view) {
        view.setVisibility(view.GONE);
    }

    @Override
    public void hideProgressbar() {
        hideProgressBar();
    }

    @Override
    public void showProgressbar() {

    }

    @Override
    public void showErrorMessage(Message msg) {
        showError(msg);
    }

    @Override
    public String getContinueButtonText() {
        return mBtnContinue.getText().toString();
    }

    @Override
    public DeliveryModes getDeliveryModes() {
        Bundle bundle = getArguments();
        DeliveryModes deliveryModes = bundle.getParcelable(IAPConstant.SET_DELIVERY_MODE);
        return deliveryModes;
    }

    @Override
    public void addOrderSummaryFragment() {
        addFragment(OrderSummaryFragment.createInstance(new Bundle(), AnimationType.NONE),
                OrderSummaryFragment.TAG, true
        );
    }

    @Override
    public AddressFields getBillingAddressFields() {
        return billingAddressFields;
    }

    @Override
    public AddressFields getShippingAddressFields() {
        return shippingAddressFields;
    }

    @Override
    public void addPaymentSelectionFragment(Bundle bundle) {
        addFragment(
                PaymentSelectionFragment.createInstance(bundle, InAppBaseFragment.AnimationType.NONE), PaymentSelectionFragment.TAG, true);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

        if (getArguments().getBoolean(IAPConstant.FROM_PAYMENT_SELECTION)) {
            if (isChecked) {
                getDLSBillingAddress().prePopulateShippingAddress();
                enableView(billingView);
            } else {
                getDLSBillingAddress().clearAllFields();
                disableView(shoppingView);
            }
            return;
        } else {
            if (isChecked) {
                disableView(billingView);
            } else {
                enableView(billingView);
                if (billingAddressFields != null && shippingAddressFields != null) {
                    getDLSBillingAddress().prePopulateShippingAddress();
                    mBtnContinue.setEnabled(true);
                }
            }

        }
        if (isChecked && addressShippingView.checkFields()) {
            mBtnContinue.setEnabled(true);

        } else if (!isChecked && (getDLSBillingAddress()).checkBillingAddressFields() && addressShippingView.checkFields()) {
            mBtnContinue.setEnabled(true);
        } else {
            mBtnContinue.setEnabled(false);
        }

        upDateUi(isChecked);
    }
}
