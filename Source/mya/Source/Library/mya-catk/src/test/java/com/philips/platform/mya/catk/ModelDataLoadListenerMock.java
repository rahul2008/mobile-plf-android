/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.mya.catk;

import com.philips.platform.mya.catk.dto.GetConsentDto;
import com.philips.platform.mya.catk.error.ConsentNetworkError;

import java.util.List;

public class ModelDataLoadListenerMock implements NetworkAbstractModel.DataLoadListener {

    @Override
    public void onModelDataLoadFinished(List<GetConsentDto> consents) {

    }

    @Override
    public void onModelDataError(ConsentNetworkError error) {

    }
}
