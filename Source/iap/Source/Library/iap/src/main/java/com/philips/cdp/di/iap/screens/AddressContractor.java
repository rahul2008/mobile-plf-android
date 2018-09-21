package com.philips.cdp.di.iap.screens;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.philips.cdp.di.iap.address.AddressFields;
import com.philips.cdp.di.iap.response.addresses.DeliveryModes;

/**
 * Created by philips on 9/18/18.
 */

public interface AddressContractor {


    void setContinueButtonState(boolean state);
    void setContinueButtonText(String buttonText);

    String getContinueButtonText();

    boolean getCheckBoxState();

    void setBillingAddressFields(AddressFields addressFields);

    void setShippingAddressFields(AddressFields shippingAddressFields);

    View getShippingAddressView();

    View getBillingAddressView();

    Activity getActivityContext();

    FragmentActivity getFragmentActivity();

    AddressBillingView getDLSBillingAddress();

    void enableView(View view);

    void disableView(View view);

    void hideProgressbar();

    void showProgressbar();

    void showErrorMessage(Message msg);

    DeliveryModes getDeliveryModes();

    void addOrderSummaryFragment();

    void addPaymentSelectionFragment(Bundle bundle);


    AddressFields getBillingAddressFields();

    AddressFields getShippingAddressFields();

}
