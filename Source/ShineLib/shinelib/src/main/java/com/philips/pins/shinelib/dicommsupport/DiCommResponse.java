package com.philips.pins.shinelib.dicommsupport;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

class DiCommResponse {

    private MessageType type = MessageType.GenericResponse;
    private StatusCode status;
    private Map<String, Object> properties;

    public DiCommResponse(DiCommMessage diCommMessage) throws InvalidParameterException {
        if (diCommMessage.getMessageTypeIdentifier() != MessageType.GenericResponse) {
            throw new InvalidParameterException("Encountered an unexpected DiComm Response Type");
        }

        byte[] payload = diCommMessage.getPayload();
        if (payload == null || payload.length < 1) {
            throw new InvalidParameterException("DiComm message payload is too short!");
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(payload);
        status = StatusCode.fromByte(byteBuffer.get());

        if (status == null) {
            throw new InvalidParameterException("DiComm response contains an invalid status code!");
        }

        if (!byteBuffer.hasRemaining()) {
            throw new InvalidParameterException("Missing JSON values string!");
        }

        byte[] propertiesData = new byte[byteBuffer.remaining() - 1];
        byteBuffer.get(propertiesData);

        assert (byteBuffer.remaining() == 1);
        String string = new String(propertiesData);
        try {
            JSONObject jsonObject = new JSONObject(string);
            properties = new HashMap<>();
            Iterator<String> keys = jsonObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                properties.put(key, jsonObject.get(key));
            }
        } catch (JSONException e) {
            throw new InvalidParameterException("JSON string has invalid format");
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
