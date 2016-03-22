/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.discovery;

public interface CppDiscoverEventListener {

    void onSignedOnViaCpp();

    void onSignedOffViaCpp();

    void onDiscoverEventReceived(DiscoverInfo discoverInfo, boolean isResponseToRequest);
}
