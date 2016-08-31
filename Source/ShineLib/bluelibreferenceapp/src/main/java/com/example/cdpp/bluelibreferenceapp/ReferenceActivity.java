package com.example.cdpp.bluelibreferenceapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.philips.pins.shinelib.SHNCentral;

public class ReferenceActivity extends AppCompatActivity {

    private SHNCentral mShnCentral;

    private static final String TAG = "ReferenceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain SHNCentral instance
        mShnCentral = ReferenceApplication.get().getShnCentral();

        // Set title
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        // Configure action for the Floating Action Button
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ReferenceActivity.this, PeripheralListActivity.class));
            }
        });

        // Display version string
        TextView versionNameTxt = (TextView) findViewById(R.id.versionNameTxt);
        versionNameTxt.setText(getVersionsString());
    }

    private final String getVersionsString() {
        final String blueLibVersion = mShnCentral.getVersion();

        return new StringBuilder(getString(R.string.bluelib_version))
                .append(blueLibVersion)
                .append("\n")
                .append(getString(R.string.app_version))
                .append(BuildConfig.VERSION_NAME)
                .toString();

    }
}

