/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cl.di.common.ssdp.controller;

import android.support.annotation.NonNull;
import android.util.Xml;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cl.di.common.ssdp.contants.ConnectionLibContants;
import com.philips.cl.di.common.ssdp.models.SSDPdevice;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.philips.cl.di.common.ssdp.contants.XmlParserConstants.CPP_ID;
import static com.philips.cl.di.common.ssdp.contants.XmlParserConstants.DEVICE_TYPE;
import static com.philips.cl.di.common.ssdp.contants.XmlParserConstants.FRIENDLY_NAME;
import static com.philips.cl.di.common.ssdp.contants.XmlParserConstants.MANUFACTURER;
import static com.philips.cl.di.common.ssdp.contants.XmlParserConstants.MODEL_NAME;
import static com.philips.cl.di.common.ssdp.contants.XmlParserConstants.MODEL_NUMBER;
import static com.philips.cl.di.common.ssdp.contants.XmlParserConstants.REGEXP;
import static com.philips.cl.di.common.ssdp.contants.XmlParserConstants.UDN;
import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

public class DescriptionParser {

    private static final String NAMESPACE = "urn:schemas-upnp-org:device-1-0";

    List<SSDPdevice> devices = new ArrayList<>();

    public List<SSDPdevice> getDevicesList() {
        return devices;
    }

    public void parse(@NonNull final String xmlToParse) {
        final XmlPullParser parser = Xml.newPullParser();

        final InputStream inputStream;
        try {
            inputStream = new ByteArrayInputStream(xmlToParse.getBytes("UTF-8"));
            parser.setInput(inputStream, null);
            parser.nextTag();

            readDescription(parser);
        } catch (XmlPullParserException | IOException e) {
            DICommLog.e(ConnectionLibContants.LOG_TAG, "Error while parsing device description XML: " + e.getMessage());
        }
    }

    private void readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(START_TAG, NAMESPACE, "root");

        while (parser.next() != END_TAG) {
            if (parser.getEventType() != START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("device")) {
                devices.add(readDevice(parser));
            } else {
                skip(parser);
            }
        }
    }

    private SSDPdevice readDevice(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(START_TAG, NAMESPACE, "device");

        SSDPdevice device = new SSDPdevice();

        while (parser.next() != END_TAG) {
            if (parser.getEventType() != START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equalsIgnoreCase(DEVICE_TYPE)) {
                final String deviceType = readTagBody(parser, DEVICE_TYPE);
                device.setDeviceType(deviceType);
            } else if (name.equalsIgnoreCase(FRIENDLY_NAME)) {
                final String friendlyName = readTagBody(parser, FRIENDLY_NAME);
                device.setFriendlyName(friendlyName);
            } else if (name.equalsIgnoreCase(MANUFACTURER)) {
                final String manufacturer = readTagBody(parser, MANUFACTURER);
                device.setManufacturer(manufacturer);
            } else if (name.equalsIgnoreCase(MODEL_NAME)) {
                final String modelName = readTagBody(parser, MODEL_NAME);
                device.setModelName(modelName);
            } else if (name.equalsIgnoreCase(MODEL_NUMBER)) {
                final String modelNumber = readTagBody(parser, MODEL_NUMBER);
                device.setModelNumber(modelNumber);
            } else if (name.equalsIgnoreCase(UDN)) {
                final String udn = readTagBody(parser, UDN);
                device.setUdn(udn);
            } else if (name.equalsIgnoreCase(CPP_ID)) {
                final String cppId = readTagBody(parser, CPP_ID);
                device.setCppId(cppId);
            }
        }
        return device;
    }

    private String readTagBody(XmlPullParser parser, String tagName) throws IOException, XmlPullParserException {
        parser.require(START_TAG, NAMESPACE, tagName);
        String tagBody = readText(parser).replaceAll(REGEXP, "");
        parser.require(END_TAG, NAMESPACE, tagName);

        return tagBody;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case END_TAG:
                    depth--;
                    break;
                case START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
