package com.philips.platform.ths.pharmacy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

public class THSPharmacyAndShippingFragment extends THSBaseFragment implements THSPharmacyShippingViewInterface, View.OnClickListener, BackEventListener {

    private THSPharmacyAndShippingPresenter thsPharmacyAndShippingPresenter;
    private Label pharmacyName, pharmacyZip, pharmacyState, pharmacyAddressLineOne, pharmacyAddressLIneTwo,
            consumerName, consumerCity, consumerShippingAddress, consumerState, consumerShippingZip;
    private THSConsumer thsConsumer;
    private ImageButton editPharmacy, ps_edit_consumer_shipping_address;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_pharmacy_shipping_fragment, container, false);
        thsPharmacyAndShippingPresenter = new THSPharmacyAndShippingPresenter(this);
        editPharmacy = (ImageButton) view.findViewById(R.id.ps_edit_pharmacy);
        ps_edit_consumer_shipping_address = (ImageButton) view.findViewById(R.id.ps_edit_consumer_shipping_address);
        editPharmacy.setOnClickListener(this);
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
        return view;
    }

    public void setConsumer(THSConsumer thsConsumer) {
        this.thsConsumer = thsConsumer;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        thsPharmacyAndShippingPresenter.fetchConsumerPreferredPharmacy(thsConsumer);

    }

    @Override
    public void onSuccessUpdateFragmentView(Pharmacy pharmacy, Address address) {
        consumerName.setText(thsConsumer.getConsumer().getFullName());
        consumerState.setText(address.getState().getCode());
        consumerShippingAddress.setText(address.getAddress1());
        consumerShippingZip.setText(address.getZipCode());
        pharmacyAddressLineOne.setText(pharmacy.getAddress().getAddress1());
        pharmacyAddressLIneTwo.setText(pharmacy.getAddress().getAddress2());
        pharmacyName.setText(pharmacy.getName());
        pharmacyState.setText(pharmacy.getAddress().getState().getCode());
        pharmacyZip.setText(pharmacy.getAddress().getZipCode());
    }

    @Override
    public int getContainerID() {
        return ((ViewGroup) getView().getParent()).getId();
    }

    @Override
    public void onFailureCallSearchPharmacy(Address address) {
        startSearchPharmacy();
    }

    private void startSearchPharmacy() {
        THSPharmacyListFragment thsPharmacyFragment = new THSPharmacyListFragment();
        THSConsumer pthConsumer = new THSConsumer();
        pthConsumer.setConsumer(thsConsumer.getConsumer());
        thsPharmacyFragment.setConsumerAndAddress(pthConsumer, null);
        thsPharmacyFragment.setActionBarListener(getActionBarListener());
        getFragmentActivity().getSupportFragmentManager().
                beginTransaction().replace(getContainerID(),
                thsPharmacyFragment, "Pharmacy List").addToBackStack(null).commit();
    }

    private void startEditShippingAddress() {
        THSShippingAddressFragment thsShippingAddressFragment = new THSShippingAddressFragment();
        thsShippingAddressFragment.setActionBarListener(getActionBarListener());
        thsShippingAddressFragment.setConsumerAndAddress(thsConsumer, null);
        getActivity().getSupportFragmentManager().
                beginTransaction().replace(getContainerID(),
                thsShippingAddressFragment, "ShippingAddressFragment").addToBackStack(null).commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ps_edit_pharmacy) {
            startSearchPharmacy();
        }
        if (v.getId() == R.id.ps_edit_consumer_shipping_address) {
            startEditShippingAddress();
        }
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }
}
