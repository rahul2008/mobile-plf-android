package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class LocalMatchActivity extends AppCompatActivity {

    private Spinner mLanguage_spinner, mCountry_spinner;
    private String mLanguage[], mCountry[], mlanguageCode[], mcountryCode[];


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_match);

        // setting language spinner
        mLanguage_spinner = (Spinner) findViewById(R.id.spinner1);
        mLanguage = getResources().getStringArray(R.array.Language);
        mlanguageCode = getResources().getStringArray(R.array.Language_code);
        ArrayAdapter<String> mLanguage_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mLanguage);
        mLanguage_spinner.setAdapter(mLanguage_adapter);

        // setting country spinner
        mCountry_spinner = (Spinner) findViewById(R.id.spinner2);
        mCountry = getResources().getStringArray(R.array.country);
        mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mCountry_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCountry);
        mCountry_spinner.setAdapter(mCountry_adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLanguage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // initializeDigitalCareLibrary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCountry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //initializeDigitalCareLibrary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
}
