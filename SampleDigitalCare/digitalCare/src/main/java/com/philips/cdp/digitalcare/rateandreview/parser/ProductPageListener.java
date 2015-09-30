package com.philips.cdp.digitalcare.rateandreview.parser;

import com.philips.cdp.digitalcare.rateandreview.productreview.model.PRXProductModel;

/**
 * Created by 310190678 on 21-Aug-15.
 */
public interface ProductPageListener {
    void onPRXProductPageReceived(PRXProductModel object);
}
