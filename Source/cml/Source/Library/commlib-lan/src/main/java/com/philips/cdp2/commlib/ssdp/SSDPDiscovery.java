/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import com.philips.cdp2.commlib.core.exception.TransportUnavailableException;

public interface SSDPDiscovery {
    void start() throws TransportUnavailableException;

    void stop();

    boolean isDiscovering();
}
