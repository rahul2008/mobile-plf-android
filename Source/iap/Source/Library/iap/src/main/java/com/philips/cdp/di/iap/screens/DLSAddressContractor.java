package com.philips.cdp.di.iap.screens;

import android.app.Activity;
import android.view.View;

import com.philips.cdp.di.iap.address.AddressFields;

/**
 * Created by philips on 9/18/18.
 */

public interface DLSAddressContractor {


     void setContinueButtonState(boolean state);
     void setBillingAddressFields(AddressFields addressFields);

     View getShippingAddressView();
     View getBillingAddressView();

     Activity getActivityContext();
}
