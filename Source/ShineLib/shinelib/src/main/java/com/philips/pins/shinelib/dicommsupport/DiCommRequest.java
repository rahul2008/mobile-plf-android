package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class DiCommRequest {

    public DiCommMessage getPropsRequestDataWithProduct(@NonNull String product, @NonNull String port) {
        byte[] byteArray = encodeHeader(product, port);

        return new DiCommMessage(MessageType.GetPropsRequest, byteArray);
    }

    public DiCommMessage putPropsRequestDataWithProduct(@NonNull String product, @NonNull String port, @NonNull Map<String, Object> properties) {
        String propertiesString = new JSONObject(properties).toString();

        byte[] byteArray = encodeHeader(product, port, propertiesString);

        return new DiCommMessage(MessageType.PutPropsRequest, byteArray);
    }

    @NonNull
    private byte[] encodeHeader(@NonNull String... strings) {
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
