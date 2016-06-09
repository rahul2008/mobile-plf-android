package com.philips.platform.appinfra.demo;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.servicediscovery.AppIdentityInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.net.URL;
import java.util.Map;

/**
 * Created by 310238655 on 6/7/2016.
 */
public class ServiceDiscoveryDemo extends AppCompatActivity implements ServiceDiscoveryInterface.OnGetServicesListener, ServiceDiscoveryInterface.OnGetServiceLocaleListener, ServiceDiscoveryInterface.OnGetServiceUrlListener {

    ServiceDiscoveryInterface mServiceDiscoveryInterface = null;
    ServiceDiscoveryInterface.OnGetServicesListener mOnGetServicesListener = null;
    ServiceDiscoveryInterface.OnGetServiceLocaleListener mOnGetServiceLocaleListener = null;
    ServiceDiscoveryInterface.OnGetServiceUrlListener mOnGetServiceUrlListener = null;
    AppInfra appInfra;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appInfra = AppInfraApplication.gAppInfra;
        mServiceDiscoveryInterface = appInfra.getServiceDiscoveryInterface();
//        mServiceDiscoveryInterface.getservice();
        mServiceDiscoveryInterface.getServicesWithCountryPreference("ugrow.privacy",this );
        mOnGetServicesListener=this;
        mOnGetServiceLocaleListener=this;
        mOnGetServiceUrlListener=this;

        mServiceDiscoveryInterface.refresh(new ServiceDiscoveryInterface.OnRefreshListener() {
            @Override
            public void onError(ERRORVALUES error, String message) {

            }

            @Override
            public void onSuccess() {
                mServiceDiscoveryInterface.getServicesWithCountryPreference("ugrow.privacy",mOnGetServicesListener );
                mServiceDiscoveryInterface.getServicesWithLanguagePreference("ugrow.terms",mOnGetServicesListener );
                mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference("ugrow.privacy",mOnGetServiceLocaleListener );
                mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference("ugrow.terms",mOnGetServiceLocaleListener );
                mServiceDiscoveryInterface.getServiceUrlWithCountryPreference("ugrow.privacy",mOnGetServiceUrlListener );
                mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference("ugrow.terms",mOnGetServiceUrlListener );
            }
        }, "https://tst.philips.com/api/v1/discovery/b2c/12345?locale=en&country=IN");
    }

    @Override
    public void onSuccess(String services) {
        Log.i("OnGetServicesListener", ""+services);
    }

    @Override
    public void onError(ERRORVALUES error, String message) {

    }

    @Override
    public void onSuccess(URL url) {
        Log.i("Success", ""+url);
    }
}
