/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.ssdp;

import android.support.annotation.NonNull;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import static com.philips.ssdp.SSDPUtils.CHARSET_UTF8;

public final class SSDPDevice {

    private static final String TAG = "SSDPDevice";

    // Device properties
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
    private Set<Icon> iconList = new HashSet<>();

    private final class Parser {
        private static final String BOOT_ID = "bootId";
        private static final String CPP_ID = "cppId";
        private static final String DEVICE = "device";
        private static final String DEVICE_TYPE = "deviceType";
        private static final String FRIENDLY_NAME = "friendlyName";
        private static final String ICON = "icon";
        private static final String ICON_LIST = "iconList";
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

        Parser(@NonNull final String descriptor) throws IOException, XmlPullParserException {
            parseDevice(descriptor);
        }

        private void parseDevice(String descriptor) throws IOException, XmlPullParserException {
            InputStream stream = new ByteArrayInputStream(descriptor.getBytes(CHARSET_UTF8));

            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            parser.nextTag();
            parser.require(XmlPullParser.START_TAG, null, ROOT);

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                switch (parser.getName()) {
                    case DEVICE:
                        readDevice(parser);
                        break;
                    default:
                        skipTag(parser);
                        break;
                }
            }
            stream.close();
        }

        private void readDevice(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, DEVICE);

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                switch (parser.getName()) {
                    case BOOT_ID:
                        SSDPDevice.this.bootId = readTag(parser, BOOT_ID);
                        break;
                    case CPP_ID:
                        SSDPDevice.this.cppId = readTag(parser, CPP_ID);
                        break;
                    case DEVICE_TYPE:
                        SSDPDevice.this.deviceType = readTag(parser, DEVICE_TYPE);
                        break;
                    case FRIENDLY_NAME:
                        SSDPDevice.this.friendlyName = readTag(parser, FRIENDLY_NAME);
                        break;
                    case ICON_LIST:
                        readIconList(parser);
                        break;
                    case IP_ADDRESS:
                        SSDPDevice.this.ipAddress = readTag(parser, IP_ADDRESS);
                        break;
                    case MANUFACTURER:
                        SSDPDevice.this.manufacturer = readTag(parser, MANUFACTURER);
                        break;
                    case MANUFACTURER_URL:
                        SSDPDevice.this.manufacturerUrl = readTag(parser, MANUFACTURER_URL);
                        break;
                    case MODEL_DESCRIPTION:
                        SSDPDevice.this.modelDescription = readTag(parser, MODEL_DESCRIPTION);
                        break;
                    case MODEL_NAME:
                        SSDPDevice.this.modelName = readTag(parser, MODEL_NAME);
                        break;
                    case MODEL_NUMBER:
                        SSDPDevice.this.modelNumber = readTag(parser, MODEL_NUMBER);
                        break;
                    case MODEL_URL:
                        SSDPDevice.this.modelUrl = readTag(parser, MODEL_URL);
                        break;
                    case PRESENTATION_URL:
                        SSDPDevice.this.presentationUrl = readTag(parser, PRESENTATION_URL);
                        break;
                    case SERIAL_NUMBER:
                        SSDPDevice.this.serialNumber = readTag(parser, SERIAL_NUMBER);
                        break;
                    case UDN:
                        SSDPDevice.this.udn = readTag(parser, UDN);
                        break;
                    default:
                        skipTag(parser);
                        break;
                }
            }
        }

        private void readIconList(XmlPullParser parser) throws IOException, XmlPullParserException {
            parser.require(XmlPullParser.START_TAG, null, ICON_LIST);

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }
                String name = parser.getName();

                if (ICON.equals(name)) {
                    SSDPDevice.this.iconList.add(readIcon(parser));
                }
            }
        }

        private Icon readIcon(XmlPullParser parser) throws IOException, XmlPullParserException {
            final Icon icon = new Icon();
            parser.require(XmlPullParser.START_TAG, null, ICON);

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                switch (parser.getName()) {
                    case Icon.MIMETYPE:
                        icon.mimetype = readTag(parser, Icon.MIMETYPE);
                        break;
                    case Icon.DEPTH:
                        icon.depth = Integer.valueOf(readTag(parser, Icon.DEPTH));
                        break;
                    case Icon.WIDTH:
                        icon.width = Integer.valueOf(readTag(parser, Icon.WIDTH));
                        break;
                    case Icon.HEIGHT:
                        icon.height = Integer.valueOf(readTag(parser, Icon.HEIGHT));
                        break;
                    case Icon.URL:
                        icon.url = readTag(parser, Icon.URL);
                        break;
                    default:
                        skipTag(parser);
                        break;
                }
            }
            return icon;
        }

        private String readTag(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
            String result = "";
            parser.require(XmlPullParser.START_TAG, null, tag);

            if (parser.next() == XmlPullParser.TEXT) {
                result = parser.getText();
                parser.nextTag();
            }
            parser.require(XmlPullParser.END_TAG, null, tag);

            return result;
        }

        private void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                throw new IllegalStateException();
            }
            int depth = 1;

            while (depth != 0) {
                switch (parser.next()) {
                    case XmlPullParser.END_TAG:
                        depth--;
                        break;
                    case XmlPullParser.START_TAG:
                        depth++;
                        break;
                }
            }
        }
    }

    private final class Icon {
        static final String MIMETYPE = "mimetype";
        static final String WIDTH = "width";
        static final String HEIGHT = "height";
        static final String DEPTH = "depth";
        static final String URL = "url";

        public String mimetype;
        public int width;
        public int height;
        public int depth;
        public String url;
    }

    private SSDPDevice(@NonNull final String descriptor) throws IOException, XmlPullParserException {
        new Parser(descriptor);
    }

    public static SSDPDevice create(@NonNull final String descriptor, String ipAddress, boolean isSecure) {
        SSDPDevice device = null;
        try {
            device = new SSDPDevice(descriptor);
            device.ipAddress = ipAddress;
            device.isSecure = isSecure;
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, "Error parsing descriptor.", e);
        }
        return device;
    }

    public boolean isSecure() {
        return isSecure;
    }

    public String getBootId() {
        return this.bootId;
    }

    public String getCppId() {
        return this.cppId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public Set<Icon> getIconList() {
        return iconList;
    }

    public String getIpAddress() {
        return this.ipAddress;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getManufacturerUrl() {
        return manufacturerUrl;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public String getModelName() {
        return this.modelName;
    }

    public String getModelNumber() {
        return this.modelNumber;
    }

    public String getModelUrl() {
        return modelUrl;
    }

    public String getPresentationUrl() {
        return presentationUrl;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public String getUdn() {
        return udn;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(getClass().getSimpleName() + "@" + hashCode() + "\n\r");

        for (Field field : getClass().getDeclaredFields()) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }
            builder.append(field.getName() + ": [");

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
