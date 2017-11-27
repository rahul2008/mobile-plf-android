package com.philips.cdp.di.iap.screens;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;
import com.philips.cdp.di.iap.response.error.Error;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.platform.uid.view.widget.CheckBox;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class DLSAddressFragment extends InAppBaseFragment implements View.OnClickListener, AddressController.AddressListener, PaymentController.PaymentListener {

    public static final String TAG = DLSAddressFragment.class.getName();
    Context mContext;
    protected Fragment shippingFragment;
    protected Fragment billingFragment;
    protected CheckBox checkBox;
    protected Button mBtnContinue;
    protected Button mBtnCancel;
    private AddressController mAddressController;
    private PaymentController mPaymentController;
    AddressFields shippingAddressFields;
    AddressFields billingAddressFields;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.iap_address, container, false);
        initializeViews(view);
        return view;
    }

    void initializeViews(View rootView) {

        TextView tv_checkOutSteps = rootView.findViewById(R.id.tv_checkOutSteps);
        tv_checkOutSteps.setText(String.format(mContext.getString(R.string.iap_checkout_steps), "2"));


        mBtnContinue = rootView.findViewById(R.id.btn_continue);
        mBtnCancel = rootView.findViewById(R.id.btn_cancel);

        mBtnContinue.setOnClickListener(this);
        mBtnCancel.setOnClickListener(this);

        mAddressController = new AddressController(mContext, this);
        mPaymentController = new PaymentController(mContext, this);

        checkBox = rootView.findViewById(R.id.use_this_address_checkbox);

        shippingFragment = getFragmentByID(R.id.fragment_shipping_address);

        billingFragment = getFragmentByID(R.id.fragment_billing_address);

        setFragmentVisibility(billingFragment, false);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // CartModelContainer.getInstance().setSwitchToBillingAddress(!isChecked);
                if (getArguments().getBoolean(IAPConstant.FROM_PAYMENT_SELECTION)) {
                    if (isChecked) {
                        ((DLSBillingAddressFragment) billingFragment).prePopulateShippingAddress();
                        setFragmentVisibility(billingFragment, true);
                    } else {
                        ((DLSBillingAddressFragment) billingFragment).clearAllFields();
                        setFragmentVisibility(shippingFragment, false);
                    }
                    return;
                } else {
                    if (isChecked) {
                        setFragmentVisibility(billingFragment, false);
                        ((DLSBillingAddressFragment) billingFragment).disableAllFields();
                        ((DLSBillingAddressFragment) billingFragment).prePopulateShippingAddress();
                        mBtnContinue.setEnabled(true);
                    } else {
                        setFragmentVisibility(billingFragment, true);
                        mBtnContinue.setEnabled(false);
                    }
                }
            }
        });


        Bundle bundle = getArguments();

        if (null != bundle && bundle.containsKey(IAPConstant.FROM_PAYMENT_SELECTION)) {
            if (bundle.containsKey(IAPConstant.UPDATE_BILLING_ADDRESS_KEY)) {
                checkBox.setVisibility(View.VISIBLE);
                checkBox.setChecked(true);
                setFragmentVisibility(billingFragment, true);
                setFragmentVisibility(shippingFragment, false);
                HashMap<String, String> mAddressFieldsHashmap = (HashMap<String, String>) bundle.getSerializable(IAPConstant.UPDATE_BILLING_ADDRESS_KEY);
                ((DLSBillingAddressFragment) billingFragment).updateFields(mAddressFieldsHashmap);
            }
        }

        if (null != bundle && bundle.containsKey(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY)) {
            checkBox.setVisibility(View.GONE);
            HashMap<String, String> mAddressFieldsHashmap = (HashMap<String, String>) bundle.getSerializable(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY);
            ((DLSShippingAddressFragment) shippingFragment).updateFields(mAddressFieldsHashmap);
        }

        if (null != bundle && bundle.containsKey(IAPConstant.ADD_BILLING_ADDRESS) && bundle.containsKey(IAPConstant.UPDATE_BILLING_ADDRESS_KEY)) {
            checkBox.setVisibility(View.GONE);
            setFragmentVisibility(shippingFragment, false);
            setFragmentVisibility(billingFragment, true);
            HashMap<String, String> mAddressFieldsHashmap = (HashMap<String, String>) bundle.getSerializable(IAPConstant.UPDATE_BILLING_ADDRESS_KEY);
            ((DLSBillingAddressFragment) billingFragment).updateFields(mAddressFieldsHashmap);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStop() {
        super.onStop();
        dismissProgressDialog();
    }

    public static DLSAddressFragment createInstance(Bundle args, AnimationType animType) {
        DLSAddressFragment fragment = new DLSAddressFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }


    void setFragmentVisibility(Fragment fragment, boolean isVisible) {
        FragmentManager fm = getFragmentManager();
        if (isVisible) {
            fm.beginTransaction()
                    .show(fragment)
                    .commit();
        } else {
            fm.beginTransaction()
                    .hide(fragment)
                    .commit();
        }

    }

    Fragment getFragmentByID(int id) {
        FragmentManager f = getChildFragmentManager();
        return f.findFragmentById(id);
    }


    @Override
    public void onClick(View v) {

        if (!isNetworkConnected()) return;
        if (v == mBtnContinue) {
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

    private void removeStaticFragments() {
        removeStaticFragments(shippingFragment);
        removeStaticFragments(billingFragment);
    }

    private void createNewAddressOrUpdateIfAddressIDPresent() {
        showProgressDialog(mContext, getString(R.string.iap_please_wait));
        if (checkBox.isChecked()) {
            CartModelContainer.getInstance().setSwitchToBillingAddress(true);
            CartModelContainer.getInstance().setBillingAddress(shippingAddressFields);
        }
        if (!CartModelContainer.getInstance().isAddessStateVisible()) {
            shippingAddressFields.setRegionIsoCode(null);
        }

        HashMap<String, String> updateAddressPayload = new HashMap<>();
        if (mBtnContinue.getText().toString().equalsIgnoreCase(getString(R.string.iap_save)))
            updateAddressPayload = addressPayload(shippingAddressFields);
        else {
            if (checkBox.isChecked() && billingAddressFields == null) {
                billingAddressFields = shippingAddressFields;
                updateAddressPayload = addressPayload(billingAddressFields);
            }
        }
        if (!getArguments().getBoolean(IAPConstant.FROM_PAYMENT_SELECTION)) {
            if (CartModelContainer.getInstance().getAddressId() != null) {
                if (CartModelContainer.getInstance().isAddessStateVisible() && CartModelContainer.getInstance().getRegionIsoCode() != null) {
                    updateAddressPayload.put(ModelConstants.REGION_ISOCODE, CartModelContainer.getInstance().getRegionIsoCode());
                }
                if (billingFragment.isVisible()) {
                    CartModelContainer.getInstance().setBillingAddress(billingAddressFields);
                    addFragment(OrderSummaryFragment.createInstance(new Bundle(), AnimationType.NONE), OrderSummaryFragment.TAG);
                } else {
                    updateAddressPayload.put(ModelConstants.ADDRESS_ID, CartModelContainer.getInstance().getAddressId());
                    mAddressController.updateAddress(updateAddressPayload);
                }

            } else {
                mAddressController.createAddress(shippingAddressFields);
            }
        } else {
            setBillingAddressAndOpenOrderSummary(billingAddressFields);
        }
    }

    private void setBillingAddressAndOpenOrderSummary(AddressFields billingAddressFields) {
        CartModelContainer.getInstance().setBillingAddress(billingAddressFields);
        dismissProgressDialog();
        addFragment(OrderSummaryFragment.createInstance(new Bundle(), AnimationType.NONE),
                OrderSummaryFragment.TAG);
    }

    private void saveShippingAddressToBackend() {
        if (!isProgressDialogShowing()) {
            showProgressDialog(mContext, mContext.getString(R.string.iap_please_wait));
            HashMap<String, String> addressHashMap = addressPayload(shippingAddressFields);
            mAddressController.updateAddress(addressHashMap);
        }
    }

     HashMap<String, String> addressPayload(AddressFields pAddressFields) {
        HashMap<String, String> mShippingAddressHashMap = new HashMap<>();

        mShippingAddressHashMap.put(ModelConstants.FIRST_NAME, pAddressFields.getFirstName());
        mShippingAddressHashMap.put(ModelConstants.LAST_NAME, pAddressFields.getLastName());
        mShippingAddressHashMap.put(ModelConstants.LINE_1, pAddressFields.getLine1());
        mShippingAddressHashMap.put(ModelConstants.LINE_2, pAddressFields.getLine2());
        mShippingAddressHashMap.put(ModelConstants.TITLE_CODE, pAddressFields.getTitleCode().toLowerCase(Locale.getDefault()));
        mShippingAddressHashMap.put(ModelConstants.COUNTRY_ISOCODE, pAddressFields.getCountryIsocode());
        mShippingAddressHashMap.put(ModelConstants.POSTAL_CODE, pAddressFields.getPostalCode().replaceAll(" ", ""));
        mShippingAddressHashMap.put(ModelConstants.TOWN, pAddressFields.getTown());
        final String addressId = CartModelContainer.getInstance().getAddressId();
        if (addressId != null) {
            mShippingAddressHashMap.put(ModelConstants.ADDRESS_ID, addressId);
        }
        mShippingAddressHashMap.put(ModelConstants.PHONE_1, pAddressFields.getPhone1().replaceAll(" ", ""));
        mShippingAddressHashMap.put(ModelConstants.PHONE_2, pAddressFields.getPhone1().replaceAll(" ", ""));
        mShippingAddressHashMap.put(ModelConstants.EMAIL_ADDRESS, pAddressFields.getEmail());
        if (!CartModelContainer.getInstance().isAddessStateVisible()) {
            mShippingAddressHashMap.put(ModelConstants.REGION_ISOCODE, null);
        } else {
            mShippingAddressHashMap.put(ModelConstants.REGION_ISOCODE, CartModelContainer.getInstance().getRegionIsoCode());
        }

        return mShippingAddressHashMap;
    }


    @Override
    public void onGetRegions(Message msg) {
        Toast.makeText(mContext, "onGetRegions", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onGetUser(Message msg) {
        Toast.makeText(mContext, "onGetUser", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreateAddress(Message msg) {
        Toast.makeText(mContext, "onCreateAddress", Toast.LENGTH_SHORT).show();
        if (msg.obj instanceof Addresses) {
            Addresses mAddresses = (Addresses) msg.obj;
            CartModelContainer.getInstance().setAddressId(mAddresses.getId());
            mAddressController.setDeliveryAddress(mAddresses.getId());
        } else if (msg.obj instanceof IAPNetworkError) {
            dismissProgressDialog();
            //((DLSShippingAddressFragment) shippingFragment).handleError(msg);
            showError(msg);
        }
    }

    @Override
    public void onGetAddress(Message msg) {
        Toast.makeText(mContext, "onGetAddress", Toast.LENGTH_SHORT).show();
        dismissProgressDialog();
        if (msg.what == RequestCode.UPDATE_ADDRESS) {
            if (msg.obj instanceof IAPNetworkError) {
                showError(msg);
            } else {
                if (mBtnContinue.getText().toString().equalsIgnoreCase(mContext.getString(R.string.iap_save))) {
                    getFragmentManager().popBackStackImmediate();
                } else {
                    mAddressController.setDeliveryAddress(CartModelContainer.getInstance().getAddressId());
                }
            }
        }
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
    public void onSetDeliveryAddress(Message msg) {
        Toast.makeText(mContext, "onSetDeliveryAddress", Toast.LENGTH_SHORT).show();
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            Bundle bundle = getArguments();
            DeliveryModes deliveryMode = bundle.getParcelable(IAPConstant.SET_DELIVERY_MODE);
            if (deliveryMode == null)
                mAddressController.getDeliveryModes();
            else
                mPaymentController.getPaymentDetails();
        } else {
            dismissProgressDialog();
            IAPLog.d(IAPLog.LOG, msg.getData().toString());
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        }
    }

    @Override
    public void onGetDeliveryModes(Message msg) {
        Toast.makeText(mContext, "onGetDeliveryModes", Toast.LENGTH_SHORT).show();
        handleDeliveryMode(msg, mAddressController);
    }

    @Override
    public void onSetDeliveryMode(Message msg) {
        Toast.makeText(mContext, "onSetDeliveryMode", Toast.LENGTH_SHORT).show();
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            if (CartModelContainer.getInstance().getBillingAddress() == null)
                mPaymentController.getPaymentDetails();
            else
                setBillingAddressAndOpenOrderSummary(billingAddressFields);
        } else {
            dismissProgressDialog();
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        }
    }

    public void setShippingAddressFields(AddressFields shippingAddressFields) {
        this.shippingAddressFields = shippingAddressFields;
    }

    public void setBillingAddressFields(AddressFields billingAddressFields) {
        this.billingAddressFields = billingAddressFields;
        CartModelContainer.getInstance().setBillingAddress(billingAddressFields);
    }

    @Override
    public void onGetPaymentDetails(Message msg) {
        dismissProgressDialog();


        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {

            CartModelContainer.getInstance().setShippingAddressFields(shippingAddressFields);
            setFragmentVisibility(billingFragment, true);
            setFragmentVisibility(shippingFragment, false);

           /* final AddressFields billingAddressFields = ((DLSBillingAddressFragment) billingFragment).billingAddressFields;
            if (billingAddressFields == null) {
                //set Billing Address same as Shipping Address
                CartModelContainer.getInstance().setBillingAddress(shippingAddressFields);
            } else {
                CartModelContainer.getInstance().setBillingAddress(billingAddressFields);
                addFragment(
                        OrderSummaryFragment.createInstance(new Bundle(), AnimationType.NONE), OrderSummaryFragment.TAG);
            }*/

        } else if ((msg.obj instanceof IAPNetworkError)) {
            NetworkUtility.getInstance().showErrorMessage(msg, getFragmentManager(), mContext);
        } else if ((msg.obj instanceof PaymentMethods)) {
            //Track new address creation
            IAPAnalytics.trackAction(IAPAnalyticsConstant.SEND_DATA,
                    IAPAnalyticsConstant.SPECIAL_EVENTS, IAPAnalyticsConstant.NEW_SHIPPING_ADDRESS_ADDED);
            PaymentMethods mPaymentMethods = (PaymentMethods) msg.obj;
            List<PaymentMethod> mPaymentMethodsList = mPaymentMethods.getPayments();
            CartModelContainer.getInstance().setShippingAddressFields(shippingAddressFields);
            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.PAYMENT_METHOD_LIST, (Serializable) mPaymentMethodsList);
            addFragment(
                    PaymentSelectionFragment.createInstance(bundle, AnimationType.NONE), PaymentSelectionFragment.TAG);
        }
    }

    @Override
    public void onSetPaymentDetails(Message msg) {
        //NOP
    }

    public void removeStaticFragments(Fragment currentFrag) {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        String fragName = "NONE";

        if (currentFrag != null)
            fragName = currentFrag.getClass().getSimpleName();


        if (currentFrag != null)
            transaction.remove(currentFrag);

        transaction.commit();

    }
}
