package com.philips.cdp.registration.ui.utils.test;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.test.InstrumentationTestCase;

import com.philips.cdp.registration.ui.utils.NetworkUtility;

/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */
public class NetworkUtilityTest extends InstrumentationTestCase {

    NetworkUtility networkUtility;
    Context context;

    @Override
    protected void setUp() throws Exception {
        context = getInstrumentation().getContext();
        networkUtility = new NetworkUtility();
    }

    public void testisNetworkAvailable() {
        boolean result = NetworkUtility.isNetworkAvailable(context);
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
        assertTrue(isConnected);
    }
}
