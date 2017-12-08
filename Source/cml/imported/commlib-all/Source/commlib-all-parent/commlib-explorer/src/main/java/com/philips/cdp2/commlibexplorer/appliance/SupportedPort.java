/*
 * © 2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlibexplorer.appliance;

public interface SupportedPort {

    String getPortName();

    String getErrorText();

    boolean isEnabled();

    String getStatusText();
}
