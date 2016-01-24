/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.di.iap.session;

import android.content.Context;

interface OAuthHandler {
    /**
     * Api to generate the oauth token
     *
     * @return Token that can be used for further transactions
     * @param context
     * @param janRainID
     * @param userID
     */
    public String generateToken(final Context context, final String janRainID, final String userID);
}