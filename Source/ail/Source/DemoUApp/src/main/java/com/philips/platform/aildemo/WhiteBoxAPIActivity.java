package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.TextView;

import com.philips.platform.appinfra.apisigning.HSDPPHSApiSigning;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by 310190722 on 11/18/2016.
 */

public class WhiteBoxAPIActivity extends AppCompatActivity {

    private static final byte[] mKey = hexStringToByteArray("e124794bab4949cd4affc267d446ddd95c938a7428d75d7901992e0cb4bc320cd94c" +
            "28dae1e56d83eaf19010ccc8574d6d83fb687cf5d12ff2afddbaf73801b5"); //for testing purpose, please don't remove
    private static final byte[] httpMethodType = {'P', 'O', 'S', 'T'}; //for testing purpose, please don't remove
    private HSDPPHSApiSigning hsdpphsApiSigning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvPsHmacResult = new TextView(this);

        //for testing purpose, please don't remove
        /*PsLib mGenerateHmacLib = new PsLib();
        byte[] resultBytes = mGenerateHmacLib.createHmac(mKey, httpMethodType);
        tvHmacResult.setText(bytesToHex(resultBytes));
        System.out.println("Result : "+bytesToHex(resultBytes));*/

        hsdpphsApiSigning = new HSDPPHSApiSigning("cafebabe-1234-dead-dead-1234567890ab",
                "e124794bab4949cd4affc267d446ddd95c938a7428d75d7901992e0cb4bc320cd94c28dae1e56d83eaf19010ccc8574d6d83fb687cf5d12ff2afddbaf73801b5");
        Map<String, String> headers = new LinkedHashMap<String, String>();
        headers.put("SignedDate","2016-11-09T13:31:13.492+0000");
        String result = hsdpphsApiSigning.createSignature("POST","applicationName=uGrow",headers,"/authentication/login/social",null).trim();
        tvPsHmacResult.setText(result);
        setContentView(tvPsHmacResult);
    }

    //for testing purpose, please don't remove this method
    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    //for testing purpose, please don't remove this method
    private static String bytesToHex(byte[] input) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : input) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
