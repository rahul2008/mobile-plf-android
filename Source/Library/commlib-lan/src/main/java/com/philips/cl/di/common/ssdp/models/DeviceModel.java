/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cl.di.common.ssdp.models;

import android.app.LauncherActivity.ListItem;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.util.List;
import java.util.Locale;

/**
 * Device class which keeps device information and services
 */
@Deprecated
public class DeviceModel {

    private static final String LOG_TAG = null;

    public static final String MANUFACTURER_PHILIPS = "philips";

    public static final String MODEL_NAME_TWO_K_NINE = "2k9"; //$NON-NLS-1$

    public static final String MODEL_NAME_TWO_K_TEN = "2k10"; //$NON-NLS-1$

    private String ipAddress;

    private Drawable icon;

    private String id;

    private boolean isLocal = false;

    private String location = null;

    private String nts = null;

    private int port;

    private SSDPdevice ssdpDevice;

    private String usn = null;

    private String bootId = null;

    private String cppId = null;

    /**
     * constructor used by TwonkyService for discovered UPNPs
     *
     * @param listInfo ListItem
     * @param isNew    boolean
     */
    public DeviceModel(final ListItem listInfo, final boolean isNew) {
        ipAddress = "";
    }

    /**
     * Creates dummy device from SSDP byebye to remove Webremote services
     *
     * @param udn String
     */
    public DeviceModel(final String udn) {
        id = udn;
    }

    /**
     * Creates device downloaded from EPG server
     *
     * @param jointspaceId String
     * @param epgId        String
     */
    public DeviceModel(final String jointspaceId, final String epgId) {
        id = jointspaceId;
    }

    /**
     * Constructor for DeviceModel.
     *
     * @param nts       String
     * @param usn       String
     * @param location  String
     * @param ipAddress String
     * @param port      int
     */
    public DeviceModel(final String nts, final String usn, final String location, final String ipAddress,
                       final int port, final String bootId) {
        this.nts = nts;
        this.usn = usn;
        this.location = location;
        this.ipAddress = ipAddress;
        this.port = port;
        this.bootId = bootId;
    }

    /**
     * Used to device comparison. In most cases we base comparison on UDN.
     * Special case is 2k9 which has a variable UDN - IP used instead
     *
     * @param other device object to compare
     * @return false if not the same device
     */
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        final DeviceModel currentObject = (DeviceModel) other;
        boolean flag;

        if ((id != null) && id.equals(currentObject.id)) {
            flag = true;
        } else if ((ipAddress != null) && ipAddress.equals(currentObject.ipAddress)) {
            flag = true;
        } else if ((ssdpDevice != null) && (currentObject.getSsdpDevice() != null)) {
            if (ssdpDevice.getModelName().equals(currentObject.getSsdpDevice().getModelName())
                    || ssdpDevice.getManufacturer().equals(currentObject.getSsdpDevice().getManufacturer())) {
                // if both devices are 2k9 and they have the same IP address
                // we assume it's the same device
                flag = true;
            }
            flag = false;
        } else {
            flag = false;
        }
        return flag;
    }

    /**
     * @return icon representing device
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * Method getIconUrl.
     *
     * @param iconList List<Icon>
     * @param baseUrl  String
     * @return String
     */
    public String getIconUrl(final List<Icon> iconList, String baseUrl) {
        int biggestHeight = -1;
        int iconHeight = 0;

        Icon biggestIcon = null;

        for (final Icon iconData : iconList) {
            if (null != iconData) {
                try {
                    iconHeight = Integer.valueOf(iconData.height);
                    if (biggestHeight < iconHeight) {
                        biggestIcon = iconData;
                        biggestHeight = iconHeight;
                    }
                } catch (final NumberFormatException e) {
                    Log.e(LOG_TAG, "NumberFormatException" + e.getMessage());
                }
            }
        }

        if ((null != biggestIcon) && (null != biggestIcon.url)) {
            baseUrl += biggestIcon.url;
        }
        return baseUrl;
    }

    /**
     * Returns device identification numer.
     *
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * @return IP address representing device
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @return the location
     */
    public String getLocation() {
        return location;
    }

    /**
     * @return the nts
     */
    public String getNts() {
        return nts;
    }

    /**
     * Method getPort.
     *
     * @return int
     */
    public int getPort() {
        return port;
    }

    /**
     * Method getSsdpDevice.
     *
     * @return SSDPdevice
     */
    public SSDPdevice getSsdpDevice() {
        return ssdpDevice;
    }

    /**
     * @return the usn
     */
    public String getUsn() {
        return usn;
    }

    /**
     * @return the bootId
     */
    public String getBootId() {
        return bootId;
    }

    /**
     * @return the cppId
     */
    public String getCppId() {
        return cppId;
    }

    /**
     * Method hashCode.
     *
     * @return int
     */
    @Override
    public int hashCode() {
        int hashcode;
        if (id != null) {
            hashcode = id.hashCode();
        } else {
            hashcode = super.hashCode();
        }
        return hashcode;
    }

    /**
     * Method isLocal.
     *
     * @return boolean
     */
    public boolean isLocal() {
        return isLocal;
    }

    /**
     * Method isPhilipsDevice.
     *
     * @return boolean
     */
    public final boolean isPhilipsDevice() {
        Boolean isPhilipsDevice = false;
        final String manufacturer = ssdpDevice.getManufacturer();
        if (null != manufacturer) {
            isPhilipsDevice = manufacturer.toLowerCase(Locale.US).contains(MANUFACTURER_PHILIPS);
        }
        return isPhilipsDevice;
    }

    /**
     * Device constructor for SSDP discovery
     *
     * @param ssdpDevice delivered by SSDP discovery service
     */
    @SuppressWarnings("deprecation")
    public void setFromSSDP(final SSDPdevice ssdpDevice) {
        this.ssdpDevice = ssdpDevice;
        if (null != ssdpDevice) {
            id = ssdpDevice.getUdn();
            isLocal = false;
            icon = new BitmapDrawable();

            if (isPhilipsDevice()) {
                ssdpDevice.getPresentationURL();
                if (ssdpDevice.getPresentationURL() != null && !ssdpDevice.getPresentationURL().contains("http://")) {
                    final StringBuilder builder = new StringBuilder(this.ssdpDevice.getBaseURL());
                    builder.append(ssdpDevice.getPresentationURL());
                    builder.append(ssdpDevice.getXScreen());
                    this.ssdpDevice.setBaseURL(builder.toString());
                } else {
                    ssdpDevice.setPresentationURL(ssdpDevice.getPresentationURL() + ssdpDevice.getXScreen());
                }
            }
        }
    }

    /**
     * Method setIcon.
     *
     * @param drawable Drawable
     */
    void setIcon(final Drawable drawable) {
        icon = drawable;
    }

    /**
     * @param id the id to set
     */
    public void setId(final String id) {
        this.id = id;
    }

    /**
     * @param ipAddress the mIpAddress to set
     */
    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @param isLocal the isLocal to set
     */
    public void setIsLocal(final boolean isLocal) {
        this.isLocal = isLocal;
    }

    /**
     * @param location the location to set
     */
    public void setLocation(final String location) {
        this.location = location;
    }

    /**
     * @param nts the nts to set
     */
    public void setNts(final String nts) {
        this.nts = nts;
    }

    /**
     * @param port the port to set
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * Method setSsdpDevice.
     *
     * @param ssdpDevice SSDPdevice
     */
    public void setSsdpDevice(final SSDPdevice ssdpDevice) {
        this.ssdpDevice = ssdpDevice;
    }

    /**
     * @param usn the usn to set
     */
    public void setUsn(final String usn) {
        this.usn = usn;
    }

    /**
     * @param bootId the bootId to set
     */
    public void setBootId(final String bootId) {
        this.bootId = bootId;
    }

    /**
     * @param cppId the cppId to set
     */
    public void setCppId(final String cppId) {
        this.cppId = cppId;
    }

    /**
     * printing method used for debugging
     *
     * @return String
     */
    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("SSDP discovered device details [Usn:").append(usn);
        builder.append("][Nts:").append(nts).append(']');
        builder.append("[Location:").append(location).append("][ IpAddress:");
        builder.append(ipAddress).append("][ Port:").append(port);
        builder.append("][BOOT ID ").append(bootId);
        builder.append("][SSDP ").append(ssdpDevice).append(']'); //$NON-NLS-1$
        return builder.toString();
    }

}
