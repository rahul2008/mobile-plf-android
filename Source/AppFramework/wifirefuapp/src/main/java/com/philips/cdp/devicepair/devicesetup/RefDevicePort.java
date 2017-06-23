package com.philips.cdp.devicepair.devicesetup;

import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import java.util.Map;

/**
 * Created by philips on 5/30/17.
 */

public class RefDevicePort extends DevicePort {

    public RefDevicePort(CommunicationStrategy communicationStrategy) {
        super(communicationStrategy);
    }

    @Override
    protected void setPortProperties(DevicePortProperties portProperties) {
        super.setPortProperties(portProperties);
    }

    @Override
    public void putProperties(Map<String, Object> dataMap) {
        super.putProperties(dataMap);
    }

    @Override
    public void putProperties(String key, String value) {
        super.putProperties(key, value);
    }
}
