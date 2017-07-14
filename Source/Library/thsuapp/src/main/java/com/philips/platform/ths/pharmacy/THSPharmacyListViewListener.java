package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.base.THSBaseView;

import java.util.List;


public interface THSPharmacyListViewListener extends THSBaseView{

    void updatePharmacyListView(List<Pharmacy> pharmacies);
    void validateForMailOrder();
    void switchView();
    void showRetailView();
    void showMailOrderView();
    void setPreferredPharmacy();
}
