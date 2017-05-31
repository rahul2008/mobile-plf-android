package com.philips.platform.appinfra.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import static com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager.HOME_COUNTRY_DATA;

public class HomeCountryUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ServiceDiscoveryManager.ACTION_HOME_COUNTRY_UPDATE)) {
            String countryCode = (String) intent.getExtras().get(HOME_COUNTRY_DATA);
            Toast.makeText(context, "Home country updated to " + countryCode, Toast.LENGTH_SHORT).show();
        }
    }
}
