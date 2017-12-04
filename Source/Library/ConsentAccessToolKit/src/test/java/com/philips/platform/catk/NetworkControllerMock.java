/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk;

import com.philips.platform.catk.dto.GetConsentDto;
import com.philips.platform.catk.NetworkAbstractModel;
import com.philips.platform.catk.NetworkController;

import java.util.List;

public class NetworkControllerMock extends NetworkController {
    public NetworkAbstractModel sendConsentRequest_model;
    public List<GetConsentDto> sendConsentRequest_onSuccessResponse;


    @Override
    public void sendConsentRequest(final NetworkAbstractModel model) {
        this.sendConsentRequest_model = model;
        if (model != null && sendConsentRequest_onSuccessResponse != null) {
            model.onResponseSuccess(sendConsentRequest_onSuccessResponse);
        }
    }

}
