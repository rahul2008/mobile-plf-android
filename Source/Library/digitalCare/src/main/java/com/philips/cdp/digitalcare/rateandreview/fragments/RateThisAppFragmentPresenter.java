package com.philips.cdp.digitalcare.rateandreview.fragments;

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
        //ViewProductDetailsModel mProductData = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
        //rateThisAppFragmentContract.onPRXProductPageReceived(mProductData);
    }


    public void validateContryChina() {
        if(Utils.isCountryChina()){
            rateThisAppFragmentContract.hidePlayStoreBtn();
        }
    }

}
