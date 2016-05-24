package com.philips.pins.shinelib.dicommsupport;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class DiCommResponse {

    private static final int MIN_PAYLOAD_SIZE = 1;
    private MessageType type = MessageType.GenericResponse;
    private StatusCode status;
    private Map<String, Object> properties;

    public DiCommResponse(DiCommMessage diCommMessage) throws InvalidParameterException {
        if (diCommMessage.getMessageType() != MessageType.GenericResponse) {
            throw new InvalidParameterException("Encountered an unexpected DiComm Response Type: " + diCommMessage.getMessageType());
        }

        byte[] payload = diCommMessage.getPayload();
        if (payload == null || payload.length < MIN_PAYLOAD_SIZE) {
            throw new InvalidParameterException("DiComm message payload is too short!");
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(payload);
        byte diCommStatusCode = byteBuffer.get();
        status = StatusCode.fromDiCommStatusCode(diCommStatusCode);

        if (status == null) {
            throw new InvalidParameterException("DiComm response contains an unknown status code! Code: " + diCommStatusCode);
        }

        if (!byteBuffer.hasRemaining()) {
            throw new InvalidParameterException("Missing JSON values string!");
        }

        byte[] propertiesData = new byte[byteBuffer.remaining() - 1];
        if (propertiesData.length > 0) {
            byteBuffer.get(propertiesData);
            properties = convertStringToMap(propertiesData);
        }

        if (byteBuffer.get() != 0) {
            throw new InvalidParameterException("Invalid message format!");
        }
    }

    private Map<String, Object> convertStringToMap(byte[] propertiesData) {
        String string = new String(propertiesData, StandardCharsets.UTF_8);
        try {
            JSONObject jsonObject = new JSONObject(string);
            Map<String, Object> properties = new HashMap<>();
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                properties.put(key, jsonObject.get(key));
            }

            return properties;
        } catch (JSONException e) {
            throw new InvalidParameterException("JSON string has invalid format " + string);
        }
    }

    public MessageType getType() {
        return type;
    }

    public StatusCode getStatus() {
        return status;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }
}
