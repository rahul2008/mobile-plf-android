/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.appliance.property;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class ApplianceSpecification {
    private String deviceName;
    private String modelId;
    private String modelName;
    private Set<PortSpecification> portSpecifications;

    public ApplianceSpecification() {
        portSpecifications = new CopyOnWriteArraySet<>();
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setPortSpecifications(Set<PortSpecification> portSpecifications) {
        this.portSpecifications = portSpecifications;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public Set<PortSpecification> getPortSpecifications() {
        return portSpecifications;
    }

    public String getModelId() {
        return modelId;
    }

    public void addPortSpecification(PortSpecification specification) {
        this.portSpecifications.add(specification);
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
}
