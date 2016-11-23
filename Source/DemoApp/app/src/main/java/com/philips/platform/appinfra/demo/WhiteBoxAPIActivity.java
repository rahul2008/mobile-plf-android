package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.philips.platform.appinfra.whiteboxapi.GenerateHmacLib;


/**
 * Created by 310190722 on 11/18/2016.
 */

public class WhiteBoxAPIActivity extends AppCompatActivity {


    private static final byte[] k = hexStringToByteArray("d9efcaeb7077f16729c1568bde56eed25635030f688990d3fc9281cb809d4666db0057e8b902382f9de16fed325889a46e7c22e31a143ee60b33c1ac22bc8b28");
    private static final byte[] d = {'P', 'O', 'S', 'T'};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView tvHmacResult = new TextView(this);

        GenerateHmacLib mGenerateHmacLib = new GenerateHmacLib();
        byte[] resultBytes = mGenerateHmacLib.createHmac(k, d);
        tvHmacResult.setText(bytesToHex(resultBytes));
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

    public static String bytesToHex(byte[] input) {
        final StringBuilder builder = new StringBuilder();
        for (byte b : input) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
