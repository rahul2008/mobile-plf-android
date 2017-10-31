/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.os.Handler;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class WifiPortTest {

    private final static String TEST_DATA = "{\"ssid\":\"TEST_SSID\",\"password\":\"TEST_PASSWORD\",\"ipaddress\":\"TEST_IP\",\"dhcp\":true,\"netmask\":\"TEST_SUBNET\",\"gateway\":\"TEST_GATEWAY\"}";

    @Mock
    private Handler handlerMock;

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();
        HandlerProvider.enableMockedHandler(handlerMock);
    }

    @Test
    public void testWifiInvalidData() {
        String parseData = "invalid data";
        assertNull(parseWifiPortData(parseData));
    }

    @Test
    public void testWifiNullData() {
        assertNull(parseWifiPortData(null));
    }

    @Test
    public void testWifiEmptyData() {
        String parseData = "";
        assertNull(parseWifiPortData(parseData));
    }

    @Test
    public void testWifiInvalidJSON() {
        String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\"";
        assertNull(parseWifiPortData(parseData));
    }

    @Test
    public void testWifiWithValidData() throws Exception {
        WifiPortProperties properties = parseWifiPortData(TEST_DATA);

        assertEquals("TEST_SSID", properties.getSsid());
        assertEquals("TEST_GATEWAY", properties.getGateway());
        assertEquals("TEST_IP", properties.getIpaddress());
//        assertEquals("TEST_SSID", properties.getMacaddress());
        assertEquals("TEST_SUBNET", properties.getNetmask());
        assertEquals("TEST_PASSWORD", properties.getPassword());
//        assertEquals("TEST_SSID", properties.getProtection());
        assertEquals(true, properties.isDhcp());
    }

    private WifiPortProperties parseWifiPortData(String parseData) {
        WifiPort wifiPort = new WifiPort(mock(CommunicationStrategy.class));
        wifiPort.processResponse(parseData);
        return wifiPort.getPortProperties();
    }

    public static String getWifiPortJson(String ssid, String password, NetworkNode networkNode) {
        JSONObject holder = new JSONObject();
        try {
            holder.put("ssid", ssid);
            holder.put("password", password);
        } catch (JSONException e) {
            DICommLog.e(DICommLog.WIFIPORT, "Error: " + e.getMessage());
        }

        return holder.toString();
    }

    public static String getWifiPortWithAdvConfigJson(String ssid, String password, String ipAdd, String subnetMask, String gateWay, NetworkNode networkNode) {
        JSONObject holder = new JSONObject();
        try {
            holder.put("ssid", ssid);
            holder.put("password", password);
            holder.put("ipaddress", ipAdd);
            holder.put("dhcp", false);
            holder.put("netmask", subnetMask);
            holder.put("gateway", gateWay);
        } catch (JSONException e) {
            DICommLog.e(DICommLog.WIFIPORT, "Error: " + e.getMessage());
        }

        return holder.toString();
    }
}
