package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.base.THSBaseView;

import java.util.List;

public interface THSSearchFragmentViewInterface extends THSBaseView{

    String getZipCode();
    void setPharmacyList(List<Pharmacy> pharmacies);
}
