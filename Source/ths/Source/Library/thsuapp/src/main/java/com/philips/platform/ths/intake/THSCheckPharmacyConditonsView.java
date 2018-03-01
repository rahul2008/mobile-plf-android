package com.philips.platform.ths.intake;

import com.americanwell.sdk.entity.Address;
import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.base.THSBaseView;

interface THSCheckPharmacyConditonsView extends THSBaseView{

    void displayPharmacy();
    void displayPharmacyAndShippingPreferenceFragment(Pharmacy pharmacy, Address address);
    void showError(String toastMessage);
    void showPharmacySearch();
}
