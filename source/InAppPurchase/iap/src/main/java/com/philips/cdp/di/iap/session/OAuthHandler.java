/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

interface OAuthHandler {
    /**
     * Api to generate the oauth token
     *
     * @return Token that can be used for further transactions
     * @param janRainID
     * @param userID
     */
    public String generateToken(final String janRainID, final String userID);
}