package com.philips.platform.appinfra.keybag;


import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;

import java.util.ArrayList;
import java.util.Map;

public interface KeyBagInterface {

    void getValueForServiceId(ArrayList<String> serviceIds, AISDResponse.AISDPreference aisdPreference, Map<String, String> replacement, ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener);
}
