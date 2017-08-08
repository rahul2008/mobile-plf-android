package com.philips.platform.appinfra.keybag;


import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.util.ArrayList;

public interface KeyBagInterface {

    void getValueForServiceId(ArrayList<String> serviceIds, AIKMServiceDiscoveryPreference aikmServiceDiscoveryPreference, ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener);

    enum AIKMServiceDiscoveryPreference {
        COUNTRY_PREFERENCE, LANGUAGE_PREFERENCE
    }
}
