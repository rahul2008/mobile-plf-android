package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class DiCommRequest {

    @NonNull
    public DiCommMessage getPropsRequestDataWithProduct(@NonNull String product, @NonNull String port) {
        byte[] byteArray = encodeDiCommPayload(product, port);

        return new DiCommMessage(MessageType.GetPropsRequest, byteArray);
    }

    @Nullable
    public DiCommMessage putPropsRequestDataWithProduct(@NonNull String product, @NonNull String port, @NonNull Map<String, Object> properties){
        if (properties.containsKey(null)) {
            return null;
        }
        String propertiesString = new JSONObject(properties).toString();
        byte[] byteArray = encodeDiCommPayload(product, port, propertiesString);

        return new DiCommMessage(MessageType.PutPropsRequest, byteArray);
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
