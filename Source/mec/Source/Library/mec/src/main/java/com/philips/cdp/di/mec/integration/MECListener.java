/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.mec.integration;


import java.util.ArrayList;


public interface MECListener {

    void onGetCartCount(int count);

    void onUpdateCartCount();

    void updateCartIconVisibility(boolean shouldShow);

    void onGetCompleteProductList(final ArrayList<String> productList);

    void onSuccess();

    void onSuccess(boolean bool);

    void onFailure(final int errorCode);

}
