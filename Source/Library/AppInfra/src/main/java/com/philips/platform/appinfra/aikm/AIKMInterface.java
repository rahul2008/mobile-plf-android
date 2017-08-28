package com.philips.platform.appinfra.aikm;


import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;

import java.util.ArrayList;
import java.util.Map;

public interface AIKMInterface {

    void getServicesForServiceIds(ArrayList<String> serviceIds, AISDResponse.AISDPreference aisdPreference,
                                  Map<String, String> replacement,
                                  ServiceDiscoveryInterface.OnGetServicesListener onGetServicesListener) throws AIKMJsonFileNotFoundException;
}
