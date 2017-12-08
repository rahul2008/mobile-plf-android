package com.philips.cdp.digitalcare.fragments.rateandreview.fragments;

import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;

/**
 * Created by philips on 7/16/17.
 */

public interface RateThisAppFragmentContract {

    void onPRXProductPageReceived(ViewProductDetailsModel data);
    void hidePlayStoreBtn();

}
