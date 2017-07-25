package com.philips.platform.appinfra;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.philips.platform.appinfra.keybag.KeyBagLib;

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


            String testString = " Testing bulk data ";
            char[] temp = keyBagLib.obfuscateDeObfuscate(testString.toCharArray(), 0xACE1);
            String s = new String(temp);
            Log.d(getClass() + " converted string ", s);

            temp = keyBagLib.obfuscateDeObfuscate(s.toCharArray(), 0xACE1);
            String s2 = new String(temp);
            Log.d(getClass() +"", " again converted string "+ s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}