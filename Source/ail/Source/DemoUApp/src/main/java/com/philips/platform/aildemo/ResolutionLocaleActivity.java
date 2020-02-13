package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.philips.platform.appinfra.demo.R;

import java.util.Locale;

public class ResolutionLocaleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resolution_locale);
        Log.d(getClass() + "", Locale.getDefault().toString());
    }
}
