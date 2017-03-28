package com.philips.cdpp.dicommtestapp;

import android.text.TextUtils;

import com.philips.cdpp.dicommtestapp.appliance.property.ApplianceSpecification;
import com.philips.cdpp.dicommtestapp.appliance.property.PortSpecification;
import com.philips.cdpp.dicommtestapp.appliance.property.Property;
import com.philips.cdpp.dicommtestapp.appliance.property.PropertyType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportedAppliances
{
    private List<PortSpecification> defaultPortSpecs;
    private ApplianceSpecification unknownApplianceSpec;
    private Map<String, ApplianceSpecification> applianceSpecs;

    public SupportedAppliances() {
        defaultPortSpecs = new ArrayList<>();
        defaultPortSpecs.add(devicePort());
        defaultPortSpecs.add(wifiPort());
        defaultPortSpecs.add(firmwarePort());
        defaultPortSpecs.add(securityPort());

        unknownApplianceSpec = new ApplianceSpecification();
        unknownApplianceSpec.setDeviceName("Unknown Appliance");
        unknownApplianceSpec.setModelNumber("Unknown");
        unknownApplianceSpec.setPortSpecifications(defaultPortSpecs);

        applianceSpecs = new HashMap<>();
        createSupportedApplianceSpecs();
    }

    public ApplianceSpecification findSpecification(String modelNumber)
    {
        // TODO Refactor this when libraries have been unified (BLE devices now have 'null' for modelType)
        if(TextUtils.isEmpty(modelNumber)) {
            modelNumber = "unknown";
        }

        ApplianceSpecification spec = applianceSpecs.get(modelNumber.toUpperCase());
        if(spec == null) {
            spec = unknownApplianceSpec;
        }
        return spec;
    }

    private void createSupportedApplianceSpecs() {
        ApplianceSpecification jaguar = ac2889();
        applianceSpecs.put(jaguar.getModelNumber().toUpperCase(), jaguar);

        ApplianceSpecification simba = ac1214();
        applianceSpecs.put(simba.getModelNumber().toUpperCase(), simba);

        ApplianceSpecification compas = scd860();
        applianceSpecs.put(compas.getModelNumber().toUpperCase(), compas);

        // Add 'default ports' to all appliance specs
        for (Map.Entry<String,ApplianceSpecification> pair : applianceSpecs.entrySet()) {
            ApplianceSpecification supportedAppliance = pair.getValue();
            for(PortSpecification port : defaultPortSpecs) {
                supportedAppliance.addPortSpecification(port);
            }
        }
    }

    // SUPPORTED APPLIANCES
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
        scd860.setModelNumber("SCD860");
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
        ac1214.setModelNumber("AC1214");
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
        ac2889.setModelNumber("AC2889");
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
