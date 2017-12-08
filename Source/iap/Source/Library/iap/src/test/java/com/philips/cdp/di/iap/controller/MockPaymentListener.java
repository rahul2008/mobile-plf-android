/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.os.Message;

public class MockPaymentListener implements PaymentController.PaymentListener {
    @Override
    public void onGetPaymentDetails(Message msg) {
    }

    @Override
    public void onSetPaymentDetails(Message msg) {
    }
}