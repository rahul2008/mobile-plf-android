/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import android.os.Handler;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;
import com.philips.cdp2.commlib.core.util.HandlerProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class DevicePortTest {

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    @Mock
    private Handler handlerMock;

    private DevicePort devicePort;
    private final String invalidJson = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\"";
    private final String validData = "{  \n" +
            "   \"name\":\"testName\",\n" +
            "   \"type\":\"testType\",\n" +
            "   \"modelid\":\"testModelid\",\n" +
            "   \"swversion\":\"testSwversion\",\n" +
            "   \"serial\":\"testSerial\",\n" +
            "   \"ctn\":\"testCtn\",\n" +
            "   \"allowuploads\":true\n" +
            "}";

    private final String minimalValidDataset = "{  \n" +
            "   \"name\":\"testName\",\n" +
            "   \"type\":\"testType\"\n" +
            "}";

    @Before
    public void setUp() {
        initMocks(this);

        DICommLog.disableLogging();
        HandlerProvider.enableMockedHandler(handlerMock);

        devicePort = new DevicePort(communicationStrategyMock);
    }

    @Test
    public void test_ShouldReturnNull_WhenProcessResponse_WithInvalidData() {
        devicePort.processResponse(invalidJson);
        DevicePortProperties properties = devicePort.getPortProperties();

        assertThat(properties).isNull();
    }

    @Test
    public void test_ShouldReturnProperties_WhenProcessResponse_WithMinimalDataset() {
        devicePort.processResponse(minimalValidDataset);
        DevicePortProperties properties = devicePort.getPortProperties();

        assertThat(properties).isNotNull();
        assertThat(properties.getName().equals("testName"));
        assertThat(properties.getType().equals("testType"));
    }

    @Test
    public void test_ShouldReturnProperties_WhenProcessResponse_WithInvalidData() {
        devicePort.processResponse(validData);
        DevicePortProperties properties = devicePort.getPortProperties();

        assertThat(properties.getName()).isEqualTo("testName");
        assertThat(properties.getType()).isEqualTo("testType");
        assertThat(properties.getModelid()).isEqualTo("testModelid");
        assertThat(properties.getSwversion()).isEqualTo("testSwversion");
        assertThat(properties.getSerial()).isEqualTo("testSerial");
        assertThat(properties.getCtn()).isEqualTo("testCtn");
        assertThat(properties.isAllowuploads()).isTrue();
    }

    @Test
    public void test_ShouldReturnTrue_WhenSupportsSubscription() {
        assertThat(devicePort.supportsSubscription()).isTrue();
    }
}
