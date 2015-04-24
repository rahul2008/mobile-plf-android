package com.philips.cl.di.dicomm.port;

import static org.mockito.Mockito.mock;

import org.json.JSONException;
import org.json.JSONObject;

import com.philips.cl.di.dev.pa.datamodel.WifiPortProperties;
import com.philips.cl.di.dev.pa.newpurifier.NetworkNode;
import com.philips.cl.di.dev.pa.util.ALog;
import com.philips.cl.di.dicomm.MockitoTestCase;
import com.philips.cl.di.dicomm.communication.CommunicationStrategy;

public class WifiPortTest extends MockitoTestCase {

    public final static String TEST_DATA = "{\"ssid\":\"TEST_SSID\",\"password\":\"TEST_PASSWORD\",\"ipaddress\":\"TEST_IP\",\"dhcp\":true,\"netmask\":\"TEST_SUBNET\",\"gateway\":\"TEST_GATEWAY\"}";

    public void testWifiInvalidData() {
        String parseData = "invalid data";
        assertNull(parseWifiPortData(parseData));
    }

    public void testWifiNullData() {
        String parseData = null;
        assertNull(parseWifiPortData(parseData));
    }

    public void testWifiInvalidJSON() {
        String parseData = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\"";
        assertNull(parseWifiPortData(parseData));
    }

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
        WifiPort wifiPort = new WifiPort(null, mock(CommunicationStrategy.class));
        wifiPort.processResponse(parseData);
        return wifiPort.getPortProperties();
    }

    public static String getWifiPortJson(String ssid, String password, NetworkNode networkNode) {
        JSONObject holder = new JSONObject();
        try {
            holder.put("ssid", ssid);
            holder.put("password", password);
        } catch (JSONException e) {
            ALog.e(ALog.EWS, "Error: " + e.getMessage());
        }
        String js = holder.toString();

        return js;
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
            ALog.e(ALog.EWS, "Error: " + e.getMessage());
        }
        String js = holder.toString();

        return js;
    }

}
