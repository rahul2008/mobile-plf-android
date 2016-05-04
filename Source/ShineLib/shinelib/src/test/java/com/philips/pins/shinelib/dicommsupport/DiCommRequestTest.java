package com.philips.pins.shinelib.dicommsupport;

import android.util.Base64;

import com.philips.pins.shinelib.RobolectricTest;

import org.junit.Before;
import org.junit.Test;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class DiCommRequestTest extends RobolectricTest {

    public static final String DEVICE = "device";
    public static final String SPECIAL_CHARACTER_DEVICE = "device\u00D6";
    public static final String PORT = "port";
    public static final String SPECIAL_CHARACTER_PORT = "port\u00D6";

    public static final int VALUE = 5;
    public static final String DATA = "data";
    public static final byte[] BYTE_VALUE = {(byte) -4, (byte) 85, (byte) 80, (byte) 72, (byte) -1, (byte) -1, (byte) -1, (byte) -1, (byte) 12, (byte) 101, (byte) 2};

    public static final String DATA_JSON = "{\"" + DATA + "\":" + VALUE + "}";

    public static final int EXPECTED_HEADER_LENGTH = DEVICE.length() + 1 + PORT.length() + 1;
    public static final int EXPECTED_MESSAGE_LENGTH = EXPECTED_HEADER_LENGTH + DATA_JSON.length() + 1;

    private Map<String, Object> properties;

    @Before
    public void setUp() throws Exception {
        properties = new HashMap<>();
        properties.put(DATA, VALUE);
    }

    @Test
    public void canCreate() throws Exception {
        new DiCommRequest();
    }

    @Test
    public void whenGetPropsRequestDataWithProductIsCalledThenReturnedMessageIsGetPropsRequestType() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.getPropsRequestDataWithProduct(DEVICE, PORT);

        assertEquals(MessageType.GetPropsRequest, message.getMessageType());
    }

    @Test
    public void whenGetPropsRequestDataWithProductIsCalledThenReturnedMessageHasProperLength() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.getPropsRequestDataWithProduct(DEVICE, PORT);

        assertEquals(EXPECTED_HEADER_LENGTH, message.getPayload().length);
    }

    @Test
    public void whenGetPropsRequestDataWithProductIsCalledThenReturnedMessageIsNullSeparated() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.getPropsRequestDataWithProduct(DEVICE, PORT);

        byte[] data = message.getPayload();
        assertEquals(0, data[DEVICE.length()]);
        assertEquals(0, data[DEVICE.length() + 1 + PORT.length()]);
    }

    @Test
    public void whenGetPropsRequestDataWithProductIsCalledThenReturnedMessageIsUTF8Encoded() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.getPropsRequestDataWithProduct(DEVICE, PORT);

        verifyDeviceAndPortName(message.getPayload());
    }

    private void verifyDeviceAndPortName(byte[] message) {
        byte[] deviceBytes = DEVICE.getBytes(StandardCharsets.UTF_8);
        assertEquals(deviceBytes[1], message[1]);
        assertEquals(deviceBytes[2], message[2]);
        assertEquals(deviceBytes[3], message[3]);
        assertEquals(deviceBytes[4], message[4]);
        assertEquals(deviceBytes[5], message[5]);

        byte[] portBytes = PORT.getBytes(StandardCharsets.UTF_8);
        assertEquals(portBytes[0], message[7]);
        assertEquals(portBytes[1], message[8]);
        assertEquals(portBytes[2], message[9]);
        assertEquals(portBytes[3], message[10]);
    }

    @Test
    public void whenGetPropsRequestDataHasSpecialCharacterThenReturnedMessageIsNullSeparated() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.getPropsRequestDataWithProduct(SPECIAL_CHARACTER_DEVICE, SPECIAL_CHARACTER_PORT);
        byte[] data = message.getPayload();

        int zeroCount = getNullSeparatorCount(data);

        assertEquals(2, zeroCount);
    }

    private int getNullSeparatorCount(byte[] data) {
        int nullCount = 0;
        for (byte element : data) {
            if (element == (byte) 0) {
                nullCount++;
            }
        }
        return nullCount;
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageIsPutPropsRequestType() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        assertEquals(MessageType.PutPropsRequest, message.getMessageType());
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageHasProperLength() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        assertEquals(EXPECTED_MESSAGE_LENGTH, message.getPayload().length);
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageIsNullSeparated() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        byte[] data = message.getPayload();
        assertEquals((byte) 0, data[DEVICE.length()]);
        assertEquals((byte) 0, data[EXPECTED_HEADER_LENGTH - 1]);
        assertEquals((byte) 0, data[EXPECTED_MESSAGE_LENGTH - 1]);
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageHeaderIsUTF8Encoded() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        verifyDeviceAndPortName(message.getPayload());
    }

    @Test
    public void whenPutPropsRequestDataWithProductIsCalledThenReturnedMessageIsJSONFormatUTF8Encoded() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();

        byte[] message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties).getPayload();

        int startIndex = EXPECTED_HEADER_LENGTH;
        byte[] JSONDataBytes = DATA_JSON.getBytes(StandardCharsets.UTF_8);
        for (int index = 0; index < DATA_JSON.length(); index++) {
            assertEquals(JSONDataBytes[index], message[startIndex++]);
        }
    }

    @Test
    public void whenPetPropsRequestDataHasSpecialCharacterThenReturnedMessageIsNullSeparated() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();
        properties.put("client\u00D1", "2");

        byte[] data = diCommRequest.putPropsRequestDataWithProduct(SPECIAL_CHARACTER_DEVICE, SPECIAL_CHARACTER_PORT, properties).getPayload();

        int zeroCount = getNullSeparatorCount(data);

        assertEquals(3, zeroCount);
    }

    @Test
    public void whenPetPropsRequestDataHasNullKeyThenReturnedMessageIsNull() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();
        properties.put(null, "2");

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        assertNull(message);
    }

    @Test
    public void whenPutPropsIsCalledWithBase64DataThenBackSlashIsNotEscaped() throws Exception {
        DiCommRequest diCommRequest = new DiCommRequest();
        properties.put(DATA, BYTE_VALUE);

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        assertNotNull(message);
        String resultString = new String(message.getPayload());

        assertFalse(resultString.contains("\\"));
    }

    @Test
    public void whenDataContainsByteDataThenItIsBase64Encoded() throws Exception {
        String expectedBase64Data = new String(Base64.encode(BYTE_VALUE, Base64.NO_WRAP), StandardCharsets.UTF_8);

        DiCommRequest diCommRequest = new DiCommRequest();
        properties.put(DATA, BYTE_VALUE);

        DiCommMessage message = diCommRequest.putPropsRequestDataWithProduct(DEVICE, PORT, properties);

        assertNotNull(message);
        String resultString = new String(message.getPayload());

        assertTrue(resultString.contains(expectedBase64Data));
    }
}