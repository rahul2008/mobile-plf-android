package com.philips.platform.ths.pharmacy;
/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

import android.support.annotation.NonNull;

import com.philips.platform.uid.view.widget.UIPicker;

public class THSShippingAddressFragmentMock extends THSShippingAddressFragment{



    @Override
    UIPicker getUiPicker() {
        return null;
    }

    @NonNull
    public THSSpinnerAdapter getThsSpinnerAdapter() {
        return null;
    }
}
