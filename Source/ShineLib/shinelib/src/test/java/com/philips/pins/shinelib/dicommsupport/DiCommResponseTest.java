package com.philips.pins.shinelib.dicommsupport;

import com.philips.pins.shinelib.RobolectricTest;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class DiCommResponseTest extends RobolectricTest {

    public static final int VALUE = 5;
    public static final String KEY = "data";
    public static final String DATA_JSON = "{\"" + KEY + "\":" + VALUE + "}";

    @Mock
    DiCommMessage diCommMessageMock;
    private byte[] data;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        data = convertStringToBytes(DATA_JSON);
        when(diCommMessageMock.getPayload()).thenReturn(data);

        when(diCommMessageMock.getMessageType()).thenReturn(MessageType.GenericResponse);
    }

    private byte[] convertStringToBytes(String string) {
        byte[] jsonDataBytes = string.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[jsonDataBytes.length + 2];
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.put(StatusCode.NoError.getDiCommStatusCode());
        byteBuffer.put(jsonDataBytes);
        byteBuffer.put((byte) 0);

        return data;
    }

    @Test
    public void canCreate() throws Exception {
        new DiCommResponse(diCommMessageMock);
    }

    @Test(expected = InvalidParameterException.class)
    public void whenPayloadIsNullThenExceptionIsGenerated() throws Exception {
        when(diCommMessageMock.getPayload()).thenReturn(null);

        new DiCommResponse(diCommMessageMock);
    }

    @Test(expected = InvalidParameterException.class)
    public void whenPayloadIsEmptyThenExceptionIsGenerated() throws Exception {
        byte[] data = new byte[0];
        when(diCommMessageMock.getPayload()).thenReturn(data);

        new DiCommResponse(diCommMessageMock);
    }

    @Test(expected = InvalidParameterException.class)
    public void whenMessageTypeIsNotResponseThenExceptionIsGenerated() throws Exception {
        when(diCommMessageMock.getMessageType()).thenReturn(MessageType.GetPropsRequest);

        new DiCommResponse(diCommMessageMock);
    }

    @Test
    public void whenResponseIsCreatedThenTypeIsGenericResponse() throws Exception {
        DiCommResponse response = new DiCommResponse(diCommMessageMock);

        assertEquals(MessageType.GenericResponse, response.getType());
    }

    @Test
    public void whenResponseIsCreatedThenStatusCodeIsRead() throws Exception {
        DiCommResponse response = new DiCommResponse(diCommMessageMock);

        assertEquals(StatusCode.NoError, response.getStatus());
    }

    @Test
    public void whenResponseHasOutOfMemoryStatusThenStatusIsOutOfMemory() throws Exception {
        data[0] = StatusCode.OutOfMemory.getDiCommStatusCode();
        when(diCommMessageMock.getPayload()).thenReturn(data);

        DiCommResponse response = new DiCommResponse(diCommMessageMock);

        assertEquals(StatusCode.OutOfMemory, response.getStatus());
    }

    @Test(expected = InvalidParameterException.class)
    public void whenResponseInvalidStatusThenExceptionIsGenerated() throws Exception {
        byte[] data = {(byte) 15};
        when(diCommMessageMock.getPayload()).thenReturn(data);

        new DiCommResponse(diCommMessageMock);
    }

    @Test(expected = InvalidParameterException.class)
    public void whenResponseHasNoPropertiesThenExceptionIsGenerated() throws Exception {
        byte[] data = {StatusCode.NoError.getDiCommStatusCode()};
        when(diCommMessageMock.getPayload()).thenReturn(data);

        new DiCommResponse(diCommMessageMock);
    }

    @Test
    public void whenResponseIsCreatedThenPayloadIsRead() throws Exception {
        DiCommResponse diCommResponse = new DiCommResponse(diCommMessageMock);

        Map<String, Object> properties = diCommResponse.getProperties();

        assertEquals(VALUE, properties.get(KEY));
    }

    @Test(expected = InvalidParameterException.class)
    public void whenResponseDataHasWrongFormatThenExceptionIsGenerated() throws Exception {
        String notJSONData = "{\"" + KEY + "\":" + VALUE;

        byte[] data = convertStringToBytes(notJSONData);
        when(diCommMessageMock.getPayload()).thenReturn(data);

        new DiCommResponse(diCommMessageMock);
    }

    @Test
    public void whenResponseDataHasSpecialCharactersThenResponseIsParsedCorrectly() throws Exception {
        String key = "data\u00D6";
        String JSONDataWithSpecialCharacter = "{\"" + key + "\":" + VALUE + "}";
        byte[] data = convertStringToBytes(JSONDataWithSpecialCharacter);
        when(diCommMessageMock.getPayload()).thenReturn(data);

        DiCommResponse diCommResponse = new DiCommResponse(diCommMessageMock);
        Map<String, Object> properties = diCommResponse.getProperties();

        assertEquals(VALUE, properties.get(key));
    }

    @Test(expected = InvalidParameterException.class)
    public void whenResponseDataHasNoTrailingZeroThenExceptionIsGenerated() throws Exception {
        byte[] jsonDataBytes = DATA_JSON.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[jsonDataBytes.length + 2];
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.put(StatusCode.NoError.getDiCommStatusCode());
        byteBuffer.put(jsonDataBytes);
        byteBuffer.put((byte) 1);

        when(diCommMessageMock.getPayload()).thenReturn(data);

        new DiCommResponse(diCommMessageMock);
    }

    @Test(expected = InvalidParameterException.class)
    public void whenResponseDataHasNoTrailingZeroThenExceptionIsGenerated2() throws Exception {
        byte[] jsonDataBytes = DATA_JSON.getBytes(StandardCharsets.UTF_8);
        byte[] data = new byte[jsonDataBytes.length + 1];
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.put(StatusCode.NoError.getDiCommStatusCode());
        byteBuffer.put(jsonDataBytes);

        when(diCommMessageMock.getPayload()).thenReturn(data);

        new DiCommResponse(diCommMessageMock);
    }

    @Test
    public void whenResponseHasNoJSONThenStatusCodeIsRead() throws Exception {
        byte[] data = new byte[2];
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        byteBuffer.put(StatusCode.OutOfMemory.getDiCommStatusCode());
        byteBuffer.put((byte) 0);

        when(diCommMessageMock.getPayload()).thenReturn(data);

        DiCommResponse diCommResponse = new DiCommResponse(diCommMessageMock);

        assertEquals(StatusCode.OutOfMemory, diCommResponse.getStatus());
        assertNull(diCommResponse.getProperties());
    }

    @Test
    public void whenResponseHasEmptyJSONThenResponseIsParsedProperly() throws Exception {
        String notJSONData = "{}";
        byte[] data = convertStringToBytes(notJSONData);

        when(diCommMessageMock.getPayload()).thenReturn(data);
        DiCommResponse diCommResponse = new DiCommResponse(diCommMessageMock);

        assertEquals(StatusCode.NoError, diCommResponse.getStatus());
        assertNotNull(diCommResponse.getProperties());
        assertTrue(diCommResponse.getProperties().isEmpty());
    }
}