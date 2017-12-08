package com.philips.platform.appinfra.aikm.model;

/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */


import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.util.List;


/**
 * This is the callback method from getValueForServiceIds() API.
 * the call back will have success method for actions completed successfully.
 * onSuccess returns the successful response
 */
public interface OnGetServicesListener extends ServiceDiscoveryInterface.OnErrorListener {
    void onSuccess(List<AIKMService> aiKmServices);
}
