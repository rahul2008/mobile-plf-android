package com.philips.platform.core.listeners;

import com.philips.platform.core.utils.DataServicesError;

public interface RegisterDeviceTokenListener {
     void onResponse(boolean status);
     void onError(DataServicesError dataServicesError);
}
