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
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.ImageButton;
import com.philips.platform.uid.view.widget.Label;

public class THSPharmacyAndShippingFragment extends THSBaseFragment implements THSPharmacyShippingViewInterface, View.OnClickListener, BackEventListener,THSUpdatePreferredPharmacy {

    public static String TAG = THSPharmacyAndShippingFragment.class.getSimpleName();
    private THSPharmacyAndShippingPresenter thsPharmacyAndShippingPresenter;
    private Label pharmacyName, pharmacyZip, pharmacyState, pharmacyAddressLineOne, pharmacyAddressLIneTwo,
            consumerName, consumerCity, consumerShippingAddress, consumerState, consumerShippingZip;
    private THSConsumer thsConsumer;
    private ImageButton editPharmacy, ps_edit_consumer_shipping_address;
    private RelativeLayout ths_shipping_pharmacy_layout;
    private Address address;
    private Pharmacy pharmacy;
    private Button continueButton;
    private ActionBarListener actionBarListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_pharmacy_shipping_fragment, container, false);
        setUpViews(view);
        ths_shipping_pharmacy_layout.setVisibility(View.INVISIBLE);
        editPharmacy.setOnClickListener(this);
        thsPharmacyAndShippingPresenter = new THSPharmacyAndShippingPresenter(this);
        updateShippingAddressView(address);
        updatePharmacyDetailsView(pharmacy);
        actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(R.string.pharmacy_shipping_fragment_name,true);
        }
        return view;
    }

    public void setUpViews(View view) {

        ths_shipping_pharmacy_layout = (RelativeLayout) view.findViewById(R.id.ths_shipping_pharmacy_layout);
        editPharmacy = (ImageButton) view.findViewById(R.id.ps_edit_pharmacy);
        ps_edit_consumer_shipping_address = (ImageButton) view.findViewById(R.id.ps_edit_consumer_shipping_address);
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
        continueButton =(Button)view.findViewById(R.id.ths_ps_continue_button);
        continueButton.setOnClickListener(this);
    }

    public void setConsumer(THSConsumer thsConsumer) {
        this.thsConsumer = thsConsumer;
    }

    public void setPharmacyAndAddress(Address address, Pharmacy pharmacy) {
        this.address = address;
        this.pharmacy = pharmacy;
    }

    public void updateShippingAddressView(Address address) {
        ths_shipping_pharmacy_layout.setVisibility(View.VISIBLE);
        consumerName.setText(thsConsumer.getConsumer().getFullName());
        consumerCity.setText(address.getCity());
        consumerState.setText(address.getState().getCode());
        consumerShippingAddress.setText(address.getAddress1());
        consumerShippingZip.setText(address.getZipCode());
    }

    public void updatePharmacyDetailsView(Pharmacy pharmacy) {
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
    public void startSearchPharmacy() {
        THSPharmacyListFragment thsPharmacyFragment = new THSPharmacyListFragment();
        THSConsumer pthConsumer = new THSConsumer();
        pthConsumer.setConsumer(thsConsumer.getConsumer());
        thsPharmacyFragment.setConsumerAndAddress(pthConsumer, null);
        thsPharmacyFragment.setUpdateCallback(this);
        thsPharmacyFragment.setActionBarListener(getActionBarListener());
        addFragment(thsPharmacyFragment,THSPharmacyListFragment.TAG,null);
    }

    @Override
    public void startEditShippingAddress() {
        THSShippingAddressFragment thsShippingAddressFragment = new THSShippingAddressFragment();
        thsShippingAddressFragment.setActionBarListener(getActionBarListener());
        thsShippingAddressFragment.setConsumerAndAddress(thsConsumer, null);
        thsShippingAddressFragment.setUpdateShippingAddressCallback(this);
        addFragment(thsShippingAddressFragment,THSShippingAddressFragment.TAG,null);
    }

    @Override
    public void onClick(View v) {
        thsPharmacyAndShippingPresenter.onEvent(v.getId());
    }

    @Override
    public boolean handleBackEvent() {
        return false;
    }

    @Override
    public void updatePharmacy(Pharmacy pharmacy) {
       this.pharmacy = pharmacy;
    }

    @Override
    public void updateShippingAddress(Address address) {
        this.address = address;

    }
}
