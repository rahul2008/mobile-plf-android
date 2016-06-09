package com.philips.platform.appinfra.demo;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppInfra appInfra = AppInfraApplication.gAppInfra;
        mServiceDiscoveryInterface = appInfra.getServiceDiscoveryInterface();
//        mServiceDiscoveryInterface.getservice();
        mServiceDiscoveryInterface.getServicesWithCountryPreference("ugrow.privacy",this );
//        mServiceDiscoveryInterface.getServicesWithLanguagePreference("ugrow.terms",this );
//        mServiceDiscoveryInterface.getServiceLocaleWithCountryPreference("ugrow.privacy",this );
//        mServiceDiscoveryInterface.getServiceLocaleWithLanguagePreference("ugrow.privacy",this );
//        mServiceDiscoveryInterface.getServiceUrlWithCountryPreference("ugrow.privacy",this );
//        mServiceDiscoveryInterface.getServiceUrlWithLanguagePreference("ugrow.terms",this );
    }

    @Override
    public void onSuccess(String services) {
        Log.i("Success", ""+services);
    }

    @Override
    public void onError(ERRORVALUES error, String message) {

    }

    @Override
    public void onSuccess(URL url) {
        Log.i("Success", ""+url);
    }
}
