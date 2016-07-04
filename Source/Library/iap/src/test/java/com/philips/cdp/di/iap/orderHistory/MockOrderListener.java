package com.philips.cdp.di.iap.orderHistory;

import android.os.Message;

import com.philips.cdp.di.iap.controller.OrderController;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class MockOrderListener implements OrderController.OrderListener {

    @Override
    public void onGetOrderList(Message msg) {

    }

    @Override
    public void onGetOrderDetail(Message msg) {

    }

    @Override
    public void updateUiOnProductList() {

    }
}
