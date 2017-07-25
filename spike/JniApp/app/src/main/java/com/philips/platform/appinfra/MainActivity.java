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

        /*String testing = "testing";
        testing = keyBagLib.passingDataToJni(testing, testing.length(), 0xACE1);

        Log.d(getClass() + "", String.valueOf(testing));*/

        SendBuffer();
//        testGettingMessage();

//        char[] temp = keyBagLib.passingCharDataToJni();
//        for (char c : temp){
//            Log.d(getClass() +" "," converted string in char "+ String.valueOf(c));
//        }
    }

   /* public void testGettingMessage() {
        String testString = "Raja Ram Mohan Roy";
        byte[] msgFromJni = keyBagLib.ConvertString(testString, testString.length(), 0xACE1);
        String s = new String(msgFromJni);
        Log.d(getClass() + "", " message from JNI " + s);

        byte[] newByte = keyBagLib.ConvertString(s, s.length(), 0xACE1);
        Log.d(getClass() + "", " message back from JNI " + new String(newByte));
    }*/

    public void SendBuffer() {

        try {
            // Convert char to string to byte
            String testString = " Testing bulk data ";
            char[] temp = keyBagLib.ConvertString(testString, testString.length(), 0xACE1);
            String s = new String(temp);
            Log.d(getClass() + " converted string ", s);

            temp = keyBagLib.ConvertString(s, s.length(), 0xACE1);
            String s2 = new String(temp);
            Log.d(getClass() +"", " again converted string "+ s2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    byte[] getJniBytes(final String source) {
        try {
            return source.getBytes("UTF-16LE");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}