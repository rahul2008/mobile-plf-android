/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

import static com.philips.platform.ths.utility.THSConstants.THS_PHARMACY_SUMMARY;
import static com.philips.platform.ths.utility.THSConstants.THS_SHIPPING_ADDRESS;

public class THSPharmacyAndShippingFragment extends THSBaseFragment implements THSPharmacyShippingViewInterface, View.OnClickListener {

    public static String TAG = THSPharmacyAndShippingFragment.class.getSimpleName();
    private THSPharmacyAndShippingPresenter thsPharmacyAndShippingPresenter;
    private Label pharmacyName, pharmacyZip, pharmacyState, pharmacyAddressLineOne, pharmacyAddressLIneTwo,
            consumerName, consumerCity, consumerShippingAddress, consumerState, consumerShippingZip, ps_shipped_to_label;
    private THSConsumerWrapper thsConsumerWrapper;
    private Label editPharmacy, ps_edit_consumer_shipping_address;
    private RelativeLayout ths_shipping_pharmacy_layout, ps_shipping_layout_item;
    private Address address;
    private Pharmacy pharmacy;
    private Button continueButton;
    private ActionBarListener actionBarListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_pharmacy_shipping_fragment, container, false);
        setUpViews(view);
        thsConsumerWrapper = THSManager.getInstance().getPTHConsumer(getContext());
        ths_shipping_pharmacy_layout.setVisibility(View.INVISIBLE);
        editPharmacy.setOnClickListener(this);
        thsPharmacyAndShippingPresenter = new THSPharmacyAndShippingPresenter(this);
        updatePharmacyDetailsView(pharmacy);
        updateShippingAddressView(address);
        actionBarListener = getActionBarListener();
        if (null != actionBarListener) {
            actionBarListener.updateActionBar(R.string.pharmacy_shipping_fragment_name, true);
        }
        return view;
    }

    public void setUpViews(View view) {

        ths_shipping_pharmacy_layout = (RelativeLayout) view.findViewById(R.id.ths_shipping_pharmacy_layout);
        ps_shipping_layout_item = (RelativeLayout) view.findViewById(R.id.ps_shipping_layout_item);
        ps_shipped_to_label = (Label) view.findViewById(R.id.ps_shipped_to_label);
        editPharmacy = view.findViewById(R.id.ps_edit_pharmacy);
        ps_edit_consumer_shipping_address =  view.findViewById(R.id.ps_edit_consumer_shipping_address);
        ps_edit_consumer_shipping_address.setOnClickListener(this);
        consumerCity = (Label) view.findViewById(R.id.ps_consumer_city);
        consumerName = (Label) view.findViewById(R.id.ps_consumer_name);
        consumerShippingAddress = (Label) view.findViewById(R.id.ps_consumer_shipping_address);
        consumerShippingZip = (Label) view.findViewById(R.id.ps_consumer_shipping_zip);
        consumerState = (Label) view.findViewById(R.id.ps_consumer_state);
        pharmacyAddressLineOne = (Label) view.findViewById(R.id.ps_pharmacy_address_line_one);
        pharmacyAddressLIneTwo = (Label) view.findViewById(R.id.ps_pharmacy_address_line_two);
        pharmacyName = (Label) view.findViewById(R.id.ps_pharmacy_name);
        pharmacyState = (Label) view.findViewById(R.id.ps_pharmacy_state);
        pharmacyZip = (Label) view.findViewById(R.id.ps_pharmacy_zip_code);
        continueButton = (Button) view.findViewById(R.id.ths_ps_continue_button);
        continueButton.setOnClickListener(this);
    }

    public void setPharmacyAndAddress(Address address, Pharmacy pharmacy) {
        this.address = address;
        this.pharmacy = pharmacy;
    }

    public void updateShippingAddressView(Address address) {
        ths_shipping_pharmacy_layout.setVisibility(View.VISIBLE);
        if (null != address) {
            consumerName.setText(thsConsumerWrapper.getConsumer().getFullName());
            consumerCity.setText(address.getCity());
            consumerState.setText(address.getState().getCode());
            consumerShippingAddress.setText(address.getAddress1());
            consumerShippingZip.setText(address.getZipCode());
        } else {
            ps_shipping_layout_item.setVisibility(View.GONE);
            ps_shipped_to_label.setVisibility(View.GONE);
        }
    }

    public void updatePharmacyDetailsView(Pharmacy pharmacy) {
        pharmacyAddressLineOne.setText(pharmacy.getAddress().getAddress1());
        pharmacyAddressLIneTwo.setText(pharmacy.getAddress().getAddress2());
        pharmacyName.setText(pharmacy.getName());
        pharmacyState.setText(pharmacy.getAddress().getState().getCode());
        pharmacyZip.setText(pharmacy.getAddress().getZipCode());
    }

    @Override
    public void startSearchPharmacy() {
        THSSearchPharmacyFragment thsSearchPharmacyFragment = new THSSearchPharmacyFragment();
        addFragment(thsSearchPharmacyFragment, THSSearchPharmacyFragment.TAG, null, true);
    }

    @Override
    public void startEditShippingAddress() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(THS_SHIPPING_ADDRESS,address);
        THSShippingAddressFragment thsShippingAddressFragment = new THSShippingAddressFragment();
        addFragment(thsShippingAddressFragment, THSShippingAddressFragment.TAG, bundle, true);
    }

    @Override
    public void onClick(View v) {
        thsPharmacyAndShippingPresenter.onEvent(v.getId());
    }

    @Override
    public void onResume() {
        super.onResume();
        THSManager.getInstance().getThsTagging().trackPageWithInfo(THS_PHARMACY_SUMMARY, null, null);
    }
}
