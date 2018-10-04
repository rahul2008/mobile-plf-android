package com.philips.cdp.di.iap.screens;

import android.text.TextUtils;

/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

public class AddressShippingPresenter {


    String addressWithNewLineIfNull( String code) {
        if (!TextUtils.isEmpty(code)) {
            return code.replaceAll(",null", " ");
        }
        return null;
    }
}
