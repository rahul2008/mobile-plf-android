/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class SSDPDeviceTest extends RobolectricTest {

    private static final String HUE_BRIDGE_DESCRIPTION = "<root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>0</minor></specVersion><URLBase>http://192.168.1.100:80/</URLBase><device><deviceType>urn:schemas-upnp-org:device:Basic:1</deviceType><friendlyName>Hue Bridge (192.168.1.100)</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><manufacturerURL>http://www.philips.com</manufacturerURL><modelDescription>Philips hue Personal Wireless Lighting</modelDescription><modelName>Philips hue bridge 2015</modelName><modelNumber>BSB002</modelNumber><modelURL>http://www.meethue.com</modelURL><serialNumber>00123456789</serialNumber><UDN>uuid:2f402f80-da50-11e1-9b23-00123456789f</UDN><presentationURL>index.html</presentationURL><iconList><icon><mimetype>image/png</mimetype><height>48</height><width>48</width><depth>24</depth><url>hue_logo_0.png</url></icon></iconList></device></root>";
    private static final String AIR_PURIFIER_DESCRIPTION = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>AirPurifier For Test</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6c74c0</UDN><cppId>1c5a6bfffe6c74c0</cppId></device></root>";
    private static final String ALMOST_CORRECT_DESCRIPTION = "<?xml version=\"1.0\"?>\"<!DOCTYPE lolz []><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>Airpurifier still next to Lui</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6c74c0</UDN><cppId>1c5a6bfffe6c74c0</cppId></device></root>";
    private static final String INVALID_DESCRIPTION = "<?xml version=\"1.0\"?>\n<!DOCTYPE lolz [\n<!ENTITY lol \"lol\">\n<!ENTITY lol2 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n<!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;\"> <!ENTITY lol4 \"&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;\"> <!ENTITY lol5 \"&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;\"> <!ENTITY lol6 \"&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;\"> <!ENTITY lol7 \"&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;\"> <!ENTITY lol8 \"&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;\"> <!ENTITY lol9 \"&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;\"> ]>\n<lolz>&lol9;</lolz>";

    private static final String TEST_IP_ADDRESS = "1.2.3.4";

    private static final String TEST_LOCATION = "http://1.2.3.4/mock/location";
    private static final String TEST_BOOTID = "1337";

    @Mock
    private SSDPMessage messageMock;

    //todo: enable if by some magic way we can get powermock and robolectric to play nice
//    @Mock
//    private URL urlMock;
//
//    @Mock
//    private URLConnection urlConnectionMock;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        initMocks(this);

        //todo: enable if by some magic way we can get powermock and robolectric to play nice
//        when(messageMock.getDescriptionUrl()).thenReturn(urlMock);
//        when(urlMock.openConnection()).thenReturn(urlConnectionMock);
//        when(urlConnectionMock.getInputStream()).thenReturn(new ByteArrayInputStream(AIR_PURIFIER_DESCRIPTION.getBytes()));
    }

    @Test
    public void testCreateGenericDevice() {
        final boolean isSecure = true;

        final SSDPDevice device = SSDPDevice.createFromXml(HUE_BRIDGE_DESCRIPTION);
        device.setIpAddress(TEST_IP_ADDRESS);
        device.setSecure(isSecure);

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

    @Test
    public void testCreateDiCommDevice() {
        final boolean isSecure = true;

        final SSDPDevice device = SSDPDevice.createFromXml(AIR_PURIFIER_DESCRIPTION);
        device.setIpAddress(TEST_IP_ADDRESS);
        device.setSecure(isSecure);

        assertThat(device.getDeviceType()).isEqualTo("urn:philips-com:device:DiProduct:1");
        assertThat(device.getCppId()).isEqualTo("1c5a6bfffe6c74c0");
    }

    @Test
    public void testAlmostCorrectDescription() {
        SSDPDevice.createFromXml(ALMOST_CORRECT_DESCRIPTION);
    }

    @Test
    public void testInvalidDescription() {
        SSDPDevice.createFromXml(INVALID_DESCRIPTION);
    }

    @Test
    public void givenDeviceIsCreated_whenDeviceIsUpdatedWithNewSsdpMessage_thenAppropriateFieldsAreUpdated() throws Exception {
        final SSDPDevice device = SSDPDevice.createFromXml(AIR_PURIFIER_DESCRIPTION);
        assertThat(device.getBootId()).isNotEqualTo(TEST_BOOTID);
        assertThat(device.getIpAddress()).isNotEqualTo(TEST_IP_ADDRESS);

        when(messageMock.get(SSDPMessage.BOOT_ID)).thenReturn(TEST_BOOTID);
        when(messageMock.getDescriptionUrl()).thenReturn(new URL(TEST_LOCATION));
        device.update(messageMock);

        assertThat(device.getBootId()).isEqualTo(TEST_BOOTID);
        assertThat(device.getIpAddress()).isEqualTo(TEST_IP_ADDRESS);
    }

    //todo: enable if by some magic way we can get powermock and robolectric to play nice
//    @Test
//    public void whenDeviceIsCreatedFromSsdpMessage_thenAllFieldsGetProperlyInitialized() throws Exception {
//
//        when(messageMock.get(SSDPMessage.LOCATION)).thenReturn(TEST_LOCATION);
//        when(messageMock.get(SSDPMessage.BOOT_ID)).thenReturn(TEST_BOOTID);
//
//        final SSDPDevice device = SSDPDevice.createFromSsdpMessage(messageMock);
//
//        assertThat(device.getBootId()).isEqualTo(TEST_BOOTID);
//        assertThat(device.getIpAddress()).isEqualTo(TEST_IP_ADDRESS);
//        assertThat(device.getCppId()).isEqualTo("1c5a6bfffe6c74c0");
//    }
}
