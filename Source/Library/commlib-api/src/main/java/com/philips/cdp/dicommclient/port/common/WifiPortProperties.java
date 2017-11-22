/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

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

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProtection() {
        return protection;
    }

    public void setProtection(String protection) {
        this.protection = protection;
    }

    public String getIpaddress() {
        return ipaddress;
    }

    public void setIpaddress(String ipddress) {
        this.ipaddress = ipddress;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public boolean isDhcp() {
        return dhcp;
    }

    public void setDhcp(boolean dhcp) {
        this.dhcp = dhcp;
    }

    public String getMacaddress() {
        return macaddress;
    }

    public void setMacaddress(String macaddress) {
        this.macaddress = macaddress;
    }

    public String getCppid() {
        return cppid;
    }

    public void setCppid(String cppid) {
        this.cppid = cppid;
    }

}
