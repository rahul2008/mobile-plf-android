/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.dicommclient.util.DICommLog;
import com.philips.cdp2.commlib.core.communication.CommunicationStrategy;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class DevicePortTest {

    @Mock
    private CommunicationStrategy communicationStrategyMock;

    private DevicePort devicePort;
    private String invalidJson = "{\"aqi\":\"1\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"75\",\"fs2\":\"855\",\"fs3\":\"2775\",\"fs4\":\"2775\",\"dtrs\":\"0\",\"aqit\":\"500\",\"clef1\":\"n\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"40226\"";
    private String validJson_incorrectContent = "{\"aqi\":\"2\",\"om\":\"a\",\"pwr\":\"1\",\"cl\":\"0\",\"aqil\":\"1\",\"fs1\":\"0\",\"fs2\":\"628\",\"fs3\":\"2548\",\"fs4\":\"2548\",\"dtrs\":\"0\",\"aqit\":\"29\",\"clef1\":\"w\",\"repf2\":\"n\",\"repf3\":\"n\",\"repf4\":\"n\",\"fspd\":\"1\",\"tfav\":\"575118\",\"psens\":\"1\"}";
    private String validData = "{  \n" +
            "   \"name\":\"testName\",\n" +
            "   \"type\":\"testType\",\n" +
            "   \"modelid\":\"testModelid\",\n" +
            "   \"swversion\":\"testSwversion\",\n" +
            "   \"serial\":\"testSerial\",\n" +
            "   \"ctn\":\"testCtn\",\n" +
            "   \"allowuploads\":true\n" +
            "}";

    private String minimalValidDataset = "{  \n" +
            "   \"name\":\"testName\",\n" +
            "   \"type\":\"testType\"\n" +
            "}";

    @Before
    public void setUp() {
        initMocks(this);
        DICommLog.disableLogging();

        devicePort = new DevicePort(communicationStrategyMock);
    }

    @Test
    public void test_ShouldReturnFalse_WhenIsResponseForThisPortIsCalledWithIncorrectData() throws Exception {
        assertThat(devicePort.isResponseForThisPort(null)).isFalse();
        assertThat(devicePort.isResponseForThisPort("")).isFalse();
        assertThat(devicePort.isResponseForThisPort("invalid data")).isFalse();
        assertThat(devicePort.isResponseForThisPort(invalidJson)).isFalse();
        assertThat(devicePort.isResponseForThisPort(validJson_incorrectContent)).isFalse();
    }

    @Test
    public void test_ShouldReturnTrue_WhenIsResponseForThisPortIsCalledWithValidData() throws Exception {
        assertThat(devicePort.isResponseForThisPort(validData)).isTrue();
    }

    @Test
    public void test_ShouldReturnNull_WhenProcessResponse_WithInvalidData() throws Exception {
        devicePort.processResponse(invalidJson);
        DevicePortProperties properties = devicePort.getPortProperties();

        assertThat(properties).isNull();
    }

    @Test
    public void test_ShouldReturnProperties_WhenProcessResponse_WithMinimalDataset() throws Exception {
        devicePort.processResponse(minimalValidDataset);
        DevicePortProperties properties = devicePort.getPortProperties();

        assertThat(properties).isNotNull();
    }

    @Test
    public void test_ShouldReturnProperties_WhenProcessResponse_WithInvalidData() throws Exception {
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
    public void test_ShouldReturnTrue_WhenSupportsSubscription() throws Exception {
        assertThat(devicePort.supportsSubscription()).isTrue();
    }
}
