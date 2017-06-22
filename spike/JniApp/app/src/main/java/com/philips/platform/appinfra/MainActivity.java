package com.philips.platform.appinfra;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.philips.platform.appinfra.keybag.KeyBagLib;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        KeyBagLib keyBagLib = new KeyBagLib();
//        keyBagLib.lfsrMain();
        // Example of a call to a native method
        TextView tv = (TextView) findViewById(R.id.sample_text);
        tv.setText(keyBagLib.getMsgFromJni());

        char[] testings = keyBagLib.passingDataToJni("Ravi kiran".toCharArray(), 12, "testing", "testing".toCharArray(), "testing".length());

        Log.d(getClass()+"",String.valueOf(testings[0]));

       /* String test = "Ravi kiran";
        byte[] bytes = null;
        try {
           bytes = test.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        char[] chars = test.toCharArray();
//        keyBagLib.lfsrMain(bytes);
        keyBagLib.lfsrObfuscate(chars, chars.length, 0xACE1);


        for (int i = 0;i<chars.length;i++){
            Log.d(getClass()+"", "After obfuscation----"+String.valueOf(chars[i]));
        }


//        keyBagLib.lfsrObfuscate(chars,test.length(), 0xACE1);

        for (int i = 0;i<chars.length;i++){
            Log.d(getClass()+"", "After de-obfuscation----"+String.valueOf(chars[i]));
        }*/
    }


}
