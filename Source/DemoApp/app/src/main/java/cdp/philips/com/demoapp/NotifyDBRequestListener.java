/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package cdp.philips.com.demoapp;

import com.philips.platform.core.datatypes.SyncType;
import com.philips.platform.core.listeners.DBFetchRequestListner;
import com.philips.platform.core.listeners.DBRequestListener;
import com.philips.platform.core.trackers.DataServicesManager;
import com.philips.platform.core.utils.DSLog;
import cdp.philips.com.demoapp.database.table.OrmConsentDetail;

import java.util.ArrayList;
import java.util.List;

public class NotifyDBRequestListener {
    public void notifySuccess(List<? extends Object> ormObjectList, DBRequestListener dbRequestListener, SyncType type) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess((ArrayList<? extends Object>) ormObjectList);
        } else {
            DSLog.i(DataServicesManager.TAG, "CallBack not registered");
        }
    }

    public void notifyPrepareForDeletion(DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onSuccess(null);
        } else {
            DSLog.i(DataServicesManager.TAG, "Callback not registered");
        }
    }

    public void notifyFailure(Exception e, DBRequestListener dbRequestListener) {
        if (dbRequestListener != null) {
            dbRequestListener.onFailure(e);
        } else {
            DSLog.i(DataServicesManager.TAG, "Callback not registered");
        }
    }

    public void notifyConsentFetchSuccess(DBFetchRequestListner dbFetchRequestListner, ArrayList<OrmConsentDetail> ormConsents) {
        if (dbFetchRequestListner != null) {
            dbFetchRequestListner.onFetchSuccess(ormConsents);
        } else {
            DSLog.i(DataServicesManager.TAG, "CallBack not registered");
        }
    }

    /*public void notifyDBChange(SyncType type) {
        if (DataServicesManager.getInstance().getDbChangeListener() != null) {
            DataServicesManager.getInstance().getDbChangeListener().dBChangeSuccess(type);
        }
    }
*/
}
