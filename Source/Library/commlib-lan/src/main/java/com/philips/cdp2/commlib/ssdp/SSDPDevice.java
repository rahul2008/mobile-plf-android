/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.ssdp;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.charset.Charset;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;

/**
 * The type Ssdp device.
 * <p>
 * <p>
 * Example description:
 * <pre>
 * {@code <root xmlns="urn:schemas-upnp-org:device-1-0">
 *   <specVersion>
 *     <major>1</major>
 *     <minor>0</minor>
 *   </specVersion>
 *   <URLBase>http://192.168.1.100:80/</URLBase>
 *   <device>
 *     <deviceType>urn:schemas-upnp-org:device:Basic:1</deviceType>
 *     <friendlyName>Hue Bridge (192.168.1.100)</friendlyName>
 *     <manufacturer>Royal Philips Electronics</manufacturer>
 *     <manufacturerURL>http://www.philips.com</manufacturerURL>
 *     <modelDescription>Philips hue Personal Wireless Lighting</modelDescription>
 *     <modelName>Philips hue bridge 2015</modelName>
 *     <modelNumber>BSB002</modelNumber>
 *     <modelURL>http://www.meethue.com</modelURL>
 *     <serialNumber>00123456789f</serialNumber>
 *     <UDN>uuid:2f402f80-da50-11e1-9b23-00123456789f</UDN>
 *     <presentationURL>index.html</presentationURL>
 *     <iconList>
 *       <icon>
 *         <mimetype>image/png</mimetype>
 *         <height>48</height>
 *         <width>48</width>
 *         <depth>24</depth>
 *         <url>hue_logo_0.png</url>
 *       </icon>
 *     </iconList>
 *   </device>
 * </root> }
 * </pre>
 */
public final class SSDPDevice {

    private static final String TAG = "SSDPDevice";

    private static final DescriptionParser PARSER = new DescriptionParser();

    private boolean isSecure;
    private String bootId;
    private String cppId;
    private String deviceType;
    private String friendlyName;
    private String ipAddress;
    private String manufacturer;
    private String manufacturerUrl;
    private String modelDescription;
    private String modelName;
    private String modelNumber;
    private String modelUrl;
    private String presentationUrl;
    private String serialNumber;
    private String udn;

    private static class DescriptionParser {
        private static final String NAMESPACE = "urn:schemas-upnp-org:device-1-0";

        private static final String BOOT_ID = "bootId";
        private static final String CPP_ID = "cppId";
        private static final String DEVICE = "device";
        private static final String DEVICE_TYPE = "deviceType";
        private static final String FRIENDLY_NAME = "friendlyName";
        private static final String IP_ADDRESS = "ipAddress";
        private static final String MANUFACTURER = "manufacturer";
        private static final String MANUFACTURER_URL = "manufacturerURL";
        private static final String MODEL_DESCRIPTION = "modelDescription";
        private static final String MODEL_NAME = "modelName";
        private static final String MODEL_NUMBER = "modelNumber";
        private static final String MODEL_URL = "modelURL";
        private static final String PRESENTATION_URL = "presentationURL";
        private static final String ROOT = "root";
        private static final String SERIAL_NUMBER = "serialNumber";
        private static final String UDN = "UDN";

        @Nullable
        SSDPDevice parse(String description) throws IOException, XmlPullParserException {
            return parseDevice(description);
        }

        private SSDPDevice parseDevice(@NonNull final String description) throws IOException, XmlPullParserException {
            final InputStream stream = new ByteArrayInputStream(description.getBytes(Charset.forName("UTF-8")));

            final XmlPullParser parser = Xml.newPullParser();
            parser.setInput(stream, null);
            parser.nextTag();
            parser.require(START_TAG, NAMESPACE, ROOT);

            while (parser.next() != END_TAG) {
                if (parser.getEventType() != START_TAG) {
                    continue;
                }

                switch (parser.getName()) {
                    case DEVICE:
                        return readDevice(parser);
                    default:
                        skipTag(parser);
                        break;
                }
            }
            stream.close();

            return null;
        }

        private SSDPDevice readDevice(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(START_TAG, NAMESPACE, DEVICE);

            SSDPDevice device = new SSDPDevice();

            while (parser.next() != END_TAG) {
                if (parser.getEventType() != START_TAG) {
                    continue;
                }

                switch (parser.getName()) {
                    case BOOT_ID:
                        device.bootId = readTag(parser, BOOT_ID);
                        break;
                    case CPP_ID:
                        device.cppId = readTag(parser, CPP_ID);
                        break;
                    case DEVICE_TYPE:
                        device.deviceType = readTag(parser, DEVICE_TYPE);
                        break;
                    case FRIENDLY_NAME:
                        device.friendlyName = readTag(parser, FRIENDLY_NAME);
                        break;
                    case IP_ADDRESS:
                        device.ipAddress = readTag(parser, IP_ADDRESS);
                        break;
                    case MANUFACTURER:
                        device.manufacturer = readTag(parser, MANUFACTURER);
                        break;
                    case MANUFACTURER_URL:
                        device.manufacturerUrl = readTag(parser, MANUFACTURER_URL);
                        break;
                    case MODEL_DESCRIPTION:
                        device.modelDescription = readTag(parser, MODEL_DESCRIPTION);
                        break;
                    case MODEL_NAME:
                        device.modelName = readTag(parser, MODEL_NAME);
                        break;
                    case MODEL_NUMBER:
                        device.modelNumber = readTag(parser, MODEL_NUMBER);
                        break;
                    case MODEL_URL:
                        device.modelUrl = readTag(parser, MODEL_URL);
                        break;
                    case PRESENTATION_URL:
                        device.presentationUrl = readTag(parser, PRESENTATION_URL);
                        break;
                    case SERIAL_NUMBER:
                        device.serialNumber = readTag(parser, SERIAL_NUMBER);
                        break;
                    case UDN:
                        device.udn = readTag(parser, UDN);
                        break;
                    default:
                        skipTag(parser);
                        break;
                }
            }
            return device;
        }

        private String readTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
            String result = "";
            parser.require(START_TAG, NAMESPACE, tag);

            if (parser.next() == TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            parser.require(END_TAG, NAMESPACE, tag);

            return result;
        }

        private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
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

    public static SSDPDevice create(@NonNull final String description, String ipAddress, boolean isSecure) {
        SSDPDevice device = null;
        try {
            device = PARSER.parse(description);
            device.ipAddress = ipAddress;
            device.isSecure = isSecure;
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Error parsing description.", e);
        }
        return device;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public void setSecure(boolean secure) {
        isSecure = secure;
    }

    public String getBootId() {
        return bootId;
    }

    public void setBootId(String bootId) {
        this.bootId = bootId;
    }

    public String getCppId() {
        return cppId;
    }

    public void setCppId(String cppId) {
        this.cppId = cppId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getManufacturerUrl() {
        return manufacturerUrl;
    }

    public void setManufacturerUrl(String manufacturerUrl) {
        this.manufacturerUrl = manufacturerUrl;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getModelNumber() {
        return modelNumber;
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public void setModelUrl(String modelUrl) {
        this.modelUrl = modelUrl;
    }

    public String getPresentationUrl() {
        return presentationUrl;
    }

    public void setPresentationUrl(String presentationUrl) {
        this.presentationUrl = presentationUrl;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getUdn() {
        return udn;
    }

    public void setUdn(String udn) {
        this.udn = udn;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName() + "@" + hashCode() + "\n\r");

        for (Field field : getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            builder.append(field.getName()).append(": [");

            try {
                builder.append(field.get(this));
            } catch (IllegalAccessException ignored) {
            } finally {
                builder.append("]\n\r");
            }
        }
        return builder.toString();
    }
}
