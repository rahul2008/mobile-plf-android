/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.platform.appinfra.AppInfra;

public class LocalMatchActivity extends AppCompatActivity implements LocaleMatchListener {

    private Spinner mLanguage_spinner, mCountry_spinner, mSector_spinner, mPlatform_spinner,mCatalog_spinner;
    private String mLanguage[], mCountry[], mlanguageCode[], mcountryCode[], mSector[], mPlatform[], mCatalog[];
    PILLocaleManager pilLocaleManager;
    private Button mCountryBased_button, mLangauageBased_button;
    String language, country;
    Platform selectedPlatform = Platform.PRX;
    Sector selectedSector = Sector.B2C;
    Catalog selectedCatalog = Catalog.CONSUMER;
    private static final String TAG = LocalMatchActivity.class.getSimpleName();
    private boolean countryReqest =false, langRequest = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_match);

        // setting language spinner
        mLanguage_spinner = (Spinner) findViewById(R.id.spinnerLanguage);
        mLanguage = getResources().getStringArray(R.array.Language);
        mlanguageCode = getResources().getStringArray(R.array.Language_code);
        ArrayAdapter<String> mLanguage_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mLanguage);
        mLanguage_spinner.setAdapter(mLanguage_adapter);

        // setting country spinner
        mCountry_spinner = (Spinner) findViewById(R.id.spinnerCountry);
        mCountry = getResources().getStringArray(R.array.country);
        mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mCountry_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCountry);
        mCountry_spinner.setAdapter(mCountry_adapter);

        // setting platform spinner
        mPlatform_spinner = (Spinner) findViewById(R.id.spinnerPlatform);
        mPlatform = getResources().getStringArray(R.array.platform_list);
       // mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mPlatform_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mPlatform);
        mPlatform_spinner.setAdapter(mPlatform_adapter);

        // setting sector spinner
        mSector_spinner = (Spinner) findViewById(R.id.spinnerSector);
        mSector = getResources().getStringArray(R.array.sector_list);
       // mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mSector_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mSector);
        mSector_spinner.setAdapter(mSector_adapter);

        // setting catalog spinner
        mCatalog_spinner = (Spinner) findViewById(R.id.spinnerCatalog);
        mCatalog = getResources().getStringArray(R.array.catalog_list);
       // mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mCatalogy_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCatalog);
        mCatalog_spinner.setAdapter(mCatalogy_adapter);


        /////////////////////////////////
        SpinnerclickActions();
        mCountryBased_button = (Button)findViewById(R.id.countrybased_btn);
        mLangauageBased_button = (Button)findViewById(R.id.language_based_btn);
        AppInfra   appInfra = new AppInfra.Builder().build(getApplicationContext());
        pilLocaleManager=appInfra.getpILLocaleManager();
        //pilLocaleManager = new PILLocaleManager(LocalMatchActivity.this);



        mCountryBased_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryReqest = true;
                langRequest = false;

                pilLocaleManager.refresh(LocalMatchActivity.this);
//                PILLocale pilLocale = null;
//                if(country!=null)
//                pilLocale = pilLocaleManager.currentLocaleWithCountryFallbackForPlatform(LocalMatchActivity.this, country, selectedPlatform, selectedSector, selectedCatalog);
//                if(pilLocale!=null) {
//                    Log.d(TAG, "****************country getCountrycodek " + pilLocale.getCountrycode());
//                    Log.d(TAG, "****************country getLanguageCode " + pilLocale.getLanguageCode());
//                    Log.d(TAG, "****************country getLocaleCode " + pilLocale.getLocaleCode());
//                }

            }
        });

        mLangauageBased_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countryReqest = false;
                langRequest = true;
                pilLocaleManager.refresh(LocalMatchActivity.this);
//                PILLocale pilLocale = null;
//                if(language!=null)
//                pilLocale = pilLocaleManager.currentLocaleWithLanguageFallbackForPlatform(LocalMatchActivity.this, language, selectedPlatform, selectedSector, selectedCatalog);
//                if(pilLocale!=null) {
//                    Log.d(TAG, "****************lang getCountrycodek " + pilLocale.getCountrycode());
//                    Log.d(TAG, "****************lang getLanguageCode " + pilLocale.getLanguageCode());
//                    Log.d(TAG, "****************lang getLocaleCode " + pilLocale.getLocaleCode());
//                }
            }
        });
    }

   public void SpinnerclickActions(){
       mLanguage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // initializeDigitalCareLibrary();


               String[] langArray = getResources().getStringArray(R.array.Language_code);
               language = langArray[position];
               onSpinnerItemChanged();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       mCountry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //initializeDigitalCareLibrary();
               String[] countryArray = getResources().getStringArray(R.array.country_code);
               country = countryArray[position];
               onSpinnerItemChanged();
           }


           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       mPlatform_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //initializeDigitalCareLibrary();
               selectedPlatform = Platform.valueOf(parent.getAdapter().getItem(position).toString());
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       mCatalog_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //initializeDigitalCareLibrary();
               selectedCatalog = Catalog.valueOf(parent.getAdapter().getItem(position).toString());
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       mSector_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //initializeDigitalCareLibrary();
               selectedSector = Sector.valueOf(parent.getAdapter().getItem(position).toString());
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });



    }

    private void onSpinnerItemChanged(){
        if(language!= null && country != null){
            pilLocaleManager.setInputLocale(language, country);

        }


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onLocaleMatchRefreshed(String s) {

        PILLocale pilLocale = null;
        if(s!=null)
        {
            if(countryReqest){
                pilLocale = pilLocaleManager.currentLocaleWithCountryFallbackForPlatform(LocalMatchActivity.this, s, selectedPlatform, selectedSector, selectedCatalog);
            }
            else{
                pilLocale = pilLocaleManager.currentLocaleWithLanguageFallbackForPlatform(LocalMatchActivity.this, s, selectedPlatform, selectedSector, selectedCatalog);
            }
        }


        if(pilLocale!=null) {
            Log.d(TAG, "****************country getCountrycodek " + pilLocale.getCountrycode());
            Log.d(TAG, "****************country getLanguageCode " + pilLocale.getLanguageCode());
            Log.d(TAG, "****************country getLocaleCode " + pilLocale.getLocaleCode());

        }

    }

    @Override
    public void onErrorOccurredForLocaleMatch(LocaleMatchError localeMatchError) {

        Log.d(TAG, "****************country getLocaleCode " );
    }
}
