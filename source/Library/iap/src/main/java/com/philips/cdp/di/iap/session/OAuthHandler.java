/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

public interface OAuthHandler {
    /**
     * Api to generate the oauth token
     *
     * @return Token that can be used for further transactions
     */
    public String generateToken();
}