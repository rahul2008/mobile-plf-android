package com.philips.platform.appinfra.aikm;


import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.aikm.model.OnGetServicesListener;
import com.philips.platform.appinfra.servicediscovery.model.AIKMResponse;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public interface AIKMInterface extends Serializable {

    /**
     * API to fetch value of url and tokens for the provided service id's
     * @param serviceIds - pass service_id to fetch url and tokens
     * @param aisdPreference - pass country or language preference
     * @param replacement - pass query parameters if required as map
     * @param onGetServicesListener - call back listener to callee
     * @throws AIKMJsonFileNotFoundException - Exception is thrown when AIKMap.json is not found in assets
     * @throws JSONException - Exception is thrown when Json structure in invalid
     * @since 3.0.0
     */
    @Deprecated
    void getValueForServiceIds(ArrayList<String> serviceIds, AISDResponse.AISDPreference aisdPreference,
                               Map<String, String> replacement,
                               OnGetServicesListener onGetServicesListener) throws AIKMJsonFileNotFoundException, JSONException;

    AIKMResponse getServiceExtension(String serviceId, String url);
}
