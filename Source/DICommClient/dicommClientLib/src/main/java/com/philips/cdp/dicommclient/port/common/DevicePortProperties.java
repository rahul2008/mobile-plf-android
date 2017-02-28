/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp2.commlib.core.port.PortProperties;

public class DevicePortProperties implements PortProperties {
    private String name;
    private String type;
    private String modelid;
    private String swversion;
    private String serial;
    private String ctn;
    private boolean allowuploads;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getModelid() {
        return modelid;
    }

    public void setModelid(String modelid) {
        this.modelid = modelid;
    }

    public String getSwversion() {
        return swversion;
    }

    public void setSwversion(String swversion) {
        this.swversion = swversion;
    }

    public String getSerial() {
        return serial;
    }

    public void setSerial(final String serial) {
        this.serial = serial;
    }

    public String getCtn() {
        return ctn;
    }

    public void setCtn(final String ctn) {
        this.ctn = ctn;
    }

    public boolean isAllowuploads() {
        return allowuploads;
    }

    public void setAllowuploads(final boolean allowuploads) {
        this.allowuploads = allowuploads;
    }
}
