/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.pharmacy;

import com.americanwell.sdk.entity.pharmacy.Pharmacy;
import com.philips.platform.ths.base.THSBaseView;

import java.util.List;


public interface THSPharmacyListViewListener extends THSBaseView{

    void updatePharmacyListView(List<Pharmacy> pharmacies);
    void validateForMailOrder(Pharmacy pharmacy);
    void switchView();
    void showRetailView();
    void showMailOrderView();
    void setPreferredPharmacy();
    void hideProgressBar();
    void showErrorToast(String errorMessage);
    void showError(String errorMessage);
}