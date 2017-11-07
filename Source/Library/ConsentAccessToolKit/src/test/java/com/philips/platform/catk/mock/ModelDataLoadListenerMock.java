package com.philips.platform.catk.mock;


import android.os.Message;

import com.philips.platform.catk.network.NetworkAbstractModel;

public class ModelDataLoadListenerMock implements NetworkAbstractModel.DataLoadListener {


    @Override
    public void onModelDataLoadFinished(Message msg) {

    }

    @Override
    public int onModelDataError(Message msg) {
        return 0;
    }
}
