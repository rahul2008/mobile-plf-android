package com.philips.cdp.wifirefuapp.observablemodules;

import com.philips.platform.core.datatypes.ConsentDetail;
import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBChangeListener;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;

import java.util.List;

/**
 * Created by philips on 6/7/17.
 */

public class ConsentObservable implements DBRequestListener<ConsentDetail>,DBFetchRequestListner<ConsentDetail>,DBChangeListener {
    @Override
    public void dBChangeSuccess(SyncType syncType) {

    }

    @Override
    public void dBChangeFailed(Exception e) {

    }

    @Override
    public void onFetchSuccess(List<? extends ConsentDetail> list) {

    }

    @Override
    public void onFetchFailure(Exception e) {

    }

    @Override
    public void onSuccess(List<? extends ConsentDetail> list) {

    }

    @Override
    public void onFailure(Exception e) {

    }
}
