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

public class LocalMatchActivity extends AppCompatActivity implements LocaleMatchListener {

    private Spinner mLanguage_spinner, mCountry_spinner;
    private String mLanguage[], mCountry[], mlanguageCode[], mcountryCode[];
    PILLocaleManager pilLocaleManager;
    private Button mCountryBased_button, mLangauageBased_button;
    String language, country;
    private static final String TAG = LocalMatchMainActivity.class.getSimpleName();

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
SpinnerclickActions();
        mCountryBased_button = (Button)findViewById(R.id.countrybased_btn);
        mLangauageBased_button = (Button)findViewById(R.id.language_based_btn);

        mCountryBased_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PILLocale pilLocale = null;
                if(country!=null)
                pilLocale = pilLocaleManager.currentLocaleWithCountryFallbackForPlatform(LocalMatchActivity.this, country, Platform.PRX, Sector.B2C, Catalog.CONSUMER);
                if(pilLocale!=null) {
                    Log.d(TAG, "****************country getCountrycodek " + pilLocale.getCountrycode());
                    Log.d(TAG, "****************country getLanguageCode " + pilLocale.getLanguageCode());
                    Log.d(TAG, "****************country getLocaleCode " + pilLocale.getLocaleCode());
                }

            }
        });

        mLangauageBased_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PILLocale pilLocale = null;
                if(language!=null)
                pilLocale = pilLocaleManager.currentLocaleWithLanguageFallbackForPlatform(LocalMatchActivity.this, language, Platform.JANRAIN, Sector.B2C, Catalog.MOBILE);
                if(pilLocale!=null) {
                    Log.d(TAG, "****************lang getCountrycodek " + pilLocale.getCountrycode());
                    Log.d(TAG, "****************lang getLanguageCode " + pilLocale.getLanguageCode());
                    Log.d(TAG, "****************lang getLocaleCode " + pilLocale.getLocaleCode());
                }
            }
        });
    }

   public void SpinnerclickActions(){
       mLanguage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // initializeDigitalCareLibrary();
               pilLocaleManager = new PILLocaleManager(LocalMatchActivity.this);
//                pilLocaleManager.setInputLocale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
               language = parent.getAdapter().getItem(position).toString();


           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       mCountry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               //initializeDigitalCareLibrary();
               country = parent.getAdapter().getItem(position).toString();
           }

           @Override
           public void onNothingSelected(AdapterView<?> parent) {

           }
       });

       if(language!= null && country != null){
           pilLocaleManager.setInputLocale(language, country);
           pilLocaleManager.refresh(LocalMatchActivity.this);
       }
    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onLocaleMatchRefreshed(String s) {

    }

    @Override
    public void onErrorOccurredForLocaleMatch(LocaleMatchError localeMatchError) {

    }
}
