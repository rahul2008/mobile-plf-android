package com.philips.platform.ths.pharmacy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.pojo.ShippingAddressPojo;
import com.philips.platform.ths.registration.THSConsumer;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uappframework.listener.BackEventListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;

import java.util.List;

public class THSShippingAddressFragment extends THSBaseFragment implements View.OnClickListener, BackEventListener {

    public static String TAG = THSShippingAddressFragment.class.getSimpleName();
    private THSConsumer thsConsumer;
    private Address address;
    private EditText addressLineOne, addressLineTwo, postalCode, town;
    private Button updateAddressButton;
    private THSShippingAddressPresenter thsShippingAddressPresenter;
    private AppCompatSpinner spinner;
    private THSSpinnerAdapter spinnerAdapter;
    private List<State> stateList = null;
    private THSUpdatePreferredPharmacy updatePreferredPharmacy;
    private ActionBarListener actionBarListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_shipping_address_fragment, container, false);
        actionBarListener = getActionBarListener();
        if(null != actionBarListener){
            actionBarListener.updateActionBar(R.string.shipping_address_fragment_name,true);
        }
        spinner = (AppCompatSpinner) view.findViewById(R.id.sa_state_spinner);

        try {
            stateList = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getConsumerManager().getValidPaymentMethodStates();
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }

        spinnerAdapter = new THSSpinnerAdapter(getActivity(), R.layout.ths_pharmacy_spinner_layout, stateList);
        spinner.setAdapter(spinnerAdapter);
        spinner.setSelection(0);
        addressLineOne = (EditText) view.findViewById(R.id.sa_shipping_address_line_one);
        addressLineTwo = (EditText) view.findViewById(R.id.sa_shipping_address_line_two);
        postalCode = (EditText) view.findViewById(R.id.sa_postal_code);
        town = (EditText) view.findViewById(R.id.sa_town);
        updateAddressButton = (Button) view.findViewById(R.id.update_shipping_address);
        updateAddressButton.setOnClickListener(this);
        thsShippingAddressPresenter = new THSShippingAddressPresenter(this);
        return view;
    }

    public void setConsumerAndAddress(THSConsumer thsConsumer, Address address) {
        this.thsConsumer = thsConsumer;
        this.address = address;
    }

    public void setUpdateShippingAddressCallback(THSUpdatePreferredPharmacy updatePreferredPharmacy){
        this.updatePreferredPharmacy = updatePreferredPharmacy;
    }

    @Override
    public void onClick(View v) {
        ShippingAddressPojo shippingAddressPojo = new ShippingAddressPojo();
        if (v.getId() == R.id.update_shipping_address) {
            try {
                Address address1 = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getNewAddress();
                address1.setAddress1(addressLineOne.getText().toString());
                address1.setAddress2(addressLineTwo.getText().toString());
                address1.setCity(town.getText().toString());
                address1.setZipCode(postalCode.getText().toString());
                address1.setState(stateList.get(spinner.getSelectedItemPosition()));
                thsShippingAddressPresenter.updateShippingAddress(thsConsumer, address1);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }

        }
    }

    public void updateShippingAddressView(Address address){
        updatePreferredPharmacy.updateShippingAddress(address);
        getActivity().getSupportFragmentManager().popBackStack();
    }
    @Override
    public boolean handleBackEvent() {
        return false;
    }
}
