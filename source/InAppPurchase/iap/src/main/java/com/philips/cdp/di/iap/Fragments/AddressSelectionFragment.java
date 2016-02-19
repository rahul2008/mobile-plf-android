/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.Fragments;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.iap.R;
import com.philips.cdp.di.iap.address.AddressController;
import com.philips.cdp.di.iap.address.AddressSelectionAdapter;
import com.philips.cdp.di.iap.response.addresses.Addresses;
import com.philips.cdp.di.iap.response.addresses.GetShippingAddressData;
import com.philips.cdp.di.iap.session.NetworkConstants;

import java.util.List;

public class AddressSelectionFragment extends BaseAnimationSupportFragment implements AddressController.AddressListener{
    private RecyclerView mAddressListView;
    private AddressController mAddrController;
    AddressSelectionAdapter mAdapter;

    @Override
    protected void updateTitle() {

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.iap_address_selection, container, false);
        mAddressListView = (RecyclerView) view.findViewById(R.id.shipping_addresses);
        mAddrController = new AddressController(getContext(),this);
        mAddrController.getShippingAddresses();
        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAddressListView.setLayoutManager(layoutManager);
    }

    @Override
    public void onFetchAddressSuccess(final GetShippingAddressData shippingAddresses) {
        List<Addresses> addresses = shippingAddresses.getAddresses();
        mAdapter = new AddressSelectionAdapter(getContext(),addresses);
        mAddressListView.setAdapter(mAdapter);
    }

    @Override
    public void onFetchAddressFailure(final Message msg) {

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
}