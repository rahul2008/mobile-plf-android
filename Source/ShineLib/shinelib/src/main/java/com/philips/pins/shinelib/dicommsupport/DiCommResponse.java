/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidMessageTerminationException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidPayloadFormatException;
import com.philips.pins.shinelib.dicommsupport.exceptions.InvalidStatusCodeException;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class DiCommResponse {

    private static final int MIN_PAYLOAD_SIZE = 3; // status byte + JSON payload + terminator byte
    private static final byte TERMINATOR_BYTE = 0;

    private MessageType mType = MessageType.GenericResponse;
    private StatusCode mStatus;
    private Map<String, Object> mProperties;
    private String mJsonString;

    public DiCommResponse(DiCommMessage diCommMessage) throws IllegalArgumentException, InvalidPayloadFormatException, InvalidStatusCodeException, InvalidMessageTerminationException {
        if (MessageType.GenericResponse != diCommMessage.getMessageType()) {
            throw new IllegalArgumentException("Encountered an unexpected DiComm Response Type: " + diCommMessage.getMessageType());
        }

        byte[] payload = diCommMessage.getPayload();
        if (payload == null || payload.length < MIN_PAYLOAD_SIZE) {
            throw new InvalidPayloadFormatException("DiComm message payload is too short!");
        }

        ByteBuffer byteBuffer = ByteBuffer.wrap(payload);
        byte diCommStatusCode = byteBuffer.get();
        mStatus = StatusCode.fromDiCommStatusCode(diCommStatusCode);

        if (mStatus == null) {
            throw new InvalidStatusCodeException("DiComm response contains an unknown status code: " + diCommStatusCode);
        }

        byte[] propertiesData = new byte[byteBuffer.remaining() - 1];
        if (propertiesData.length > 0) {
            byteBuffer.get(propertiesData);
            mJsonString = new String(propertiesData, StandardCharsets.UTF_8);
            try {
                mProperties = convertStringToMap(mJsonString);
            } catch (JSONException e) {
                throw new InvalidPayloadFormatException("Error evaluating JSON from payload string: " + mJsonString);
            }
        }

        if (byteBuffer.get() != TERMINATOR_BYTE) {
            throw new InvalidMessageTerminationException("Message not terminated correctly.");
        }
    }

    private Map<String, Object> convertStringToMap(@NonNull String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        Map<String, Object> properties = new HashMap<>();
        Iterator<String> keys = jsonObject.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            properties.put(key, jsonObject.get(key));
        }
        return properties;
    }

    public MessageType getType() {
        return mType;
    }

    public StatusCode getStatus() {
        return mStatus;
    }

    public Map<String, Object> getProperties() {
        return mProperties;
    }

    public String getPropertiesAsString() {
        return mJsonString;
    }
}
