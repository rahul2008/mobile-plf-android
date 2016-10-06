/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap.session;

public interface OAuthListener {
    String getAccessToken();

    void refreshToken(RequestListener listener);

    void resetAccessToken();
}