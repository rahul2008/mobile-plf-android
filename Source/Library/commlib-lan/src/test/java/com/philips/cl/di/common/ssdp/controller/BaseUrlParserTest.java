/*
 * (C) Koninklijke Philips N.V., 2015, 2016.
 * All rights reserved.
 */
package com.philips.cl.di.common.ssdp.controller;

import com.philips.cdp.dicommclient.util.DICommLog;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class BaseUrlParserTest {
    public static final String CORRECT_XML_DESCRIPTION = "<?xml version=\"1.0\"?><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>Airpurifier still next to Lui</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6c74c0</UDN><cppId>1c5a6bfffe6c74c0</cppId></device></root>";
    public static final String ALMOST_CORRECT_XML_DESCRIPTION = "<?xml version=\"1.0\"?>\"<!DOCTYPE lolz []><root xmlns=\"urn:schemas-upnp-org:device-1-0\"><specVersion><major>1</major><minor>1</minor></specVersion><device><deviceType>urn:philips-com:device:DiProduct:1</deviceType><friendlyName>Airpurifier still next to Lui</friendlyName><manufacturer>Royal Philips Electronics</manufacturer><modelName>AirPurifier</modelName><UDN>uuid:12345678-1234-1234-1234-1c5a6b6c74c0</UDN><cppId>1c5a6bfffe6c74c0</cppId></device></root>";
    public static final String WRONG_XML_DESCRIPTION = "<?xml version=\"1.0\"?>\n" +
            "<!DOCTYPE lolz [\n" +
            "<!ENTITY lol \"lol\">\n" +
            "<!ENTITY lol2 \"&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;&lol;\">\n" +
            "<!ENTITY lol3 \"&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;&lol2;\"> <!ENTITY lol4 \"&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;&lol3;\"> <!ENTITY lol5 \"&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;&lol4;\"> <!ENTITY lol6 \"&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;&lol5;\"> <!ENTITY lol7 \"&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;&lol6;\"> <!ENTITY lol8 \"&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;&lol7;\"> <!ENTITY lol9 \"&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;&lol8;\"> ]>\n" +
            "<lolz>&lol9;</lolz>";


    private BaseUrlParser parser;

    @Before
    public void setUp() throws Exception {
        DICommLog.disableLogging();
        parser = new BaseUrlParser();
    }

    @Test
    public void givenValidXML_shouldHaveDeviceList_whenParsing() {
        parser.parse(CORRECT_XML_DESCRIPTION);

        List deviceList = parser.getDevicesList();
        assertNotNull(deviceList);
    }

    @Test
    public void givenValidXML_shouldHaveNonEmptyDeviceList_whenParsing() {
        parser.parse(CORRECT_XML_DESCRIPTION);

        List deviceList = parser.getDevicesList();
        assertNotEquals(0, deviceList.size());
    }

    @Test
    public void givenAlmostValidXML_shouldHaveDeviceList_whenParsing() {
        parser.parse(ALMOST_CORRECT_XML_DESCRIPTION);

        List deviceList = parser.getDevicesList();
        assertNotNull(deviceList);
    }

    @Test
    public void givenAlmostValidXML_shouldHaveEmptyDeviceList_whenParsing() {
        parser.parse(ALMOST_CORRECT_XML_DESCRIPTION);

        List deviceList = parser.getDevicesList();
        assertEquals(0, deviceList.size());
    }

    @Test
    public void givenWrongXML_shouldHaveDeviceList_whenParsing() {
        parser.parse(WRONG_XML_DESCRIPTION);

        List deviceList = parser.getDevicesList();
        assertNotNull(deviceList);
    }

    @Test
    public void givenWrongXML_shouldHaveEmptyDeviceList_whenParsing() {
        parser.parse(WRONG_XML_DESCRIPTION);

        List deviceList = parser.getDevicesList();
        assertEquals(0, deviceList.size());
    }
}