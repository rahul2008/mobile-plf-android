package com.philips.platform.appinfra.keybag;


import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.util.ArrayList;

public interface KeyBagInterface {

    void getValueForServiceId(ArrayList<String> serviceId, ServiceDiscoveryInterface.OnGetKeyBagMapListener onGetKeyBagMapListener);
}
