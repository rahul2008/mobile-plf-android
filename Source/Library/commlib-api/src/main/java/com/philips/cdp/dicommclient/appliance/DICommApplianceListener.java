/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.appliance;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.request.Error;
import com.philips.cdp2.commlib.core.appliance.Appliance;

public interface DICommApplianceListener {

    void onAppliancePortUpdate(Appliance appliance, DICommPort<?> port);

    void onAppliancePortError(Appliance appliance, DICommPort<?> port, Error error);
}
