package com.philips.platform.ths.pharmacy;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBasePresenter;

public class THSPharmacyAndShippingPresenter implements THSBasePresenter{

    private THSPharmacyShippingViewInterface thsBaseView;

    public THSPharmacyAndShippingPresenter(THSPharmacyShippingViewInterface thsBaseView){
        this.thsBaseView = thsBaseView;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.ps_edit_pharmacy) {
            thsBaseView.startSearchPharmacy();
        }
        if (componentID == R.id.ps_edit_consumer_shipping_address) {
            thsBaseView.startEditShippingAddress();
        }
    }
}
