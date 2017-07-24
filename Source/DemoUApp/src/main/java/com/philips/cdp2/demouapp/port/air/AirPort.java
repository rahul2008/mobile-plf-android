/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.demouapp.port.air;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp.dicommclient.util.GsonProvider;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

public abstract class AirPort<T extends AirPortProperties> extends DICommPort<T> {

    final String NAME = "air";
    final int PRODUCTID = 1;
    final Gson jsonParser;
    final Class<T> propertiesClass;

    public AirPort(final CommunicationStrategy communicationStrategy, Class<T> propertiesClass) {
        super(communicationStrategy);
        jsonParser = GsonProvider.get();
        this.propertiesClass = propertiesClass;
    }

    @Override
    public boolean isResponseForThisPort(final String jsonResponse) {
        try {
            AirPortProperties portProperties = jsonParser.fromJson(jsonResponse, propertiesClass);

            return portProperties != null && portProperties.lightIsSet();
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
