/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.commlibexplorer.appliance;

import android.util.Log;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlibexplorer.appliance.property.PortSpecification;
import com.philips.cdp2.commlibexplorer.appliance.property.Property;

import java.util.Map;

public class PropertyPort extends DICommPort implements SupportedPort {
    private static final String TAG = "PropertyPort";
    private PortSpecification portSpec;
    private String errorText;
    private boolean isEnabled;
    private String statusText;

    PropertyPort(CommunicationStrategy communicationStrategy, PortSpecification portSpec) {
        super(communicationStrategy);
        this.portSpec = portSpec;
    }

    @Override
    public boolean isResponseForThisPort(String json) {
        return true;
    }

    @Override
    protected void processResponse(String json) {
        //noinspection unchecked
        Map<String, Object> portResponse = gson.fromJson(json, Map.class);

        for (Property prop : portSpec.getProperties()) {
            Object propValue = portResponse.get(prop.getKey());
            if (propValue != null) {
                String valueText = propValue.toString();
                if (valueText.isEmpty()) {
                    valueText = "--";
                }

                prop.setValueText(valueText);
                String propMsg = String.format("processResponse: Property '%s' has value '%s'", prop.getKey(), propValue);
                Log.d(TAG, propMsg);
            }
        }
    }

    @Override
    protected String getDICommPortName() {
        return portSpec.getName();
    }

    @Override
    protected int getDICommProductId() {
        return portSpec.getProductID();
    }

    @Override
    public boolean supportsSubscription() {
        return portSpec.supportsSubscription();
    }

    @Override
    public String getPortName() {
        return getDICommPortName();
    }

    @Override
    public String getErrorText() {
        if (errorText == null) {
            return "";
        }
        return errorText;
    }

    public void setErrorText(String mErrorText) {
        this.errorText = mErrorText;
    }

    public PortSpecification getPortSpecification() {
        return portSpec;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    @Override
    public String getStatusText() {
        if (statusText == null) {
            return "";
        }
        return statusText;
    }
}
