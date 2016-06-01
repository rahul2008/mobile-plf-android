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

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.adapters.AddressSelectionAdapter;
import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.analytics.IAPAnalytics;
import com.philips.cdp.di.iap.analytics.IAPAnalyticsConstant;
import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.cdp.di.iap.controller.AddressController;
import com.philips.cdp.di.iap.controller.PaymentController;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.response.payment.PaymentMethod;
import com.philips.cdp.di.iap.response.payment.PaymentMethods;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.IAPNetworkError;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.ModelConstants;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.EditDeletePopUP;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddressSelectionFragment extends BaseAnimationSupportFragment implements AddressController.AddressListener,
        EventListener, PaymentController.PaymentListener {
    private RecyclerView mAddressListView;
    private AddressController mAddrController;
    AddressSelectionAdapter mAdapter;
    private List<Addresses> mAddresses = new ArrayList<>();
    private Button mCancelButton;
    private Context mContext;
    public static final String TAG = AddressSelectionFragment.class.getName();
    private boolean mIsAddressUpdateAfterDelivery;
    private String mJanRainEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAddrController = new AddressController(getContext(), this);
        sendShippingAddressesRequest();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_address_selection, container, false);
        mAddressListView = (RecyclerView) view.findViewById(R.id.shipping_addresses);
        mCancelButton = (Button) view.findViewById(R.id.btn_cancel);
        bindCancelListener();
        mJanRainEmail = HybrisDelegate.getInstance(getContext()).getStore().getJanRainEmail();
        registerEvents();
        return view;
    }

    public void registerEvents() {
        EventHelper.getInstance().registerEventNotification(EditDeletePopUP.EVENT_EDIT, this);
        EventHelper.getInstance().registerEventNotification(EditDeletePopUP.EVENT_DELETE, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.SHIPPING_ADDRESS_FRAGMENT, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.ADD_DELIVERY_ADDRESS, this);
    }

    public void bindCancelListener() {
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                moveToShoppingCart();
            }
        });
    }

    private void moveToShoppingCart() {
        moveToFragment(ShoppingCartFragment.TAG);
    }

    private void sendShippingAddressesRequest() {
        String msg = getContext().getString(R.string.iap_please_wait);
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), msg);
            mAddrController.getShippingAddresses();
        }
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
        IAPAnalytics.trackPage(IAPAnalyticsConstant.SHIPPING_ADDRESS_SELECTION_PAGE_NAME);
        setTitle(R.string.iap_address);
        mAdapter = new AddressSelectionAdapter(getContext(), mAddresses);
        mAddressListView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEvents();
    }

    public void unregisterEvents() {
        EventHelper.getInstance().unregisterEventNotification(EditDeletePopUP.EVENT_EDIT, this);
        EventHelper.getInstance().unregisterEventNotification(EditDeletePopUP.EVENT_DELETE, this);
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.SHIPPING_ADDRESS_FRAGMENT, this);
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.ADD_DELIVERY_ADDRESS, this);
    }

    @Override
    public void onGetAddress(Message msg) {
        if (mIsAddressUpdateAfterDelivery) {
            mIsAddressUpdateAfterDelivery = false;
            return;
        }

        Utility.dismissProgressDialog();
        if (msg.obj instanceof IAPNetworkError) {
            getIAPActivity().getNetworkUtility().showErrorMessage(msg, getFragmentManager(), getContext());
            moveToShoppingCart();
        } else {
            if (msg.what == RequestCode.DELETE_ADDRESS) {
                mAddresses.remove(mAdapter.getOptionsClickPosition());
                mAdapter.setAddresses(mAddresses);
                mAdapter.notifyDataSetChanged();
            } else if (isVisible()) {
                if (msg.obj instanceof GetShippingAddressData) {
                    GetShippingAddressData shippingAddresses = (GetShippingAddressData) msg.obj;
                    mAddresses = shippingAddresses.getAddresses();
                    mAdapter = new AddressSelectionAdapter(getContext(), mAddresses);
                    mAddressListView.setAdapter(mAdapter);
                }
            }
        }
    }

    @Override
    public void onCreateAddress(Message msg) {

    }

    @Override
    public void onSetDeliveryAddress(final Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            Addresses selectedAddress = retrieveSelectedAddress();
            mIsAddressUpdateAfterDelivery = true;
            mAddrController.setDefaultAddress(selectedAddress);
            mAddrController.setDeliveryMode();
        } else {
            getIAPActivity().getNetworkUtility().showErrorMessage(msg, getFragmentManager(), getContext());
            Utility.dismissProgressDialog();
        }
    }

    @Override
    public void onSetDeliveryModes(final Message msg) {
        if (msg.obj.equals(IAPConstant.IAP_SUCCESS)) {
            checkPaymentDetails();
        } else {
            getIAPActivity().getNetworkUtility().showErrorMessage(msg, getFragmentManager(), getContext());
            Utility.dismissProgressDialog();
        }
    }

    @Override
    public void onGetRegions(Message msg) {

    }

    public static AddressSelectionFragment createInstance(final Bundle args, final AnimationType animType) {
        AddressSelectionFragment fragment = new AddressSelectionFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onEventReceived(final String event) {
        if (!TextUtils.isEmpty(event)) {
            if (EditDeletePopUP.EVENT_EDIT.equals(event)) {
                HashMap<String, String> addressHashMap = updateShippingAddress();
                moveToShippingAddressFragment(addressHashMap);
            } else if (EditDeletePopUP.EVENT_DELETE.equals(event)) {
                deleteShippingAddress();
            }
        }
        if (event.equalsIgnoreCase(IAPConstant.SHIPPING_ADDRESS_FRAGMENT)) {
            Bundle args = new Bundle();
            args.putBoolean(IAPConstant.IS_SECOND_USER, true);
            addFragment(ShippingAddressFragment.createInstance(args, AnimationType.NONE),
                    ShippingAddressFragment.TAG);
        } else if (event.equalsIgnoreCase(IAPConstant.ADD_DELIVERY_ADDRESS)) {
            if (!Utility.isProgressDialogShowing()) {
                Utility.showProgressDialog(getContext(), getResources().getString(R.string.iap_please_wait));
                mAddrController.setDeliveryAddress(retrieveSelectedAddress().getId());
                CartModelContainer.getInstance().setAddressId(retrieveSelectedAddress().getId());
            }
        }
    }

    public void checkPaymentDetails() {
        PaymentController paymentController = new PaymentController(mContext, this);
        paymentController.getPaymentDetails();
    }

    private void deleteShippingAddress() {
        if (!Utility.isProgressDialogShowing()) {
            Utility.showProgressDialog(getContext(), getString(R.string.iap_please_wait));
            int pos = mAdapter.getOptionsClickPosition();
            mAddrController.deleteAddress(mAddresses.get(pos).getId());
        }
    }

    private HashMap<String, String> updateShippingAddress() {
        int pos = mAdapter.getOptionsClickPosition();
        Addresses address = mAddresses.get(pos);
        HashMap<String, String> addressHashMap = new HashMap<>();

        String titleCode = address.getTitleCode();

        if (titleCode.trim().length() > 0)
            titleCode = titleCode.substring(0, 1).toUpperCase(Locale.getDefault()) + titleCode.substring(1);

        addressHashMap.put(ModelConstants.FIRST_NAME, address.getFirstName());
        addressHashMap.put(ModelConstants.LAST_NAME, address.getLastName());
        addressHashMap.put(ModelConstants.TITLE_CODE, titleCode);
        addressHashMap.put(ModelConstants.COUNTRY_ISOCODE, address.getCountry().getIsocode());
        addressHashMap.put(ModelConstants.LINE_1, address.getLine1());
        addressHashMap.put(ModelConstants.LINE_2, address.getLine2());
        addressHashMap.put(ModelConstants.POSTAL_CODE, address.getPostalCode());
        addressHashMap.put(ModelConstants.TOWN, address.getTown());
        addressHashMap.put(ModelConstants.ADDRESS_ID, address.getId());
        addressHashMap.put(ModelConstants.DEFAULT_ADDRESS, address.getLine1());
        addressHashMap.put(ModelConstants.PHONE_1, address.getPhone1());

        if (address.getRegion() != null) {
            addressHashMap.put(ModelConstants.REGION_ISOCODE, address.getRegion().getName());
            addressHashMap.put(ModelConstants.REGION_CODE, address.getRegion().getIsocode());
        }

        if (address.getEmail() != null)
            addressHashMap.put(ModelConstants.EMAIL_ADDRESS, address.getEmail());
        else
            addressHashMap.put(ModelConstants.EMAIL_ADDRESS, mJanRainEmail);

        return addressHashMap;
    }

    private void moveToShippingAddressFragment(final HashMap<String, String> payload) {
        Bundle extras = new Bundle();
        extras.putSerializable(IAPConstant.UPDATE_SHIPPING_ADDRESS_KEY, payload);
        addFragment(ShippingAddressFragment.createInstance(extras, AnimationType.NONE),
                ShippingAddressFragment.TAG);
    }

    @Override
    public void onGetPaymentDetails(Message msg) {
        Utility.dismissProgressDialog();
        if ((msg.obj).equals(NetworkConstants.EMPTY_RESPONSE)) {

            Addresses address = retrieveSelectedAddress();
            AddressFields selectedAddress = prepareAddressFields(address);
            CartModelContainer.getInstance().setShippingAddressFields(selectedAddress);
            addFragment(BillingAddressFragment.createInstance(new Bundle(), AnimationType.NONE),
                    BillingAddressFragment.TAG);
        } else if ((msg.obj instanceof IAPNetworkError)) {
            getIAPActivity().getNetworkUtility().showErrorMessage(msg, getFragmentManager(), getContext());
        } else if ((msg.obj instanceof PaymentMethods)) {
            AddressFields selectedAddress = prepareAddressFields(retrieveSelectedAddress());
            CartModelContainer.getInstance().setShippingAddressFields(selectedAddress);
            PaymentMethods mPaymentMethods = (PaymentMethods) msg.obj;
            List<PaymentMethod> mPaymentMethodsList = mPaymentMethods.getPayments();

            Bundle bundle = new Bundle();
            bundle.putSerializable(IAPConstant.PAYMENT_METHOD_LIST, (Serializable) mPaymentMethodsList);
            addFragment(
                    PaymentSelectionFragment.createInstance(bundle, AnimationType.NONE), PaymentSelectionFragment.TAG);
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
        if (isNotNullNorEmpty(addr.getFirstName())) {
            fields.setFirstName(addr.getFirstName());
        }
        if (isNotNullNorEmpty(addr.getLastName())) {
            fields.setLastName(addr.getLastName());
        }
        if (isNotNullNorEmpty(addr.getTitleCode())) {
            String titleCode = addr.getTitleCode();
            if (titleCode.trim().length() > 0)
                fields.setTitleCode(titleCode.substring(0, 1).toUpperCase(Locale.getDefault()) + titleCode.substring(1));
        }
        if (isNotNullNorEmpty(addr.getLine1())) {
            fields.setLine1(addr.getLine1());
        }
        if (isNotNullNorEmpty(addr.getLine2())) {
            fields.setLine2(addr.getLine2());
        }
        if (isNotNullNorEmpty(addr.getTown())) {
            fields.setTown(addr.getTown());
        }
        if (isNotNullNorEmpty(addr.getPostalCode())) {
            fields.setPostalCode(addr.getPostalCode());
        }
        if (isNotNullNorEmpty(addr.getCountry().getIsocode())) {
            fields.setCountryIsocode(addr.getCountry().getIsocode());
        }

        if (isNotNullNorEmpty(addr.getEmail())) {
            fields.setEmail(addr.getEmail());
        } else {
            fields.setEmail(mJanRainEmail); // Since there is no email response from hybris
        }

        if (isNotNullNorEmpty(addr.getPhone1())) {
            fields.setPhoneNumber(addr.getPhone1());
        }

        if (addr.getRegion() != null) {
            fields.setRegionIsoCode(addr.getRegion().getName());
            CartModelContainer.getInstance().setRegionIsoCode(addr.getRegion().getIsocode());
        }

        return fields;
    }

    private boolean isNotNullNorEmpty(String field) {
        return !TextUtils.isEmpty(field);
    }
}