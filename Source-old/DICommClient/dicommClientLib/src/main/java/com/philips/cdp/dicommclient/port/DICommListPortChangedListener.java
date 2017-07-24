/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port;


interface DICommListPortChangedListener {

    void onListEntryPortAdded(DICommListEntryPort<?> listEntryPort);

    void onListEntryPortRemoved(DICommListEntryPort<?> listEntryPort);

}
