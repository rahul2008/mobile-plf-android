/*
 * © Koninklijke Philips N.V., 2015.
 *   All rights reserved.
 */

package com.philips.cdp.dicommclient.port.common;

import com.philips.cdp.dicommclient.communication.CommunicationStrategy;
import com.philips.cdp.dicommclient.testutil.RobolectricTest;

import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;

public class DevicePortTest extends RobolectricTest {

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

    @Override
    public void setUp() throws Exception {
        super.setUp();

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
}
