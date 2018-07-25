/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.dicommclient.port;

import android.os.Handler;
import android.support.annotation.NonNull;
import com.google.gson.annotations.SerializedName;
import com.philips.cdp.dicommclient.util.DICommLog;
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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class NestedDICommPortTest {

    @Mock
    private Handler handlerMock;

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    private static final String NESTED_RESPONSE = "{\"long\":5,\"bool\":\"true\",\"map\":{\"long\":\"6\", \"bool\":\"false\"}}";
    private NestedDiPort nestedDiPort;
    private MapDiPort mapDiPort;

    @Before
    public void setUp() throws Exception {
        HandlerProvider.enableMockedHandler(handlerMock);
        DICommLog.disableLogging();

        nestedDiPort = new NestedDiPort(communicationStrategyMock);
        mapDiPort = new MapDiPort(communicationStrategyMock);
    }

    @Test
    public void itShouldHaveValueForNestedObjectWhenPresentInJsonResponse() {
        nestedDiPort.processResponse(NESTED_RESPONSE);

        assertNotNull(nestedDiPort.getPortProperties().testNested);
    }

    @Test
    public void itShouldHaveProperValueForNestedObjectWhenPresentInJsonResponse() {
        nestedDiPort.processResponse(NESTED_RESPONSE);

        assertEquals(6L, nestedDiPort.getPortProperties().testNested.testLong);
        assertEquals(false, nestedDiPort.getPortProperties().testNested.testBoolean);
        assertNull(nestedDiPort.getPortProperties().testNested.testNested);
    }

    @Test
    public void itShouldHaveTestMapInitializedWhenPresentInJsonResponse() {
        mapDiPort.processResponse(NESTED_RESPONSE);

        assertNotNull(mapDiPort.getPortProperties().testMap);
    }

    @Test
    public void itShouldHaveProperValueWhenPresentInJsonResponse() {
        mapDiPort.processResponse(NESTED_RESPONSE);

        assertEquals(5L, mapDiPort.getPortProperties().testLong);
        assertEquals(true, mapDiPort.getPortProperties().testBoolean);
        assertEquals(2, mapDiPort.getPortProperties().testMap.size());
    }

    @Test
    public void itShouldHaveProperValueForMapWhenPresentInJsonResponse() {
        mapDiPort.processResponse(NESTED_RESPONSE);

        assertTrue(mapDiPort.getPortProperties().testMap.containsKey("long"));
        assertEquals("6", mapDiPort.getPortProperties().testMap.get("long"));
        assertTrue(mapDiPort.getPortProperties().testMap.containsKey("bool"));
        assertEquals("false", mapDiPort.getPortProperties().testMap.get("bool"));
    }

    private static class MapProperties implements PortProperties {

        @SerializedName("long")
        private long testLong;

        @SerializedName("bool")
        private boolean testBoolean;

        @SerializedName("map")
        private Map<String, String> testMap;
    }

    private static class MapDiPort extends DICommPort<MapProperties> {

        MapDiPort(@NonNull CommunicationStrategy communicationStrategy) {
            super(communicationStrategy);
        }

        @Override
        protected void processResponse(String jsonResponse) {
            final MapProperties mapProperties = gson.fromJson(jsonResponse, MapProperties.class);
            setPortProperties(mapProperties);
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
    }

    private static class NestedProperties implements PortProperties {

        @SerializedName("long")
        private long testLong;

        @SerializedName("bool")
        private boolean testBoolean;

        @SerializedName("map")
        private NestedProperties testNested;
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
    }
}