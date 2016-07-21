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

import java.net.URL;

/**
 * Created by 310238655 on 6/7/2016.
 */
public class ServiceDiscoveryDemo extends AppCompatActivity implements ServiceDiscoveryInterface.OnGetServiceLocaleListener, ServiceDiscoveryInterface.OnGetServiceUrlListener, ServiceDiscoveryInterface.OnGetHomeCountryListener {

    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryInterface.OnGetServiceLocaleListener mOnGetServiceLocaleListener = null;
    ServiceDiscoveryInterface.OnGetServiceUrlListener mOnGetServiceUrlListener = null;
    ServiceDiscoveryInterface.OnGetHomeCountryListener mOnGetHomeCountryListener = null;
    AppInfraInterface appInfra;

    TextView resultView;
    EditText idEditText;
    String editTextData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appInfra = AppInfraApplication.gAppInfra;
        mServiceDiscoveryInterface = appInfra.getServiceDiscovery();
        mOnGetServiceLocaleListener=this;
        mOnGetServiceUrlListener=this;
        mOnGetHomeCountryListener=this;

        setContentView(R.layout.service_discovery_demopage);

        idEditText = (EditText) findViewById(R.id.serviceid_editText);
        Button localeByLang = (Button) findViewById(R.id.getlocal_by_lang_button);
        Button localeByCountry = (Button) findViewById(R.id.getlocal_by_country_butn);
        Button urlbyLang = (Button) findViewById(R.id.geturl_by_country_btn);
        Button urlbyCountry = (Button) findViewById(R.id.geturl_by_lang_buttn);
        Button getHomecountryBtn = (Button) findViewById(R.id.gethome_country_btn);

        editTextData= idEditText.getText().toString();

        resultView = (TextView) findViewById(R.id.textView2) ;

        localeByLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editTextData= idEditText.getText().toString();
                mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference(editTextData,mOnGetServiceLocaleListener );


            }
        });
        localeByCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextData= idEditText.getText().toString();
                mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference(editTextData,mOnGetServiceLocaleListener );
            }
        });
        urlbyLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextData= idEditText.getText().toString();
                mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference(editTextData,mOnGetServiceUrlListener );
            }
        });
        urlbyCountry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextData= idEditText.getText().toString();
                mServiceDiscoveryInterface.getServiceUrlWithCountryPreference(editTextData,mOnGetServiceUrlListener );
            }
        });
        getHomecountryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editTextData= idEditText.getText().toString();
                mServiceDiscoveryInterface.getHomeCountry(mOnGetHomeCountryListener);
            }
        });


    }

    @Override
    public void onSuccess(String services) {
        Log.i("OnGetServicesListener", ""+services);
        resultView.setText(services);
    }

    @Override
    public void onError(ERRORVALUES error, String message) {
        Log.i("onError", ""+message);
        resultView.setText(message);
    }

    @Override
    public void onSuccess(URL url) {
        Log.i("Success", ""+url);
        resultView.setText(""+url);
    }

    @Override
    public void onSuccess(String countryCode, SOURCE source) {
        resultView.setText(countryCode);
    }
}
