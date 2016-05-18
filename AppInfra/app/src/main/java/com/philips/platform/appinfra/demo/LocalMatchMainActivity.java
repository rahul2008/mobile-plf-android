package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.localematch.LocaleMatchListener;
import com.philips.cdp.localematch.PILLocale;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.LocaleMatchError;
import com.philips.cdp.localematch.enums.Platform;
import com.philips.cdp.localematch.enums.Sector;

public class LocalMatchMainActivity extends AppCompatActivity implements LocaleMatchListener {

    private static final String TAG = LocalMatchMainActivity.class.getSimpleName();
    Button buttonRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_local_match);
         buttonRefresh = (Button) findViewById(R.id.button1);
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLocalematchRequest();

                  /*Testing purpose*/
//                pilLocaleManager.setInputLocale("", "");
//                pilLocaleManager.setInputLocale("e", "u");
//                pilLocaleManager.setInputLocale("enr", "usr");
//                pilLocaleManager.setInputLocale(null, null);

            }

            private void initLocalematchRequest() {
                pilLocaleManager = new PILLocaleManager(LocalMatchMainActivity.this);
//                pilLocaleManager.setInputLocale(Locale.getDefault().getLanguage(), Locale.getDefault().getCountry());
                pilLocaleManager.setInputLocale("hi", "IN");
                pilLocaleManager.refresh(LocalMatchMainActivity.this);
                /*for(int i=0;i<1000;i++) {
                    pilLocaleManager.refresh(MainActivity.this);
                    Log.d(TAG,"i is "+i);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<1000;i++) {
                            pilLocaleManager.refresh(MainActivity.this);
                            Log.d(TAG,"i is "+i);
                        }
                    }
                },1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<1000;i++) {
                            pilLocaleManager.refresh(MainActivity.this);
                            Log.d(TAG,"i is "+i);
                        }
                    }
                },1000);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0;i<1000;i++) {
                            pilLocaleManager.refresh(MainActivity.this);
                            Log.d(TAG,"i is "+i);
                        }
                    }
                },1000);*/
//                Toast.makeText(MainActivity.this, pilLocaleManager.getLanguageCode(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    PILLocaleManager pilLocaleManager;

    @Override
    public void onLocaleMatchRefreshed(String s) {
        Log.d(TAG, "Response Received SuccessFully " + s);
//        pilLocaleManager.currentLocaleWithCountryFallbackForPlatform(MainActivity.this,)
        PILLocale pilLocale = null;
        pilLocale = pilLocaleManager.currentLocaleWithCountryFallbackForPlatform(this, s, Platform.PRX, Sector.B2C, Catalog.CONSUMER);
        if(pilLocale!=null) {
            Log.d(TAG, "****************country getCountrycodek " + pilLocale.getCountrycode());
            Log.d(TAG, "****************country getLanguageCode " + pilLocale.getLanguageCode());
            Log.d(TAG, "****************country getLocaleCode " + pilLocale.getLocaleCode());
        }


        pilLocale = pilLocaleManager.currentLocaleWithLanguageFallbackForPlatform(this, s, Platform.JANRAIN, Sector.B2C, Catalog.MOBILE);
        if(pilLocale!=null) {
            Log.d(TAG, "****************lang getCountrycodek " + pilLocale.getCountrycode());
            Log.d(TAG, "****************lang getLanguageCode " + pilLocale.getLanguageCode());
            Log.d(TAG, "****************lang getLocaleCode " + pilLocale.getLocaleCode());
        }
    }

    @Override
    public void onErrorOccurredForLocaleMatch(LocaleMatchError localeMatchError) {
        Log.d(TAG, "Response Received on Failed Scenario : " + localeMatchError.toString());
    }
}
