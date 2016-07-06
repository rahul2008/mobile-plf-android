package com.philips.cdp.dicommclientsample.airpurifier;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.port.DICommPort;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public abstract class AirPort<T extends AirPortProperties> extends DICommPort<T> {

    //This name and ID need to match with the appliance's DiComm protocol port name and ID
    final String NAME = "air";
    final int PRODUCTID = 1;
    final Gson jsonParser;
    final Class<T> propertiesClass;

    public AirPort(final NetworkNode networkNode, final CommunicationStrategy communicationStrategy, Class<T> propertiesClass) {
        super(networkNode, communicationStrategy);
        jsonParser = new Gson();
        this.propertiesClass = propertiesClass;
    }

    @Override
    public boolean isResponseForThisPort(final String jsonResponse) {
        try {
            AirPortProperties portProperties = jsonParser.fromJson(jsonResponse, propertiesClass);

            // We need to check that the portProperties content is actually not null
            return portProperties.lightIsSet();
        } catch (JsonSyntaxException exception) {
            return false;
        }
    }

    @Override
    protected void processResponse(final String jsonResponse) {
        T airPortProperties = jsonParser.fromJson(jsonResponse, propertiesClass);
        setPortProperties(airPortProperties);
    }

    @Override
    protected String getDICommPortName() {
        return NAME;
    }

    @Override
    protected int getDICommProductId() {
        return PRODUCTID;
    }

    @Override
    public boolean supportsSubscription() {
        return true;
    }

    public abstract void setLight(final boolean light);
}
