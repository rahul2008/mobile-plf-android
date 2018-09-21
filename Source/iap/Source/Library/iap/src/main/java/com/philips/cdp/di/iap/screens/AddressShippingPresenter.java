package com.philips.cdp.di.iap.screens;

import android.text.TextUtils;

/**
 * Created by philips on 9/20/18.
 */

public class AddressShippingPresenter {


    String addressWithNewLineIfNull( String code) {
        if (!TextUtils.isEmpty(code)) {
            return code.replaceAll(",null", " ");
        }
        return null;
    }
}
