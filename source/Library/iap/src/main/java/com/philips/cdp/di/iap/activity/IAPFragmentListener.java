package com.philips.cdp.di.iap.activity;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public interface IAPFragmentListener {
    void setHeaderTitle(int pResourceId);
    void updateCount(int count);
    void setCartIconVisibility(int visibility);
    void setBackButtonVisibility(int isVisible);
    void setHeaderTitle(String title);
}
