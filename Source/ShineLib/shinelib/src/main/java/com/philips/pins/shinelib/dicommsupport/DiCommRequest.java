package com.philips.pins.shinelib.dicommsupport;

import android.support.annotation.NonNull;

import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

class DiCommRequest {

    CharsetEncoder UTF_8_Encoder = StandardCharsets.UTF_8.newEncoder();

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
        int length = 0;

        for (String string : strings) {
            length += string.length() + 1;
        }

        byte[] byteArray = new byte[length];
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);

        for (String string : strings) {
            UTF_8_Encoder.encode(CharBuffer.wrap(string), byteBuffer, true);
            byteBuffer.put((byte) 0);
        }
        return byteBuffer.array();
    }
}
