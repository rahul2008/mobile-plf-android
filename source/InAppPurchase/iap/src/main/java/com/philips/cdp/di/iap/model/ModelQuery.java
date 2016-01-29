/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.model;

public interface ModelQuery {
    public String getUrl(int reqeuestCode);

    public Object parseResponse(Object response);

    public int getMethod();

    public String reqeustBody();
}
