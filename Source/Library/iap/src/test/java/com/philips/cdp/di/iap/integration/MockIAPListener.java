package com.philips.cdp.di.iap.integration;

import java.util.ArrayList;

/**
 * Created by indrajitkumar on 22/09/16.
 */

public class MockIAPListener implements IAPListener {
    @Override
    public void onGetCartCount(int count) {

    }

    @Override
    public void onUpdateCartCount() {

    }

    @Override
    public void updateCartIconVisibility(boolean shouldShow) {

    }

    @Override
    public void onGetCompleteProductList(ArrayList<String> productList) {

    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFailure(int errorCode) {

    }
}
