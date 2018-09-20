package com.philips.cdp.di.iap.screens;

import android.view.View;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;

/**
 * Created by philips on 9/20/18.
 */

public class DLSBillingAddressPresenter {


    boolean isUSSelected(EditText editText){
    return editText.getText().toString().equals("US");
    }

    void showUSRegions(EditText mEtCountryBilling, InputValidationLayout mlLStateBilling) {
        if (isUSSelected(mEtCountryBilling)) {
            mlLStateBilling.setVisibility(View.VISIBLE);
            CartModelContainer.getInstance().setAddessStateVisible(true);
        } else {
            mlLStateBilling.setVisibility(View.GONE);
            CartModelContainer.getInstance().setAddessStateVisible(false);
        }
    }
}
