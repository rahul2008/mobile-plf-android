/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.connectivitypowersleep.datamodels;

import android.support.annotation.VisibleForTesting;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.philips.cdp.dicommclient.port.DICommPort;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;

public class GenericPort<T extends PortProperties> extends DICommPort<T> {
    private final static String TAG = "GenericPort";

    private final int productId;
    private final Class<T> propertiesClass;
    private final Gson jsonParser;
    protected final String name;

    public GenericPort(CommunicationStrategy communicationStrategy, String name, int productId, Class<T> propertiesClass) {
        super(communicationStrategy);
        this.name = name;
        this.productId = productId;
        this.propertiesClass = propertiesClass;

        jsonParser = new Gson();
    }

    @Override
    public void processResponse(String jsonResponse) {
        T properties = parseProperties(jsonResponse);
        setPortProperties(properties);
    }

    private T parseProperties(String jsonResponse) {
        try {
            return jsonParser.fromJson(jsonResponse, propertiesClass);
        } catch (JsonSyntaxException exception) {
//            Logger.e(TAG, "Unable to parse properties", exception);
            return null;
        }
    }

    @Override
    @VisibleForTesting
    public String getDICommPortName() {
        return name;
    }

    @Override
    public int getDICommProductId() {
        return productId;
    }

    @Override
    public boolean supportsSubscription() {
        return true;
    }
}
