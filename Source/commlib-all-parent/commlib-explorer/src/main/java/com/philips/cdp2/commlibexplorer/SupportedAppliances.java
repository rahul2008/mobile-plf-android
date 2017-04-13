/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.philips.cdp2.commlibexplorer.appliance.property.ApplianceSpecification;
import com.philips.cdp2.commlibexplorer.appliance.property.PortSpecification;
import com.philips.cdp2.commlibexplorer.appliance.property.Property;
import com.philips.cdp2.commlibexplorer.appliance.property.PropertyType;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


public class SupportedAppliances {
    private Set<PortSpecification> defaultPortSpecs;
    private ApplianceSpecification unknownApplianceSpec;
    private Map<String, ApplianceSpecification> applianceSpecsByModelId;
    private Map<String, ApplianceSpecification> applianceSpecsByModelName;

    public SupportedAppliances() {
        defaultPortSpecs = new CopyOnWriteArraySet<>();
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

        PortSpecification climatePortSpec = new PortSpecification();
        climatePortSpec.setName("climate");
        climatePortSpec.setProductID(1);
        climatePortSpec.isSubscriptionSupported(true);
        climatePortSpec.addProperty(new Property("Climate", "climate", PropertyType.ARRAY));

        ApplianceSpecification scd860 = new ApplianceSpecification();
        scd860.setDeviceName("uGrow Smart Baby Monitor");
        scd860.setModelId("SCD860");
        scd860.addPortSpecification(userPort());
        scd860.addPortSpecification(climatePortSpec);
        return scd860;
    }

    private ApplianceSpecification ac1214() {
        PortSpecification airPort = new PortSpecification();
        airPort.addProperty(new Property("Operation Mode", "om", PropertyType.STRING));
        airPort.addProperty(new Property("Power", "pwr", PropertyType.INT));
        airPort.addProperty(new Property("Child Lock", "cl", PropertyType.INT));
        airPort.addProperty(new Property("Air Quality Index Light", "aqil", PropertyType.INT));
        airPort.addProperty(new Property("User Interface Light", "uil", PropertyType.STRING));
        airPort.addProperty(new Property("Device Timer", "dt", PropertyType.INT));
        airPort.addProperty(new Property("Device Timer Remaining", " dtrs", PropertyType.INT));
        airPort.addProperty(new Property("Mode", "mode", PropertyType.STRING));
        airPort.addProperty(new Property("PM2.5", "pm25", PropertyType.INT));
        airPort.addProperty(new Property("Indoor Air Quality", "iaql", PropertyType.INT));
        airPort.addProperty(new Property("Air Quality Index Threshold", "aqit", PropertyType.INT));
        airPort.addProperty(new Property("Device Display Parameter", "ddp", PropertyType.INT));
        airPort.addProperty(new Property("Error Code", "err", PropertyType.INT));

        ApplianceSpecification ac1214 = new ApplianceSpecification();
        ac1214.setDeviceName("Simba Air Purifier");
        ac1214.setModelId("AC1214");
        ac1214.addPortSpecification(userPort());
        ac1214.addPortSpecification(airPort);
        return ac1214;
    }

    private ApplianceSpecification ac2889() {

        PortSpecification airPort = new PortSpecification();
        airPort.setName("air");
        airPort.setProductID(1);
        airPort.isSubscriptionSupported(true);
        airPort.addProperty(new Property("AQILight", "aqil", PropertyType.STRING));

        ApplianceSpecification ac2889 = new ApplianceSpecification();
        ac2889.setDeviceName("Jaguar AirPurifier");
        ac2889.setModelName("AirPurifier");
        ac2889.setModelId("AC2889");
        ac2889.addPortSpecification(airPort);
        return ac2889;
    }

    @NonNull
    private PortSpecification userPort() {
        PortSpecification userPort = new PortSpecification();
        userPort.setName("userinfo");
        userPort.setProductID(0);
        userPort.isSubscriptionSupported(true);
        userPort.addProperty(new Property("User identifiers", "userids", PropertyType.ARRAY));
        return userPort;
    }

    // DEFAULT PORTS
    private PortSpecification devicePort() {
        PortSpecification devicePort = new PortSpecification();
        devicePort.setName("device");
        devicePort.setProductID(1);
        devicePort.isSubscriptionSupported(true);

        devicePort.addProperty(new Property("Name", "name", PropertyType.STRING));
        devicePort.addProperty(new Property("Type", "type", PropertyType.STRING));
        devicePort.addProperty(new Property("Model ID", "modelid", PropertyType.STRING));
        devicePort.addProperty(new Property("Software version", "swversion", PropertyType.STRING));

        return devicePort;
    }

    private PortSpecification wifiPort() {
        PortSpecification wifiPort = new PortSpecification();
        wifiPort.setName("wifi");
        wifiPort.setProductID(0);
        wifiPort.isSubscriptionSupported(false);

        wifiPort.addProperty(new Property("Netmask", "netmask", PropertyType.STRING));
        wifiPort.addProperty(new Property("Password", "password", PropertyType.STRING));
        wifiPort.addProperty(new Property("CPPID", "cppid", PropertyType.STRING));
        wifiPort.addProperty(new Property("Protection", "protection", PropertyType.STRING));
        wifiPort.addProperty(new Property("IP Address", "ipaddress", PropertyType.STRING));
        wifiPort.addProperty(new Property("MAC Address", "macaddress", PropertyType.STRING));
        wifiPort.addProperty(new Property("SSID", "ssid", PropertyType.STRING));
        wifiPort.addProperty(new Property("Gateway", "gateway", PropertyType.STRING));
        wifiPort.addProperty(new Property("DHCP", "dhcp", PropertyType.BOOL));

        return wifiPort;
    }

    private PortSpecification firmwarePort() {
        PortSpecification firmwarePort = new PortSpecification();
        firmwarePort.setName("firmware");
        firmwarePort.setProductID(0);
        firmwarePort.isSubscriptionSupported(false);

        firmwarePort.addProperty(new Property("Mandatory", "mandatory", PropertyType.BOOL));
        firmwarePort.addProperty(new Property("Upgrade", "upgrade", PropertyType.STRING));
        firmwarePort.addProperty(new Property("Version", "version", PropertyType.STRING));
        firmwarePort.addProperty(new Property("Status Message", "statusmsg", PropertyType.STRING));
        firmwarePort.addProperty(new Property("Progress", "progress", PropertyType.INT));
        firmwarePort.addProperty(new Property("Name", "name", PropertyType.STRING));
        firmwarePort.addProperty(new Property("State", "state", PropertyType.STRING));

        return firmwarePort;
    }

    private PortSpecification securityPort() {
        PortSpecification securityPort = new PortSpecification();
        securityPort.setName("security");
        securityPort.setProductID(0);
        securityPort.isSubscriptionSupported(true);

        return securityPort;
    }
}
