package com.philips.pins.shinelib.dicommsupport;

import com.philips.pins.shinelib.RobolectricTest;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class DiCommRequestTest extends RobolectricTest {

    public static final String DEVICE = "device";
    public static final String PORT = "port";
    public static final String DATA = "data";

    public static final String DATA_JSON = "{\"data\":5}";

    public static final int EXPECTED_HEADER_LENGTH = DEVICE.length() + 1 + PORT.length() + 1;
    public static final int EXPECTED_MESSAGE_LENGTH = EXPECTED_HEADER_LENGTH + DATA_JSON.length() + 1;

    private Map<String, Object> properties;

    @Before
    public void setUp() throws Exception {
        properties = new HashMap<>();
        properties.put(DATA, 5);
    }

    @Test
    public void canCreate() throws Exception {
        new DiCommRequest();
    }

    @Test
    public void whenGetPropsRequestDataWithProductIsCalledThenReturnedMessageIsGetPropsRequestType() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.getPropsRequestDataWithProduct(DEVICE, PORT);

        assertEquals(MessageType.GetPropsRequest, message.getType());
    }

    @Test
    public void whenGetPropsRequestDataWithProductIsCalledThenReturnedMessageHasProperLength() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.getPropsRequestDataWithProduct(DEVICE, PORT);

        assertEquals(EXPECTED_HEADER_LENGTH, message.getData().length);
    }

    @Test
    public void whenGetPropsRequestDataWithProductIsCalledThenReturnedMessageIsNullSeparated() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.getPropsRequestDataWithProduct(DEVICE, PORT);

        byte[] data = message.getData();
        assertEquals(0, data[DEVICE.length()]);
        assertEquals(0, data[DEVICE.length() + 1 + PORT.length()]);
    }

    @Test
    public void whenGetPropsRequestDataWithProductIsCalledThenReturnedMessageIsUTF8Encoded() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.getPropsRequestDataWithProduct(DEVICE, PORT);

        verifyDeviceAndPortName(message.getData());
    }

    private void verifyDeviceAndPortName(byte[] message) {
        assertEquals(DEVICE.getBytes(StandardCharsets.UTF_8)[1], message[1]);
        assertEquals(DEVICE.getBytes(StandardCharsets.UTF_8)[2], message[2]);
        assertEquals(DEVICE.getBytes(StandardCharsets.UTF_8)[3], message[3]);
        assertEquals(DEVICE.getBytes(StandardCharsets.UTF_8)[4], message[4]);
        assertEquals(DEVICE.getBytes(StandardCharsets.UTF_8)[5], message[5]);

        assertEquals(PORT.getBytes(StandardCharsets.UTF_8)[0], message[7]);
        assertEquals(PORT.getBytes(StandardCharsets.UTF_8)[1], message[8]);
        assertEquals(PORT.getBytes(StandardCharsets.UTF_8)[2], message[9]);
        assertEquals(PORT.getBytes(StandardCharsets.UTF_8)[3], message[10]);
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageIsPutPropsRequestType() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        assertEquals(MessageType.PutPropsRequest, message.getType());
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageHasProperLength() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        assertEquals(EXPECTED_MESSAGE_LENGTH, message.getData().length);
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageIsNullSeparated() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        byte[] data = message.getData();
        assertEquals(0, data[DEVICE.length()]);
        assertEquals(0, data[EXPECTED_HEADER_LENGTH - 1]);
        assertEquals(0, data[EXPECTED_MESSAGE_LENGTH - 1]);
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageHeaderIsUTF8Encoded() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        verifyDeviceAndPortName(message.getData());
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageIsJSONFormatUTF8Encoded() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        byte[] message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties).getData();

        int startIndex = EXPECTED_HEADER_LENGTH;
        for (int index = 0; index < DATA_JSON.length(); index++) {
            assertEquals(DATA_JSON.getBytes(StandardCharsets.UTF_8)[index], message[startIndex++]);
        }
    }
}