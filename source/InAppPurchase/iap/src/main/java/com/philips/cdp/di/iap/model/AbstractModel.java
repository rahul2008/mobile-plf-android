/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.model;

import android.os.Bundle;

import com.philips.cdp.di.iap.store.Store;

public abstract class AbstractModel {
    final protected Store store;

    public AbstractModel(Store store) {
        this.store = store;
    }

    public abstract String getUrl(int requestCode);

    public abstract Object parseResponse(Object response);

    public abstract int getMethod(int requestCode);

    public abstract Bundle requestBody();

    public abstract String getTestUrl(int requestCode);
}
