/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.platform.catk.NetworkAbstractModel;
import com.philips.platform.catk.NetworkController;

public class NetworkControllerMock extends NetworkController {
    public NetworkAbstractModel sendConsentRequest_model;

    @Override
    public void sendConsentRequest(final NetworkAbstractModel model) {
        this.sendConsentRequest_model = model;
    }

}