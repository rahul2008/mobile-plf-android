package com.philips.platform.appinfra.keybag;


import com.philips.platform.appinfra.keybag.exception.KeyBagJsonFileNotFoundException;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;

import java.util.ArrayList;
import java.util.Map;

public interface KeyBagInterface {

    void getServicesForServiceIds(ArrayList<String> serviceIds, AISDResponse.AISDPreference aisdPreference,
                                  Map<String, String> replacement,
                                  ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListener) throws KeyBagJsonFileNotFoundException;
}
