package com.philips.cdp.di.iap.screens;

import android.view.View;

import com.philips.cdp.di.iap.container.CartModelContainer;
import com.philips.platform.uid.view.widget.EditText;
import com.philips.platform.uid.view.widget.InputValidationLayout;

/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

public class AddressBillingPresenter {


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
