package com.philips.platform.appinfra.aikm;


import com.philips.platform.appinfra.aikm.exception.AIKMJsonFileNotFoundException;
import com.philips.platform.appinfra.aikm.model.OnGetServicesListener;
import com.philips.platform.appinfra.servicediscovery.model.AISDResponse;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Map;

public interface AIKMInterface {

    void getServicesForServiceIds(ArrayList<String> serviceIds, AISDResponse.AISDPreference aisdPreference,
                                  Map<String, String> replacement,
                                  OnGetServicesListener onGetServicesListener) throws AIKMJsonFileNotFoundException, JSONException;
}
