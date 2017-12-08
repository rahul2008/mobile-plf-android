package com.philips.cdp.digitalcare.fragments.rateandreview.fragments;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.util.Utils;

/**
 * Created by philips on 7/16/17.
 */

public class RateThisAppFragmentPresenter {


    private RateThisAppFragmentContract rateThisAppFragmentContract;

    public RateThisAppFragmentPresenter(RateThisAppFragmentContract rateThisAppFragmentContract) {
        this.rateThisAppFragmentContract = rateThisAppFragmentContract;
    }

    public void handleProductData(){
        ViewProductDetailsModel mProductData = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
        if(mProductData!=null)
           rateThisAppFragmentContract.onPRXProductPageReceived(mProductData);
    }


    public void validateContryChina() {
        if(Utils.isCountryChina()){
            rateThisAppFragmentContract.hidePlayStoreBtn();
        }
    }

}
