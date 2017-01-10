/*
 * Copyright (c) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 */

package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Base64;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DiCommRequest {

    @NonNull
    public DiCommMessage getPropsRequestDataWithProduct(@NonNull String product, @NonNull String port) {
        byte[] byteArray = encodeDiCommPayload(product, port);

        return new DiCommMessage(MessageType.GetPropsRequest, byteArray);
    }

    @Nullable
    public DiCommMessage putPropsRequestDataWithProduct(@NonNull String product, @NonNull String port, @NonNull Map<String, Object> properties) {
        if (properties.containsKey(null)) {
            return null;
        }
        Map<String, Object> encodedProperties = encodeByteArraysToBase64(properties);

        String propertiesString = GsonProvider.getGson().toJson(encodedProperties);
        byte[] byteArray = encodeDiCommPayload(product, port, propertiesString);

        return new DiCommMessage(MessageType.PutPropsRequest, byteArray);
    }

    private Map<String, Object> encodeByteArraysToBase64(Map<String, Object> properties) {
        Map<String, Object> result = new HashMap<>();

        for (String key : properties.keySet()) {
            Object value = properties.get(key);

            if (value instanceof byte[]) {
                byte[] byteData = (byte[]) value;
                String encodedString = new String(Base64.encode(byteData, Base64.NO_WRAP), StandardCharsets.UTF_8);

                result.put(key, encodedString);
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    @NonNull
    private byte[] encodeDiCommPayload(@NonNull String... strings) {
        byte[][] byteArray = new byte[strings.length][];
        int totalDataLength = 0;

        for (int index = 0; index < strings.length; index++) {
            byteArray[index] = strings[index].getBytes(StandardCharsets.UTF_8);
            totalDataLength += byteArray[index].length;
        }

        byte[] resultArray = new byte[totalDataLength + strings.length];

        ByteBuffer byteBuffer = ByteBuffer.wrap(resultArray);
        for (int index = 0; index < strings.length; index++) {
            byteBuffer.put(byteArray[index]);
            byteBuffer.put((byte) 0);
        }

        return resultArray;
    }
}
