package com.philips.platform.appinfra;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.philips.platform.appinfra.keybag.KeyBagLib;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private KeyBagLib keyBagLib;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        keyBagLib = new KeyBagLib();
    }

    public void onClick(View view) {

        SendBuffer();
    }

    public void SendBuffer() {

        try {
            // Convert char to string to byte


            char[] array = hexStringToByteArray("b3a5085a2de916729f8e55955ba482656cfceb10fe79");


            String testString = "Raja Ram Mohan Roy";
            String hex = convertToHexDecimal(testString,"UTF-8");
            char[] temp = keyBagLib.obfuscateDeObfuscate(testString.toCharArray(), 0xACE1);
            String s = new String(temp);
            String obfuscatedHex = convertToHexDecimal(s,"UTF-8");
            Log.d(getClass() + " converted string ", obfuscatedHex);


            String raviHexValue = "b3a5085a2de916729f8e55955ba482656cfc";

            temp = keyBagLib.obfuscateDeObfuscate(convertHexDataToString(raviHexValue).toCharArray(), 0xACE1);
            String s2 = new String(temp);
            Log.d(getClass() +"", " again converted string "+ s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String convertHexDataToString(String hex) {
        int l = hex.length();
        char[] data = new char[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (char) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return new String(data);
    }

    private static char[] hexStringToByteArray(String hex) {
        int l = hex.length();
        char[] data = new char[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (char) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    private static String convertToHexDecimal(String input, String charsetName) throws UnsupportedEncodingException {
        if (input == null) throw new NullPointerException();
        return asHex(input.getBytes());
    }

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    private static String asHex(byte[] buf)
    {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }
}