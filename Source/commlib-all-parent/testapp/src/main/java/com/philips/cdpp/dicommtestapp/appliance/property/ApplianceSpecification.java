package com.philips.cdpp.dicommtestapp.appliance.property;

import java.util.ArrayList;
import java.util.List;

public class ApplianceSpecification {
    private String deviceName;
    private String modelNumber;
    private List<PortSpecification> portSpecifications;

    public ApplianceSpecification() {
        portSpecifications = new ArrayList<>();
    }

    public void setModelNumber(String modelNumber) {
        this.modelNumber = modelNumber;
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

    public String getModelNumber() {
        return modelNumber;
    }

    public void addPortSpecification(PortSpecification specification) {
        this.portSpecifications.add(specification);
    }
}
