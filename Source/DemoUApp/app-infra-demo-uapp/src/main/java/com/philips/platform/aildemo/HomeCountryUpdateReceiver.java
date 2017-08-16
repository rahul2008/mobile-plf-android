package com.philips.platform.aildemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager;

import static com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryManager.AIL_HOME_COUNTRY;

public class HomeCountryUpdateReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ServiceDiscoveryManager.AIL_SERVICE_DISCOVERY_HOMECOUNTRY_CHANGE_ACTION)) {
            String countryCode = (String) intent.getExtras().get(AIL_HOME_COUNTRY);
            Toast.makeText(context, "Home country updated to " + countryCode, Toast.LENGTH_SHORT).show();
            Log.v(getClass() + "", "Home country changed to " + countryCode);
        }
    }
}
