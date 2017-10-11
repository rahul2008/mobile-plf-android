/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.Country;
import com.americanwell.sdk.entity.State;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.registration.THSConsumerWrapper;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uappframework.listener.ActionBarListener;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.EditText;

import java.util.List;

public class THSShippingAddressFragment extends THSBaseFragment implements View.OnClickListener {

    public static String TAG = THSShippingAddressFragment.class.getSimpleName();
    private THSConsumerWrapper thsConsumerWrapper;
    private Address address;
    private EditText addressLineOne, addressLineTwo, postalCode, town;
    private Button updateAddressButton;
    private THSShippingAddressPresenter thsShippingAddressPresenter;
    private AppCompatSpinner spinner;
    private THSSpinnerAdapter spinnerAdapter;
    private List<State> stateList = null;
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
            final List<Country> supportedCountries = getSupportedCountries();
            stateList = getValidShippingStates(supportedCountries);
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

    @NonNull
    public List<Country> getSupportedCountries() throws AWSDKInstantiationException {
        return THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getSupportedCountries();
    }

    public List<State> getValidShippingStates(List<Country> supportedCountries) throws AWSDKInstantiationException {
        return THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getConsumerManager().getValidShippingStates(supportedCountries.get(0));
    }

    public void setConsumerAndAddress(THSConsumerWrapper thsConsumerWrapper, Address address) {
        this.thsConsumerWrapper = thsConsumerWrapper;
        this.address = address;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.update_shipping_address) {
            try {
                Address address1 = THSManager.getInstance().getAwsdk(getActivity().getApplicationContext()).getNewAddress();
                address1.setAddress1(addressLineOne.getText().toString());
                address1.setAddress2(addressLineTwo.getText().toString());
                address1.setCity(town.getText().toString());
                address1.setZipCode(postalCode.getText().toString());
                address1.setState(stateList.get(spinner.getSelectedItemPosition()));
                thsShippingAddressPresenter.updateShippingAddress(address1);
            } catch (AWSDKInstantiationException e) {
                e.printStackTrace();
            }

        }
    }
}
