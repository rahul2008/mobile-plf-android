package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.base.THSBaseView;

public interface THSPharmacyShippingViewInterface extends THSBaseView {

    void onSuccessUpdateFragmentView(Pharmacy pharmacy, Address address);
    void onFailureCallSearchPharmacy();
}
