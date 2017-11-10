/*
 * Copyright (c) 2017 Koninklijke Philips N.V.
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.catk.mock;

import com.philips.platform.catk.network.NetworkAbstractModel;

import android.os.Message;

public class ModelDataLoadListenerMock implements NetworkAbstractModel.DataLoadListener {

    @Override
    public void onModelDataLoadFinished(Message msg) {

    }

    @Override
    public int onModelDataError(Message msg) {
        return 0;
    }
}
