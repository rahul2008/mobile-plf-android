package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.philips.platform.appinfra.apisigning.PshmacLib;

/**
 * Created by 310190722 on 11/18/2016.
 */

public class WhiteBoxAPIActivity extends AppCompatActivity {

    private static final byte[] mKey = hexStringToByteArray("e124794bab4949cd4affc267d446ddd95c938a7428d75d7901992e0cb4bc320cd94c28dae1e56d83eaf19010ccc8574d6d83fb687cf5d12ff2afddbaf73801b5");
    private static final byte[] httpMethodType = {'P', 'O', 'S', 'T'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvHmacResult = new TextView(this);


        PshmacLib mGenerateHmacLib = new PshmacLib();
        byte[] resultBytes = mGenerateHmacLib.createHmac(mKey, httpMethodType);
        tvHmacResult.setText(bytesToHex(resultBytes));
        System.out.println("Result : "+bytesToHex(resultBytes));
        setContentView(tvHmacResult);
    }

    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static String bytesToHex(byte[] input) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : input) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
