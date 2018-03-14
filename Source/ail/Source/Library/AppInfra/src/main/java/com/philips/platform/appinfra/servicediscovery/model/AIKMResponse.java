/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.appinfra.servicediscovery.model;


import com.philips.platform.appinfra.aikm.AIKManager;

import java.util.Map;
@SuppressWarnings("unchecked")
public class AIKMResponse {

    private Map<?,?> kMap;
    private AIKManager.KError kError;

    public Map<?,?> getkMap() {
        return kMap;
    }

    public void setkMap(Map<?,?> kMap) {
        this.kMap = kMap;
    }

    public AIKManager.KError getkError() {
        return kError;
    }

    public void setkError(AIKManager.KError kError) {
        this.kError = kError;
    }
}
