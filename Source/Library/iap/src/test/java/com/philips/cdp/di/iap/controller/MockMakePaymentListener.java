/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.controller;

import android.os.Message;

public class MockMakePaymentListener implements PaymentController.MakePaymentListener {
    @Override
    public void onMakePayment(Message msg) {
    }

    @Override
    public void onPlaceOrder(Message msg) {
    }
}