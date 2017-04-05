/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdpp.dicommtestapp.appliance.property;

import java.util.ArrayList;
import java.util.List;

public class ApplianceSpecification {
    private String deviceName;
    private String modelId;
    private String modelName;
    private List<PortSpecification> portSpecifications;

    public ApplianceSpecification() {
        portSpecifications = new ArrayList<>();
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public void setPortSpecifications(List<PortSpecification> portSpecifications) {
        this.portSpecifications = portSpecifications;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public List<PortSpecification> getPortSpecifications() {
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
