package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveyService;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by 310238655 on 6/7/2016.
 */
public class ServiceDiscoveryDemo extends AppCompatActivity implements ServiceDiscoveryInterface.OnGetServiceLocaleListener, ServiceDiscoveryInterface.OnGetServiceUrlListener, ServiceDiscoveryInterface.OnGetHomeCountryListener, ServiceDiscoveryInterface.OnGetServiceUrlMapListener {

    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryInterface.OnGetServiceLocaleListener mOnGetServiceLocaleListener = null;
    ServiceDiscoveryInterface.OnGetServiceUrlListener mOnGetServiceUrlListener = null;
    ServiceDiscoveryInterface.OnGetHomeCountryListener mOnGetHomeCountryListener = null;
    ServiceDiscoveryInterface.OnGetServiceUrlMapListener mOnGetServiceUrlMapListener = null;
    AppInfraInterface appInfra;

    TextView resultView;
    EditText idEditText;
    EditText idEditTextCountry;
    String editTextData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appInfra = AppInfraApplication.gAppInfra;
        mServiceDiscoveryInterface = appInfra.getServiceDiscovery();
        mOnGetServiceLocaleListener = this;
        mOnGetServiceUrlListener = this;
        mOnGetHomeCountryListener = this;
        mOnGetServiceUrlMapListener = this;

        setContentView(R.layout.service_discovery_demopage);

        idEditText = (EditText) findViewById(R.id.serviceid_editText);
        Button localeByLang = (Button) findViewById(R.id.getlocal_by_lang_button);
        Button localeByCountry = (Button) findViewById(R.id.getlocal_by_country_butn);
        Button urlbyLang = (Button) findViewById(R.id.geturl_by_country_btn);
        Button urlbyCountry = (Button) findViewById(R.id.geturl_by_lang_buttn);
        Button getHomecountryBtn = (Button) findViewById(R.id.gethome_country_btn);
        Button geturlbyCountry_ServiceIDs = (Button) findViewById(R.id.getul_country_btn);
        Button geturlbyLanguage_ServiceIds = (Button) findViewById(R.id.getul_language_btn);
        Button setHomeCountry = (Button) findViewById(R.id.button2);
        idEditTextCountry = (EditText) findViewById(R.id.contry_edittext);

        editTextData = idEditText.getText().toString();

        resultView = (TextView) findViewById(R.id.textView2);

        setHomeCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String country = idEditTextCountry.getText().toString();
                if(country.length() == 2){
                    mServiceDiscoveryInterface.setHomeCountry(country.toUpperCase());
                }

            }
        });

        localeByLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextData = idEditText.getText().toString();
                mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(editTextData, mOnGetServiceLocaleListener);


            }
        });
        localeByCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextData = idEditText.getText().toString();
                mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(editTextData, mOnGetServiceLocaleListener);
            }
        });
        urlbyLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextData = idEditText.getText().toString();
                mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(editTextData, mOnGetServiceUrlListener);
            }
        });
        urlbyCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextData = idEditText.getText().toString();
                mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(editTextData, mOnGetServiceUrlListener);
            }
        });
        getHomecountryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextData = idEditText.getText().toString();
                mServiceDiscoveryInterface.getHomeCountry(mOnGetHomeCountryListener);
            }
        });


        geturlbyCountry_ServiceIDs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] serviceIds = idEditText.getText().toString().split(",");
                ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
                mServiceDiscoveryInterface.getServicesWithCountryPreference(serviceId, mOnGetServiceUrlMapListener);

            }
        });

        geturlbyLanguage_ServiceIds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] serviceIds = idEditText.getText().toString().split(",");
                ArrayList<String> serviceId = new ArrayList<String>(Arrays.asList(serviceIds));
                mServiceDiscoveryInterface.getServicesWithLanguagePreference(serviceId ,mOnGetServiceUrlMapListener);
            }
        });
    }

    @Override
    public void onSuccess(String services) {
        Log.i("OnGetServicesListener", "" + services);
        resultView.setText(services);
    }

    @Override
    public void onError(ERRORVALUES error, String message) {
        Log.i("onError", "" + message);
        resultView.setText(message);
    }

    @Override
    public void onSuccess(URL url) {
        Log.i("Success", "" + url);
        resultView.setText("" + url);
    }

    @Override
    public void onSuccess(String countryCode, SOURCE source) {
        resultView.setText("Country Code : "+countryCode+" Source : "+source);
    }

    @Override
    public void onSuccess(Map urlMap) {
        ServiceDiscoveyService service = new ServiceDiscoveyService();

        String key = null;
        String locale= null;
        String configUrl= null ;
        Iterator it = urlMap.entrySet().iterator();
        Map mMap= new HashMap<String,Map>();
        Map dataMap = new HashMap<String,String >();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            key = pair.getKey().toString();
            service = (ServiceDiscoveyService) pair.getValue();
            locale = service.getLocale();
            configUrl = service.getConfigUrls();

            dataMap.put(locale, configUrl);
            mMap.put(key, dataMap);

            it.remove(); // avoids a ConcurrentModificationException
        }
        resultView.setText(" URL Model   : " +mMap);

    }
}
