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

        String testing = "testing";
        testing = keyBagLib.passingDataToJni(testing, testing.length(),0xACE1);

        Log.d(getClass()+"",String.valueOf(testing));
    }


}
