package com.philips.platform.dprdemo.utils;

import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import com.philips.platform.dprdemo.database.table.OrmConsentDetail;

import java.util.ArrayList;
import java.util.List;


public class NotifyDBRequestListener {


    public void notifySuccess(List<? extends Object> ormObjectList, DBRequestListener dbRequestListener, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess((ArrayList<? extends Object>) ormObjectList);
        } else {
            //CallBack not registered
            DSLog.i(DataServicesManager.TAG, "CallBack not registered");
        }
    }


    public void notifyPrepareForDeletion(DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(null);
        } else {
            //Callback not registered
            DSLog.i(DataServicesManager.TAG, "Callback not registered");
        }
    }


    public void notifyFailure(Exception e, DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onFailure(e);
        } else {
            //Callback No registered
            DSLog.i(DataServicesManager.TAG, "Callback not registered");
        }
    }


    public void notifyConsentFetchSuccess(DBFetchRequestListner dbFetchRequestListner, ArrayList<OrmConsentDetail> ormConsents) {
        if (dbFetchRequestListner != null) {
            dbFetchRequestListner.onFetchSuccess(ormConsents);
        } else {
            //CallBack not registered
            DSLog.i(DataServicesManager.TAG, "CallBack not registered");
        }
    }

    public void notifyDBChange(SyncType type) {
        if (DataServicesManager.getInstance().getDbChangeListener() != null) {
            DataServicesManager.getInstance().getDbChangeListener().dBChangeSuccess(type);
        }
    }

}
