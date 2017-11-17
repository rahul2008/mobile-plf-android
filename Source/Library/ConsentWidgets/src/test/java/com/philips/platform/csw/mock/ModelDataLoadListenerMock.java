/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.csw.mock;

import android.os.Message;

import com.philips.platform.catk.dto.GetConsentDto;
import com.philips.platform.catk.error.ConsentNetworkError;
import com.philips.platform.catk.network.NetworkAbstractModel;

import java.util.List;

public class ModelDataLoadListenerMock implements NetworkAbstractModel.DataLoadListener {



    @Override
    public void onModelDataLoadFinished(List<GetConsentDto> consents) {

    }

    @Override
    public int onModelDataError(ConsentNetworkError error) {
        return 0;
    }
}
