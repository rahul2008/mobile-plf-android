/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class SSDPDeviceTest extends RobolectricTest {

    private static final String TEST_DESCRIPTION_XML = "<root xmlns=\"urn:schemas-upnp-org:device-1-0\">" +
            "<specVersion><major>1</major><minor>0</minor></specVersion>" +
            "<URLBase>http://192.168.1.100:80/</URLBase>" +
            "<device>" +
            "<deviceType>urn:schemas-upnp-org:device:Basic:1</deviceType>" +
            "<friendlyName>Hue Bridge (192.168.1.100)</friendlyName>" +
            "<manufacturer>Royal Philips Electronics</manufacturer>" +
            "<manufacturerURL>http://www.philips.com</manufacturerURL>" +
            "<modelDescription>Philips hue Personal Wireless Lighting</modelDescription>" +
            "<modelName>Philips hue bridge 2015</modelName>" +
            "<modelNumber>BSB002</modelNumber>" +
            "<modelURL>http://www.meethue.com</modelURL>" +
            "<serialNumber>00123456789</serialNumber>" +
            "<cppId>01234abcd56789</cppId>" +
            "<UDN>uuid:2f402f80-da50-11e1-9b23-00123456789f</UDN>" +
            "<presentationURL>index.html</presentationURL>" +
            "<iconList><icon><mimetype>image/png</mimetype><height>48</height><width>48</width><depth>24</depth><url>hue_logo_0.png</url></icon></iconList>" +
            "</device>" +
            "</root>";

    private static final String TEST_IP_ADDRESS = "1.2.3.4";

    @Test
    public void testCreate() {
        boolean isSecure = true;
        final SSDPDevice device = SSDPDevice.create(TEST_DESCRIPTION_XML, TEST_IP_ADDRESS, isSecure);

        assertThat(device.getCppId()).isEqualTo("01234abcd56789");
        assertThat(device.getDeviceType()).isEqualTo("urn:schemas-upnp-org:device:Basic:1");
        assertThat(device.getFriendlyName()).isEqualTo("Hue Bridge (192.168.1.100)");
        assertThat(device.getManufacturer()).isEqualTo("Royal Philips Electronics");
        assertThat(device.getManufacturerUrl()).isEqualTo("http://www.philips.com");
        assertThat(device.getModelDescription()).isEqualTo("Philips hue Personal Wireless Lighting");
        assertThat(device.getModelName()).isEqualTo("Philips hue bridge 2015");
        assertThat(device.getModelNumber()).isEqualTo("BSB002");
        assertThat(device.getModelUrl()).isEqualTo("http://www.meethue.com");
        assertThat(device.getSerialNumber()).isEqualTo("00123456789");
        assertThat(device.getUdn()).isEqualTo("uuid:2f402f80-da50-11e1-9b23-00123456789f");
        assertThat(device.getPresentationUrl()).isEqualTo("index.html");
        assertThat(device.getIpAddress()).isEqualTo(TEST_IP_ADDRESS);
        assertThat(device.isSecure()).isEqualTo(isSecure);
    }

}
