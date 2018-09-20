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

public interface DLSAddressContractor {


     void setContinueButtonState(boolean state);
     void setBillingAddressFields(AddressFields addressFields);

     View getShippingAddressView();
     View getBillingAddressView();

     Activity getActivityContext();

     FragmentActivity getFragmentActivity();

     DLSBillingAddressView getDLSBillingAddress();

     void enableView(View view);
     void disableView(View view);

     void hideProgressbar();

     void showProgressbar();

     void showErrorMessage(Message msg);

     String getContinueButtonText();

     DeliveryModes getDeliveryModes();

     void addOrderSummaryFragment();

     AddressFields getBillingAddressFields();

     AddressFields getShippingAddressFields();

     void addPaymentSelectionFragment(Bundle bundle);

     void setShippingAddressFields(AddressFields shippingAddressFields);

     boolean getCheckBoxState();
}
