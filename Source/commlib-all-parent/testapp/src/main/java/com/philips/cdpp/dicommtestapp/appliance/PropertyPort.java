package com.philips.cdpp.dicommtestapp.appliance;

import android.util.Log;

import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdpp.dicommtestapp.appliance.property.PortSpecification;
import com.philips.cdpp.dicommtestapp.appliance.property.Property;

import java.util.Map;

public class PropertyPort extends DICommPort
{
    private static final String TAG = "PropertyPort";
    private PortSpecification mPortSpec;
    private String mErrorText;
    private boolean isEnabled;
    private String mStatusText;

    PropertyPort(CommunicationStrategy communicationStrategy, PortSpecification portSpec) {
        super(communicationStrategy);
        mPortSpec = portSpec;
    }

    @Override
    public boolean isResponseForThisPort(String json) {
        return true;
    }

    @Override
    protected void processResponse(String json) {
        //noinspection unchecked
        Map<String, Object> portResponse = gson.fromJson(json, Map.class);

        for(Property prop : mPortSpec.getProperties()) {
            Object propValue = portResponse.get(prop.getKey());
            if(propValue != null) {
                String valueText = propValue.toString();
                if(valueText.isEmpty()) {
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
        return mPortSpec.getName();
    }

    @Override
    protected int getDICommProductId() {
        return mPortSpec.getProductID();
    }

    @Override
    public boolean supportsSubscription() {
        return mPortSpec.supportsSubscription();
    }

    public String getPortName() {
        return getDICommPortName();
    }

    public String getErrorText() {
        if(mErrorText == null) {
            return "";
        }
        return mErrorText;
    }

    public void setErrorText(String mErrorText) {
        this.mErrorText = mErrorText;
    }

    public PortSpecification getPortSpecification() {
        return mPortSpec;
    }

    public boolean isEnabled() {
        return isEnabled;
    }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public void setStatusText(String statusText) {
        this.mStatusText = statusText;
    }

    public String getStatusText() {
        if(mStatusText == null) {
            return "";
        }
        return mStatusText;
    }
}
