/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.address.AddressSelectionAdapter;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.model.ModelConstants;
import com.philips.cdp.di.iap.payment.PaymentController;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.cdp.di.iap.utils.NetworkUtility;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.EditDeletePopUP;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class AddressSelectionFragment extends BaseAnimationSupportFragment implements AddressController.AddressListener,
        EventListener, PaymentController.PaymentListener {
    private RecyclerView mAddressListView;
    private AddressController mAddrController;
    AddressSelectionAdapter mAdapter;
    private List<Addresses> mAddresses;
    private List<PaymentMethod> mPaymentMethodsList;
    private Button mCancelButton;
    private Context mContext;

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_address_selection, container, false);
        mAddressListView = (RecyclerView) view.findViewById(R.id.shipping_addresses);
        mAddrController = new AddressController(getContext(), this);
        mCancelButton = (Button) view.findViewById(R.id.btn_cancel);
        bindCancelListener();
        sendShippingAddressesRequest();

        registerEvents();
        return view;
    }

    public void registerEvents() {
        EventHelper.getInstance().registerEventNotification(EditDeletePopUP.EVENT_EDIT, this);
        EventHelper.getInstance().registerEventNotification(EditDeletePopUP.EVENT_DELETE, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.ORDER_SUMMARY_FRAGMENT, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.SHIPPING_ADDRESS_FRAGMENT, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.ADD_DELIVERY_ADDRESS, this);
    }

    public void bindCancelListener() {
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                //Go back to shopping cart
                moveToShoppingCart();
            }
        });
    }

    private void moveToShoppingCart() {
        addFragment(ShoppingCartFragment.createInstance(new Bundle(), AnimationType.NONE), null);
    }

    private void sendShippingAddressesRequest() {
        String msg = getContext().getString(R.string.iap_please_wait);
        Utility.showProgressDialog(getContext(), msg);
        mAddrController.getShippingAddresses();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAddressListView.setLayoutManager(layoutManager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitle(R.string.iap_shipping_address);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEvents();
    }

    public void unregisterEvents() {
        EventHelper.getInstance().unregisterEventNotification(EditDeletePopUP.EVENT_EDIT, this);
        EventHelper.getInstance().unregisterEventNotification(EditDeletePopUP.EVENT_DELETE, this);
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.ORDER_SUMMARY_FRAGMENT, this);
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.SHIPPING_ADDRESS_FRAGMENT, this);
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.ADD_DELIVERY_ADDRESS, this);
    }

    @Override
    public void onFetchAddressSuccess(final Message msg) {
        if (msg.what == RequestCode.DELETE_ADDRESS) {
            mAddresses.remove(mAdapter.getOptionsClickPosition());
            mAdapter.setAddresses(mAddresses);
            mAdapter.notifyDataSetChanged();
        } else {
            GetShippingAddressData shippingAddresses = (GetShippingAddressData) msg.obj;
            mAddresses = shippingAddresses.getAddresses();
            mAdapter = new AddressSelectionAdapter(getContext(), mAddresses);
            mAddressListView.setAdapter(mAdapter);
        }
        Utility.dismissProgressDialog();
    }

    @Override
    public void onFetchAddressFailure(final Message msg) {
        // TODO: 2/19/2016 Fix error case scenario
        NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_time_out), getString(R.string.iap_time_out_description));
        Utility.dismissProgressDialog();
        moveToShoppingCart();
    }

    @Override
    public void onCreateAddress(boolean isSuccess) {

    }

    @Override
    public void onSetDeliveryAddress(final Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            mAddrController.setDeliveryMode();
        } else {
            Toast.makeText(getContext(), "Error in setting delivery address", Toast.LENGTH_SHORT).show();
            Utility.dismissProgressDialog();
        }
    }

    @Override
    public void onGetDeliveryAddress(final Message msg) {
    }

    @Override
    public void onSetDeliveryModes(final Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            checkPaymentDetails();
        } else {
            Toast.makeText(getContext(), "Error in setting delivery address", Toast.LENGTH_SHORT).show();
            Utility.dismissProgressDialog();
        }
    }

    @Override
    public void onGetDeliveryModes(final Message msg) {

    }

    public static AddressSelectionFragment createInstance(final Bundle args, final AnimationType animType) {
        AddressSelectionFragment fragment = new AddressSelectionFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void raiseEvent(final String event) {

    }

    @Override
    public void onEventReceived(final String event) {
        IAPLog.d(IAPLog.SHIPPING_ADDRESS_FRAGMENT, "onEventReceived = " + event);
        if (!TextUtils.isEmpty(event)) {
            if (EditDeletePopUP.EVENT_EDIT.equals(event)) {
                HashMap<String, String> addressHashMap = updateShippingAddress();
                moveToShippingAddressFragment(addressHashMap);
            } else if (EditDeletePopUP.EVENT_DELETE.equals(event)) {
                deleteShippingAddress();
            }
        }
        if (event.equalsIgnoreCase(IAPConstant.ORDER_SUMMARY_FRAGMENT)) {

            PaymentController paymentController = new PaymentController(mContext, this);

            if (!Utility.isProgressDialogShowing()) {
                if (Utility.isInternetConnected(mContext)) {
                    Utility.showProgressDialog(mContext, getResources().getString(R.string.iap_please_wait));
                    paymentController.getPaymentDetails();
                } else {
                    NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
                }
            }
        }
        if (event.equalsIgnoreCase(IAPConstant.SHIPPING_ADDRESS_FRAGMENT)) {
            Bundle args = new Bundle();
            args.putBoolean(IAPConstant.IS_SECOND_USER, true);
            //addFragment(ShippingAddressFragment.createInstance(args, AnimationType.NONE), null);
            addFragment(ShippingAddressFragment.createInstance(args, AnimationType.NONE), null);
        } else if(event.equalsIgnoreCase(IAPConstant.ADD_DELIVERY_ADDRESS)) {
            Utility.showProgressDialog(getContext(),getResources().getString(R.string.iap_please_wait));
            mAddrController.setDeliveryAddress(retrieveSelectedAddress().getId());
        }
    }

    public void checkPaymentDetails() {
        PaymentController paymentController = new PaymentController(mContext, this);

        if (Utility.isInternetConnected(mContext)) {
            paymentController.getPaymentDetails();
        } else {
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_time_out), getString(R.string.iap_time_out_description));
            Utility.dismissProgressDialog();
        }
    }

    private void deleteShippingAddress() {
        if (Utility.isInternetConnected(getContext())) {
            if (!Utility.isProgressDialogShowing())
                Utility.showProgressDialog(getContext(), getString(R.string.iap_delete_address));
            int pos = mAdapter.getOptionsClickPosition();
            mAddrController.deleteAddress(mAddresses.get(pos).getId());
        } else {
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_network_error), getString(R.string.iap_check_connection));
        }
    }

    private HashMap updateShippingAddress() {
        int pos = mAdapter.getOptionsClickPosition();
        Addresses address = mAddresses.get(pos);
        HashMap<String, String> addressHashMap = new HashMap<>();
        addressHashMap.put(ModelConstants.FIRST_NAME, address.getFirstName());
        addressHashMap.put(ModelConstants.LAST_NAME, address.getLastName());
        addressHashMap.put(ModelConstants.TITLE_CODE, address.getTitle());
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, address.getCountry().getIsocode());
        addressHashMap.put(ModelConstants.LINE_1, address.getLine1());
        addressHashMap.put(ModelConstants.LINE_2, address.getLine2());
        addressHashMap.put(ModelConstants.POSTAL_CODE, address.getPostalCode());
        addressHashMap.put(ModelConstants.TOWN, address.getTown());
        addressHashMap.put(ModelConstants.ADDRESS_ID, address.getId());
        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, address.getLine1());
        addressHashMap.put(ModelConstants.PHONE_NUMBER, address.getPhone());
        return addressHashMap;
    }

    private void moveToShippingAddressFragment(final HashMap<String, String> payload) {
        Bundle extras = new Bundle();
        extras.putSerializable(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY, payload);
        addFragment(ShippingAddressFragment.createInstance(extras, AnimationType.NONE), null);
    }

    @Override
    public void onGetPaymentDetails(Message msg) {
        Utility.dismissProgressDialog();
        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {
            Addresses address = retrieveSelectedAddress();
            AddressFields selectedAddress = prepareAddressFields(address);
            CartModelContainer.getInstance().setShippingAddressFields(selectedAddress);

            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.ADDRESS_FIELDS, selectedAddress);
            addFragment(
                    BillingAddressFragment.createInstance(bundle, AnimationType.NONE), null);
        } else if ((msg.obj instanceof VolleyError)) {
            NetworkUtility.getInstance().showErrorDialog(getFragmentManager(), getString(R.string.iap_ok), getString(R.string.iap_time_out), getString(R.string.iap_time_out_description));
        } else if ((msg.obj instanceof PaymentMethods)) {
            AddressFields selectedAddress = prepareAddressFields(retrieveSelectedAddress());
            CartModelContainer.getInstance().setShippingAddressFields(selectedAddress);

            PaymentMethods mPaymentMethods = (PaymentMethods) msg.obj;
            mPaymentMethodsList = mPaymentMethods.getPayments();

            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.ADDRESS_FIELDS, selectedAddress);
            bundle.putSerializable(IAPConstant.PAYMENT_METHOD_LIST, (Serializable) mPaymentMethodsList);
            addFragment(
                    PaymentSelectionFragment.createInstance(bundle, AnimationType.NONE), null);
        }
    }

    @Override
    public void onSetPaymentDetails(Message msg) {

    }

    private Addresses retrieveSelectedAddress() {
        int pos = mAdapter.getSelectedPosition();
        return mAddresses.get(pos);
    }

    private AddressFields prepareAddressFields(Addresses addr) {
        AddressFields fields = new AddressFields();
        if (addr.getFirstName() != null) {
            fields.setFirstName(addr.getFirstName());
        }
        if (addr.getLastName() != null) {
            fields.setLastName(addr.getLastName());
        }
        if (addr.getTown() != null) {
            fields.setTown(addr.getTown());
        }
        if (addr.getPostalCode() != null) {
            fields.setPostalCode(addr.getPostalCode());
        }
        if (addr.getCountry().getIsocode() != null) {
            fields.setCountryIsocode(addr.getCountry().getIsocode());
        }
        if (addr.getLine1() != null) {
            fields.setLine1(addr.getLine1());
        }
        if (addr.getLine2() != null) {
            fields.setLine2(addr.getLine2());
        }
        if (addr.getPhone() != null) {
            fields.setPhoneNumber(addr.getPhone());
        }

        return fields;
    }
}