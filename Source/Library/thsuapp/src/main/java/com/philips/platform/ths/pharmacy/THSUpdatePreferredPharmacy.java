package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;

/**
 * Created by philips on 7/20/17.
 */

public interface THSUpdatePreferredPharmacy {
    void updatePharmacy(Pharmacy pharmacy);
    void updateShippingAddress(Address address);
}
