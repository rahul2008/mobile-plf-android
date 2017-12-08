/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp2.commlib.core.appliance.Appliance;
import com.philips.cdp2.commlib.core.port.PortProperties;

/**
 * Properties of the {@link WifiPort}.
 *
 * @publicApi
 */
public class WifiPortProperties implements PortProperties {

    private String ssid;
    private String password;
    private String protection;
    private String ipaddress;
    private String netmask;
    private String gateway;
    private boolean dhcp;
    private String macaddress;
    private String cppid;

    /**
     * Gets WiFi AP ssid.
     *
     * @return the ssid
     */
    public String getSsid() {
        return ssid;
    }

    /**
     * Sets WiFi AP ssid.
     *
     * @param ssid the ssid
     */
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    /**
     * Gets WiFi password.
     *
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets WiFi password.
     *
     * @param password the password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gets protection.
     *
     * @return the protection
     */
    public String getProtection() {
        return protection;
    }

    /**
     * Sets protection.
     *
     * @param protection the protection
     */
    public void setProtection(String protection) {
        this.protection = protection;
    }

    /**
     * Gets {@link Appliance} ipaddress.
     *
     * @return the ipaddress
     */
    public String getIpaddress() {
        return ipaddress;
    }

    /**
     * Sets {@link Appliance} ipaddress.
     *
     * @param ipddress the ipddress
     */
    public void setIpaddress(String ipddress) {
        this.ipaddress = ipddress;
    }

    /**
     * Gets netmask.
     *
     * @return the netmask
     */
    public String getNetmask() {
        return netmask;
    }

    /**
     * Sets netmask.
     *
     * @param netmask the netmask
     */
    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    /**
     * Gets gateway.
     *
     * @return the gateway
     */
    public String getGateway() {
        return gateway;
    }

    /**
     * Sets gateway.
     *
     * @param gateway the gateway
     */
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    /**
     * Is DHCP used by the {@link Appliance}
     *
     * @return <code>true</code> if DHCP is used.
     */
    public boolean isDhcp() {
        return dhcp;
    }

    /**
     * Sets whether to use DHCP for automatic IP address configuration.
     *
     * @param dhcp <code>true</code> if DHCP should be used.
     */
    public void setDhcp(boolean dhcp) {
        this.dhcp = dhcp;
    }

    /**
     * Gets macaddress.
     *
     * @return the macaddress
     */
    public String getMacaddress() {
        return macaddress;
    }

    /**
     * Sets macaddress.
     *
     * @param macaddress the macaddress
     */
    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    /**
     * Gets the {@link Appliance} cppid.
     *
     * @return the cppid
     */
    public String getCppid() {
        return cppid;
    }

    /**
     * Sets the {@link Appliance} cppid.
     *
     * @param cppid the cppid
     */
    public void setCppid(String cppid) {
        this.cppid = cppid;
    }
}
