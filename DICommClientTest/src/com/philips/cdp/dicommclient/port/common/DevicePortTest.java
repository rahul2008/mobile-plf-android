/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import static org.mockito.Mockito.mock;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.port.common.DevicePort;
import com.philips.cdp.dicommclient.port.common.DevicePortProperties;
import com.philips.cdp.dicommclient.testutil.MockitoTestCase;

public class DevicePortTest extends MockitoTestCase {

    public void testWifiInvalidData() {
        String parseData = "invalid data";
        assertNull(parseDevicePortData(parseData));
    }

    public void testWifiNullData() {
        String parseData = null;
        assertNull(parseDevicePortData(parseData));
    }

    public void testWifiEmptyData() {
    	String parseData = "";
    	assertNull(parseDevicePortData(parseData));
    }

    public void testWifiInvalidJSON() {
        String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\"";
        assertNull(parseDevicePortData(parseData));
    }

    private DevicePortProperties parseDevicePortData(String parseData) {
        DevicePort devicePort = new DevicePort(null, mock(CommunicationStrategy.class));
        devicePort.processResponse(parseData);
        return devicePort.getPortProperties();
    }

}
