/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

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
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.address.AddressSelectionAdapter;
import com.philips.cdp.di.iap.eventhelper.EventHelper;
import com.philips.cdp.di.iap.eventhelper.EventListener;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.session.NetworkConstants;
import com.philips.cdp.di.iap.session.RequestCode;
import com.philips.cdp.di.iap.utils.IAPConstant;
import com.philips.cdp.di.iap.utils.Utility;
import com.philips.cdp.di.iap.view.EditDeletePopUP;

import java.util.List;

public class AddressSelectionFragment extends BaseAnimationSupportFragment implements AddressController.AddressListener,
        EventListener {
    private RecyclerView mAddressListView;
    private AddressController mAddrController;
    AddressSelectionAdapter mAdapter;
    private List<Addresses> mAddresses;
    private Button mCancelButton;

    @Override
    protected void updateTitle() {
        setTitle(R.string.iap_shipping_address);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_address_selection, container, false);
        mAddressListView = (RecyclerView) view.findViewById(R.id.shipping_addresses);
        mAddrController = new AddressController(getContext(), this);
        mCancelButton = (Button) view.findViewById(R.id.btn_cancel);
        bindCancelListener();
        sendShippingAddressesRequest();

        EventHelper.getInstance().registerEventNotification(EditDeletePopUP.EVENT_EDIT, this);
        EventHelper.getInstance().registerEventNotification(EditDeletePopUP.EVENT_DELETE, this);
        EventHelper.getInstance().registerEventNotification(IAPConstant.ORDER_SUMMARY_FRAGMENT, this);
        return view;
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
        getMainActivity().addFragmentAndRemoveUnderneath(ShoppingCartFragment.createInstance(AnimationType.NONE), false);
    }

    private void sendShippingAddressesRequest() {
        String msg = getContext().getString(R.string.iap_please_wait);
        showProgress(msg);
        mAddrController.getShippingAddresses();
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAddressListView.setLayoutManager(layoutManager);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTitle();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventHelper.getInstance().unregisterEventNotification(EditDeletePopUP.EVENT_EDIT, this);
        EventHelper.getInstance().unregisterEventNotification(EditDeletePopUP.EVENT_DELETE, this);
        EventHelper.getInstance().unregisterEventNotification(IAPConstant.ORDER_SUMMARY_FRAGMENT, this);
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
        dismissProgress();
    }

    @Override
    public void onFetchAddressFailure(final Message msg) {
        // TODO: 2/19/2016 Fix error case scenario
        dismissProgress();
        moveToShoppingCart();
    }

    @Override
    public void onCreateAddress(boolean isSuccess) {

    }

    public static AddressSelectionFragment createInstance(final AnimationType animType) {
        AddressSelectionFragment fragment = new AddressSelectionFragment();
        Bundle args = new Bundle();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AnimationType getDefaultAnimationType() {
        return AnimationType.NONE;
    }

    @Override
    public void raiseEvent(final String event) {

    }

    @Override
    public void onEventReceived(final String event) {
        if (!TextUtils.isEmpty(event)) {
            if (EditDeletePopUP.EVENT_EDIT.equals(event)) {
            } else if (EditDeletePopUP.EVENT_DELETE.equals(event)) {
                int pos = mAdapter.getOptionsClickPosition();
                mAddrController.deleteAddress(mAddresses.get(pos).getId());
            }
        }
        if (event.equalsIgnoreCase(IAPConstant.ORDER_SUMMARY_FRAGMENT)) {
            getMainActivity().addFragmentAndRemoveUnderneath(
                    OrderSummaryFragment.createInstance(AnimationType.NONE, new Bundle()), false);
        }
    }

    private void showProgress(String msg) {
        Utility.showProgressDialog(getContext(), msg);
    }

    private void dismissProgress() {
        Utility.dismissProgressDialog();
    }
}