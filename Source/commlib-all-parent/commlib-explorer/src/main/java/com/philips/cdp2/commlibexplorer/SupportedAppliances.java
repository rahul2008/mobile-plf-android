/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer;

import android.text.TextUtils;

import com.philips.cdp2.commlibexplorer.appliance.property.ApplianceSpecification;
import com.philips.cdp2.commlibexplorer.appliance.property.PortSpecification;
import com.philips.cdp2.commlibexplorer.appliance.property.Property;
import com.philips.cdp2.commlibexplorer.appliance.property.PropertyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SupportedAppliances {
    private List<PortSpecification> defaultPortSpecs;
    private ApplianceSpecification unknownApplianceSpec;
    private Map<String, ApplianceSpecification> applianceSpecsByModelId;
    private Map<String, ApplianceSpecification> applianceSpecsByModelName;

    public SupportedAppliances() {
        defaultPortSpecs = new ArrayList<>();
        defaultPortSpecs.add(devicePort());
        defaultPortSpecs.add(wifiPort());
        defaultPortSpecs.add(firmwarePort());
        defaultPortSpecs.add(securityPort());

        unknownApplianceSpec = new ApplianceSpecification();
        unknownApplianceSpec.setDeviceName("Unknown Appliance");
        unknownApplianceSpec.setModelId("Unknown");
        unknownApplianceSpec.setPortSpecifications(defaultPortSpecs);

        applianceSpecsByModelId = new HashMap<>();
        applianceSpecsByModelName = new HashMap<>();
        createSupportedApplianceSpecs();
    }

    public ApplianceSpecification findSpecification(String modelId, String modelName) {
        // TODO Refactor this when libraries have been unified (BLE devices now have 'null' for modelName)
        if (TextUtils.isEmpty(modelId)) {
            modelId = "unknown";
        }

        ApplianceSpecification spec = applianceSpecsByModelId.get(modelId.toUpperCase());
        if (spec == null) {
            spec = applianceSpecsByModelName.get(modelName.toUpperCase());
        }
        if (spec == null) {
            spec = unknownApplianceSpec;
        }
        return spec;
    }

    private void createSupportedApplianceSpecs() {
        ApplianceSpecification jaguar = ac2889();
        applianceSpecsByModelId.put(jaguar.getModelId().toUpperCase(), jaguar);
        applianceSpecsByModelName.put(jaguar.getModelName().toUpperCase(), jaguar);

        ApplianceSpecification simba = ac1214();
        applianceSpecsByModelId.put(simba.getModelId().toUpperCase(), simba);

        ApplianceSpecification compas = scd860();
        applianceSpecsByModelId.put(compas.getModelId().toUpperCase(), compas);

        ApplianceSpecification refnode = refnode();
        applianceSpecsByModelId.put(refnode.getModelId().toUpperCase(), refnode);
        applianceSpecsByModelName.put(refnode.getModelName().toUpperCase(), refnode);

        // Add 'default ports' to all appliance specs
        for (Map.Entry<String, ApplianceSpecification> pair : applianceSpecsByModelId.entrySet()) {
            ApplianceSpecification supportedAppliance = pair.getValue();
            for (PortSpecification port : defaultPortSpecs) {
                supportedAppliance.addPortSpecification(port);
            }
        }
    }

    // SUPPORTED APPLIANCES
    private ApplianceSpecification refnode() {
        ApplianceSpecification refnode = new ApplianceSpecification();
        refnode.setDeviceName("Reference node");
        refnode.setModelId("PS1234");
        refnode.setModelName("ReferenceNode");
        return refnode;
    }

    private ApplianceSpecification scd860() {
        Property useridsProp = new Property("User identifiers", "userids", PropertyType.ARRAY);
        PortSpecification userPortSpec = new PortSpecification();
        userPortSpec.setName("userinfo");
        userPortSpec.setProductID(0);
        userPortSpec.isSubscriptionSupported(true);
        userPortSpec.addProperty(useridsProp);

        Property climateProp = new Property("Climate", "climate", PropertyType.ARRAY);
        PortSpecification climatePortSpec = new PortSpecification();
        climatePortSpec.setName("climate");
        climatePortSpec.setProductID(1);
        climatePortSpec.isSubscriptionSupported(true);
        climatePortSpec.addProperty(climateProp);

        ApplianceSpecification scd860 = new ApplianceSpecification();
        scd860.setDeviceName("uGrow Smart Baby Monitor");
        scd860.setModelId("SCD860");
        scd860.addPortSpecification(userPortSpec);
        scd860.addPortSpecification(climatePortSpec);
        return scd860;
    }

    private ApplianceSpecification ac1214() {
        Property omProp = new Property("Operation Mode", "om", PropertyType.STRING);
        Property pwrProp = new Property("Power", "pwr", PropertyType.INT);
        Property clProp = new Property("Child Lock", "cl", PropertyType.INT);
        Property aqilProp = new Property("Air Quality Index Light", "aqil", PropertyType.INT);
        Property uilProp = new Property("User Interface Light", "uil", PropertyType.STRING);
        Property dtProp = new Property("Device Timer", "dt", PropertyType.INT);
        Property dtrsProp = new Property("Device Timer Remaining", " dtrs", PropertyType.INT);
        Property modeProp = new Property("Mode", "mode", PropertyType.STRING);
        Property pm25Prop = new Property("PM2.5", "pm25", PropertyType.INT);
        Property iaqlProp = new Property("Indoor Air Quality", "iaql", PropertyType.INT);
        Property aqitProp = new Property("Air Quality Index Threshold", "aqit", PropertyType.INT);
        Property ddpProp = new Property("Device Display Parameter", "ddp", PropertyType.INT);
        Property errProp = new Property("Error Code", "err", PropertyType.INT);
        PortSpecification airPort = new PortSpecification();
        airPort.addProperty(omProp);
        airPort.addProperty(pwrProp);
        airPort.addProperty(clProp);
        airPort.addProperty(aqilProp);
        airPort.addProperty(uilProp);
        airPort.addProperty(dtProp);
        airPort.addProperty(dtrsProp);
        airPort.addProperty(modeProp);
        airPort.addProperty(pm25Prop);
        airPort.addProperty(iaqlProp);
        airPort.addProperty(aqitProp);
        airPort.addProperty(ddpProp);
        airPort.addProperty(errProp);

        Property useridsProp = new Property("User identifiers", "userids", PropertyType.ARRAY);
        PortSpecification userPortSpec = new PortSpecification();
        userPortSpec.setName("userinfo");
        userPortSpec.setProductID(0);
        userPortSpec.isSubscriptionSupported(true);
        userPortSpec.addProperty(useridsProp);

        ApplianceSpecification ac1214 = new ApplianceSpecification();
        ac1214.setDeviceName("Simba Air Purifier");
        ac1214.setModelId("AC1214");
        ac1214.addPortSpecification(userPortSpec);
        ac1214.addPortSpecification(airPort);
        return ac1214;
    }

    private ApplianceSpecification ac2889() {
        Property aqilProp = new Property("AQILight", "aqil", PropertyType.STRING);

        PortSpecification airPort = new PortSpecification();
        airPort.setName("air");
        airPort.setProductID(1);
        airPort.isSubscriptionSupported(true);
        airPort.addProperty(aqilProp);

        ApplianceSpecification ac2889 = new ApplianceSpecification();
        ac2889.setDeviceName("Jaguar AirPurifier");
        ac2889.setModelName("AirPurifier");
        ac2889.setModelId("AC2889");
        ac2889.addPortSpecification(airPort);
        return ac2889;
    }

    // DEFAULT PORTS
    private PortSpecification devicePort() {
        PortSpecification devicePort = new PortSpecification();
        devicePort.setName("device");
        devicePort.setProductID(1);
        devicePort.isSubscriptionSupported(true);

        Property nameProp = new Property("Name", "name", PropertyType.STRING);
        devicePort.addProperty(nameProp);

        Property typeProp = new Property("Type", "type", PropertyType.STRING);
        devicePort.addProperty(typeProp);

        Property modelIdProp = new Property("Model ID", "modelid", PropertyType.STRING);
        devicePort.addProperty(modelIdProp);

        Property swVersionProp = new Property("Software version", "swversion", PropertyType.STRING);
        devicePort.addProperty(swVersionProp);

        return devicePort;
    }

    private PortSpecification wifiPort() {
        PortSpecification wifiPort = new PortSpecification();
        wifiPort.setName("wifi");
        wifiPort.setProductID(0);
        wifiPort.isSubscriptionSupported(false);

        Property netmaskProp = new Property("Netmask", "netmask", PropertyType.STRING);
        wifiPort.addProperty(netmaskProp);

        Property passwordProp = new Property("Password", "password", PropertyType.STRING);
        wifiPort.addProperty(passwordProp);

        Property cppidProp = new Property("CPPID", "cppid", PropertyType.STRING);
        wifiPort.addProperty(cppidProp);

        Property protectionProp = new Property("Protection", "protection", PropertyType.STRING);
        wifiPort.addProperty(protectionProp);

        Property ipAddressProp = new Property("IP Address", "ipaddress", PropertyType.STRING);
        wifiPort.addProperty(ipAddressProp);

        Property macAddressProp = new Property("MAC Address", "macaddress", PropertyType.STRING);
        wifiPort.addProperty(macAddressProp);

        Property ssidProp = new Property("SSID", "ssid", PropertyType.STRING);
        wifiPort.addProperty(ssidProp);

        Property gatewayProp = new Property("Gateway", "gateway", PropertyType.STRING);
        wifiPort.addProperty(gatewayProp);

        Property dhcpProp = new Property("DHCP", "dhcp", PropertyType.BOOL);
        wifiPort.addProperty(dhcpProp);

        return wifiPort;
    }

    private PortSpecification firmwarePort() {
        PortSpecification firmwarePort = new PortSpecification();
        firmwarePort.setName("firmware");
        firmwarePort.setProductID(0);
        firmwarePort.isSubscriptionSupported(false);

        Property mandatoryProp = new Property("Mandatory", "mandatory", PropertyType.BOOL);
        firmwarePort.addProperty(mandatoryProp);

        Property upgradeProp = new Property("Upgrade", "upgrade", PropertyType.STRING);
        firmwarePort.addProperty(upgradeProp);

        Property versionProp = new Property("Version", "version", PropertyType.STRING);
        firmwarePort.addProperty(versionProp);

        Property statusMsgProp = new Property("Status Message", "statusmsg", PropertyType.STRING);
        firmwarePort.addProperty(statusMsgProp);

        Property progressProp = new Property("Progress", "progress", PropertyType.INT);
        firmwarePort.addProperty(progressProp);

        Property nameProp = new Property("Name", "name", PropertyType.STRING);
        firmwarePort.addProperty(nameProp);

        Property stateProp = new Property("State", "state", PropertyType.STRING);
        firmwarePort.addProperty(stateProp);

        return firmwarePort;
    }

    private PortSpecification securityPort() {
        PortSpecification wifiPort = new PortSpecification();
        wifiPort.setName("security");
        wifiPort.setProductID(0);
        wifiPort.isSubscriptionSupported(true);

        return wifiPort;
    }
}
