/*
 * Copyright (c) 2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.port;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.port.PortProperties;
import com.philips.cdp2.commlib.core.util.HandlerProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class NestedDICommPortTest {

    @Mock
    private Handler handlerMock;

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    private static final String RESPONSE = "{\"long\":5,\"bool\":\"true\",\"map\":{\"test1\":\"5\", \"test2\":\"6\"}}";
    private NestedDiPort nestedDiPort;

    @Before
    public void setUp() throws Exception {
        HandlerProvider.enableMockedHandler(handlerMock);

        nestedDiPort = new NestedDiPort(communicationStrategyMock);
    }

    @Test
    public void itShouldHaveTestMapInitializedWhenPresentInJsonResponse() {
        nestedDiPort.processResponse(RESPONSE);

        assertNotNull(nestedDiPort.getMap());
    }

    @Test
    public void itShouldHaveProperValueWhenPresentInJsonResponse() {
        nestedDiPort.processResponse(RESPONSE);

        assertEquals(5L, nestedDiPort.getPortProperties().testInt);
        assertEquals(true, nestedDiPort.getPortProperties().testBoolean);
        assertEquals(2, nestedDiPort.getPortProperties().testMap.size());
    }

    @Test
    public void itShouldHaveProperValueForMapWhenPresentInJsonResponse() {
        nestedDiPort.processResponse(RESPONSE);

        assertTrue(nestedDiPort.getPortProperties().testMap.containsKey("test1"));
        assertEquals("5", nestedDiPort.getPortProperties().testMap.get("test1"));
        assertTrue(nestedDiPort.getPortProperties().testMap.containsKey("test2"));
        assertEquals("6", nestedDiPort.getPortProperties().testMap.get("test2"));
    }

    private static class NestedProperties implements PortProperties {

        @SerializedName("long")
        private long testInt;

        @SerializedName("bool")
        private boolean testBoolean;

        @SerializedName("map")
        private Map<String, String> testMap;
    }

    private static class NestedDiPort extends DICommPort<NestedProperties> {


        NestedDiPort(@NonNull CommunicationStrategy communicationStrategy) {
            super(communicationStrategy);
        }

        @Override
        protected void processResponse(String jsonResponse) {
            final NestedProperties nestedProperties = gson.fromJson(jsonResponse, NestedProperties.class);
            setPortProperties(nestedProperties);
        }

        @Override
        public String getDICommPortName() {
            return null;
        }

        @Override
        protected int getDICommProductId() {
            return 0;
        }

        @Override
        public boolean supportsSubscription() {
            return false;
        }

        public Map<String, String> getMap() {
            return getPortProperties().testMap;
        }
    }

}